package xml;

import filter.Filter;
import servlet.Servlet;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 处理xml文件解析出来的servlet、filter、listen，把他们存进map, 方便使用的时候获取
 * @Author: wws
 * @Date: 2020-07-14 17:39
 */
public class WebContext {

    private Map<String, String> entitiesMap = new HashMap<String, String>();        //存储servlet-name和servlet-class
    private Map<String, String> entityMappingsMap = new HashMap<String, String>();  //存储url-pattern和servlet-name

    private Map<String, String> filtersMap = new HashMap<String, String>();         //存储filter-name和filter-class
    private Map<String, List<String>> filterMappingsMap = new HashMap<>();  //存储url-pattern和filter-name

    private Map<String, String> url_servlet_map = new HashMap<String, String>();    //存储url及其对应的servlet
    private Map<String, List<String>> url_filter_map = new HashMap<>();     //存储url及其对应的filters


    public WebContext(List<Entity> entities, List<EntityMapping> entityMappings, List<FilterEntity> filters, List<FilterEntityMapping> filterEntityMappings) {


        for (Entity entity : entities)
            entitiesMap.put(entity.getName(), entity.getClazz());

        for (EntityMapping entityMapping : entityMappings)
            for (String urlPattern : entityMapping.getUrlPatterns())
                entityMappingsMap.put(urlPattern, entityMapping.getName());

        for (FilterEntity filterEntity : filters)
            filtersMap.put(filterEntity.getName(), filterEntity.getClazz());

        for (FilterEntityMapping filterEntityMapping : filterEntityMappings) {
            for (String servletName : filterEntityMapping.getServletName()) {
                for (EntityMapping entityMapping : entityMappings)
                    if (entityMapping.getName().equals(servletName))
                        for (String url : entityMapping.getUrlPatterns()) {
                            filterMappingsMap.computeIfAbsent(url, k -> new ArrayList<String>(){});
                            filterMappingsMap.get(url).add(filterEntityMapping.getName());
                        }
            }
            for (String urlPattern : filterEntityMapping.getUrlPatterns()) {
                filterMappingsMap.computeIfAbsent(urlPattern, k -> new ArrayList<String>() {});
                filterMappingsMap.get(urlPattern).add(filterEntityMapping.getName());
            }
        }

        for (String urlPattern : entityMappingsMap.keySet())
            url_servlet_map.put(urlPattern, entitiesMap.get(entityMappingsMap.get(urlPattern)));

        for (String urlPattern : filterMappingsMap.keySet())
            for (String filterName : filterMappingsMap.get(urlPattern)){
                url_filter_map.computeIfAbsent(urlPattern, k -> new ArrayList<String>());
                url_filter_map.get(urlPattern).add(filtersMap.get(filterName));
            }
    }


    public List<Filter> getFilterByUrl(String url) {
        List<Filter> filters = new ArrayList<>();
        if (url_filter_map.get(url) == null)
            return new ArrayList<>();
        for (String filterClass : url_filter_map.get(url)) {
            try {
                filters.add((Filter) Class.forName(filterClass).getConstructor().newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return filters;

    }

    public Servlet getServletByUrl(String url) {
        Servlet servlet = null;
        try {
            servlet = (Servlet)Class.forName(url_servlet_map.get(url)).getConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return servlet;
    }

}