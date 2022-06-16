package org.kariya.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.kariya.common.utils.PageUtils;
import org.kariya.gulimall.ware.entity.WareInfoEntity;

import java.util.Map;

/**
 * 仓库信息
 *
 * @author kariya
 * @email eric15195016307@gmail.com
 * @date 2022-06-16 11:37:03
 */
public interface WareInfoService extends IService<WareInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

