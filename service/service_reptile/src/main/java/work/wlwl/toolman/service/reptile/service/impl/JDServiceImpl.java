package work.wlwl.toolman.service.reptile.service.impl;

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
import work.wlwl.toolman.service.reptile.service.BrandService;
import work.wlwl.toolman.service.reptile.service.JDService;
import work.wlwl.toolman.service.reptile.service.ProductService;
import work.wlwl.toolman.service.reptile.utils.JsoupUtils;
import work.wlwl.toolman.service.reptile.utils.StrUtils;

import java.util.ArrayList;
import java.util.List;
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


    /**
     * 爬取品牌列表
     *
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveOrUpdateBrand() {
        String url = reptileUrl.getTestUrl();
        Document context = JsoupUtils.pares(url);
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
            Document document = JsoupUtils.pares(url);
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
        Document context = JsoupUtils.pares(url);
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
            Document context = JsoupUtils.pares(url);
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
    public boolean savePropertyBySku(String sku) {



        return false;
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
        String context = JsoupUtils.pares(reptileUrl.getGetBuyCountUrl() + sku).body().text();
        return StrUtils.subStr(context, "\"CommentCountStr\":\"", "\"");
    }


}