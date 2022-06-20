package org.kariya.gulimall.product.vo;

import lombok.Data;
import org.kariya.gulimall.product.entity.CategoryEntity;

import java.util.List;

//用于表示
@Data
public class CategoryVo {
    private Long catId;
    private String name;
    private Long parentCid;
    private Integer catLevel;
    private Integer showStatus;
    private Integer sort;
    private String icon;
    private Integer productCount;
    private String productUnit;
    //用于表示是否有子分类
    private List<CategoryVo> chidren;
}
