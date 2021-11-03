package work.wlwl.toolman.service.reptile;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import work.wlwl.toolman.service.base.entity.ResultCodeEnum;
import work.wlwl.toolman.service.base.exception.GlobalException;
import work.wlwl.toolman.service.reptile.utils.JsoupUtils;

import java.io.IOException;

public class JsoupTest {

    @Test
    void test() throws IOException {
//        File file = new File("D:\\IdeaProjects\\ToolMan\\service\\service_reptile\\src\\test\\java\\work\\wlwl\\toolman\\service\\reptile\\test.html");
//        Document pares = Jsoup.parse(file, "UTF-8");
        Document pares = JsoupUtils.parse("https://item.jd.com/100011203359.html");
        Elements e = pares.select(".Ptable");
        if (e.first() == null) {
            System.out.println(pares);
            throw new GlobalException(ResultCodeEnum.JD_CONNECT_ERROR);
        }
//        Element color = pares.select("#choose-attrs .dd").first();
//        Elements es = color.select(".item");
//        for (Element e : es) {
//            String attr = e.attr("data-value");
////            System.out.println(attr);
//        }

    }

}

