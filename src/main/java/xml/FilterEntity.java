package xml;

/**
 * 对应xml文件中读到的<filter></filter>
 * @Author: wws
 * @Date: 2020-07-14 12:46
 */
public class FilterEntity {

    private String name;

    private String clazz;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public FilterEntity(){}

    public FilterEntity(String name, String clazz) {
        this.name = name;
        this.clazz = clazz;
    }
}
