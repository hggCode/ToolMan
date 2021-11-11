package work.wlwl.toolman.service.statistics.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import work.wlwl.toolman.service.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 网站统计日数据
 * </p>
 *
 * @author hgg&fbb
 * @since 2021-10-16
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("statistics_daily")
@ApiModel(value = "Daily对象", description = "网站统计日数据")
public class Daily extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("统计日期")
    private String dateCalculated;

    @ApiModelProperty("注册人数")
    private Integer registerNum;

    @ApiModelProperty("登录人数")
    private Integer loginNum;


}
