package work.wlwl.toolman.service.reptile.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * @author 海哥哥
 */
public class JsoupUtils {

    private static final String USERAGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36";

    private static final String COOKIE ="__jdu=204027574; shshshfpa=be3bcf8d-d9a2-9b5f-6754-1aa888f81d6c-1569927037; shshshfpb=j8YS35Dx8K0Oj1Rmu%20Y5rXQ%3D%3D; pinId=pJkB_WBlWVdLToFYknyE27V9-x-f3wj7; user-key=68d4ea22-bf1c-4db1-bf02-d83037c61efa; areaId=19; ipLoc-djd=19-1704-37734-0; pin=jd_74926756b91f7; unick=%E7%B5%AF%E7%A8%9A%E5%95%8Ai; _tp=QE86QOagwtLuqm62qwjNznaSoR4avzz1tlJlDaPaqRI%3D; _pst=jd_74926756b91f7; unpl=V2_ZzNtbUVWQ0B3CEcGKxBfAGJUQVxKXkBBcV1CBi5LVQEyBhMKclRCFnUURlVnG1wUZwEZXEdcQBZFCEdkfh1eAmUzIlxyVEMlfThFVX4YXwNjBRdtclBzJS5dKFF8Hw9VZ1MSXUEFFB1FCENWex5dAGUAGm1DZ0MQcABHVHkYXwxXSHxcD1RCEHQLQFB9HGwEVwA%3d; __jdv=76161171|www.infinitynewtab.com|t_45363_|tuiguang|601d310ca8244fb0982e5d5cdc85d40f|1635656663519; ceshi3.com=103; TrackID=1gtYvne06uukTjKH5j3T0mILuY7bRg2gMc6QY8aKBJETQKyVloMcuAgF6x3Q_TOiKbQZn5gs77Ad0mYeydmfhlkhdYNFoy9FqSxpPuqMC8wA; __jdc=122270672; shshshfp=29ababa60016796b13365f66939502a2; 3AB9D23F7A4B3C9B=YSSCPW7A2S3GTKF4DS7H2H2KFGIPUZALRZI53TWY352MI7YG4CSSFU6362ZDUZN3HAD5YLO3GGKQZPWZJBKVWDHEMM; token=56c5b2327ff5f25d603ce170a27a93d5,3,908756; __tk=kreDAroFjrawjua1jzAqjugEAugzAU4yBVGyBVs5kVs,3,908756; shshshsID=f688f026297a696adc72eb0b8a31fae5_1_1635761618504; __jda=122270672.204027574.1569927029.1635756175.1635761619.204; __jdb=122270672.1.204027574|204.1635761619; thor=FC6CBA4620A3C29B2B20A57D6761D33A451800D4A7553E5256125795EC85021AD037A570733849E2A700227A6C9EFD90ED07CAA3BFBF9157EA5E2C2E2C29336512FD24C73AD42E3C16B9F69AFC02EE20F6639394A939176D4AB3016B442BBA45D6F42A6A239F3BBBBDC53900927EF973D2BAC7C3567B3B15E383036AC866DE10F9955F5A91AA3F0B5A53DC4557D257816266BC137AE70F06DC45DB6E94CF408A; seckillSku=100026667884; seckillSid=; ip_cityCode=1704";
    /**
     * 自定义连接超时
     *
     * @param url
     * @param timeout
     * @return
     */
    public static Document pares(String url, int timeout) {
        Document document = null;
        try {
            document = Jsoup.connect(url)
                    .timeout(timeout)
                    .userAgent(USERAGENT)
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
        Document document = null;
        try {
            document = Jsoup.connect(url)
                    .timeout(3000)
                    .cookies(CookieUtils.cookie2Map(COOKIE))
                    .userAgent(USERAGENT)
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return document;
    }
}
