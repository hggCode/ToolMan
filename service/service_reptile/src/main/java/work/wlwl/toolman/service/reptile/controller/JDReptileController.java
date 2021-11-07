package work.wlwl.toolman.service.reptile.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
public class JDReptileController {

    @Autowired
    private JDService jdService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private ProductService productService;


    @GetMapping("brand")
    public R getBrand() {
        String mes = jdService.saveOrUpdateBrand();
        return R.ok().message(mes);
    }


    @GetMapping("save/product/{name}")
    public R getProductList(
            @PathVariable("name") String name) {
        QueryWrapper<Brand> wrapper = new QueryWrapper();
        wrapper.eq("name", name);
        Brand brand = brandService.getOne(wrapper);
        int count = jdService.saveProductByBrand(brand);
        return R.ok().message("插入了" + count);
    }

    @GetMapping("save/product/byBrand/{sort}")
    public R saveProduct(@PathVariable("sort") String sort) {
        QueryWrapper<Brand> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("ranking");
        wrapper.ge("ranking", sort);
        List<Brand> list = brandService.list(wrapper);
        int count = jdService.saveProductByBrand(list);
        return R.ok().message("保存了" + count + "条");
    }

    @GetMapping("remove")
    public R remove() {
        boolean b = jdService.deleteBrand();
        return R.ok().message(b + "");
    }

    @GetMapping("save/property/{sort}")
    public R saveProperty(@PathVariable String sort) {
        QueryWrapper<Brand> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("ranking");
        wrapper.ge("ranking", sort);
        List<Brand> list = brandService.list(wrapper);
        int count = jdService.savePropertyBySku(list);
        return R.ok().message("保存了" + count + "条");
    }


    @GetMapping("save/edition/by/sku/{sku}")
    public R saveEditionBySku(@PathVariable String sku) {
        QueryWrapper<Product> queryWrapper = new QueryWrapper();
        queryWrapper.eq("main_sku", sku);
        Product product = productService.getOne(queryWrapper);
        int count = jdService.saveEdition(product);
        product.setRanking(1);
        productService.saveOrUpdate(product);
        return R.ok().message("保存了" + count + "条");
    }

    @GetMapping("save/edition/by/product")
    public R saveEdition() {
        QueryWrapper<Product> wrapper=new QueryWrapper();
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


}
