package work.wlwl.toolman.common.base.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtils {


    /**
     * 截取字符串中的数字
     *
     * @param context
     * @return 字符串型数字
     * @author hgg
     */
    public static String getNum(String context) {
        String regex = "(\\D+)";
        Pattern p = Pattern.compile(regex);//验证数字
        Matcher m = p.matcher(context);

        return m.replaceAll("").trim();
    }
}
