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
            "__jdu=204027574; shshshfpa=be3bcf8d-d9a2-9b5f-6754-1aa888f81d6c-1569927037; shshshfpb=j8YS35Dx8K0Oj1Rmu%20Y5rXQ%3D%3D; pinId=pJkB_WBlWVdLToFYknyE27V9-x-f3wj7; user-key=68d4ea22-bf1c-4db1-bf02-d83037c61efa; areaId=19; ipLoc-djd=19-1704-37734-0; pin=jd_74926756b91f7; unick=%E7%B5%AF%E7%A8%9A%E5%95%8Ai; _tp=QE86QOagwtLuqm62qwjNznaSoR4avzz1tlJlDaPaqRI%3D; _pst=jd_74926756b91f7; _contrast=100009077475; mt_xid=V2_52007VwATV1xbVF4ZTCldAGYBFVBbX04OGxobQAAwA0JOVQsAWQNOHVoBZAIWUV5dUF4vShhfBXsDEk5cXUNbHkIbVA5mCiJQbVhiXxdPHVQAZQQXUG1YVF4b; unpl=V2_ZzNtbRBTFEZ2XxRcKR4IAmIDEwoRAxEXIF9AVHtMVQJkBhFbclRCFnUURlRnGFkUZgIZWEBcQxxFCEdkfh1eAmUzIlxyVEMlfThFVX4YXwNjBRdtclBzJS5dKFF8Hw9VZ1MSXUEFFB1FCENWex5dAGUAGm1DZ0MQcABHVHkYXwxXSHxcD1RCEHQLQFB9HGwEVwA%3d; TrackID=1AKrHIoK_lFgTG0y81oKHJDlqVjIxflO_pzVfIJo8oX9CHyPc7y9ZUfNEmXeaB3jzv845X9-DuoHRGDX42mzuh1y5gvlM9KhpsdHoTBZ0Wc0; thor=FC6CBA4620A3C29B2B20A57D6761D33A451800D4A7553E5256125795EC85021A06EC6407AB676DEBC4855E1E29CF9480217F551B2477AB09568644BBA1D2C8FA17C2F416CE28D680DBFF2924AD262198B0FE05C181F0D4FE487722CDEE4A38C0A893ED1BE1DC4C7D750DB3EBD4ADDBCBAA6444C4201BE2D5F79DB916F44E50C9C7E860807A470767275051360C3D5DF4BD072FCBD1250CF66986EAAD5174DEF7; ceshi3.com=103; __jdv=76161171|www.infinitynewtab.com|t_45363_|tuiguang|c5fb2fc9c6e6410fbec3df711d862427|1635957808502; __jda=122270672.204027574.1569927029.1635952678.1635957411.215; __jdc=122270672; shshshfp=29ababa60016796b13365f66939502a2; token=1f4ea6fd356ba7e600adcce9fae67dc0,3,908865; __tk=YpkDusaxXzq5ZUYSuphFY3kzvcYTvcuzXSkEYpXDuSX,3,908865; shshshsID=2a1f0a4b26fd8427ecae25b622341294_9_1635957815836; __jdb=122270672.12.204027574|215.1635957411; _contrast_status=show; ip_cityCode=1704; 3AB9D23F7A4B3C9B=V62PSDHT7T7GU4LOE7MJDTRVMSX3ISPGCHM543NYFO5TVVRWHKJSRHXNTOF75ZOZXHTPKEATWMJXA7NRLGWOJL6CQI";
    private static int TIMEOUT = 3000;

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
