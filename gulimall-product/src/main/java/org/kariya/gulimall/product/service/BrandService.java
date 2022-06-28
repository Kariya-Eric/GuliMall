package org.kariya.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.kariya.common.utils.PageUtils;
import org.kariya.gulimall.product.entity.BrandEntity;

import java.util.Map;

/**
 * 品牌
 *
 * @author kariya
 * @email eric15195016307@gmail.com
 * @date 2022-06-16 10:15:15
 */
public interface BrandService extends IService<BrandEntity> {

    PageUtils queryPage(Map<String, Object> params);

    boolean brandExist(String name);
}

