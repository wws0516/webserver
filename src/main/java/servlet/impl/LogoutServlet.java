package servlet.impl;

import http.request.Request;
import http.response.Response;
import servlet.HttpServlet;

import java.io.IOException;

public class LogoutServlet extends HttpServlet {
    
    @Override
    public void doGet(Request request, Response response) {

    }

    @Override
    public void service(Request request, Response response) {
        request.getSession(true, response).remove();

        try {
            response.redirect("/login.html");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}