package org.kariya.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import org.kariya.gulimall.product.vo.CategoryVo;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.kariya.common.utils.PageUtils;
import org.kariya.common.utils.Query;

import org.kariya.gulimall.product.dao.CategoryDao;
import org.kariya.gulimall.product.entity.CategoryEntity;
import org.kariya.gulimall.product.service.CategoryService;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryVo> listWithTree() {
        //查出所有分类
        List<CategoryEntity> categoryEntities = baseMapper.selectList(null);
        //将categoryentity list转换为categoryvo list
        List<CategoryVo> categoryVos = categoryEntities.stream().map(
                category -> switchCategoryEntityToCategoryVo(category)
        ).collect(Collectors.toList());
        //找到所有分类的一级分类,组装成父子树形结构
        List<CategoryVo> level1Menu = categoryVos.stream().filter(categoryVo ->
                        categoryVo.getParentCid() == 0
                ).map(categoryVo -> {
                    categoryVo.setChidren(getChildrens(categoryVo, categoryVos));
                    return categoryVo;
                })
                .sorted((menu1, menu2) -> (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort()))
                .collect(Collectors.toList());
        return level1Menu;
    }

    @Override
    public void removeMenuByIds(Long[] catIds) {
        //TODO, 检查当前删除的菜单是否被其他地方引用
        //逻辑删除
        baseMapper.deleteBatchIds(Arrays.asList(catIds));
    }

    @Override
    public boolean menuExist(String name) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("name", name);
        Long count = baseMapper.selectCount(wrapper);
        return count > 0;
    }

    @Override
    public void saveCategoryVo(CategoryVo category) {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setParentCid(category.getCatId());
        categoryEntity.setCatLevel(category.getCatLevel() + 1);
        //设置排序为最后一位,若没有子节点则排序字段为0
        if (category.getChidren().size() == 0) {
            categoryEntity.setSort(0);
        } else {
            categoryEntity.setSort(getSort(category.getChidren()));
        }
        categoryEntity.setName(category.getName());
        categoryEntity.setIcon(category.getIcon());
        categoryEntity.setProductCount(category.getProductCount());
        categoryEntity.setProductUnit(category.getProductUnit());
        categoryEntity.setShowStatus(1);
        baseMapper.insert(categoryEntity);
    }

    @Override
    public void sort(CategoryEntity start, CategoryEntity end, String type) {
        //实际排序获取当前节点位置的列表
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("parent_cid", end.getParentCid());
        List<CategoryEntity> categoryEntities = baseMapper.selectList(queryWrapper);
        List<CategoryEntity> sortedList;
        if (!start.getParentCid().equals(end.getParentCid())) {
            //拖拽两次的父节点不想同，需要将前者的父节点修改为后者,再将后续的所有节点排序后移
            start.setParentCid(end.getParentCid());
            for (int i = 0; i < categoryEntities.size(); i++) {
                categoryEntities.get(i).setSort(i);
                if (end.getCatId().equals(categoryEntities.get(i).getCatId())) {
                    end.setSort(i);
                }
            }
            sortedList = categoryEntities.stream().map(entity -> {
                if ("before".equals(type)) {
                    if (entity.getSort().intValue() >= end.getSort().intValue()) {
                        entity.setSort(entity.getSort() + 1);
                    }
                } else {
                    if (entity.getSort().intValue() > end.getSort().intValue()) {
                        entity.setSort(entity.getSort() + 1);
                    }
                }
                return entity;
            }).collect(Collectors.toList());
            //为排序好的list添加
            if ("before".equals(type)) {
                start.setSort(end.getSort());
            } else {
                start.setSort(end.getSort() + 1);
            }
            sortedList.add(start);

        } else {
            //同一节点位置的拖拽，实际只有排序的交换
            for (int i = 0; i < categoryEntities.size(); i++) {
                categoryEntities.get(i).setSort(i);
                if (end.getCatId().equals(categoryEntities.get(i).getCatId())) {
                    end.setSort(i);
                }
                if (start.getCatId().equals(categoryEntities.get(i).getCatId())) {
                    start.setSort(i);
                }
            }
            categoryEntities.remove(start.getSort().intValue());
            sortedList = categoryEntities.stream().map(entity -> {
                if ("before".equals(type)) {
                    if (entity.getSort().intValue() > start.getSort().intValue() && entity.getSort().intValue() < end.getSort()) {
                        entity.setSort(entity.getSort() - 1);
                    }
                } else {
                    if (entity.getSort().intValue() > start.getSort().intValue() && entity.getSort().intValue() <= end.getSort()) {
                        entity.setSort(entity.getSort() - 1);
                    }
                }
                if(entity.getCatId().equals(start.getCatId())){
                    entity.setSort(end.getSort());
                }
                return entity;
            }).collect(Collectors.toList());
            if ("before".equals(type)) {
                start.setSort(end.getSort()-1);
            }else{
                start.setSort(end.getSort());
            }
            sortedList.add(start);
        }
        //对sortedCategory进行遍历进行update,此处存在疑惑就是未影响的会走数据库吗？
        for (CategoryEntity entity : sortedList) {
            baseMapper.updateById(entity);
        }
    }

    //将categoryentity转换为vo的方法
    private CategoryVo switchCategoryEntityToCategoryVo(CategoryEntity categoryEntity) {
        CategoryVo categoryVo = new CategoryVo();
        categoryVo.setCatId(categoryEntity.getCatId());
        categoryVo.setCatLevel(categoryEntity.getCatLevel());
        categoryVo.setChidren(null);
        categoryVo.setIcon(categoryEntity.getIcon());
        categoryVo.setName(categoryEntity.getName());
        categoryVo.setProductCount(categoryEntity.getProductCount());
        categoryVo.setParentCid(categoryEntity.getParentCid());
        categoryVo.setSort(categoryEntity.getSort());
        categoryVo.setShowStatus(categoryEntity.getShowStatus());
        categoryVo.setProductUnit(categoryEntity.getProductUnit());
        return categoryVo;
    }

    //递归获取菜单的子菜单
    private List<CategoryVo> getChildrens(CategoryVo entity, List<CategoryVo> categoryEntities) {
        List<CategoryVo> collect = categoryEntities.stream().filter(categoryVo ->
                        //此处必须要用equals,用双等好会丢失数据。
                        entity.getCatId().equals(categoryVo.getParentCid()))
                .map(vo -> {
                    vo.setChidren(getChildrens(vo, categoryEntities));
                    return vo;
                }).sorted((vo1, vo2) -> (vo1.getSort() == null ? 0 : vo1.getSort()) - (vo2.getSort() == null ? 0 : vo2.getSort()))
                .collect(Collectors.toList());
        return collect;
    }

    //获取子集最后sort，为新加的category排序
    public Integer getSort(List<CategoryVo> categoryVos) {
        List<CategoryVo> sortedVos = categoryVos.stream().sorted((vo1, vo2) -> vo2.getSort() - vo1.getSort())
                .collect(Collectors.toList());
        return sortedVos.get(0).getSort() + 1;
    }
}