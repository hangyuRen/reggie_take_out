package com.itheima.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.domain.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    int getTotal(String name);
    List<Setmeal> selectByPage(int page,int pageSize,String name);
    Setmeal selectById(Long id);
    int modifyStatusForSingleOrBatch(int status,List<Long> ids);
    int deleteSingleOrBatchById(List<Long> ids);
    int getStatusById(Long id);
}
