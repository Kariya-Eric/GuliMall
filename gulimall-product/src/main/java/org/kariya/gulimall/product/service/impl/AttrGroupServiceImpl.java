package org.kariya.gulimall.product.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.kariya.gulimall.product.entity.CategoryEntity;
import org.kariya.gulimall.product.service.CategoryService;
import org.kariya.gulimall.product.vo.AttrGroupEntityVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.kariya.common.utils.PageUtils;
import org.kariya.common.utils.Query;

import org.kariya.gulimall.product.dao.AttrGroupDao;
import org.kariya.gulimall.product.entity.AttrGroupEntity;
import org.kariya.gulimall.product.service.AttrGroupService;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Autowired
    private CategoryService categoryService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String catId = (String) params.get("catId");
        String name = (String) params.get("name");
        IPage<AttrGroupEntity> page = null;
        QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper();
        wrapper.and((w) -> {
            w.eq("attr_group_id", name).or().like("attr_group_name", name);
        });
        if (Long.valueOf(catId) == 0) {
            page = this.page(
                    new Query<AttrGroupEntity>().getPage(params), wrapper);
        } else {
            wrapper.eq("catelog_id", catId);
            page = this.page(
                    new Query<AttrGroupEntity>().getPage(params), wrapper);
        }
        return new PageUtils(page);
    }

    @Override
    public AttrGroupEntityVo getInfoById(Long attrGroupId) {
        AttrGroupEntity attrGroup = baseMapper.selectById(attrGroupId);
        AttrGroupEntityVo attrGroupEntityVo = switchEntityToVo(attrGroup);
        Long catelogId = attrGroup.getCatelogId();
        List<Long> catelogIds = new ArrayList<>();
        catelogIds = getCatelogIds(catelogId, catelogIds);
        long[] ids = new long[catelogIds.size()];
        for (int i = 0; i < catelogIds.size(); i++) {
            ids[i] = catelogIds.get(catelogIds.size() - i - 1);
        }
        attrGroupEntityVo.setCatelogIds(ids);
        return attrGroupEntityVo;
    }

    //根据分类id找出级联菜单的数组
    private List<Long> getCatelogIds(Long parentCid, List<Long> cateList) {
        //根据catelogid获取catelogid数组
        if (parentCid != 0) {
            cateList.add(parentCid);
            CategoryEntity entity = categoryService.getById(parentCid);
            return getCatelogIds(entity.getParentCid(), cateList);
        }
        return cateList;
    }

    //将AttrGroupEntity转换为Vo
    private AttrGroupEntityVo switchEntityToVo(AttrGroupEntity attrGroupEntity) {
        AttrGroupEntityVo attrGroupEntityVo = new AttrGroupEntityVo();
        attrGroupEntityVo.setAttrGroupId(attrGroupEntity.getAttrGroupId());
        attrGroupEntityVo.setAttrGroupName(attrGroupEntity.getAttrGroupName());
        attrGroupEntityVo.setCatelogId(attrGroupEntity.getCatelogId());
        attrGroupEntityVo.setDescript(attrGroupEntity.getDescript());
        attrGroupEntityVo.setIcon(attrGroupEntity.getIcon());
        attrGroupEntityVo.setSort(attrGroupEntity.getSort());
        return attrGroupEntityVo;
    }
}



