package com.itheima.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.domain.DishFlavor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DishFlavorMapper extends BaseMapper<DishFlavor> {
    List<DishFlavor> getFlavorsById(@Param("id") Long id);
    int deleteBatches(@Param("ids") List<Long> ids);
}
