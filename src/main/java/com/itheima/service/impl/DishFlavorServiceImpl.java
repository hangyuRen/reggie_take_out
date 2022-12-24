package com.itheima.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.domain.DishFlavor;
import com.itheima.mapper.DishFlavorMapper;
import com.itheima.service.DishFlavorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Override
    public List<DishFlavor> getFlavorsById(Long id) {
        return dishFlavorMapper.getFlavorsById(id);
    }

    @Override
    public int deleteBatches(List<Long> ids) {
        return dishFlavorMapper.deleteBatches(ids);
    }
}
