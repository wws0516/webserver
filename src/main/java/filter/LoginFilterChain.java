package filter;

import http.request.Request;
import http.response.Response;
import servlet.Servlet;
import util.Assert;
import util.ObjectUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @Author: wws
 * @Date: 2020-07-17 00:47
 */
public class LoginFilterChain implements FilterChain{

    private Request request;

    private Response response;

    private final List<Filter> filters;

    private Iterator<Filter> iterator;

    public LoginFilterChain() {
        this.filters = Collections.emptyList();
    }

    /**
     * Create a FilterChain with a Servlet.
     * @param servlet the Servlet to invoke
     * @since 3.2
     */
//    public LoginFilterChain(Servlet servlet) {
//        this.filters = initFilterList(servlet);
//    }

    /**
     * Create a {@code FilterChain} with Filter's and a Servlet.
     * @param servlet the {@link Servlet} to invoke in this {@link FilterChain}
     * @param filters the {@link Filter}'s to invoke in this {@link FilterChain}
     * @since 3.2
     */
//    public LoginFilterChain(Servlet servlet, Filter... filters) {
//        if (filters == null)
//            try {
//                throw new Exception("filters cannot be null");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        if (filters.length == 0)
//            try {
//                throw new Exception("filters cannot contain null values");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        this.filters = initFilterList(servlet, filters);
//    }
//
//    private static List<Filter> initFilterList(Servlet servlet, Filter... filters) {
//        Filter[] allFilters = ObjectUtils.addObjectToArray(filters, new ServletFilterProxy(servlet));
//        return Arrays.asList(allFilters);
//    }


    /**
     * Return the request that {@link #doFilter} has been called with.
     */

    public Request getRequest() {
        return this.request;
    }

    /**
     * Return the response that {@link #doFilter} has been called with.
     */

    public Response getResponse() {
        return this.response;
    }

    /**
     * Invoke registered {@link Filter}s and/or {@link Servlet} also saving the
     * request and response.
     */
    @Override
    public void doFilter(Request request, Response response) {
        Assert.notNull(request, "Request must not be null");
        Assert.notNull(response, "Response must not be null");
        Assert.state(this.request == null, "This FilterChain has already been called!");

        if (this.iterator == null) {
            this.iterator = this.filters.iterator();
        }

        if (this.iterator.hasNext()) {
            Filter nextFilter = this.iterator.next();
            nextFilter.doFilter(request, response, this);
        }

        this.request = request;
        this.response = response;
    }


}
