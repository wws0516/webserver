package http;

import filter.Filter;
import filter.FilterChain;
import http.request.Request;
import http.response.Response;
import server.ApplicationContext;
import server.ServerWrapper;
import servlet.Servlet;
import xml.WebContext;
import xml.XMLParse;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 负责对request处理，也就是把response返回客户端
 * @author: wws
 * @Date: 2020-07-16 06:58
 */
public class RequestHandler implements FilterChain{

    private ServerWrapper serverWrapper;

    Map<String, List<Filter>> filtersMap = new HashMap<>();

    List<Filter> matchedFilters;

    int filterIndex = 0;

    //解析xml，获取过滤器，存到Map<String, List<Filter>>中
    //解析request,获取request的url,到map中取对应的过滤器集合
    //如果没有过滤器，直接执行servlet；若有，执行过滤器链
    public void handler(Request request, Response response) {

        XMLParse xmlParse = XMLParse.getInstance();
        WebContext webContext = null;
        try {
            webContext = xmlParse.startParse();
        } catch (Exception e) {
            e.printStackTrace();
        }

        matchedFilters = webContext.getFilterByUrl(request.getUrl());

        if (matchedFilters.size()>0)
            doFilter(request, response);

        Servlet servlet = null;

        try {
            servlet = webContext.getServletByUrl(request.getUrl());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (servlet != null) {
            response.setCode(HttpStatusCode.OK);
            servlet.service(request, response);
        }
        else
            response.setCode(HttpStatusCode.NOT_FOUND);

    }


//    public void run() {
//        serverWrapper.run();
//    }

    public void flushRes(Response response){
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

    }
}
