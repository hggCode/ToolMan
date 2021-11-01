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
@TableName("reptile_product_img")
@ApiModel(value = "ProductImg对象", description = "")
public class ProductImg extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("产品id")
    private String productId;

    @ApiModelProperty("图片地址")
    private String src;

    @ApiModelProperty("显示排序")
    private Integer sort;


}
