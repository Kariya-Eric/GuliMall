package org.kariya.gulimall.product;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kariya.gulimall.product.entity.BrandEntity;
import org.kariya.gulimall.product.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class GulimallProductApplicationTests {

    @Autowired
    BrandService brandService;

    @Test
    public void contextLoads(){
        BrandEntity brandEntity=new BrandEntity();
        brandEntity.setBrandId(1L);
        brandEntity.setDescript("华为，中国人的骄傲!");
        brandService.updateById(brandEntity);
    }
}
