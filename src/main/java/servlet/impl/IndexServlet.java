package servlet.impl;

import http.request.Request;
import http.response.Response;
import servlet.HttpServlet;

import java.io.IOException;

/**
 * @Author: wws
 * @Date: 2020-08-02 17:32
 */
public class IndexServlet extends HttpServlet {

    @Override
    public void service(Request req, Response res) {

        Object username = req.getSession(true, res).getAttribute("username");
        if (username != null) {
            try {
                res.redirect("index.html");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
