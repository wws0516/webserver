package http.cookie;

/**
 * @Author: wws
 * @Date: 2020-07-15 12:09
 */
public class Cookie {

    private String name;	// NAME= ... "$Name" style is reserved
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Cookie(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
