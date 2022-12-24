package com.itheima.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.domain.Setmeal;
import com.itheima.mapper.SetmealMapper;
import com.itheima.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealMapper setmealMapper;

    @Override
    public int getTotal(String name) {
        return setmealMapper.getTotal(name);
    }

    @Override
    public List<Setmeal> selectByPage(int page, int pageSize,String name) {
        return setmealMapper.selectByPage(page,pageSize,name);
    }

    @Override
    public Setmeal selectById(Long id) {
        return setmealMapper.selectById(id);
    }

    @Override
    public int modifyStatusForSingleOrBatch(int status, List<Long> ids) {
        return setmealMapper.modifyStatusForSingleOrBatch(status,ids);
    }

    @Override
    public int deleteSingleOrBatchById(List<Long> ids) {
        return setmealMapper.deleteSingleOrBatchById(ids);
    }

    @Override
    public int getStatusById(Long id) {
        return setmealMapper.getStatusById(id);
    }
}
