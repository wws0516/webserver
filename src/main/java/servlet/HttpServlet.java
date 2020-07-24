package servlet;

import http.request.Request;
import http.response.Response;

/**
 * @Author: wws
 * @Date: 2020-07-16 06:51
 */
public class HttpServlet implements Servlet{
    public void service(Request req, Response res) {

    }

    public void doGet(Request req, Response res) {
        service(req, res);
    }

    public void doPost(Request req, Response res) {
        service(req, res);
    }
}
