package com.itheima.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.domain.OrderDetail;
import com.itheima.mapper.OrderDetailMapper;
import com.itheima.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Override
    public List<OrderDetail> getOrderDetailByOrderId(Long id) {
        return orderDetailMapper.getOrderDetailByOrderId(id);
    }
}
