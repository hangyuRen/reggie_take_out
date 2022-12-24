package com.itheima.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.domain.Dish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {
    int getTotal(@Param("name") String name);
    List<Dish> selectByPage(@Param("page") int page, @Param("pageSize") int pageSize, @Param("name") String name);
    Dish getById(@Param("id") Long id);
    int changeStatus(@Param("status") int status, @Param("id") Long id);
    int changeStatusForBatch(@Param("status") int status,@Param("ids") List<Long> ids);
    int deleteBatches(@Param("ids") List<Long> ids);
    int getStatusById(@Param("id") Long id);
    List<Dish> listDish(@Param("categoryId") Long categoryId);
}
