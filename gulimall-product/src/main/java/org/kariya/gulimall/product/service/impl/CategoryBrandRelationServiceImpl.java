package org.kariya.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.kariya.gulimall.product.dao.BrandDao;
import org.kariya.gulimall.product.dao.CategoryDao;
import org.kariya.gulimall.product.entity.BrandEntity;
import org.kariya.gulimall.product.entity.CategoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.kariya.common.utils.PageUtils;
import org.kariya.common.utils.Query;

import org.kariya.gulimall.product.dao.CategoryBrandRelationDao;
import org.kariya.gulimall.product.entity.CategoryBrandRelationEntity;
import org.kariya.gulimall.product.service.CategoryBrandRelationService;


@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {
    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private BrandDao brandDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String brandId = (String) params.get("brandId");
        IPage<CategoryBrandRelationEntity> page = this.page(
                new Query<CategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<CategoryBrandRelationEntity>().eq("brand_id", brandId)
        );

        return new PageUtils(page);
    }

    @Override
    public void saveRelation(Map<String, Object> params) {
        String brandId = (String) params.get("brandId");
        String catelogId = (String) params.get("catelogId");
        BrandEntity brandEntity = brandDao.selectById(brandId);
        CategoryEntity category = categoryDao.selectById(catelogId);
        CategoryBrandRelationEntity categoryBrandRelation = new CategoryBrandRelationEntity();
        categoryBrandRelation.setBrandId(brandEntity.getBrandId());
        categoryBrandRelation.setBrandName(brandEntity.getName());
        categoryBrandRelation.setCatelogId(category.getCatId());
        categoryBrandRelation.setCatelogName(category.getName());
        baseMapper.insert(categoryBrandRelation);
    }

    @Override
    public void updateCategory(Long catId, String name) {
        UpdateWrapper<CategoryBrandRelationEntity> updateWrapper=new UpdateWrapper<>();
        CategoryBrandRelationEntity categoryBrandRelation=new CategoryBrandRelationEntity();
        categoryBrandRelation.setCatelogName(name);
        updateWrapper.eq("catelog_id",catId);
        this.update(categoryBrandRelation,updateWrapper);
    }

}