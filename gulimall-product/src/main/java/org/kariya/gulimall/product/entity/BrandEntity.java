package org.kariya.gulimall.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import org.hibernate.validator.constraints.URL;
import org.kariya.common.validator.group.AddGroup;
import org.kariya.common.validator.group.Group;
import org.kariya.common.validator.group.SortValid;
import org.kariya.common.validator.group.UpdateGroup;

import javax.validation.constraints.*;

/**
 * 品牌
 *
 * @author kariya
 * @email eric15195016307@gmail.com
 * @date 2022-06-16 10:15:15
 */
@Data
@TableName("pms_brand")
public class BrandEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 品牌id
     */
    @NotNull(message = "修改必须指定品牌id", groups = {UpdateGroup.class})
    @TableId
    private Long brandId;
    /**
     * 品牌名
     */
    //后端注解校验
    @NotBlank(message = "品牌名不能为空", groups = {UpdateGroup.class, AddGroup.class})
    private String name;
    /**
     * 品牌logo地址
     */
    @NotEmpty(message = "品牌logo地址不能为空", groups = {UpdateGroup.class, AddGroup.class})
    @URL(message = "品牌地址非法", groups = {UpdateGroup.class, AddGroup.class})
    private String logo;
    /**
     * 介绍
     */
    private String descript;
    /**
     * 显示状态[0-不显示；1-显示]
     */
    @SortValid(groups = {AddGroup.class, UpdateGroup.class})
    private Integer showStatus;
    /**
     * 检索首字母
     */
    @NotEmpty(message = "检索首字母不能为空", groups = {UpdateGroup.class, AddGroup.class})
    @Pattern(regexp = "^[a-zA-Z]$", message = "检索首字母必须是一个字母", groups = {UpdateGroup.class, AddGroup.class})
    private String firstLetter;
    /**
     * 排序
     */
    private Integer sort;

}
