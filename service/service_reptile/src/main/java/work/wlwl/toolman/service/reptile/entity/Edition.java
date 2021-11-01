package work.wlwl.toolman.service.reptile.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
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
@TableName("reptile_edition")
@ApiModel(value = "Edition对象", description = "")
public class Edition extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("产品id")
    private String productId;

    @ApiModelProperty("对于京东sku")
    private String sku;

    @ApiModelProperty("商品名")
    private String name;

    @ApiModelProperty("发布价格")
    private BigDecimal initPrice;

    @ApiModelProperty("京东参考价格")
    private BigDecimal jdPrice;

    @ApiModelProperty("封面")
    private String avatar;

    @ApiModelProperty("内存")
    private String ram;


}
