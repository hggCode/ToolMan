package work.wlwl.toolman.service.ucenter.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import work.wlwl.toolman.service.base.entity.BaseEntity;

/**
 * <p>
 * 
 * </p>
 *
 * @author hgg&fbb
 * @since 2021-11-11
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("ucenter_admin")
@ApiModel(value = "Admin对象", description = "")
public class Admin extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("昵称")
    private String name;

    @ApiModelProperty("密码")
    private String password;


}
