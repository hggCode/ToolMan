package work.wlwl.toolman.service.reptile.service.impl;

import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
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
     * 进入当个产品的页面 获取产品数据
     * 如果该产品是否存在，不存在即插入，否者则更新
     *
     * @param sku
     * @return 影响条数
     */
    @Override
    public int saveProductBySku(String sku, String id) {
        String url = reptileUrl.getItemUrl() + sku + ".html";
        Document document = JsoupUtils.pares(url);
        Product product = new Product();
        String name = document.title();
        name = StrUtils.getName(name);
        product.setName(name);  //产品名
        product.setMainSku(sku);   //默认sku
        String buyCount = this.getBuyCountBySku(sku);
        String brandId = this.getBrandId(document);
        if (StringUtils.hasLength(id) && !id.equals(brandId)) {
            return 0;
        }
        if ("-1".equals(brandId) || "-1".equals(buyCount)) {
            throw new GlobalException(ResultCodeEnum.JD_CONNECT_ERROR);
        }
        product.setBuyCount(buyCount);  //购买数
        product.setBrandId(brandId);
        UpdateWrapper wrapper = new UpdateWrapper();
        wrapper.eq("name", name);
        boolean b = productService.saveOrUpdate(product, wrapper);
        return b ? 1 : 0;
    }


    //    根据品牌获取品牌下的所有的产品
    @Override
    public int saveProductByBrand(Brand brand) {
        String brandName = brand.getName();
        String url = reptileUrl.getGetProductByBrandUrl() + brandName;
        Document context = JsoupUtils.pares(url);
        Elements elements = context.getElementsByClass("gl-warp");
        if (elements.size() == 0) {
            return -1;
        }
        int count = 0;
        for (Element element : elements) {
//            获取到每个产品
            Elements items = element.select("li");
            for (Element item : items) {
//                剔除不是直营的
                Elements e = item.getElementsByClass("goods-icons");
                if (e.size() == 0) {
                    continue;
                }
                String sku = item.attr("data-sku");
                count += this.saveProductBySku(sku, brand.getId());
            }
        }

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
        String context = HttpUtil.get(reptileUrl.getGetBuyCountUrl() + sku);
        return StrUtils.subStr(context, "\"CommentCountStr\":\"", "\"");
    }


}
