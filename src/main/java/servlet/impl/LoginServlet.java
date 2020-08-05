package servlet.impl;

import http.request.Request;
import http.response.Response;
import servlet.HttpServlet;

import java.io.IOException;

/**
 * @Author: wws
 * @Date: 2020-07-15 10:07
 */
public class LoginServlet extends HttpServlet {

    @Override
    public void service(Request req, Response res) {
        System.out.println(req.getUrl());

        String username = req.getParameter("username");
        req.getSession(true, res).setAttribute("username", username);

        try {
            res.redirect("index.html");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
