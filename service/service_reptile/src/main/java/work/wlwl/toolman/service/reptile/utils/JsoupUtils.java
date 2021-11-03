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
            "__jdu=204027574; shshshfpa=be3bcf8d-d9a2-9b5f-6754-1aa888f81d6c-1569927037; shshshfpb=j8YS35Dx8K0Oj1Rmu%20Y5rXQ%3D%3D; pinId=pJkB_WBlWVdLToFYknyE27V9-x-f3wj7; user-key=68d4ea22-bf1c-4db1-bf02-d83037c61efa; ipLoc-djd=19-1704-37734-0; areaId=19; pin=jd_74926756b91f7; unick=%E7%B5%AF%E7%A8%9A%E5%95%8Ai; _tp=QE86QOagwtLuqm62qwjNznaSoR4avzz1tlJlDaPaqRI%3D; _pst=jd_74926756b91f7; _contrast=100009077475; mt_xid=V2_52007VwATV1xbVF4ZTCldAGYBFVBbX04OGxobQAAwA0JOVQsAWQNOHVoBZAIWUV5dUF4vShhfBXsDEk5cXUNbHkIbVA5mCiJQbVhiXxdPHVQAZQQXUG1YVF4b; unpl=V2_ZzNtbRdfRRx1XREGKxpeBGJWEg1KUEEVcFpGUytJXgA3UxEOclRCFnUURlRnGFkUZgMZWERcQhdFCEdkfh1eAmUzIlxyVEMlfThFVX4YXwNjBRdtclBzJS5dKFF8Hw9VZ1MSXUEFFB1FCENWex5dAGUAGm1DZ0MQcABHVHkYXwxXSHxcD1RCEHQLQFB9HGwEVwA%3d; TrackID=1I37COc31c9Jc0B0_dXusNSRwUTdzj6JbHd6oY0OI3Iw2IGFCREcjQk3H4_jpWWzd0Lqyae19irrNZFKdcMN4o49G1cbuPRIZpHSo9RD00ZA; ceshi3.com=103; __jdv=76161171|www.infinitynewtab.com|t_45363_|tuiguang|d9781dfca2304d1a96314c16aa34aa2b|1635961642821; __jda=122270672.204027574.1569927029.1635957411.1635961628.216; __jdc=122270672; shshshfp=29ababa60016796b13365f66939502a2; _contrast_status=side; token=c24fd1c9c538d8f773839cc57e5a167c,3,908868; __tk=bb6538d30df312546fa9420c2e2418e9,3,908868; ip_cityCode=1704; thor=FC6CBA4620A3C29B2B20A57D6761D33A451800D4A7553E5256125795EC85021AB783F01623AA8519CC6E27A2FD99711A8AAB4E34687C7DD74F47C96CADFA65A72315092E60780D070979A35F2BA41479DBAE1189FBB57793938A1DEF41897642B6C663DBBDB6FEBA4A553378B1767BD59CDA04C58DC458FE8B83735824B318FA010F4C73AA654A2A9A23D6D2605CA63192C78BF7586E03A2B5A7961DD615E503; __jdb=122270672.13.204027574|216.1635961628; shshshsID=8d3cc0144f33147b4a4d3b209a70d6be_10_1635962539668; 3AB9D23F7A4B3C9B=V62PSDHT7T7GU4LOE7MJDTRVMSX3ISPGCHM543NYFO5TVVRWHKJSRHXNTOF75ZOZXHTPKEATWMJXA7NRLGWOJL6CQI";
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
