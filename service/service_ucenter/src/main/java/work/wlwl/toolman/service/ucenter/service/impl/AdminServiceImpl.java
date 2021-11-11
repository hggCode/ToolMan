package work.wlwl.toolman.service.ucenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import work.wlwl.toolman.common.base.utils.JwtInfo;
import work.wlwl.toolman.common.base.utils.JwtUtils;
import work.wlwl.toolman.common.base.utils.MD5;
import work.wlwl.toolman.service.base.entity.ResultCodeEnum;
import work.wlwl.toolman.service.base.exception.GlobalException;
import work.wlwl.toolman.service.ucenter.entity.Admin;
import work.wlwl.toolman.service.ucenter.entity.vo.AdminVo;
import work.wlwl.toolman.service.ucenter.mapper.AdminMapper;
import work.wlwl.toolman.service.ucenter.service.AdminService;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author hgg&fbb
 * @since 2021-11-11
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

    @Override
    public String login(AdminVo adminVo) {
        String name = adminVo.getName();
        String password = adminVo.getPassword();

        if (!StringUtils.hasLength(name)) {
            throw new GlobalException(ResultCodeEnum.PARAM_ERROR);
        }
//        查询管理员是否存在
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        Admin admin = baseMapper.selectOne(queryWrapper);
        if (admin == null) {
            throw new GlobalException(ResultCodeEnum.LOGIN_MOBILE_ERROR);
        }

        //校验密码
        if (!MD5.encrypt(password).equals(admin.getPassword())) {
            throw new GlobalException(ResultCodeEnum.LOGIN_PASSWORD_ERROR);
        }
        JwtInfo jwtInfo = new JwtInfo();
        jwtInfo.setId(admin.getId());
        jwtInfo.setNickname(admin.getName());
        jwtInfo.setAvatar("https://tool-man.oss-cn-beijing.aliyuncs.com/avatar/default_handsome.jpg");
        return JwtUtils.getJwtToken(jwtInfo, 1800);
    }
}
