package work.wlwl.toolman.service.reptile.entity;

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
 * 
 * </p>
 *
 * @author hgg&fbb
 * @since 2021-11-01
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("reptile_property")
@ApiModel(value = "Property对象", description = "")
public class Property extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("对应产品id")
    private String productId;

    @ApiModelProperty("对应属性id")
    private String typeId;

    @ApiModelProperty("具体的值")
    private String value;


}
