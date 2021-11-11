package work.wlwl.toolman.service.reptile.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import work.wlwl.toolman.service.base.entity.R;
import work.wlwl.toolman.service.reptile.entity.Brand;
import work.wlwl.toolman.service.reptile.entity.Product;
import work.wlwl.toolman.service.reptile.service.BrandService;
import work.wlwl.toolman.service.reptile.service.JDService;
import work.wlwl.toolman.service.reptile.service.ProductService;
import work.wlwl.toolman.service.reptile.utils.SleepUtils;

import java.util.List;

@RestController
@RequestMapping("/reptile/jd")
@Api("京东爬虫")
public class JDReptileController {

    @Autowired
    private JDService jdService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private ProductService productService;


    @GetMapping("brand")
    @ApiOperation("更新品牌列表")
    public R getBrand() {
        int count = jdService.saveOrUpdateBrand();
        return R.ok().message("添加了" + count + "个品牌");
    }

    @ApiOperation("根据名字爬取指定的产品")
    @GetMapping("save/product/{name}")
    public R getProductList(@PathVariable String name) {
        return R.ok();
    }

    @ApiOperation("根据sku爬取指定的产品")
    @GetMapping("save/product/by/sku/{sku}")
    public R saveProductBySku(@ApiParam(value = "sku", required = true)
                              @PathVariable String sku) {
        boolean b = jdService.saveProductBySku(sku);
        if (b) {
            return R.ok().message("保存成功");
        }
        return R.error().message("保存失败");
    }


    @GetMapping("save/product/byBrand")
    @ApiOperation("保存每个品牌的product")
    public R saveProduct() {
        QueryWrapper<Brand> wrapper = new QueryWrapper<>();
        List<Brand> list = brandService.list();
        int count = jdService.saveProductByBrand(list);
        return R.ok().message("保存了" + count + "条");
    }


    @GetMapping("save/edition/by/sku/{sku}")
    @ApiOperation("根据sku更新或保存product的版本")
    public R saveEditionBySku(@PathVariable String sku) {
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("main_sku", sku);
        Product product = productService.getOne(queryWrapper);
        int count = jdService.saveEdition(product);
        return R.ok().message("更新了" + count + "条");
    }

    @GetMapping("save/edition/by/product")
    @ApiOperation("刚刚创建搭建时，用于保存产品版本使用")
    public R saveEdition() {
        QueryWrapper<Product> wrapper = new QueryWrapper<>();
        wrapper.isNull("ranking");
        List<Product> list = productService.list(wrapper);
        int count = 0;
        for (Product product : list) {
            count += jdService.saveEdition(product);
            product.setRanking(1);
            productService.saveOrUpdate(product);
            SleepUtils.seconds(2);
        }
        return R.ok().message("保存了" + count + "条");
    }


    @GetMapping("save/img/by/product/{id}")
    @ApiOperation("保存图片根据productId")
    public R saveImg(@PathVariable String id) {
        Product product = productService.getById(id);
        int count = jdService.saveColor(product);
        product.setRanking(2);
        productService.saveOrUpdate(product);
        SleepUtils.seconds(2);
        return R.ok().message("保存了" + count + "条");
    }


    @GetMapping("save/img/by/product")
    @ApiOperation("保存所有的product的图片")
    public R saveImg() {
        QueryWrapper<Product> wrapper = new QueryWrapper();
        wrapper.eq("ranking", 1);
        List<Product> list = productService.list(wrapper);
        int count = 0;
        for (Product product : list) {
            count += jdService.saveColor(product);
            product.setRanking(2);
            productService.saveOrUpdate(product);
            SleepUtils.seconds(2);
        }
        return R.ok().message("保存了" + count + "条");
    }


}
