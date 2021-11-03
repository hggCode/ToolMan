package work.wlwl.toolman.service.reptile;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import work.wlwl.toolman.service.reptile.entity.Property;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JsoupTest {

    @Test
    void test() throws IOException {
        File file = new File("D:\\IdeaProjects\\ToolMan\\service\\service_reptile\\src\\test\\java\\work\\wlwl\\toolman\\service\\reptile\\test.html");
        Document pares = Jsoup.parse(file, "UTF-8");
//        Element color = pares.select("#choose-attrs .dd").first();
//        Elements es = color.select(".item");
//        for (Element e : es) {
//            String attr = e.attr("data-value");
//            System.out.println(attr);
//        }
//        System.out.println(color);
        Elements e = pares.select(".Ptable");
        Elements elements = e.select(".Ptable-item");
        Map<String, String> map = new HashMap<>();
        Property property = new Property();
        for (Element element : elements) {
            Elements select = element.select(".clearfix");
            for (Element element1 : select) {
                String dt = element1.select("dt").text().trim();
                String dd = element1.select("dd").text().trim();
                map.put(dt, dd);
            }
        }
        map.forEach((k, v) -> {
            System.out.println(k + ":\t" + v);
        });
        property.setName(map.get("产品名称"));
        property.setWeight(map.get("机身重量（g）"));
//        property.set
        System.out.println(property);
    }

}

