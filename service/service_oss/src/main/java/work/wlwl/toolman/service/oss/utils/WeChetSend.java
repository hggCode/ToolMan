package work.wlwl.toolman.service.oss.utils;

import cn.hutool.http.HttpUtil;

public class WeChetSend {
    public static void send(String title, String context) {
        HttpUtil.get("https://sctapi.ftqq.com/SCT82247TwjcZrqLtg6XlLLnYmYvZ97Kp.send?title=" + title + "&desp=" + context);
    }
}
