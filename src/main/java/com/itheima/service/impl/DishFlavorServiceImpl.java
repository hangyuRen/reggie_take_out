package com.itheima.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.annotation.CacheEvictBatch;
import com.itheima.annotation.MyLog;
import com.itheima.domain.DishFlavor;
import com.itheima.mapper.DishFlavorMapper;
import com.itheima.service.DishFlavorService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
    @Resource
    private DishFlavorMapper dishFlavorMapper;
    @Override
    @MyLog
    @Cacheable(value = "flavorCache",key = "#id",condition = "#id != null")
    public List<DishFlavor> getFlavorsById(Long id) {
        return dishFlavorMapper.getFlavorsById(id);
    }

    @Override
    @CacheEvictBatch(value = "flavorCache",keys = "#ids")
    @MyLog
    public int deleteBatches(List<Long> ids) {
        return dishFlavorMapper.deleteBatches(ids);
    }
}
