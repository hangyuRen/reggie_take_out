package com.itheima.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.domain.Setmeal;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SetmealMapper extends BaseMapper<Setmeal> {
    List<Setmeal> selectByPage(@Param("page") int page, @Param("pageSize") int pageSize, @Param("name") String name);
    int getTotal(@Param("name") String name);
    int modifyStatusForSingleOrBatch(@Param("status") int status, @Param("ids") List<Long> ids);
    int deleteSingleOrBatchById(@Param("ids") List<Long> ids);
    int getStatusById(@Param("id") Long id);
}
