package com.itheima.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.common.Result;
import com.itheima.domain.Setmeal;
import com.itheima.domain.SetmealDish;
import com.itheima.dto.SetmealDto;

import java.util.List;
@SuppressWarnings("all")
public interface SetmealDishService extends IService<SetmealDish> {
    Result<String> saveSetmeal(SetmealDto setmealDto);
    Result<Page<SetmealDto>> selectByPage(int page, int pageSize, String name);
    Result<SetmealDto> getByIdWithDishes(Long id);
    Result<String> modifyStatusForSingleOrBatch(int status, List<Long> ids);
    Result<String> updateSetmeal(SetmealDto setmealDto);
    Result<String> deleteSingleOrBatch(List<Long> ids);
    Result<List<Setmeal>> setmealList(Long categoryId,Integer status);
}
