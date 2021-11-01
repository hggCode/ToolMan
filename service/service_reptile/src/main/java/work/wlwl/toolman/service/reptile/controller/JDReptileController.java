package work.wlwl.toolman.service.reptile.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import work.wlwl.toolman.service.base.entity.R;
import work.wlwl.toolman.service.reptile.entity.Brand;
import work.wlwl.toolman.service.reptile.service.BrandService;
import work.wlwl.toolman.service.reptile.service.JDService;

import java.util.List;

@RestController
@RequestMapping("/reptile/jd")
public class JDReptileController {

    @Autowired
    private JDService jdService;

    @Autowired
    private BrandService brandService;

    @GetMapping("brand")
    public R getBrand() {
        String mes = jdService.saveOrUpdateBrand();
        return R.ok().message(mes);
    }


    @GetMapping("getProductListByBrand")
    public R getProductList() {
        Brand brand = new Brand();
        brand.setName("Apple");
        brand.setId("14026");
        int count = jdService.saveProductByBrand(brand);
        return R.ok().message("插入了" + count);
    }

    @GetMapping("saveProductByBrandList")
    public R saveProduct() {
        List<Brand> list = brandService.list();
        int count = jdService.saveProductByBrand(list);
        return R.ok().message("保存了" + count + "条");
    }
}
