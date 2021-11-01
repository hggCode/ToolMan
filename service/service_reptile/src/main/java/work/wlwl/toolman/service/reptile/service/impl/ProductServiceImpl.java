package work.wlwl.toolman.service.reptile.service.impl;

import work.wlwl.toolman.service.reptile.entity.Product;
import work.wlwl.toolman.service.reptile.mapper.ProductMapper;
import work.wlwl.toolman.service.reptile.service.ProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hgg&fbb
 * @since 2021-11-01
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

}
