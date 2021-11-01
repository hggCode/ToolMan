package work.wlwl.toolman.service.reptile.utils;


import org.springframework.util.StringUtils;
import work.wlwl.toolman.service.base.exception.GlobalException;

import java.util.Arrays;
import java.util.List;

/**
 * 字符串操作类
 *
 * @author 海哥哥
 * @since 2021.10.31
 */
public class StrUtils {


    /**
     * 截取文本中指定的内容 从str到end 不包含start 跟end
     *
     * @param context
     * @param start
     * @param end
     * @return
     */
    public static String subStr(String context, String start, String end) {
        return subStr(context, start, end, true);
    }

    /**
     * 截取文本中指定的内容 从str到end 不包含start 跟end
     *
     * @param context 目标内容
     * @param start   起始内容
     * @param end
     * @param flag    true 获取第一个  false 获取最后一个
     * @return
     */
    public static String subStr(String context, String start, String end, boolean flag) {
        if (!StringUtils.hasLength(context)) {
            return "-1";
        }
        context = context.trim();
        int start_index;
        if (flag) {
            start_index = context.indexOf(start);
        } else {
            start_index = context.lastIndexOf(start);
        }
        if (start_index == -1) {
            System.out.println("在context中找不到start语句-->" + start + "哦,context内容长度为" + context.length());
            return "-1";
        }
        int end_index = context.indexOf(end, start_index + start.length());
        if (end_index == -1) {
            System.out.println("在context中找不到end语句-->" + end + "哦,context内容长度为" + context.length());
            return "-1";
        }
        System.out.println("start_index===>" + start_index + "  end_index===>" + end_index);
//      因为substring 默认包含前面 所以要去掉
        context = context.substring(start_index + start.length(), end_index);
        return context.trim();
    }


    /**
     * 按标识符(delimiter)将context分隔成list
     *
     * @param context
     * @param delimiter
     * @return
     */
    public static List<String> split(String context, String delimiter) {
        String[] split = context.split(delimiter);
        return Arrays.asList(split);
    }

    /**
     * 检查context中是否含有target
     *
     * @param context
     * @param target
     * @return
     */
    public static Boolean contain(String context, String target) {
        context = context.trim();
        boolean flag = context.matches("(.*)" + target + "(.*)");
        return flag;
    }

    /**
     * @param context
     * @param start
     * @param end
     * @return 返回指定删除的内容
     */
    public static String rm(String context, String start, String end) {
        String regex = start + "(.*)" + end;
        return context.replaceAll(regex, "");
    }

    /**
     * 获取js中属性值
     *
     * @param context 需要操作的上下文
     * @param attr    需要获取的属性名
     * @return 属性值
     */
    public static String getAttr(String context, String attr) {
        return subStr(context, attr + ": ", ",");
    }

    /**
     * 从产品标题中获取名称
     *
     * @param context
     * @return
     */
    public static String getName(String context) {
        return subStr(context, "【", "】");
    }


}
