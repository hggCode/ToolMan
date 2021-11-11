package work.wlwl.toolman.service.statistics.service.impl;

import work.wlwl.toolman.service.statistics.entity.Daily;
import work.wlwl.toolman.service.statistics.mapper.DailyMapper;
import work.wlwl.toolman.service.statistics.service.DailyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author hgg&fbb
 * @since 2021-10-16
 */
@Service
public class DailyServiceImpl extends ServiceImpl<DailyMapper, Daily> implements DailyService {

    @Override
    public void createStatisticsByDay(String day) {

    }
}
