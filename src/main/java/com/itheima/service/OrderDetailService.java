package com.itheima.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.domain.OrderDetail;

import java.util.List;

public interface OrderDetailService extends IService<OrderDetail> {
    List<OrderDetail> getOrderDetailByOrderId(Long id);
}
