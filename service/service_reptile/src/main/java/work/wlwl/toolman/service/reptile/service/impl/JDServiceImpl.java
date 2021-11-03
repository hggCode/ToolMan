package work.wlwl.toolman.service.reptile.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import work.wlwl.toolman.common.base.utils.RegexUtils;
import work.wlwl.toolman.service.base.entity.ResultCodeEnum;
import work.wlwl.toolman.service.base.exception.GlobalException;
import work.wlwl.toolman.service.reptile.config.ReptileUrl;
import work.wlwl.toolman.service.reptile.entity.Brand;
import work.wlwl.toolman.service.reptile.entity.Product;
import work.wlwl.toolman.service.reptile.entity.Property;
import work.wlwl.toolman.service.reptile.service.BrandService;
import work.wlwl.toolman.service.reptile.service.JDService;
import work.wlwl.toolman.service.reptile.service.ProductService;
import work.wlwl.toolman.service.reptile.service.PropertyService;
import work.wlwl.toolman.service.reptile.utils.JsoupUtils;
import work.wlwl.toolman.service.reptile.utils.StrUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class JDServiceImpl implements JDService {

    @Autowired
    private BrandService brandService;

    @Autowired
    private ReptileUrl reptileUrl;

    @Autowired
    private ProductService productService;

    @Autowired
    private PropertyService propertyService;


    /**
     * 爬取品牌列表
     *
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveOrUpdateBrand() {
        String url = reptileUrl.getTestUrl();
        Document context = JsoupUtils.parse(url);
        int ranking = 1;
        Elements elements = context.getElementsByClass("v-fixed");
        if (elements.size() == 0) {
            return "更新出错";
        }
        List<Brand> list = new ArrayList<>();
        for (Element element : elements) {
            Elements items = element.select("li");
            for (Element item : items) {
                Brand brand = new Brand();
                String id = item.attr("id");
                id = RegexUtils.getNum(id);
                brand.setId(id);
                String name = item.select("a").text();
                brand.setName(name);
                brand.setRanking(ranking++);
                list.add(brand);
            }
        }
        brandService.saveOrUpdateBatch(list);
        return "成功";
    }


    /**
     * 根据skuList批量保存product
     *
     * @param skuList
     * @param id
     * @return
     */
    @Override
    public int saveProductBySku(List<String> skuList, String id) {
        List<Product> productList = new ArrayList<>();
//        遍历skuList中的每一个sku对应的产品
        for (String sku : skuList) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String url = reptileUrl.getItemUrl() + sku + ".html";
            Document document = JsoupUtils.parse(url);
            Product product = new Product();
            String name = document.title();
            name = StrUtils.getName(name);
            product.setName(name);  //产品名
            product.setMainSku(sku);   //默认sku
            String buyCount = this.getBuyCountBySku(sku);
            String brandId = this.getBrandId(document);
            if ("-1".equals(brandId) || "-1".equals(buyCount)) {
                log.error(sku);
                throw new GlobalException(ResultCodeEnum.JD_CONNECT_ERROR);
            }
            if (StringUtils.hasLength(id) && !id.equals(brandId)) {
//              当前产品的品牌id不等于传进来的品牌id
                continue;
            }
            product.setBuyCount(buyCount);  //购买数
            product.setBrandId(brandId);
            productList.add(product);
        }
        boolean b = productService.saveOrUpdateBatch(productList);
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return productList.size();
    }


    //    根据品牌获取品牌下的所有的产品
    @Override
    public int saveProductByBrand(Brand brand) {
        String brandName = brand.getName();
        String url = reptileUrl.getGetProductByBrandUrl() + brandName;
        Document context = JsoupUtils.parse(url);
        Element element = context.select(".gl-warp").first();
        List<String> skuList = new ArrayList<>();
        if (element == null) {
//            被京东拦截
            log.error("一条都没插进去就被京东嘎了");
            throw new GlobalException(ResultCodeEnum.JD_CONNECT_ERROR);
        }
//            获取到每个产品
        Elements items = element.select("li");
        for (Element item : items) {
//                剔除不是直营的
            Elements e = item.getElementsByClass("goods-icons");
            if (e.size() == 0) {
                continue;
            }
            String sku = item.attr("data-sku");
            skuList.add(sku);
        }
        int count = this.saveProductBySku(skuList, brand.getId());

        return count;
    }

    @Override
    public int saveProductByBrand(List<Brand> brands) {
        int count = 0;
        for (Brand brand : brands) {
            count += this.saveProductByBrand(brand);
        }
        return count;
    }

    /**
     * 删除jd自营都没有的手机品牌
     *
     * @return
     */
    @Override
    public boolean deleteBrand() {
        List<Brand> list = brandService.list();
        List<String> brandIds = new ArrayList<>();
        for (Brand brand : list) {
            String brandName = brand.getName();
            String url = reptileUrl.getGetProductByBrandUrl() + brandName;
            Document context = JsoupUtils.parse(url);
            Element element = context.getElementsByClass("gl-warp").first();
            int count = 0;
            if (element == null) {
                if (brandIds.size() > 0) {
                    brandService.removeByIds(brandIds);
                }
                log.error("被嘎前删了" + count + "条");
                throw new GlobalException(ResultCodeEnum.JD_CONNECT_ERROR);
            }
//            获取到每个产品
            Elements items = element.select("li");
            for (Element item : items) {
//                剔除不是直营的
                Elements e = item.getElementsByClass("goods-icons");
                if (e.size() == 0) {
                    continue;
                }
                count += 1;
            }
            if (count < 1) {
                brandIds.add(brand.getId());
            }

        }
        if (brandIds.size() > 0) {
            return brandService.removeByIds(brandIds);
        }
        return false;

    }

    @Override
    public boolean savePropertyBySku(Brand brand) {
        ArrayList<Property> properties = new ArrayList<>();
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("brand_id", brand.getId())
                .select("main_sku");
        List<Product> list = productService.list(queryWrapper);
        for (Product product : list) {
            try {
                TimeUnit.MILLISECONDS.sleep(3000);

                String sku = product.getMainSku();

                Document pares = JsoupUtils.parse(reptileUrl.getItemUrl() + sku + ".html");
                Elements e = pares.select(".Ptable");
                Element load = pares.select(".iloading").first();
                while (load != null) {
                    pares = JsoupUtils.parse(reptileUrl.getItemUrl() + sku + ".html");
                    load = pares.select(".iloading").first();
                    System.out.println("正在重新尝试" + sku);

                    TimeUnit.MILLISECONDS.sleep(3000);

                }

                if (e.first() == null) {
                    System.out.println(pares);
                    log.error(brand.getId());
                    throw new GlobalException(ResultCodeEnum.JD_CONNECT_ERROR);
                }
                Elements elements = e.select(".Ptable-item");
                Map<String, String> map = new HashMap<>();
                Property property = new Property();
                for (Element element : elements) {
                    String h3 = element.select("h3").text().trim();
                    String dl = element.select("dl").text().trim();
                    if ("前置摄像头".equals(h3) || "后置摄像头".equals(h3) || "主芯片".equals(h3)) {
                        map.put(h3, dl);
                    } else {
                        Elements select = element.select(".clearfix");
                        for (Element element1 : select) {
                            element1.select("p").remove();
                            String dt = element1.select("dt").text().trim();
                            String dd = element1.select("dd").text().trim();
                            map.put(dt, dd);
                        }
                    }
                }
                property.setName(map.get("产品名称"));
                property.setWeight(map.get("机身重量（g）"));
                String initDate = map.get("上市年份") + map.get("上市月份") + map.get("首销日期");
                property.setInitDate(initDate);
                property.setScreenSize(map.get("主屏幕尺寸"));
                property.setDisplayRefresh(map.get("屏幕刷新率"));
                property.setScreenType(map.get("屏幕材质类型"));
                property.setSimNum(map.get("最大支持SIM卡数量"));
                property.setSimType(map.get("SIM卡类型"));
                property.setNet_5g(map.get("5G网络"));
                property.setNet_4g(map.get("4G网络"));
                property.setNet_3g(map.get("3G/2G网络"));
                property.setAvOut(map.get("耳机接口类型"));
                property.setChargerPort(map.get("充电接口类型"));
                String size = map.get("机身长度（mm）") + "x" + map.get("机身宽度（mm）") + "x" + map.get("机身厚度(mm)");
                property.setSize(size);
                property.setNfc(map.get("NFC/NFC模式"));
                property.setBeforeCamera(map.get("前置摄像头"));
                property.setAfterCamera(map.get("后置摄像头"));
                property.setCpu(map.get("主芯片"));
                properties.add(property);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        boolean b = propertyService.saveOrUpdateBatch(properties);
        return b;

    }

    @Override
    public boolean savePropertyBySku(List<Brand> brandIds) {
        for (Brand brand : brandIds) {
            this.savePropertyBySku(brand);
        }
        return true;
    }


    /**
     * 获取文档中的brandID
     *
     * @param document
     * @return
     */
    @Override
    public String getBrandId(Document document) {
        Element element = document.select("script[charset]").first();
        if (element == null) {
            System.out.println(document);
            return "-1";
        }
        String data = element.data();
        return StrUtils.getAttr(data, "brand");
    }


    /**
     * 获取购买次数
     *
     * @param sku
     * @return
     */
    @Override
    public String getBuyCountBySku(String sku) {
        String context = JsoupUtils.parse(reptileUrl.getGetBuyCountUrl() + sku).body().text();
        return StrUtils.subStr(context, "\"CommentCountStr\":\"", "\"");
    }


}
