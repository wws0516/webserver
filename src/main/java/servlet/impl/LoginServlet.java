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

        //            res.println("<html>");
//            res.println("<title>np</title>");
//            res.println("</html>");
//            res.flush();
        try {
            res.redirect("index.html");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
