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
import work.wlwl.toolman.service.reptile.entity.*;
import work.wlwl.toolman.service.reptile.feign.OssFileService;
import work.wlwl.toolman.service.reptile.service.*;
import work.wlwl.toolman.service.reptile.utils.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

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

    @Autowired
    private ProductColorService productColorService;

    @Autowired
    private ProductImgService productImgService;

    @Autowired
    private OssFileService ossFileService;


    /**
     * 爬取品牌列表
     *
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int saveOrUpdateBrand() {
        String url = reptileUrl.getIndexUrl();
        Document document = JsoupUtils.parse(url);
        Element element = document.selectFirst(".v-fixed");
        int ranking = 1;
        //当element为空时 重新获取
        this.tryAgain(element, "saveOrUpdateBrand", url, ".v-fixed");
        List<Brand> list = new ArrayList<>();
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
        for (Brand brand : list) {
            String brandName = brand.getName();
            String item_url = reptileUrl.getProductByBrandUrl() + brandName;
            Document itemDoc = JsoupUtils.parse(url);
            Element itemE = itemDoc.selectFirst(".gl-warp");
            int count = 0;
            this.tryAgain(itemE, "deleteBrand", item_url, ".gl-warp");

//            获取到每个产品
            Elements liItems = element.select("li");
            for (Element item : liItems) {
//                剔除不是直营的
                Elements e = item.getElementsByClass("goods-icons");
                if (e.size() == 0) {
                    continue;
                }
                count += 1;
            }
            if (count < 1) {
                list.remove(brand);
            }
        }
        brandService.saveOrUpdateBatch(list);
        return list.size();
    }

    /**
     * 根据sku保存单个产品
     *
     * @param sku
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveProductBySku(String sku) {
        String url = reptileUrl.getItemUrl() + sku + ".html";
        Document document = JsoupUtils.parse(url);
        log.info("正在连接:" + url);
//        当document为长度不足200时 重试
        this.tryAgain(document, "saveProductBySku", url, sku);
        Product product = new Product();
        String name = document.title();
        name = StrUtils.getName(name);
        product.setName(name);  //产品名
        product.setMainSku(sku);   //默认sku
        String buyCount = this.getBuyCountBySku(sku);
        product.setBuyCount(buyCount);  //购买数
        String trim = document.selectFirst("script[charset='gbk']").data().trim();
        product.setBrandId(StrUtils.getBrandId(trim)); //品牌id
        //获取属性
        boolean save = productService.save(product);
        if (save) {
            Property property = this.getProperty(product);
            propertyService.save(property); //保存属性
            this.saveEdition(product);   //保存版本
            this.saveColor(product); //保存颜色和图片
        }
        return save;
    }

    /**
     * 根据product获取property
     *
     * @param product
     * @return Property
     */
    public Property getProperty(Product product) {
        Property property = new Property();
        String sku = product.getMainSku();
        String url = reptileUrl.getItemUrl() + sku + ".html";
        Document pares = JsoupUtils.parse(url);
        Element e = pares.selectFirst(".Ptable");
        log.info(url);
//        当e为null是重试
        this.tryAgain(e, "getProperty", url, ".Ptable");

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
        return property;
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
        Document document = JsoupUtils.parse(url);
        Element element = document.selectFirst(".gl-warp");
//        当e为空时 进行十次重新链接
        this.tryAgain(element, "saveProductByBrand", url, ".gl-warp");
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

//        根据skuList批量保存product
        List<Product> productList = new ArrayList<>();
//        遍历skuList中的每一个sku对应的产品
        for (String sku : skuList) {
            url = reptileUrl.getItemUrl() + sku + ".html";
            document = JsoupUtils.parse(url);
            log.info(url);
//            当document为长度不足200时重试
            this.tryAgain(document, "saveProductByBrand，遍历每个sku对应的产品", url);

            Product product = new Product();
            String name = document.title();
            name = StrUtils.getName(name);
            product.setName(name);  //产品名
            product.setMainSku(sku);   //默认sku
//            可能返回值为-1  继续保存  日后开启定时任务添加
            String buyCount = this.getBuyCountBySku(sku);
            product.setBuyCount(buyCount);  //购买数
            product.setBrandId(brand.getId());
            productList.add(product);
            //休眠两秒
            SleepUtils.seconds(2);
        }
        boolean b = productService.saveOrUpdateBatch(productList);
        return b ? productList.size() : -1;

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
        Document document = JsoupUtils.parse(url);
        log.info(url);

        this.tryAgain(document, "saveEdition", url);


        Edition edition = new Edition();
        edition.setProductId(product.getId()); //设置产品id
        edition.setSku(mainSku); //设置sku
        Element nameE = document.selectFirst(".sku-name");
        String name = nameE == null ? " " : nameE.text();
        Element colorDoc = document.selectFirst(".p-choose[data-type='颜色'] .selected");
        if (colorDoc != null) {
            String color = colorDoc.text();
            name = name.replace(color, "");
        }
        edition.setName(name);  //设置名字
        this.getPrice(mainSku, edition);  //设置价格
        Element typeDoc = document.selectFirst(".p-choose[data-type='版本']");
        String ram = "";
        int count = 1;
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
                QueryWrapper<Edition> wrapper = new QueryWrapper<>();
                wrapper.eq("name", nameTemp);
                editionService.saveOrUpdate(ed, wrapper);
                count++;
            }
        }
        edition.setRam(ram);  //设置内存
        QueryWrapper<Edition> wrapper = new QueryWrapper<>();
        wrapper.eq("name", edition.getName());
        editionService.saveOrUpdate(edition, wrapper);
        return count;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int saveColor(Product product) {
        String mainSku = product.getMainSku();
        String url = reptileUrl.getItemUrl() + mainSku + ".html";
        Document document = JsoupUtils.parse(url);

        this.tryAgain(document, "外saveColor", url);

        Map<String, String[]> map = new HashMap<>();  //用来装颜色跟图片的对应 获取完毕后一起请求oss保存
        String data = document.selectFirst("script[charset='gbk']").data().trim();
        String[] imgUrls = StrUtils.getImgUrls(data); //图片链接集合
        Element colors = document.selectFirst(".p-choose[data-type='颜色']");
        String curColor = "";
        if (colors != null) {
            curColor = colors.select(".selected").remove().text(); //获取当前颜色
            Elements items = colors.select(".item");
            for (Element item : items) {
//            将其他颜色填加到数据库
                ProductColor itemProductColor = new ProductColor();
                String itemColor = item.text();
                itemProductColor.setColor(itemColor);  //颜色
                itemProductColor.setProductId(product.getId());  //产品id
                productColorService.save(itemProductColor);

                String sku = item.attr("data-sku");
                SleepUtils.seconds(2);
                url = reptileUrl.getItemUrl() + sku + ".html";
                document = JsoupUtils.parse(url);
                log.info(reptileUrl.getItemUrl() + sku + ".html");
                //重试
                this.tryAgain(document, "获取其他颜色的时候，很突然，嘎被拉黑了", url);

                String itemData = document.selectFirst("script[charset='gbk']").data().trim();
                String[] itemImgUrls = StrUtils.getImgUrls(itemData); //图片链接集合
                map.put(itemProductColor.getId(), itemImgUrls);
            }
        }
        ProductColor curProductColor = new ProductColor();
        curProductColor.setColor(curColor); //颜色
        curProductColor.setProductId(product.getId());  //产品id
        productColorService.save(curProductColor);  //保存当前颜色到数据库

        map.put(curProductColor.getId(), imgUrls);//保存当前颜色与对应的图片数组


//        向oss保存图片并将图片url保存到数据库

        Brand brand = brandService.getById(product.getBrandId());
        String brandName = brand.getName();
        if (brandName.contains("（")) {
            brandName = brandName.substring(0, brandName.lastIndexOf("（"));
        }
        AtomicInteger count = new AtomicInteger();
        String finalBrandName = brandName;
        map.forEach((k, v) -> {
            for (int i = 0; i < v.length; i++) {
                Random random = new Random();
                int pre = random.nextInt(4) + 10;
                int n = random.nextInt(5) + 1;
                // 保存图片
                String baseUrl = "https://img" + pre + ".360buyimg.com/n" + n + "/s450x450_";
                String src = "";
                do {
                    src = ossFileService.update(baseUrl + v[i], "product/" + finalBrandName + "/" + product.getName());
                    SleepUtils.seconds(1);
                    log.info(baseUrl + v[i]);
                    log.info(src);
                }
                while ("-1".equals(src));
                ProductImg productImg = new ProductImg();
                productImg.setProductId(product.getId());
                productImg.setColorId(k);
                productImg.setSort(i + 1);
                productImg.setSrc(src);
                productImgService.save(productImg);
                count.getAndIncrement();
            }
        });
        return count.get();
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
        Document document = JsoupUtils.parse(url);

        this.tryAgain(document, "getShopId", url);

        Elements elements = document.select(".gl-item");
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
            SleepUtils.seconds(2);
        }
        JSONObject json = JSON.parseObject(JSON.parseArray(context).get(0).toString());
        String p = (String) json.get("p");
        String op = (String) json.get("op");
        edition.setJdPrice(new BigDecimal(p));
        edition.setInitPrice(new BigDecimal(op));
    }

    /**
     * 用于重新链接
     *
     * @param document 文档
     * @param target   当前方法
     * @param url      目标url
     */
    public void tryAgain(Document document, String target, String url) {
        int retry = 0;//用于记录重试次数
        while (document.toString().length() < 200) {
            if (retry > 10) {
                WeChetSend.send("嘎，被拉黑了", "执行" + target + "的时候挂了\t" + url);
                throw new GlobalException(ResultCodeEnum.JD_CONNECT_ERROR);
            }
            SleepUtils.seconds(2);
            document = JsoupUtils.parse(url);
            log.info("正在第一次连接第" + retry + "次，" + url);
            retry++;

        }
    }

    /**
     * 用于重新链接
     *
     * @param element 文档
     * @param target  当前方法
     * @param url     目标url
     */
    public void tryAgain(Element element, String target, String url, String select) {
        int retry = 0;//用于记录重试次数
        while (element == null) {
            if (retry > 10) {
                WeChetSend.send("嘎，被拉黑了", "执行" + target + "的时候挂了");
                throw new GlobalException(ResultCodeEnum.JD_CONNECT_ERROR);
            }
            SleepUtils.seconds(2);
            element = JsoupUtils.parse(url).selectFirst(select);
            log.info("正在第一次连接第" + retry + "次，" + url);
            retry++;

        }
    }

}
