package work.wlwl.toolman.service.reptile.service;

import work.wlwl.toolman.service.reptile.entity.Brand;
import work.wlwl.toolman.service.reptile.entity.Product;

import java.util.List;

public interface JDService {


    //更新品牌
    String saveOrUpdateBrand();


    //根据品牌获取品牌下的产品
    int saveProductByBrand(Brand brand);

    //根据品牌获取品牌下的产品并保存
    int saveProductByBrand(List<Brand> brands);

    boolean deleteBrand();

    int savePropertyBySku(Brand sku);

    int savePropertyBySku(List<Brand> brandIds);

    int saveEdition(Product product);


}
