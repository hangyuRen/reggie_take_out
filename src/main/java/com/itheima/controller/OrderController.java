package com.itheima.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.common.Result;
import com.itheima.domain.Orders;
import com.itheima.dto.OrdersDto;
import com.itheima.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@SuppressWarnings("all")
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 后台 订单分页查询
     * @param page
     * @param pageSize
     * @param number
     * @param inTime
     * @param endTime
     * @return
     */
    @GetMapping("/page")
    public Result<Page<OrdersDto>> selectByPage(@RequestParam("page") int page, @RequestParam("pageSize") int pageSize, String number, LocalDateTime inTime ,LocalDateTime endTime) {
        return orderService.selectByPage(page,pageSize,number,inTime,endTime);
    }

    /**
     * 前台 最新订单查询
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/userPage")
    public Result<Page<OrdersDto>> getOrdersInfo(@RequestParam("page") int page,@RequestParam("pageSize") int pageSize) {
        return orderService.getOrdersInfo(page,pageSize);
    }

    /**
     * 提交订单
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public Result<String> submitOrder(@RequestBody Orders orders) {
        return orderService.submitOrder(orders);
    }

    /**
     * 再来一单
     * @param map 订单id （并非订单号）
     * @return
     */
    @PostMapping("/again")
    public Result<String> againSubmit(@RequestBody Map<String,String> map) {
        return orderService.againSubmit(map);
    }
}
