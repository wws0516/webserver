package server;

import filter.Filter;
import http.cookie.Cookie;
import http.response.Response;
import http.session.Session;
import listener.ServletContextListener;
import listener.RequestListener;
import listener.SessionListener;
import listener.event.SessionEvent;
import servlet.Servlet;
import xml.*;

import java.util.*;

/**
 * @Author: wws
 * @Date: 2020-07-15 08:12
 */
public class ApplicationContext {

    private static ApplicationContext applicationContext;

    private WebContext webContext;

    private Map<String, Session> appSessions = new HashMap<>();

    //监听器们
    List<ServletContextListener> servletContextListeners = new ArrayList<>();
    List<SessionListener> sessionListeners = new ArrayList<>();
    List<RequestListener> requestListeners = new ArrayList<>();



    public static ApplicationContext getInstance(){

        if (applicationContext == null) {
            XMLParse xmlParse = XMLParse.getInstance();
            WebContext webContext = null;

            try {
                webContext = xmlParse.startParse();
            } catch (Exception e) {
                e.printStackTrace();
            }

            applicationContext = new ApplicationContext(webContext);

        }
        return applicationContext;

    }

    private ApplicationContext(WebContext webContext) {
        this.webContext = webContext;
        // 解析listener
        for (Listen listener : webContext.getListens()) {
            EventListener eventListener = null;
            try {
                eventListener = (EventListener) Class.forName(listener.getClz()).newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (eventListener instanceof ServletContextListener) {
                servletContextListeners.add((ServletContextListener) eventListener);
            }
            if (eventListener instanceof SessionListener) {
                sessionListeners.add((SessionListener) eventListener);
            }
            if (eventListener instanceof RequestListener) {
                requestListeners.add((RequestListener) eventListener);
            }
        }
    }

    public List<Filter> getFilterByUrl(String url) {

        return webContext.getFilterByUrl(url);

    }

    public Servlet getServletByUrl(String url) {
        return webContext.getServletByUrl(url);

    }

     public Session getSession(String sessionId){

         //遍历全局的session
         for (String sessId : appSessions.keySet())
             if (sessionId.equals(sessId))
                 return appSessions.get(sessId);
         return null;
     }

    public Session createSession(Response response) {

        //创建一个session放到全局中
        Session session = new Session();
        session.setId(UUID.randomUUID().toString().replace("-","").toUpperCase());
        appSessions.put(session.getId(), session);

        //session监听器执行操作
        SessionEvent sessionEvent = new SessionEvent(session);
        for (SessionListener sessionListener : sessionListeners){
            sessionListener.sessionCreated(sessionEvent);
        }

        //在响应中设置cookie
        response.addCookie(new Cookie("JSESSIONID", session.getId()));

        return session;
    }

    //移除当前session,并且监听器执行相关操作
    public void removeSession(Session session) {
        appSessions.remove(session.getId());
        SessionEvent httpSessionEvent = new SessionEvent(session);
        for (SessionListener listener : sessionListeners) {
            listener.sessionDestroyed(httpSessionEvent);
        }
    }

}
