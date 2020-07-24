package http.session;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: wws
 * @Date: 2020-07-15 11:29
 */
public class Session {

    long creationTime;

    private String id;
    private long lastAccessedTime;
    private Map<String, Object> attribute = new HashMap<>();
    private int maxInactiveInterval;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getAttribute(String var1){
        return attribute.get(var1);
    }

    void setAttribute(String var1, Object var2){
        attribute.put(var1, var2);
    }

    void removeAttribute(String var1){
        attribute.remove(var1);
    }

    boolean invalidate(){
        return System.currentTimeMillis() - lastAccessedTime < maxInactiveInterval;
    }


}
