package xml;

import java.util.Set;

/**
 * 对应xml文件中读到的<servlet-mapping>
 * @Author: wws
 * @Date: 2020-07-14 12:47
 */
public class EntityMapping {

    private String name;

    private Set<String> urlPatterns;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getUrlPatterns() {
        return urlPatterns;
    }

    public void setUrlPatterns(Set<String> urlPatterns) {
        this.urlPatterns = urlPatterns;
    }

    public EntityMapping() {
    }

    public EntityMapping(Set<String> urlPatterns) {
        this.urlPatterns = urlPatterns;
    }
}
