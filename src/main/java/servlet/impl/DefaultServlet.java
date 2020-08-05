package servlet.impl;

import http.request.Request;
import http.response.Response;
import servlet.HttpServlet;
import util.RequestMethod;

//默认的servlet，可处理 静态资源请求
public class DefaultServlet extends HttpServlet {

    @Override
    public void service(Request request, Response response) {
        if (request.getMethod() == RequestMethod.GET) {
            //首页
            if (request.getUrl().equals("/")) {
                request.setUrl("/index.html");
            }

            request.forward(request.getUrl(), response);
        }
    }

    @Override
    public void doGet(Request req, Response res) {

    }

    @Override
    public void doPost(Request req, Response res) {

    }
}