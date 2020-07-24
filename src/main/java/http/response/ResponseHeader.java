package http.response;

/**
 * @Author: wws
 * @Date: 2020-07-15 22:46
 */
public class ResponseHeader {

    String key;
    String value;

    public ResponseHeader(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
