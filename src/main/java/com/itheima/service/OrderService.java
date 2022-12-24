package com.itheima.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.common.Result;
import com.itheima.domain.Orders;
import com.itheima.dto.OrdersDto;

import java.time.LocalDateTime;
import java.util.Map;

public interface OrderService extends IService<Orders> {
    Result<Page<OrdersDto>> selectByPage(int page, int pageSize, String number, LocalDateTime inTime, LocalDateTime endTime);
    Result<Page<OrdersDto>> getOrdersInfo(int page,int pageSize);
    Result<String> submitOrder(Orders orders);
    Result<String> againSubmit(Map<String,String> map);
}
