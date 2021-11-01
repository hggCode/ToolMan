package work.wlwl.toolman.service.reptile.utils;

import java.util.HashMap;
import java.util.Map;

public class CookieUtils {

    public static Map<String, String> cookie2Map(String cookie) {
        Map<String, String> map = new HashMap<>();
        String[] cookies = cookie.trim().split(";");
        for (String item : cookies) {
            String[] split = item.trim().split("=");
            if (split.length > 1) {
                map.put(split[0], split[1]);
            }
        }
        return map;
    }
}
