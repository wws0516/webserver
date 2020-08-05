package xml;

import java.util.List;
import java.util.Set;

/**
 * @Author: wws
 * @Date: 2020-07-14 12:47
 * 对应xml中读到的<filter-mapping></filter-mapping>
 */


public class FilterEntityMapping {

    private String name;

    private List<String> urlPatterns;

    private List<String> servletName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getUrlPatterns() {
        return urlPatterns;
    }

    public List<String> getServletName() {
        return servletName;
    }

    public FilterEntityMapping(List<String> urlPatterns, List<String> servletName) {
        this.urlPatterns = urlPatterns;
        this.servletName = servletName;
    }

}
