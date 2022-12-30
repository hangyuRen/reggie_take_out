package com.itheima.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.domain.OrderDetail;
import com.itheima.mapper.OrderDetailMapper;
import com.itheima.service.OrderDetailService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
    @Resource
    private OrderDetailMapper orderDetailMapper;
    @Override
    @Cacheable(value = "orderDetailCache",key = "#id",condition = "#id != null")
    public List<OrderDetail> getOrderDetailByOrderId(Long id) {
        return orderDetailMapper.getOrderDetailByOrderId(id);
    }
}
