package com.itheima.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.annotation.CacheEvictBatch;
import com.itheima.domain.Setmeal;
import com.itheima.mapper.SetmealMapper;
import com.itheima.service.SetmealService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Resource
    private SetmealMapper setmealMapper;

    @Override
    public int getTotal(String name) {
        return setmealMapper.getTotal(name);
    }

    @Override
    @Cacheable(value = "setMealCache",key = "T(String).valueOf(#page).concat('-').concat(#pageSize)")
    public List<Setmeal> selectByPage(int page, int pageSize,String name) {
        return setmealMapper.selectByPage(page,pageSize,name);
    }

    @Override
    @Cacheable(value = "setMealCache",key = "#id",condition = "#id != null")
    public Setmeal selectById(Long id) {
        return setmealMapper.selectById(id);
    }

    @Override
    @CacheEvictBatch(value = "setMealCache",keys = "#ids")
    public int modifyStatusForSingleOrBatch(int status, List<Long> ids) {
        return setmealMapper.modifyStatusForSingleOrBatch(status,ids);
    }

    @Override
    @CacheEvictBatch(value = "setMealCache",keys = "#ids")
    public int deleteSingleOrBatchById(List<Long> ids) {
        return setmealMapper.deleteSingleOrBatchById(ids);
    }

    @Override
    public int getStatusById(Long id) {
        return setmealMapper.getStatusById(id);
    }
}
