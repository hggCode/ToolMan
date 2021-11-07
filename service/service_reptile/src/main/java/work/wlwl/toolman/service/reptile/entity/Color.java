package work.wlwl.toolman.service.reptile.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import work.wlwl.toolman.service.base.entity.BaseEntity;

/**
 * <p>
 * 
 * </p>
 *
 * @author hgg&fbb
 * @since 2021-11-01
 */
@Data

@Accessors(chain = true)
@TableName("reptile_color")
@ApiModel(value = "Color对象", description = "")
public class Color extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("产品id")
    private String productId;

    @ApiModelProperty("颜色")
    private String color;

    @ApiModelProperty("封面")
    private String avatar;


}
