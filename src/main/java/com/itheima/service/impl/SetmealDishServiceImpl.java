package com.itheima.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.annotation.CacheEvictBatch;
import com.itheima.annotation.MyLog;
import com.itheima.common.Result;
import com.itheima.domain.Category;
import com.itheima.domain.Setmeal;
import com.itheima.domain.SetmealDish;
import com.itheima.dto.SetmealDto;
import com.itheima.exception.CustomException;
import com.itheima.mapper.SetmealDishMapper;
import com.itheima.service.CategoryService;
import com.itheima.service.SetmealDishService;
import com.itheima.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
@SuppressWarnings("all")
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishMapper, SetmealDish> implements SetmealDishService {
    @Resource
    private SetmealService setmealService;
    @Resource
    private SetmealDishMapper setmealDishMapper;
    @Resource
    private CategoryService categoryService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @MyLog
    public Result<String> saveSetmeal(SetmealDto setmealDto) {
        boolean bool = setmealService.save(setmealDto);
        if(!bool) {
            throw new CustomException("添加失败");
        }
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        boolean b = this.saveBatch(setmealDishes);
        if(!b) {
            return Result.error("添加失败");
        }

        return Result.success("添加成功");
    }

    @Override
    @Cacheable(value = "setMealDishCache",key = "T(String).valueOf(#page).concat('-').concat(#pageSize)")
    public Result<Page<SetmealDto>> selectByPage(int page,int pageSize,String name) {
        Page<SetmealDto> ipage = new Page<>();
        if(name != null) {
            name += "%";
        }
        int total = setmealService.getTotal(name);
        if(total == 0) {
            return Result.success(null);
        }

        List<Setmeal> setmeals = setmealService.selectByPage(pageSize * (page - 1), pageSize, name);
        List<SetmealDto> mealDto = setmeals.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item,setmealDto);
            Long categoryId = item.getCategoryId();
            String categoryName = categoryService.getById(categoryId).getName();
            setmealDto.setCategoryName(categoryName);
            return setmealDto;
        }).collect(Collectors.toList());

        ipage.setRecords(mealDto);
        ipage.setTotal(total);
        return Result.success(ipage);
    }

    @Override
    @Cacheable(value = "setMealDishCache",key = "#id",condition = "#id != null")
    public Result<SetmealDto> getByIdWithDishes(Long id) {
        Setmeal setmeal = setmealService.selectById(id);
        Category category = categoryService.getById(setmeal.getCategoryId());
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal,setmealDto);
        setmealDto.setCategoryName(category.getName());

        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,id);
        List<SetmealDish> setmealDishes = setmealDishMapper.selectList(queryWrapper);

        setmealDto.setSetmealDishes(setmealDishes);
        return Result.success(setmealDto);
    }

    @Override
    @CacheEvictBatch(value = "setMealDishCache",keys = "#ids")
    public Result<String> modifyStatusForSingleOrBatch(int status, List<Long> ids) {
        int i = setmealService.modifyStatusForSingleOrBatch(status,ids);
        if(i <= 0) {
            return Result.error("修改失败");
        }
        return Result.success("修改成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "setMealDishCache",key = "#setmealDto.id",condition = "#setmealDto.id != null")
    public Result<String> updateSetmeal(SetmealDto setmealDto) {
        Long setmealId = setmealDto.getId();
        boolean b = setmealService.updateById(setmealDto);
        if(!b) {
            throw new CustomException("修改失败");
        }

        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,setmealId);
        boolean remove = this.remove(queryWrapper);
        if(!remove) {
            throw new CustomException("修改失败");
        }

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes = setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealId);
            return item;
        }).collect(Collectors.toList());
        boolean saveBatch = this.saveBatch(setmealDishes);
        if(!saveBatch) {
            throw new CustomException("修改失败");
        }

        return Result.success("修改成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvictBatch(value = "setMealDishCache",keys = "#ids")
    public Result<String> deleteSingleOrBatch(List<Long> ids) {
        for(Long id:ids) {
            int statusById = setmealService.getStatusById(id);
            if(statusById == 1) {
                throw new CustomException("在售套餐不能删除");
            }
        }
        int i = setmealService.deleteSingleOrBatchById(ids);
        if(i <= 0) {
            throw new CustomException("删除失败");
        }

        int byId = setmealDishMapper.deleteSingleOrBatchById(ids);
        if(byId <= 0) {
            throw new CustomException("删除失败");
        }
        return Result.success("删除成功");
    }

    @Override
    @Cacheable(value = "setMealDishCache",key = "T(String).valueOf(#categoryId).concat('-').concat(#status)")
    public Result<List<Setmeal>> setmealList(Long categoryId, Integer status) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(categoryId != null,Setmeal::getCategoryId,categoryId);
        queryWrapper.eq(status != null,Setmeal::getStatus,status);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> list = setmealService.list(queryWrapper);
        return Result.success(list);
    }
}
