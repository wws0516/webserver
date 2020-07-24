package server;

import http.request.Request;
import http.response.Response;
import http.cookie.Cookie;
import http.session.Session;
import servlet.Servlet;
import xml.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Author: wws
 * @Date: 2020-07-15 08:12
 */
public class ApplicationContext {

    public static Map<String, Session> appSessions = new HashMap<>();


     public static Session getSession(String sessionId){

         for (String sessId : appSessions.keySet())
             if (sessionId.equals(sessId))
                 return appSessions.get(sessId);
         return null;
     }

    public static Session createSession(Response response) {

        Session session = new Session();
        session.setId(UUID.randomUUID().toString().replace("-","").toUpperCase());
        ApplicationContext.appSessions.put(session.getId(), session);

        response.addCookie(new Cookie("JSESSIONID", session.getId()));

        return session;
    }

}
