package com.itheima.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.annotation.MyLog;
import com.itheima.common.Result;
import com.itheima.domain.Category;
import com.itheima.domain.Dish;
import com.itheima.domain.Setmeal;
import com.itheima.exception.CustomException;
import com.itheima.mapper.CategoryMapper;
import com.itheima.mapper.DishMapper;
import com.itheima.mapper.SetmealMapper;
import com.itheima.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Slf4j
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    @Override
    @MyLog
    public Result<String> add(Category category) {
        log.info("category{}",category.toString());
        int insert = categoryMapper.insert(category);

        if(insert > 0) {
            return Result.success("添加成功");
        }

        return Result.error("添加失败");
    }

    @Override
    @Cacheable(value = "categoryCache",key = "T(String).valueOf(#page).concat('-').concat(#pageSize)",unless = "#result = null")
    public Result<Page<Category>> selectByPage(int page, int pageSize) {
        log.info("page = {},pageSize = {}",page,pageSize);
        Page<Category> iPage = new Page<>();
        iPage.setTotal(categoryMapper.getTotal());
        List<Category> categories = categoryMapper.selectByPage((page - 1) * pageSize, pageSize);
        iPage.setRecords(categories);
        return Result.success(iPage);
    }

    @Override
    @CacheEvict(value = "categoryCache",key = "#id",condition = "#id != null")
    public Result<String> deleteById(Long id) {
        log.info("delete id = {}",id);

        LambdaQueryWrapper<Dish> dishQueryWrapper = new LambdaQueryWrapper<>();
        dishQueryWrapper.eq(Dish::getCategoryId,id);
        int dishCount = dishMapper.selectCount(dishQueryWrapper);
        if(dishCount > 0) {
            throw new CustomException("当前分类下关联了套餐，不能删除");
        }

        LambdaQueryWrapper<Setmeal> mealQueryWrapper = new LambdaQueryWrapper<>();
        mealQueryWrapper.eq(Setmeal::getCategoryId,id);
        int mealCount = setmealMapper.selectCount(mealQueryWrapper);
        if(mealCount > 0) {
            throw new CustomException("当前分类下关联了套餐，不能删除");
        }

        int i = categoryMapper.deleteById(id);
        if(i > 0) {
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }

    @Override
    @CacheEvict(value = "categoryCache",key = "#category.id",condition = "category != null")
    public Result<String> updateCategory(Category category) {
        log.info("category:{}",category.toString());

        int i = categoryMapper.updateById(category);
        System.out.println(i);
        if(i > 0) {
            return Result.success("修改成功");
        }

        return Result.error("修改失败");
    }

    @Override
    @Cacheable(value = "categoryCache",key = "#category.type",condition = "#category.type != null")
    public Result<List<Category>> myList(Category category) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(category.getType() != null,Category::getType,category.getType());
        queryWrapper.orderByAsc(Category::getType).orderByDesc(Category::getUpdateTime);
        List<Category> categories = categoryMapper.selectList(queryWrapper);
        return Result.success(categories);
    }
}
