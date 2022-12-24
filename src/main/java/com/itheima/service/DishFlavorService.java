package com.itheima.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.domain.DishFlavor;

import java.util.List;

public interface DishFlavorService extends IService<DishFlavor> {
    List<DishFlavor> getFlavorsById(Long id);
    int deleteBatches(List<Long> ids);
}
