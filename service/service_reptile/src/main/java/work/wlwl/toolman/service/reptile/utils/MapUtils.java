package work.wlwl.toolman.service.reptile.utils;

import java.util.Map;

public class MapUtils {

    public static String getV(Map<String, String> map, String target) {
        return map.get(target) == null ? " " : map.get(target);
    }
}
