package http;

import filter.Filter;
import filter.filterchain.FilterChain;
import http.request.Request;
import http.response.Response;
import server.ApplicationContext;
import server.ServerWrapper;
import servlet.Servlet;
import xml.WebContext;
import xml.XMLParse;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 负责对request处理，也就是把response返回客户端
 * @author: wws
 * @Date: 2020-07-16 06:58
 */
public class RequestHandler implements FilterChain, Runnable{

    private ServerWrapper serverWrapper;

    private Request request;
    private Response response;

    //标识是否在过滤器链中就已经完成 请求
    boolean isFinished = false;

    private ApplicationContext applicationContext = ApplicationContext.getInstance();

    Map<String, List<Filter>> filtersMap = new HashMap<>();

    List<Filter> matchedFilters;

    int filterIndex = 0;

    public RequestHandler(ServerWrapper serverWrapper, Request request, Response response) {
        this.serverWrapper = serverWrapper;
        this.request = request;
        this.response = response;
        response.setRequestHandler(this);
    }

    //解析request,获取request的url,到map中取对应的过滤器集合
    //如果没有过滤器，直接执行servlet；若有，执行过滤器链
    public void handler() {

        System.out.println(applicationContext);
        matchedFilters = applicationContext.getFilterByUrl(request.getUrl());

        if (matchedFilters.size()>0)
            doFilter(request, response);
        else service();

    }

    //执行servlet的service方法
    private void service() {
        Servlet servlet = null;

        try {
            //获取url对应的servlet，默认为defaultServlet
            servlet = applicationContext.getServletByUrl(request.getUrl());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (servlet != null) {
            response.setCode(HttpStatusCode.OK);
            servlet.service(request, response);
        }
        else
            response.setCode(HttpStatusCode.NOT_FOUND);

//        flushRes(response);
    }


    public void flushRes(Response response){
        //请求处理完毕
        isFinished = true;

        SocketChannel socketChannel = serverWrapper.getsChannel();
        try {
            socketChannel.write(response.pushToBrowser());
//            socketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public ServerWrapper getServerWrapper() {
        return serverWrapper;
    }

    public void setServerWrapper(ServerWrapper serverWrapper) {
        this.serverWrapper = serverWrapper;
    }

    @Override
    public void doFilter(Request request, Response response) {

        if (filterIndex < matchedFilters.size())
            matchedFilters.get(filterIndex++).doFilter(request, response, this);
        else if (!isFinished)
            service();
    }

    @Override
    public void run() {
        handler();
    }
}
