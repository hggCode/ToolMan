package work.wlwl.toolman.service.reptile.service;

import work.wlwl.toolman.service.reptile.entity.Brand;
import work.wlwl.toolman.service.reptile.entity.Product;

import java.util.List;

public interface JDService {


    //更新品牌
    int saveOrUpdateBrand();

    //保存产品根据sku
    boolean saveProductBySku(String sku);

    //保存产品的版本
    int saveEdition(Product product);

    //保存产品的颜色和图片
    int saveColor(Product product);

    //根据品牌获取品牌下的产品
    int saveProductByBrand(Brand brand);

    //根据品牌获取品牌下的产品并保存
    int saveProductByBrand(List<Brand> brands);




}
