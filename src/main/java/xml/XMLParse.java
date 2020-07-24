package xml;

import lombok.Data;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @Author: wws
 * @Date: 2020-07-14 11:18
 */
@Data
public class XMLParse {

    private static XMLParse instance = null;

    //存储所有的servlet
    private List<Entity> entities;

    private List<EntityMapping> entityMappings;

    //存储所有的过滤器
    private List<FilterEntity> filters = new ArrayList<>();

    private List<FilterEntityMapping> filterMappsings = new ArrayList<>();
    private List<Listen> listens = new ArrayList<>();

    public WebContext startParse() throws Exception {

        XMLParse xmlParse = new XMLParse();

        //SAX解析
        //1、获取解析工厂
        SAXParserFactory factory = SAXParserFactory.newInstance();
        //2、通过解析工厂获取解析器
        SAXParser parser = factory.newSAXParser();
        //3、创建 文档处理器
        WebHandler webHandler = new WebHandler();
        //4、解析器开始解析
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("web.xml");
        parser.parse(is, webHandler);

//        return instance;
//        List<Entity> entityList = webHandler.getEntities();
//        List<EntityMapping> entityMappingList = webHandler.getEntityMappings();
//
        WebContext webContext = new WebContext(entities, entityMappings, filters, filterMappsings);
        return webContext;

    }

    public static XMLParse getInstance() {
        if (instance == null)
            instance = new XMLParse();
        return instance;
    }


    private XMLParse() {}

    //内部类
    class WebHandler extends DefaultHandler {

        private Entity entity;
        private EntityMapping entityMapping;

        private FilterEntity filterEntity;
        private FilterEntityMapping filterEntityMapping;

        private Listen listen;

        private String tag;     //存储当前处理的标签名
        private int status;

        private Object[] objects = {entity};

        @Override
        public void startDocument() throws SAXException {
            entities = new ArrayList<Entity>();
            entityMappings = new ArrayList<EntityMapping>();
            System.out.println("文档解析开始");
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            tag = qName;

            if (tag.equals("filter")) {
                filterEntity = new FilterEntity();
                status = 0;
            } else if (tag.equals("filter-mapping")) {
                filterEntityMapping = new FilterEntityMapping(new ArrayList<String>(), new ArrayList<String>());
                status = 1;
            } else if (tag.equals("servlet")) {
                entity = new Entity();
                status = 2;
            } else if (tag.equals("servlet-mapping")) {
                entityMapping = new EntityMapping(new HashSet<String>());
                status = 3;
            } else if (tag.equals("listen")) {
                listen = new Listen();
                status = 4;
            }
            System.out.println(qName + "元素开始解析");
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {

            if (status == 0)
                if (tag.equals("filter-name")) {
                    filterEntity.setName(new String(ch, start, length));
                } else if (tag.equals("filter-class"))
                    filterEntity.setClazz(new String(ch, start, length));

            if (status == 1)
                if (tag.equals("filter-name"))
                    filterEntityMapping.setName(new String(ch, start, length));
                else if (tag.equals("url-pattern"))
                    filterEntityMapping.getUrlPatterns().add(new String(ch, start, length));
                else if (tag.equals("servlet-name"))
                    filterEntityMapping.getServletName().add(new String(ch, start, length));

            if (status == 2)
                if (tag.equals("servlet-name"))
                    entity.setName(new String(ch, start, length));
                else if (tag.equals("servlet-class"))
                    entity.setClazz(new String(ch, start, length));

            if (status == 3)
                if (tag.equals("servlet-name"))
                    entityMapping.setName(new String(ch, start, length));
                else if (tag.equals("url-pattern"))
                    entityMapping.getUrlPatterns().add(new String(ch, start, length));

            if (status == 4)
                if (tag.equals("listen-class"))
                    listen.setClz(new String(ch, start, length));

            System.out.println("元素内容为" + new String(ch, start, length));
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if (qName.equals("filter")) {
                filters.add(filterEntity);
            } else if (qName.equals("filter-mapping")) {
                filterMappsings.add(filterEntityMapping);
            } else if (qName.equals("servlet")) {
                entities.add(entity);
            } else if (qName.equals("servlet-mapping")) {
                entityMappings.add(entityMapping);
            } else if (qName.equals("listen")) {
                listens.add(listen);
            }

            tag = "";
            System.out.println(qName + "元素解析结束");
        }

        @Override
        public void endDocument() throws SAXException {
            System.out.println("文档解析结束");
        }


        public List<Entity> getEntities() {
            return entities;
        }

        public List<EntityMapping> getEntityMappings() {
            return entityMappings;
        }
    }
}