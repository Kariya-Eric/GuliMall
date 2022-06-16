package org.kariya.gulimall.product.dao;

import org.kariya.gulimall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author kariya
 * @email eric15195016307@gmail.com
 * @date 2022-06-16 10:15:15
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
