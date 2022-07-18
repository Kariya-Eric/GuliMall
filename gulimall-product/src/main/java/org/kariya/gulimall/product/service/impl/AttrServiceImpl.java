package org.kariya.gulimall.product.service.impl;

import org.apache.commons.lang.StringUtils;
import org.kariya.gulimall.product.dao.AttrAttrgroupRelationDao;
import org.kariya.gulimall.product.dao.AttrGroupDao;
import org.kariya.gulimall.product.dao.CategoryDao;
import org.kariya.gulimall.product.entity.AttrAttrgroupRelationEntity;
import org.kariya.gulimall.product.entity.AttrGroupEntity;
import org.kariya.gulimall.product.entity.CategoryEntity;
import org.kariya.gulimall.product.vo.AttrEditVo;
import org.kariya.gulimall.product.vo.AttrVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.kariya.common.utils.PageUtils;
import org.kariya.common.utils.Query;

import org.kariya.gulimall.product.dao.AttrDao;
import org.kariya.gulimall.product.entity.AttrEntity;
import org.kariya.gulimall.product.service.AttrService;
import org.springframework.transaction.annotation.Transactional;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {
    @Autowired
    private AttrAttrgroupRelationDao attrAttrgroupRelationDao;

    @Autowired
    private AttrGroupDao attrGroupDao;

    @Autowired
    private CategoryDao categoryDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String name = (String) params.get("name");
        String catId = (String) params.get("catId");
        IPage<AttrEntity> page = null;
        QueryWrapper<AttrEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.and((wrapper) -> {
            wrapper.like("attr_name", name).or().eq("attr_id", name);
        });
        if (Long.valueOf(catId) == 0) {
            page = this.page(
                    new Query<AttrEntity>().getPage(params), queryWrapper);
        } else {
            queryWrapper.eq("catelog_id", catId);
            page = this.page(
                    new Query<AttrEntity>().getPage(params), queryWrapper);
        }
        List<AttrEntity> records = page.getRecords();
        List<AttrVo> attrVos = records.stream().map(attrEntity -> {
            AttrVo attrVo = new AttrVo();
            BeanUtils.copyProperties(attrEntity, attrVo);
            //设置分类和分组的名字
            AttrAttrgroupRelationEntity relationEntity = attrAttrgroupRelationDao.selectOne(
                    new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrEntity.getAttrId()));
            if (relationEntity != null) {
                AttrGroupEntity attrGroupEntity = attrGroupDao.selectOne(
                        new QueryWrapper<AttrGroupEntity>().eq("attr_group_id", relationEntity.getAttrGroupId()));
                attrVo.setAttrGroupName(attrGroupEntity.getAttrGroupName());
            }

            CategoryEntity categoryEntity = categoryDao.selectOne(
                    new QueryWrapper<CategoryEntity>().eq("cat_id", attrEntity.getCatelogId()));
            if (categoryEntity != null) {
                attrVo.setCateLogName(categoryEntity.getName());
            }
            return attrVo;
        }).collect(Collectors.toList());
        PageUtils pageUtils = new PageUtils(page);
        pageUtils.setList(attrVos);
        return pageUtils;
    }

    @Override
    @Transactional
    public void saveAttr(AttrVo attrvo) {
        //将AttrVo的值转换为Attr
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attrvo, attrEntity);
        //保存AttrEntity基本数据
        this.getBaseMapper().insert(attrEntity);
        //保存relation表格中的
        AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
        relationEntity.setAttrGroupId(attrvo.getAttrGroup());
        //如果是新增的话，这个attrvo的getattrid为0,需要从数据库获取
        relationEntity.setAttrId(attrEntity.getAttrId());
        attrAttrgroupRelationDao.insert(relationEntity);
    }

    @Override
    public AttrEditVo getAttrEditVoById(Long attrId) {
        AttrEntity attrEntity = baseMapper.selectById(attrId);
        AttrEditVo attrEditVo = new AttrEditVo();
        BeanUtils.copyProperties(attrEntity, attrEditVo);
        Long catelogId = attrEntity.getCatelogId();
        List<Long> catelogIds = new ArrayList<>();
        catelogIds = getCatelogIds(catelogId, catelogIds);
        long[] ids = new long[catelogIds.size()];
        for (int i = 0; i < catelogIds.size(); i++) {
            ids[i] = catelogIds.get(catelogIds.size() - i - 1);
        }
        attrEditVo.setCatelogIds(ids);
        AttrAttrgroupRelationEntity relationEntity = attrAttrgroupRelationDao.selectById(attrId);
        if (relationEntity != null) {
            AttrGroupEntity attrGroupEntity = attrGroupDao.selectOne(new QueryWrapper<AttrGroupEntity>()
                    .eq("attr_group_id", relationEntity.getAttrGroupId()));
            attrEditVo.setAttrGroup(attrGroupEntity.getAttrGroupId());
        }
        return attrEditVo;
    }

    @Override
    @Transactional
    public void updateAttr(AttrVo attrvo) {
        //将AttrVo的值转换为Attr
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attrvo, attrEntity);
        //修改AttrEntity基本数据
        System.out.println(attrEntity);
        //保存relation表格中的
        AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
        relationEntity.setAttrGroupId(attrvo.getAttrGroup());
        //如果是新增的话，这个attrvo的getattrid为0,需要从数据库获取
        relationEntity.setAttrId(attrEntity.getAttrId());
        System.out.println(relationEntity);
    }

    //根据分类id找出级联菜单的数组
    private List<Long> getCatelogIds(Long parentCid, List<Long> cateList) {
        //根据catelogid获取catelogid数组
        if (parentCid != 0) {
            cateList.add(parentCid);
            CategoryEntity entity = categoryDao.selectById(parentCid);
            return getCatelogIds(entity.getParentCid(), cateList);
        }
        return cateList;
    }
}