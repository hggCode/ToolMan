package work.wlwl.toolman.service.reptile;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;
import work.wlwl.toolman.service.reptile.utils.JsoupUtils;

@Slf4j
public class JsoupTest {


    @Test
    void Test() {
        Document document = JsoupUtils.parse("https://item.jd.com/100009836741.html");
        int length = document.toString().length();
        System.out.println(length);
        Element element = document.selectFirst(".sku-name");
        System.out.println(element);
    }

    @Test
    void t() {
        String brandName = "摩托罗拉（Motorola）";
        if (brandName.contains("（")) {
            brandName = brandName.substring(0, brandName.lastIndexOf("（"));
            System.out.println("进来了1");
        }
        System.out.println(brandName);
    }


}

