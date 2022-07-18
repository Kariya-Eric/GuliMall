package org.kariya.gulimall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.alibaba.fastjson.JSON;
import org.json.JSONObject;
import org.kariya.gulimall.product.vo.CategoryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.kariya.gulimall.product.entity.CategoryEntity;
import org.kariya.gulimall.product.service.CategoryService;
import org.kariya.common.utils.PageUtils;
import org.kariya.common.utils.R;



/**
 * 商品三级分类
 *
 * @author kariya
 * @email eric15195016307@gmail.com
 * @date 2022-06-16 10:15:15
 */
@RestController
@RequestMapping("product/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 查出所有分类以及子分类，以树形结构组装起来
     */
    @RequestMapping("/list/tree")
    //@RequiresPermissions("product:category:list")
    public R list(){
        //PageUtils page = categoryService.queryPage(params);
        List<CategoryVo> entities=categoryService.listWithTree();
        return R.ok().put("data", entities);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{catId}")
    //@RequiresPermissions("product:category:info")
    public R info(@PathVariable("catId") Long catId){
		CategoryEntity category = categoryService.getById(catId);

        return R.ok().put("category", category);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:category:save")
    public R save(@RequestBody CategoryVo category){
		categoryService.saveCategoryVo(category);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:category:update")
    public R update(@RequestBody CategoryEntity category){
        categoryService.updateCategory(category);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:category:delete")
    public R delete(@RequestBody Long[] catIds){
		//categoryService.removeByIds(Arrays.asList(catIds));
        categoryService.removeMenuByIds(catIds);
        return R.ok();
    }

    //新增Category前的名称校验
    @RequestMapping("/menuExist/{name}")
    public R menuExist(@PathVariable("name")String name){
        boolean exist=categoryService.menuExist(name);
        if(!exist){
            return R.ok();
        }
        return R.error("目录名已存在");
    }

    //拖拽category之后排序方法
    @RequestMapping("/sort")
    public R sort(@RequestBody Map<String,Object> paramMap){
        Map<String,Object> startMap= (Map<String, Object>) paramMap.get("start");
        Map<String,Object> endMap= (Map<String, Object>) paramMap.get("end");
        CategoryEntity start=JSON.parseObject(JSON.toJSONString(startMap), CategoryEntity.class);
        CategoryEntity end=JSON.parseObject(JSON.toJSONString(endMap),CategoryEntity.class);
        String type= (String) paramMap.get("type");
        categoryService.sort(start,end,type);
        return R.ok();
    }
}
