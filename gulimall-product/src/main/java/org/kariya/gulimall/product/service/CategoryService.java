package org.kariya.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.kariya.common.utils.PageUtils;
import org.kariya.gulimall.product.entity.CategoryEntity;
import org.kariya.gulimall.product.vo.CategoryVo;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author kariya
 * @email eric15195016307@gmail.com
 * @date 2022-06-16 10:15:15
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<CategoryVo> listWithTree();

    void removeMenuByIds(Long[] catIds);

    boolean menuExist(String name);

    void saveCategoryVo(CategoryVo category);

    void sort(CategoryEntity start, CategoryEntity end, String type);
}

