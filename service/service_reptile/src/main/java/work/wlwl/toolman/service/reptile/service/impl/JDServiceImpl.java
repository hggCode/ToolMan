package work.wlwl.toolman.service.reptile.service.impl;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import work.wlwl.toolman.common.base.utils.RegexUtils;
import work.wlwl.toolman.service.base.entity.ResultCodeEnum;
import work.wlwl.toolman.service.base.exception.GlobalException;
import work.wlwl.toolman.service.reptile.config.ReptileUrl;
import work.wlwl.toolman.service.reptile.entity.Brand;
import work.wlwl.toolman.service.reptile.entity.Edition;
import work.wlwl.toolman.service.reptile.entity.Product;
import work.wlwl.toolman.service.reptile.entity.Property;
import work.wlwl.toolman.service.reptile.service.*;
import work.wlwl.toolman.service.reptile.utils.JsoupUtils;
import work.wlwl.toolman.service.reptile.utils.MapUtils;
import work.wlwl.toolman.service.reptile.utils.SleepUtils;
import work.wlwl.toolman.service.reptile.utils.StrUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private EditionService editionService;


    /**
     * 爬取品牌列表
     *
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveOrUpdateBrand() {
        String url = reptileUrl.getIndexUrl();
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
                String shopId = this.getShopId(name);
                brand.setShopId(shopId);
                list.add(brand);
            }
        }
        brandService.saveOrUpdateBatch(list);
        return "成功";
    }


    /**
     * 根据品牌列表保存品牌下的所有产品
     *
     * @param brands
     * @return
     */
    @Override
    public int saveProductByBrand(List<Brand> brands) {
        int count = 0;
        for (Brand brand : brands) {
            count += this.saveProductByBrand(brand);
            SleepUtils.seconds(2);
        }
        return count;
    }

    /**
     * 根据品牌获取品牌下的所有的产品
     *
     * @param brand
     * @return
     */
    @Override
    public int saveProductByBrand(Brand brand) {
        String brandName = brand.getName();
        String url = reptileUrl.getProductByBrandUrl() + brandName;
        Document context = JsoupUtils.parse(url);
        Element element = context.selectFirst(".gl-warp");
        if (element == null) {
//            被京东拦截
            log.error("获取每个产品的sku列表的时候就被嘎了");
            throw new GlobalException(ResultCodeEnum.JD_CONNECT_ERROR);
        }
//            获取到每个产品
        Elements items = element.select("li");
        List<String> skuList = new ArrayList<>();   //用于保存品牌下的sku
        for (Element item : items) {
            String currShop = item.select(".curr-shop").attr("href");
            currShop = RegexUtils.getNum(currShop);
            if (!brand.getShopId().equals(currShop)) {
                continue;
            }
            String sku = item.attr("data-sku");
            skuList.add(sku);
        }
        log.info("{}\t{}", brandName, skuList);
        return this.saveProductBySku(skuList, brand.getId());
    }

    /**
     * 根据skuList批量保存product
     *
     * @param skuList
     * @return
     */
    public int saveProductBySku(List<String> skuList, String brandId) {
        List<Product> productList = new ArrayList<>();
//        遍历skuList中的每一个sku对应的产品
        for (String sku : skuList) {
            String url = reptileUrl.getItemUrl() + sku + ".html";
            Document document = JsoupUtils.parse(url);
            log.info(url);
            int r = 0;
            while (document != null && document.toString().contains("<script>window.location.href=")) {
                if (r > 6) {
                    log.error("大于六次重试就嘎掉了");
                    throw new GlobalException(ResultCodeEnum.JD_CONNECT_ERROR);
                }
                System.out.println(document);
                SleepUtils.seconds(5);
                log.info("正在重新尝试连接第" + r + "次，目标url" + url);
                document = JsoupUtils.parse(url);
                r++;
            }
            Product product = new Product();
            String name = document.title();
            name = StrUtils.getName(name);
            product.setName(name);  //产品名
            product.setMainSku(sku);   //默认sku
//            可能返回值为-1  继续保存  日后开启定时任务添加
            String buyCount = this.getBuyCountBySku(sku);
            product.setBuyCount(buyCount);  //购买数
            product.setBrandId(brandId);
            productList.add(product);
            //休眠两秒
            SleepUtils.seconds(2);
        }
        log.info("产品{}", productList);
        boolean b = productService.saveOrUpdateBatch(productList);
        return productList.size();
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
            String url = reptileUrl.getProductByBrandUrl() + brandName;
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
    public int savePropertyBySku(Brand brand) {
        ArrayList<Property> properties = new ArrayList<>();
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("brand_id", brand.getId())
                .select("id", "main_sku");
        List<Product> list = productService.list(queryWrapper);
        for (Product product : list) {
            Property property = new Property();
            String sku = product.getMainSku();
            String url = reptileUrl.getItemUrl() + sku + ".html";
            Document pares = JsoupUtils.parse(url);
            Element e = pares.selectFirst(".Ptable");
            log.info(url);
            int flag = 0;
            while (e == null || pares.toString().contains("<script>window.location.href=")) {
                if (flag > 10) {
                    throw new GlobalException(ResultCodeEnum.JD_CONNECT_ERROR);
                }
                System.out.println(pares);
                SleepUtils.seconds(flag * 10);
                log.info("正在重新尝试连接第" + flag + "次，目标url" + url);
                e = JsoupUtils.parse(url).selectFirst(".Ptable");
                flag++;
            }
            Elements elements = e.select(".Ptable-item");
            Map<String, String> map = new HashMap<>();
            for (Element element : elements) {
                element.select("p").remove();
                String h3 = element.select("h3").text().trim();
                if ("前置摄像头".equals(h3) || "后置摄像头".equals(h3) || "主芯片".equals(h3)) {
                    String dl = element.selectFirst("dl").text().trim();
                    map.put(h3, dl);
                    continue;
                }
                Elements select = element.select(".clearfix");
                String dd = "";
                for (Element element1 : select) {
                    String dt = element1.select("dt").text().trim();
                    dd = element1.select("dd").text().trim();
                    if (dd.indexOf("备注") != -1) {
                        dd = StrUtils.subStr(dd, "", "备注");
                    }
                    map.put(dt, dd);
                }
            }
            String initDate = MapUtils.getV(map, "上市年份") + MapUtils.getV(map, "上市月份")
                    + MapUtils.getV(map, "首销日期");

            String size = MapUtils.getV(map, "机身长度（mm）") + "x"
                    + MapUtils.getV(map, "机身宽度（mm）") + "x"
                    + MapUtils.getV(map, "机身厚度(mm)");

            property.setName(MapUtils.getV(map, "产品名称"))
                    .setWeight(MapUtils.getV(map, "机身重量（g）"))
                    .setInitDate(initDate)
                    .setScreenSize(MapUtils.getV(map, "主屏幕尺寸"))
                    .setDisplayRefresh(MapUtils.getV(map, "屏幕刷新率"))
                    .setScreenType(MapUtils.getV(map, "屏幕材质类型"))
                    .setSimNum(MapUtils.getV(map, "最大支持SIM卡数量"))
                    .setSimType(MapUtils.getV(map, "SIM卡类型"))
                    .setNet_5g(MapUtils.getV(map, "5G网络"))
                    .setNet_4g(MapUtils.getV(map, "4G网络"))
                    .setNet_3g(MapUtils.getV(map, "3G/2G网络"))
                    .setAvOut(MapUtils.getV(map, "耳机接口类型"))
                    .setChargerPort(MapUtils.getV(map, "充电接口类型"))
                    .setNfc(MapUtils.getV(map, "NFC/NFC模式"))
                    .setBeforeCamera(MapUtils.getV(map, "前置摄像头"))
                    .setAfterCamera(MapUtils.getV(map, "后置摄像头"))
                    .setCpu(MapUtils.getV(map, "主芯片"))
                    .setSize(size)
                    .setProductId(product.getId());
            System.out.println(product.getId());
            properties.add(property);
            SleepUtils.seconds(2);
        }
        propertyService.saveOrUpdateBatch(properties);
        return properties.size();

    }

    @Override
    public int savePropertyBySku(List<Brand> brandIds) {
        int count = 0;
        for (Brand brand : brandIds) {
            count += this.savePropertyBySku(brand);
            SleepUtils.seconds(2);
        }
        return count;
    }

    /**
     * 根据传进来的product获取main_sku
     * 根据main_sku 进入具体页面后保存当前sku的属性和颜色根据并保存其他的sku
     * 根据其他sku 进入到不同的sku页面 保存属性和颜色
     *
     * @param product
     * @return
     */
    @Override
    public int saveEdition(Product product) {
        String mainSku = product.getMainSku();
        String url = reptileUrl.getItemUrl() + mainSku + ".html";
        Document parse = JsoupUtils.parse(url);
        log.info(url);
        List<Edition> edList = new ArrayList<>();
        Edition edition = new Edition();
        edition.setProductId(product.getId()); //设置产品id
        edition.setSku(mainSku); //设置sku
        Element nameE = parse.selectFirst(".sku-name");
        String name = nameE == null ? " " : nameE.text();
        Element colorDoc = parse.selectFirst(".p-choose[data-type='颜色'] .selected");
        if (colorDoc != null) {
            String color = colorDoc.text();
            name = name.replace(color, "");
        }
        edition.setName(name);  //设置名字
        this.getPrice(mainSku, edition);  //设置价格
        Element typeDoc = parse.selectFirst(".p-choose[data-type='版本']");
        String ram = "";
        if (typeDoc != null) {
            System.out.println("嘎，挂了,没内存");
            Elements ramDoc = typeDoc.select(".selected").remove();
            ram = ramDoc.text();
            Elements items = typeDoc.select(".item");
            for (Element item : items) {
                Edition ed = new Edition();
                String ramTemp = item.text();
                ed.setProductId(product.getId()); //pid
                ed.setRam(item.text()); //内存
                String sku = item.attr("data-sku");
                ed.setSku(sku); //sku
                this.getPrice(sku, ed);  //价格
                String nameTemp = name.replace(ram, ramTemp);
                ed.setName(nameTemp); //名称
                edList.add(ed);
            }
        }
        edition.setRam(ram);  //设置内存
        edList.add(edition);
        editionService.saveBatch(edList);
        return edList.size();
    }


    /**
     * 获取购买次数
     *
     * @param sku
     * @return
     */
    public String getBuyCountBySku(String sku) {
        String context = JsoupUtils.parse(reptileUrl.getBuyCountUrl() + sku).body().text();
        return StrUtils.subStr(context, "\"CommentCountStr\":\"", "\"");
    }

    public String getShopId(String name) {
        String url = reptileUrl.getProductByBrandUrl() + name;
        Document context = JsoupUtils.parse(url);
        Elements elements = context.select(".gl-item");
        if (elements.first() == null) {
//            被京东拦截
            log.error(context.toString().length() + "");
            log.error("添加店铺失败");
            throw new GlobalException(ResultCodeEnum.JD_CONNECT_ERROR);
        }
        for (Element element : elements) {
            Elements e = element.select(".goods-icons");
            if (e.size() == 0) {
                continue;
            }
            String href = element.select(".curr-shop").attr("href");
            return RegexUtils.getNum(href);
        }
        return "-1";
    }

    public void getPrice(String sku, Edition edition) {
        String context = HttpUtil.get(reptileUrl.getPriceUrl() + sku);
        while (context.contains("error")) {
            log.error("获取价格失败");
            context = HttpUtil.get(reptileUrl.getPriceUrl() + sku);
            SleepUtils.seconds(10);
        }
        JSONObject json = JSON.parseObject(JSON.parseArray(context).get(0).toString());
        String p = (String) json.get("p");
        String op = (String) json.get("op");
        edition.setJdPrice(new BigDecimal(p));
        edition.setInitPrice(new BigDecimal(op));
    }
}
