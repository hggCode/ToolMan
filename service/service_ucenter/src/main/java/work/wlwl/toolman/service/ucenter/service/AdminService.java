package work.wlwl.toolman.service.ucenter.service;

import work.wlwl.toolman.service.ucenter.entity.Admin;
import com.baomidou.mybatisplus.extension.service.IService;
import work.wlwl.toolman.service.ucenter.entity.vo.AdminVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hgg&fbb
 * @since 2021-11-11
 */
public interface AdminService extends IService<Admin> {

    String login(AdminVo adminVo);
}
