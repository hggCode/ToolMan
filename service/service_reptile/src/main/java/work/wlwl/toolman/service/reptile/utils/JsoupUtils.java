package work.wlwl.toolman.service.reptile.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 海哥哥
 */
public class JsoupUtils {


    private static final String COOKIE =
            "__jdu=204027574; shshshfpa=be3bcf8d-d9a2-9b5f-6754-1aa888f81d6c-1569927037; shshshfpb=j8YS35Dx8K0Oj1Rmu%20Y5rXQ%3D%3D; pinId=pJkB_WBlWVdLToFYknyE27V9-x-f3wj7; user-key=68d4ea22-bf1c-4db1-bf02-d83037c61efa; areaId=19; ipLoc-djd=19-1704-37734-0; pin=jd_74926756b91f7; unick=%E7%B5%AF%E7%A8%9A%E5%95%8Ai; _tp=QE86QOagwtLuqm62qwjNznaSoR4avzz1tlJlDaPaqRI%3D; _pst=jd_74926756b91f7; mt_xid=V2_52007VwATV1xbVF4ZTCldAGYBFVBbX04OGxobQAAwA0JOVQsAWQNOHVoBZAIWUV5dUF4vShhfBXsDEk5cXUNbHkIbVA5mCiJQbVhiXxdPHVQAZQQXUG1YVF4b; TrackID=1K3Rea1KnyEgTgZdKShD-R2CSTcYu-1BqXT9UMts3C9EeYwinXfkJKOrE9znh98ADvWwGBqdh53AQhs0b2v0aCZRfpC2acddedFXYQaI30sk; unpl=V2_ZzNtbUNWQUF3DRQGLB5eAWIKEQ0SXhYWcw8WVHsbVABmBxIOclRCFnUURlRnGFkUZwQZXkpcQh1FCEdkfh1eAmUzIlxyVEMlfThFVX4YXwNjBRdtclBzJS5dKFF8Hw9VZ1MSXUEFFB1FCENWex5dAGUAGm1DZ0MQcABHVHkYXwxXSHxcD1RCEHQLQFB9HGwEVwA%3d; __jdv=76161171|www.infinitynewtab.com|t_45363_|tuiguang|003e34ccf635482aa8d276a11394051b|1636014549742; __jdc=122270672; shshshfp=29ababa60016796b13365f66939502a2; __jda=122270672.204027574.1569927029.1636011854.1636028917.219; token=c4a84cba656380ae4f6e179e752bce0a,2,908905; __tk=442bb59c7681380da5fa976b9a231c0a,2,908905; shshshsID=823b55d2d97cf8a5ec36191ec87da783_6_1636030479728; __jdb=122270672.6.204027574|219.1636028917; 3AB9D23F7A4B3C9B=V62PSDHT7T7GU4LOE7MJDTRVMSX3ISPGCHM543NYFO5TVVRWHKJSRHXNTOF75ZOZXHTPKEATWMJXA7NRLGWOJL6CQI";
    private static int TIMEOUT = 5000;

    /**
     * 自定义连接超时
     *
     * @param url
     * @param timeout
     * @return
     */
    public static Document parse(String url, int timeout) {
        if (!StringUtils.hasLength(url)) {
            return null;
        }
        Document document = null;
        try {
            document = Jsoup.connect(url)
                    .headers(initHeader())
                    .cookies(CookieUtils.cookie2Map(COOKIE))
                    .timeout(timeout)
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return document;
    }

    /**
     * 默认自定义超时3s
     *
     * @param url
     * @return
     */
    public static Document parse(String url) {
        return parse(url, TIMEOUT);
    }

    private static Map<String, String> initHeader() {
        Map<String, String> map = new HashMap<>();
        map.put("cache-control", "max-age=0");
        map.put("sec-ch-ua", "Chromium\";v=\"92\", \" Not A;Brand\";v=\"99\", \"Google Chrome\";v=\"92\"");
        map.put("sec-ch-ua-mobile", "?0");
        map.put("upgrade-insecure-requests", "1");
        map.put("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36");
        map.put("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        map.put("sec-fetch-site", "same-site");
        map.put("sec-fetch-mode", "navigate");
        map.put("sec-fetch-user", "?1");
        map.put("sec-fetch-dest", "document");
        map.put("referer", "https://search.jd.com/");
        map.put("accept-encoding", "gzip, deflate, br");
        map.put("accept-language", "zh-CN,zh;q=0.9");
        return map;
    }
}
