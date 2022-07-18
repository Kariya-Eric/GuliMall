package org.kariya.gulimall.product.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang.StringUtils;
import org.kariya.gulimall.product.dao.CategoryBrandRelationDao;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.transaction.annotation.Transactional;


@Service("brandService")
public class BrandServiceImpl extends ServiceImpl<BrandDao, BrandEntity> implements BrandService {
    @Autowired
    private CategoryBrandRelationDao categoryBrandRelationDao;

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

    //此方法用于更新品牌后，将数据库中所有相关的冗余字段同时更新
    //但是实际本人项目中将品牌名锁定无法修改后，无法实际演示效果
    //并且尚硅谷项目判断entity的name是否为空，不为空表示需要修改name？？不确定他前端的参数怎么传递的
    //此处扔根据id查找数据库原有name，判断修改后name与原name是否相同
    @Override
    @Transactional
    public void updateBrand(BrandEntity brand) {
        //this.baseMapper.updateById(brand);
        //获取数据库原来的entity
        BrandEntity brandEntity=this.baseMapper.selectById(brand.getBrandId());
        //修改brand对应数据表数据
        this.baseMapper.updateById(brandEntity);
        //判断name是否修改了，若修改了，则需更新relation表中冗余字段
        if(!brandEntity.getName().equals(brand.getName())){
            categoryBrandRelationDao.updateBrand(brand.getBrandId(),brand.getName());
        }
    }

}