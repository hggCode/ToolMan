package work.wlwl.toolman.service.ucenter.controller;


import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import work.wlwl.toolman.common.base.utils.JwtInfo;
import work.wlwl.toolman.common.base.utils.JwtUtils;
import work.wlwl.toolman.service.base.entity.R;
import work.wlwl.toolman.service.base.entity.ResultCodeEnum;
import work.wlwl.toolman.service.base.exception.GlobalException;
import work.wlwl.toolman.service.ucenter.entity.vo.AdminVo;
import work.wlwl.toolman.service.ucenter.service.AdminService;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author hgg&fbb
 * @since 2021-11-11
 */
@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @ApiOperation(value = "管理员登录")
    @PostMapping("login")
    public R login(AdminVo adminVo) {
        String token = adminService.login(adminVo);
        return R.ok().data("token", token);
    }

    @ApiOperation(value = "根据token获取登录信息")
    @GetMapping("getInfo")
    public R getLoginInfo(HttpServletRequest request) {
        try {
            JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);
            return R.ok().data("info", jwtInfo);
        } catch (Exception e) {
            log.error("解析用户信息失败，" + e.getMessage());
            throw new GlobalException(ResultCodeEnum.FETCH_USERINFO_ERROR);
        }
    }

}
