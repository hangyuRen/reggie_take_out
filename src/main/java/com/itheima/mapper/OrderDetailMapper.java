package com.itheima.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.domain.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {
    List<OrderDetail> getOrderDetailByOrderId(@Param("id") Long id);
}
