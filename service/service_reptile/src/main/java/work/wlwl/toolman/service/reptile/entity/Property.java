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
 * @since 2021-11-03
 */
@Data
@Accessors(chain = true)
@TableName("reptile_property")
@ApiModel(value = "Property对象", description = "")
public class Property extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("对应产品id")
    private String productId;

    @ApiModelProperty("上市时间")
    private String initDate;

    @ApiModelProperty("产品名")
    private String name;

    @ApiModelProperty("手机尺寸（长宽高）")
    private String size;

    @ApiModelProperty("机身颜色颜色")
    private String color;

    @ApiModelProperty("手机重量")
    private String weight;

    @ApiModelProperty("内存")
    private String ram;

    @ApiModelProperty("屏幕材质")
    private String screenType;

    @ApiModelProperty("主屏尺寸")
    private String screenSize;

    @ApiModelProperty("屏幕刷新频率")
    private String displayRefresh;

    @ApiModelProperty("屏幕分辨率")
    private String qvga;

    @ApiModelProperty("处理器")
    private String cpu;

    @ApiModelProperty("充电口类型")
    private String chargerPort;

    @ApiModelProperty("耳机接口类型")
    private String avOut;

    @ApiModelProperty("nfc")
    private String nfc;

    @ApiModelProperty("SIM卡类型")
    private String simType;

    @ApiModelProperty("最大支持sim卡数量")
    private String simNum;

    @ApiModelProperty("5g")
    private String net5g;

    @ApiModelProperty("4g")
    private String net4g;

    @ApiModelProperty("3g")
    private String net3g;

    @ApiModelProperty("充电功率")
    private String chargerPower;

    @ApiModelProperty("前置摄像头")
    private String beforeCamera;

    @ApiModelProperty("后置摄像头")
    private String afterCamera;


}
