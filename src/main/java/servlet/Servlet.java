package servlet;

import http.request.Request;
import http.response.Response;

/**
 * @Author: wws
 * @Date: 2020-07-15 07:42
 */
public interface Servlet {

    public void service(Request req, Response res);

    public void doGet(Request req, Response res);

    public void doPost(Request req, Response res);

}
