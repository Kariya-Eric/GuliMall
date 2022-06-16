package org.kariya.gulimall.ware.dao;

import org.kariya.gulimall.ware.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品库存
 * 
 * @author kariya
 * @email eric15195016307@gmail.com
 * @date 2022-06-16 11:37:03
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {
	
}
