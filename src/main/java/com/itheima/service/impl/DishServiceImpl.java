package com.itheima.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.common.Result;
import com.itheima.domain.Category;
import com.itheima.domain.Dish;
import com.itheima.domain.DishFlavor;
import com.itheima.dto.DishDto;
import com.itheima.exception.CustomException;
import com.itheima.mapper.DishMapper;
import com.itheima.service.CategoryService;
import com.itheima.service.DishFlavorService;
import com.itheima.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings({"all"})
@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryService categoryService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> saveWithFlavor(DishDto dishDto) {
        log.info(dishDto.toString());
        int insert = dishMapper.insert(dishDto);
        if(!(insert > 0)) {
            return Result.error("添加失敗");
        }
        //使每种口味与菜品id关联
        Long id = dishDto.getId();
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) -> {
            item.setDishId(id);
            return item;
        }).collect(Collectors.toList());

        boolean b = dishFlavorService.saveBatch(flavors);
        if(b) {
            return Result.success("新增菜品成功");
        }
        return Result.error("添加失败");
    }

    @Override
    public Result<Page<Dish>> selectByPage(int page, int pageSize,String name) {
        Page<Dish> iPage = new Page<>();
        if(name != null) {
            name += "%";
        }
        List<Dish> dishes = dishMapper.selectByPage((page - 1) * pageSize, pageSize, name);
        int total = dishMapper.getTotal(name);
        iPage.setRecords(dishes);
        iPage.setTotal(total);
        return Result.success(iPage);
    }

    @Override
    public Result<Page<DishDto>> select(int page, int pageSize, String name) {
        Page<DishDto> dishDtoPage = new Page<>();
        if(name != null) {
            name += "%";
        }
        int total = dishMapper.getTotal(name);
        List<Dish> dishes = dishMapper.selectByPage((page - 1) * pageSize, pageSize, name);

        List<DishDto> list = dishes.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            String categoryName = category.getName();
            dishDto.setCategoryName(categoryName);
            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);
        dishDtoPage.setTotal(total);
        return Result.success(dishDtoPage);
    }

    @Override
    public Result<DishDto> getByIdWithFlavor(Long id) {
        DishDto dishDto = new DishDto();
        Dish dish = dishMapper.getById(id);
        BeanUtils.copyProperties(dish,dishDto);
        List<DishFlavor> flavorsById = dishFlavorService.getFlavorsById(id);
        dishDto.setFlavors(flavorsById);
        return Result.success(dishDto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> updateWithFlavor(DishDto dishDto) {
        //更新dish表
        dishMapper.updateById(dishDto);

        //更新dish_flavor表
        //一个菜品可能关联多个flavor所以 先删除再插入
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(queryWrapper);
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
        return Result.success("修改成功");
    }

    @Override
    public Result<String> changeStatus(int status, Long ids) {
        int i = dishMapper.changeStatus(status, ids);
        if(i > 0) {
            return Result.success("修改成功");
        }
        return Result.error("修改失败");
    }

    @Override
    public Result<String> changeStatusForBatch(int status, List<Long> ids) {
        int i = dishMapper.changeStatusForBatch(status, ids);
        if(i > 0) {
            return Result.success("修改成功");
        }
        return Result.error("修改失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> deleteBatches(List<Long> ids) {
        log.info(ids.toString());
        for(Long id:ids) {
            int statusById = dishMapper.getStatusById(id);
            if(statusById == 1) {
                throw new CustomException("在售商品不能删除");
            }
        }

        int dishCount = dishMapper.deleteBatches(ids);
        int dishFlavorCount = dishFlavorService.deleteBatches(ids);
        if(dishCount < 0) {
            return Result.error("修改失败");
        }

        if(dishFlavorCount < 0) {
            return Result.error("修改失败");
        }

        return Result.success("修改成功");
    }

    @Override
    public Result<List<DishDto>> listDish(Long categoryId) {
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getCategoryId,categoryId);
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        queryWrapper.eq(Dish::getStatus,1);
        List<Dish> dishes = dishMapper.selectList(queryWrapper);
        if(dishes == null) {
            throw new CustomException("未查询到数据");
        }

        List<DishDto> dishDtoList = dishes.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            Long id = item.getCategoryId();
            Category category = categoryService.getById(id);
            if(category != null) {
                dishDto.setCategoryName(category.getName());
            }
            List<DishFlavor> flavorsById = dishFlavorService.getFlavorsById(item.getId());

            dishDto.setFlavors(flavorsById);

            return dishDto;
        }).collect(Collectors.toList());


        return Result.success(dishDtoList);
    }
}
