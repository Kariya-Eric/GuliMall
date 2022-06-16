package org.kariya.gulimall.coupon.dao;

import org.kariya.gulimall.coupon.entity.SeckillSessionEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 秒杀活动场次
 * 
 * @author kariya
 * @email eric15195016307@gmail.com
 * @date 2022-06-16 10:56:31
 */
@Mapper
public interface SeckillSessionDao extends BaseMapper<SeckillSessionEntity> {
	
}
