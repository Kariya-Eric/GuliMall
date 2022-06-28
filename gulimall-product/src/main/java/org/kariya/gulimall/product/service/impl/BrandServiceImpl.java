package org.kariya.gulimall.product.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.kariya.common.utils.PageUtils;
import org.kariya.common.utils.Query;

import org.kariya.gulimall.product.dao.BrandDao;
import org.kariya.gulimall.product.entity.BrandEntity;
import org.kariya.gulimall.product.service.BrandService;


@Service("brandService")
public class BrandServiceImpl extends ServiceImpl<BrandDao, BrandEntity> implements BrandService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String name= (String) params.get("name");
        IPage<BrandEntity> page = this.page(
                new Query<BrandEntity>().getPage(params),
                new QueryWrapper<BrandEntity>().like(StringUtils.isNotBlank(name),"name", name)
                        .orderByAsc("sort"));

        return new PageUtils(page);
    }

    @Override
    public boolean brandExist(String name) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("name", name);
        Long count = baseMapper.selectCount(wrapper);
        return count > 0;
    }

}