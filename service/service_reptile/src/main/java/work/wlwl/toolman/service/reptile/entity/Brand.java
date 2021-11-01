package work.wlwl.toolman.service.reptile.entity;

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
 * @since 2021-11-01
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("reptile_brand")
@ApiModel(value = "Brand对象", description = "")
public class Brand extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("品牌名字")
    private String name;


    @ApiModelProperty("排名")
    private int ranking;


}
