package filter;

import http.request.Request;
import http.response.Response;

import java.io.IOException;

/**
 * @Author: wws
 * @Date: 2020-07-16 20:44
 */
public interface Filter {

    void init(FilterConfig var1);

    void doFilter(Request var1, Response var2, FilterChain var3);

    void destroy();

}
