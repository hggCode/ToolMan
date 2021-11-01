package work.wlwl.toolman.service.reptile.service;

import org.jsoup.nodes.Document;
import work.wlwl.toolman.service.reptile.entity.Brand;

import java.util.List;

public interface JDService {


    //更新品牌
    String saveOrUpdateBrand();

    //获取品牌id
    String getBrandId(Document document);

    //根据sku保存品牌
    int saveProductBySku(String sku, String id);

    //获取购买次数
    String getBuyCountBySku(String sku);

    //根据品牌获取品牌下的产品
    int saveProductByBrand(Brand brand);

    //根据品牌获取品牌下的产品
    int saveProductByBrand(List<Brand> brands);

}
