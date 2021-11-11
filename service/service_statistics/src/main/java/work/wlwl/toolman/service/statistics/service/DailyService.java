package work.wlwl.toolman.service.statistics.service;

import work.wlwl.toolman.service.statistics.entity.Daily;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 网站统计日数据 服务类
 * </p>
 *
 * @author hgg&fbb
 * @since 2021-10-16
 */
public interface DailyService extends IService<Daily> {

    void createStatisticsByDay(String day);
}
