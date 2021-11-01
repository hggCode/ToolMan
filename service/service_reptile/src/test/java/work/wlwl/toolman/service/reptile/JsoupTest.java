package work.wlwl.toolman.service.reptile;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;
import work.wlwl.toolman.service.reptile.entity.Product;
import work.wlwl.toolman.service.reptile.utils.JsoupUtils;
import work.wlwl.toolman.service.reptile.utils.StrUtils;

import java.io.File;
import java.io.IOException;

public class JsoupTest {

    @Test
    void parse() throws IOException {
//        String body = HttpRequest.get("https://item.jd.com/100009077475.html")
//                .cookie("__jdu=204027574; shshshfpa=be3bcf8d-d9a2-9b5f-6754-1aa888f81d6c-1569927037; shshshfpb=j8YS35Dx8K0Oj1Rmu%20Y5rXQ%3D%3D; pinId=pJkB_WBlWVdLToFYknyE27V9-x-f3wj7; user-key=68d4ea22-bf1c-4db1-bf02-d83037c61efa; areaId=19; ipLoc-djd=19-1704-37734-0; pin=jd_74926756b91f7; unick=%E7%B5%AF%E7%A8%9A%E5%95%8Ai; _tp=QE86QOagwtLuqm62qwjNznaSoR4avzz1tlJlDaPaqRI%3D; _pst=jd_74926756b91f7; unpl=V2_ZzNtbUVWQ0B3CEcGKxBfAGJUQVxKXkBBcV1CBi5LVQEyBhMKclRCFnUURlVnG1wUZwEZXEdcQBZFCEdkfh1eAmUzIlxyVEMlfThFVX4YXwNjBRdtclBzJS5dKFF8Hw9VZ1MSXUEFFB1FCENWex5dAGUAGm1DZ0MQcABHVHkYXwxXSHxcD1RCEHQLQFB9HGwEVwA%3d; __jdv=76161171|www.infinitynewtab.com|t_45363_|tuiguang|601d310ca8244fb0982e5d5cdc85d40f|1635656663519; __jdc=122270672; shshshfp=29ababa60016796b13365f66939502a2; __jda=122270672.204027574.1569927029.1635656664.1635659657.196; wlfstk_smdl=7rxrxjytii9ly6nk23n4vrt2m8n647sb; TrackID=1qfaFhQG_lx4p9-XWE1SpN_23cKWOPmlWakUPSsO07L0a2mkJZdY1A1fXUbiuuVsopjS82EIXx2L_55wDV8qWhk0yo6onxYD99cf-fxDDGi4; ceshi3.com=103; token=abeb6421597800d9fb8a65b45a384d67,3,908702; __tk=4605fd3f178518a3962345d1ed6b9d1d,3,908702; shshshsID=5539f9aeff689e79eb3b3d20264144cf_19_1635664988997; __jdb=122270672.25.204027574|196.1635659657; thor=FC6CBA4620A3C29B2B20A57D6761D33A451800D4A7553E5256125795EC85021AE4AF0FF0F049A276B789A7E21B8E7CA73188942F4C466B7487A2060189E978804D8C040122E05C216A78DDE53ED0BFC5B4B9252E5B6C9FE7E8973BE470EF67F0A3FC77E03B67D8CC49FDF69ECA7F7B59625C434131ADFC95C01483483AF3557923B1CD1BDE8712D1A6B998356849BB4D053E2F7A39C315A21B109644F7010062; seckillSku=100009077475; seckillSid=; ip_cityCode=1704; 3AB9D23F7A4B3C9B=V62PSDHT7T7GU4LOE7MJDTRVMSX3ISPGCHM543NYFO5TVVRWHKJSRHXNTOF75ZOZXHTPKEATWMJXA7NRLGWOJL6CQI")
//                .execute()
//                .body();
//        System.out.println(body);
        Product product = new Product();
        File file = new File("D:\\IdeaProjects\\ToolMan\\service\\service_reptile\\src\\test\\java\\work\\wlwl\\toolman\\service\\reptile\\test.html");
        Document document = Jsoup.parse(file, "UTF-8");
//        设置产品名称
        String title = document.title();
        product.setName(title);
//        System.out.println(StrUtils.getTitle(title));

        String data = document.select("script[charset]").first().data();
//        String sku = StrUtils.getAttr(data, "skuid");
        String brandId = StrUtils.getAttr(data, "brand");
        product.setBrandId(brandId);
        System.out.println(product);
    }

    @Test
    void test() {
        Document document = JsoupUtils.pares("https://item.jd.com/100009077475.html");
        System.out.println(document);
        Element first = document.select("script[charset]").first();
        System.out.println("=================");
        if (first==null){
            return;
        }
        System.out.println(first.data());
    }
}

