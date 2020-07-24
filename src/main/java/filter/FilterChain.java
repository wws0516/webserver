package filter;

import http.request.Request;
import http.response.Response;

import java.io.IOException;

/**
 * @Author: wws
 * @Date: 2020-07-16 20:46
 */
public interface FilterChain {
    void doFilter(Request var1, Response var2);
}
