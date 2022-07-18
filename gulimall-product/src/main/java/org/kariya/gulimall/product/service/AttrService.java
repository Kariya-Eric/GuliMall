package org.kariya.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.kariya.common.utils.PageUtils;
import org.kariya.gulimall.product.entity.AttrEntity;
import org.kariya.gulimall.product.vo.AttrEditVo;
import org.kariya.gulimall.product.vo.AttrVo;

import java.util.Map;

/**
 * 商品属性
 *
 * @author kariya
 * @email eric15195016307@gmail.com
 * @date 2022-06-16 10:15:15
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveAttr(AttrVo attrvo);

    AttrEditVo getAttrEditVoById(Long attrId);

    void updateAttr(AttrVo attrvo);
}

