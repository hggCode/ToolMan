package work.wlwl.toolman.service.reptile;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;
import work.wlwl.toolman.service.reptile.entity.Edition;
import work.wlwl.toolman.service.reptile.utils.JsoupUtils;

import java.io.IOException;
import java.math.BigDecimal;

@Slf4j
public class JsoupTest {

    @Test
    void test() throws IOException {
//        File file = new File("D:\\IdeaProjects\\ToolMan\\service\\service_reptile\\src\\test\\java\\work\\wlwl\\toolman\\service\\reptile\\test.html");
//        Document pares = Jsoup.parse(file, "UTF-8");
//        Document pares = JsoupUtils.parse("https://item.jd.com/100016437168.html");
        String s = HttpUtil.get("https://p.3.cn/prices/mgets?skuIds=J_100009077475");
        JSONObject parse = JSON.parseObject(JSON.parseArray(s).get(0).toString());
        System.out.println(parse);
        Object p = parse.get("p");
        Object op = parse.get("op");
        log.info("现价{}，原价{}", p, op);
        System.out.println(new Edition().setInitPrice(new BigDecimal(op.toString())));


    }


    @Test
    void Test() {
        Document parse = JsoupUtils.parse("https://item.jd.com/100009077475.html");
//        Element nameE = parse.selectFirst(".sku-name");
//        Edition edition = new Edition();
//        String name = nameE == null ? " " : nameE.text();
//        edition.setName(name);
//        Element typeDoc = parse.selectFirst(".p-choose[data-type='版本']");
//        if (typeDoc == null) {
//            System.out.println("嘎，挂了");
//            return;
//        }
//        Elements ramDoc = typeDoc.select(".selected").remove();
//        String ram = ramDoc.text();
//        edition.setRam(ram);
////        获取价格
//        this.getPrice(edition);
//        System.out.println(edition);
//        List<Edition> list = new ArrayList<>();
//        Elements items = typeDoc.select(".item");
//        for (Element item : items) {
//            Edition ed = new Edition();
//            ed.setRam(item.text());
//            String sku = item.attr("data-sku");
//            ed.setSku(sku);
//
//            list.add(ed);
//        }
//        System.out.println(list);
        Element colorDoc = parse.selectFirst(".spec-items");
        System.out.println(colorDoc);
    }

    public void getPrice(Edition edition) {
        String context = HttpUtil.get("https://p.3.cn/prices/mgets?skuIds=J_100009077475");
        JSONObject json = JSON.parseObject(JSON.parseArray(context).get(0).toString());
        String p = (String) json.get("p");
        String op = (String) json.get("op");
        edition.setJdPrice(new BigDecimal(p));
        edition.setInitPrice(new BigDecimal(op));
    }

}

