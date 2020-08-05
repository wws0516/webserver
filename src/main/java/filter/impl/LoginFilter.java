package filter.impl;

import filter.Filter;
import filter.FilterConfig;
import filter.filterchain.FilterChain;
import http.request.Request;
import http.response.Response;
import http.session.Session;

import java.io.IOException;

/**
 * @Author: wws
 * @Date: 2020-07-16 20:57
 */
public class LoginFilter implements Filter {
    public void init(FilterConfig config) {

    }

    public void doFilter(Request req, Response res, FilterChain filterChain) {


        Session session = req.getSession(true, res);

        String username = (String) session.getAttribute("username");

        System.out.println("LoginFilter "+username);
        if (username != null && !username.equals("")) {
            // 如果现在存在了session，则请求向下继续传递
                filterChain.doFilter(req, res);
        } else {
            // 跳转到提示登陆页面
            try {
                res.redirect("/login.html");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void destroy() {

    }
}
