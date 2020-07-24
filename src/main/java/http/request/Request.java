package http.request;

import http.CharConstant;
import http.cookie.Cookie;
import http.response.Response;
import http.session.Session;
import org.apache.commons.io.IOUtils;
import server.ApplicationContext;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.*;

import Exception.*;

/**
 * @Author: wws
 * @Date: 2020-07-15 07:57
 */
public class Request {

    //请求的所有信息
    private String requestInfo;

    //请求方法
    private String method;

    //请求url
    private String url;

    //请求参数
    private Map<String, String> parameter = new HashMap<>();

    private Map<String, String> headers = new HashMap<>();

    private List<Cookie> cookies = new ArrayList<>();

    private Session session;

    public Request(String requestInfo) {
        this.requestInfo = requestInfo;
    }

    public String getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(String requestInfo) {
        this.requestInfo = requestInfo;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getParameter() {
        return parameter;
    }

    public void setParameter(Map<String, String> parameter) {
        this.parameter = parameter;
    }

    /*
       解析请求头
     */
    public void parseRequest() {
        this.method = this.requestInfo.substring(0, this.requestInfo.indexOf(" ")).toLowerCase();
        /*
            获取请求url: 第一个/ 到 HTTP/
            可能包含请求参数? 前面的为url
         */
        //1)、获取/的位置
        int startIdx = this.requestInfo.indexOf("/");
        //2)、获取 HTTP/的位置
        int endIdx = this.requestInfo.indexOf("HTTP/");
        //3)、分割字符串
        this.url = this.requestInfo.substring(startIdx, endIdx).trim();

        if (url.contains("?")){
            String[] urlPar = url.split("[?]");
            url = urlPar[0];

            //4)、获取请求参数
            for (String keyValue : urlPar[1].split("&")){
                String[] kv = keyValue.split("=");
                parameter.put(kv[0], kv[1]);
            }

        }


        try {
            requestInfo = URLDecoder.decode(new String(requestInfo.getBytes(), Charset.defaultCharset()), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println(requestInfo);
        String[] lines = requestInfo.split(CharConstant.CRLF);

        try {

            if (lines.length <= 1)
                throw new InvalidRequestException("请求格式错误");
        }catch (InvalidRequestException e) {
            System.out.println(e.getErrMsg());
        }
        for (int i=1; i<lines.length; i++) {
            if (lines[i].equals(""))
                break;
            String[] kv = lines[i].split(": ");
            headers.put(kv[0], kv[1]);
        }
        if (headers.containsKey("Cookie")) {
            String[] cookies = headers.get("Cookie").split(";");
            for (String cookie : cookies)
                this.cookies.add(new Cookie(cookie.split("=")[0], cookie.split("=")[1]));
        }

        if (headers.containsKey("Content-Length") && Integer.valueOf(headers.get("Content-Length")) != 0)
            parseBody(requestInfo.split(CharConstant.CRLF + CharConstant.CRLF)[1]);
    }

    /*
        解析请求体
     */
    public void parseBody(String body){
        byte[] bytes = body.getBytes(Charset.defaultCharset());
        int length = Integer.parseInt(this.headers.get("Content-Length"));
        body = new String(bytes, 0, Math.min(length,bytes.length), Charset.defaultCharset()).trim();
        String[] parameters = body.split("&|=");

        for (int i=0; i < parameters.length-1; i=i+2)
            parameter.put(parameters[i], parameters[i+1]);

    }


    /*

     */
    public Session getSession(boolean create, Response response) {
        if (session != null) {
            return session;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("JSESSIONID")) {
                Session currentSession = ApplicationContext.getSession(cookie.getValue());
                if (currentSession != null) {
                    session = currentSession;
                    return session;
                }
            }
        }
        if (!create) {
            return null;
        }
        session = ApplicationContext.createSession(response);
        return session;
    }


    public void forward(String url, Response response) {

        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(url);

        try {
            if (is == null) {
                throw new ResourceNotFoundException("转发的页面文件找不到");
            }
            response.setContent(IOUtils.toString(is));
            System.out.println(IOUtils.toString(is));
            response.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
