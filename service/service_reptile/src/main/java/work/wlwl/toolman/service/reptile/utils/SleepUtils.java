package work.wlwl.toolman.service.reptile.utils;

import java.util.concurrent.TimeUnit;

public class SleepUtils {

    /**
     *  休眠  单位s
     * @param time 秒
     */
    public static void seconds(int time) {
        try {
            TimeUnit.SECONDS.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 休眠
     * @param time 毫秒
     */
    public static void millisSeconds(int time) {
        try {
            TimeUnit.MILLISECONDS.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
