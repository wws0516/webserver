package http.response;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import http.HttpStatusCode;
import http.RequestHandler;
import http.cookie.Cookie;
import org.apache.commons.io.IOUtils;

import Exception.*;

public class Response {

	//用于将response刷回客户端
	private RequestHandler requestHandler;

	private StringBuilder sb = new StringBuilder();

	//正文
	private StringBuilder content = new StringBuilder();
	//协议头（状态行与请求头 回车）信息
	private StringBuilder headInfo = new StringBuilder();
	private int len; //正文的字节数

	private int code = HttpStatusCode.OK;

	private final String BLANK =" ";
	private final String CRLF = "\r\n";

	private List<Cookie> cookies = new ArrayList<Cookie>();
	private List<ResponseHeader> headers = new ArrayList<ResponseHeader>();

	public Response() {

	}

	//动态添加内容
	public Response print(String info) {

		content.append(info);
		len+=info.getBytes().length;
		return this;
	}
	public	Response println(String info) {
		content.append(info).append(CRLF);
		len+=(info+CRLF).getBytes().length;
		return this;
	}


	//添加cookie
	public void addCookie(Cookie cookie){
		cookies.add(cookie);
	}

	//推送响应信息
	public ByteBuffer pushToBrowser() {

		sb.append(headInfo);
//		sb.append(CRLF);
		sb.append(content);
		return ByteBuffer.wrap(sb.toString().getBytes());
	}

	//刷新响应信息
	public void flush() throws IOException {

		createHeadInfo();
		requestHandler.flushRes(this);
	}

	//构建头信息
	private void createHeadInfo() {
		//1、响应行: HTTP/1.1 200 OK
		headInfo.append("HTTP/1.1").append(BLANK);
		headInfo.append(code).append(BLANK);
		switch(code) {
			case 200:
				headInfo.append("OK").append(CRLF);
				break;
			case 302:
				headInfo.append("MOVED TMP").append(CRLF);
				break;
			case 404:
				headInfo.append("NOT FOUND").append(CRLF);
				break;
			case 500:
				headInfo.append("SERVER ERROR").append(CRLF);
				break;
		}
		//2、响应头(最后一行存在空行):
		headInfo.append("Date: ").append(new Date()).append(CRLF);
		headInfo.append("Server: ").append("wws Server/0.0.1;charset=GBK").append(CRLF);

		for (ResponseHeader header : headers)
			headInfo.append(header.key + ": " + header.value + CRLF);

		//将set-cookie添加进响应头
		if (cookies.size() > 0)
			for (Cookie cookie : cookies)
				headInfo.append("Set-Cookie: "+cookie.getName()+"="+cookie.getValue()).append(CRLF);

		headInfo.append("Content-type: text/html").append(CRLF);
		headInfo.append("Content-length: ").append(len).append(CRLF);
		headInfo.append(CRLF);
	}

	//重定向时设置content内容
	public void setContent(String body){

		content.append(body);
		len = 0;
		len += body.getBytes().length;

	}

	public void redirect(String page) throws IOException {

		try {
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(page);
			if (is == null)
				throw new ResourceNotFoundException("redirect: could not found resource");

			this.setContent(IOUtils.toString(is));
		}catch (ResourceNotFoundException e){
			e.printStackTrace();
		}

		addHeader(new ResponseHeader("location" , page));
		setCode(HttpStatusCode.MOVED_TMP);

		createHeadInfo();
//		sb = headInfo.append(content);
		requestHandler.flushRes(this);
	}

	public void addHeader(ResponseHeader rh) throws IOException {
		headers.add(rh);
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public void setRequestHandler(RequestHandler requestHandler) {
		this.requestHandler = requestHandler;
	}
}
