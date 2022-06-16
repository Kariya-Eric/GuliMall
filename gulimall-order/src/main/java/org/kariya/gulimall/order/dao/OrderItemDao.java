package org.kariya.gulimall.order.dao;

import org.kariya.gulimall.order.entity.OrderItemEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单项信息
 * 
 * @author kariya
 * @email eric15195016307@gmail.com
 * @date 2022-06-16 11:36:31
 */
@Mapper
public interface OrderItemDao extends BaseMapper<OrderItemEntity> {
	
}
