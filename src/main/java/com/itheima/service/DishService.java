package com.itheima.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.common.Result;
import com.itheima.domain.Dish;
import com.itheima.dto.DishDto;

import java.util.List;

public interface DishService extends IService<Dish> {
    Result<String> saveWithFlavor(DishDto dishDto);
    Result<Page<Dish>> selectByPage(int page,int pageSize,String name);
    Result<Page<DishDto>> select(int page,int pageSize,String name);
    Result<DishDto> getByIdWithFlavor(Long id);
    Result<String> updateWithFlavor(DishDto dishDto);
    Result<String> changeStatus(int status, Long ids);
    Result<String> changeStatusForBatch(int status, List<Long> ids);
    Result<String> deleteBatches(List<Long> ids);
    Result<List<DishDto>> listDish(Long categoryId);
}
