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
@TableName("reptile_product")
@ApiModel(value = "Product对象", description = "")
public class Product extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("品牌id")
    private String brandId;

    @ApiModelProperty("默认sku")
    private String mainSku;

    @ApiModelProperty("商品名")
    private String name;

    @ApiModelProperty("排名")
    private Integer ranking;

    @ApiModelProperty("销售量 来着京东")
    private String buyCount;

    @ApiModelProperty("浏览量 ")
    private Long viewCount;


}
