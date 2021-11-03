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
            "token=d0de9d98d01139b14625f68e350b5583,1,908855; __tk=rcSD2zTsrUfErAanqwaAqYa5qY152wTXqAsEqY2XKUM,1,908855; shshshfp=29ababa60016796b13365f66939502a2; shshshfpa=af504b83-c7c4-1a0a-8c48-248c21c4ed62-1635939887; shshshsID=b6456933d484800c807af08732a1b4ec_1_1635939887442; __jda=122270672.16359398874611896024716.1635939887.1635939887.1635939887.1; __jdb=122270672.1.16359398874611896024716|1.1635939887; __jdc=122270672; __jdv=122270672|direct|-|none|-|1635939887461; shshshfpb=cR4%2By6%2FIodc4arIUsVgswXg%3D%3D; __jdu=16359398874611896024716; areaId=19; ipLoc-djd=19-1704-37734-0; ip_cityCode=1704; 3AB9D23F7A4B3C9B=YSSCPW7A2S3GTKF4DS7H2H2KFGIPUZALRZI53TWY352MI7YG4CSSFU6362ZDUZN3HAD5YLO3GGKQZPWZJBKVWDHEMM";
    private static int TIMEOUT = 3000;

    /**
     * 自定义连接超时
     *
     * @param url
     * @param timeout
     * @return
     */
    public static Document pares(String url, int timeout) {
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
    public static Document pares(String url) {
        return pares(url, TIMEOUT);
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
