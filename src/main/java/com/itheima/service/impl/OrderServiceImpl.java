package com.itheima.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.common.BaseContext;
import com.itheima.common.Result;
import com.itheima.domain.*;
import com.itheima.dto.OrdersDto;
import com.itheima.exception.CustomException;
import com.itheima.mapper.OrderMapper;
import com.itheima.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@SuppressWarnings("all")
@Service
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders> implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private UserService userService;
    @Autowired
    private AddressBookService addressBookService;
    @Autowired
    private ShoppingCartService shoppingCartService;

    @Override
    public Result<Page<OrdersDto>> selectByPage(int page, int pageSize, String number, LocalDateTime inTime, LocalDateTime endTime) {
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(number != null,Orders::getNumber,number);
        queryWrapper.between(inTime != null && endTime != null,Orders::getOrderTime,inTime,endTime);

        Page<Orders> ordersPage = new Page<>(page,pageSize);
        List<Orders> records = orderMapper.selectPage(ordersPage, queryWrapper).getRecords();
        List<OrdersDto> orderDto = records.stream().map((item) -> {
            OrdersDto ordersDto = new OrdersDto();
            BeanUtils.copyProperties(item,ordersDto);

            String name = userService.getById(item.getUserId()).getName();
            ordersDto.setUserName(name);

            AddressBook defaultAddressByUserId = addressBookService.getDefaultAddressByUserId(item.getUserId());
            String address = concatAddress(defaultAddressByUserId.getProvinceName(),defaultAddressByUserId.getCityName(),defaultAddressByUserId.getDistrictName(),defaultAddressByUserId.getDetail());

            ordersDto.setAddress(address);
            return ordersDto;
        }).collect(Collectors.toList());

        Page<OrdersDto> result = new Page<>();
        result.setRecords(orderDto);
        result.setTotal(ordersPage.getTotal());
        return Result.success(result);
    }

    public String concatAddress(String provinceName, String cityName, String districtName, String detail) {
        StringBuilder stringBuilder=new StringBuilder();
        if(provinceName != null) {
            stringBuilder.append(provinceName);
        }
        if(cityName != null) {
            stringBuilder.append(cityName);
        }
        if(districtName != null) {
            stringBuilder.append(districtName);
        }
        if(detail != null) {
            stringBuilder.append(detail);
        }
        return stringBuilder.toString();
    }

    @Override
    public Result<Page<OrdersDto>> getOrdersInfo(int page, int pageSize) {
        Page<Orders> ordersPage = new Page<>(page,pageSize);
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getUserId, BaseContext.getCurrentId());
        queryWrapper.orderByDesc(Orders::getOrderTime);
        orderMapper.selectPage(ordersPage,queryWrapper);

        List<OrdersDto> ordersDtoList = ordersPage.getRecords().stream().map(item -> {
            OrdersDto ordersDto = new OrdersDto();
            BeanUtils.copyProperties(item,ordersDto);
            Long id = Long.parseLong(item.getNumber());
            List<OrderDetail> orderDetailByOrderId = orderDetailService.getOrderDetailByOrderId(id);
            ordersDto.setOrderDetails(orderDetailByOrderId);
            return ordersDto;
        }).collect(Collectors.toList());

        Page<OrdersDto> ordersDtoPage = new Page<>();
        ordersDtoPage.setRecords(ordersDtoList);
        ordersDtoPage.setTotal(ordersPage.getTotal());
        return Result.success(ordersDtoPage);
    }

    @Override
    public Result<String> submitOrder(Orders orders) {
        log.info("orders:{}",orders.toString());
        Long currentId = BaseContext.getCurrentId();

        List<ShoppingCart> userShoppingCart = shoppingCartService.getUserShoppingCart(currentId);
        if(userShoppingCart.size() == 0) {
            throw new CustomException("购物车为空，下单失败");
        }
        long orderId = IdWorker.getId();

        AtomicInteger amount = new AtomicInteger(0);
        List<OrderDetail> orderDetails = userShoppingCart.stream().map((item) -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setNumber(item.getNumber());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setName(item.getName());
            orderDetail.setImage(item.getImage());
            orderDetail.setAmount(item.getAmount());
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            return orderDetail;
        }).collect(Collectors.toList());

        User byId = userService.getById(currentId);
        Long addressBookId = orders.getAddressBookId();
        AddressBook addressBook = addressBookService.getById(addressBookId);
        if(addressBook == null) {
            throw new CustomException("地址有误");
        }

        orders.setUserId(currentId);
        orders.setNumber(String.valueOf(orderId));
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setStatus(2);
        orders.setUserName(byId.getName());
        orders.setAmount(new BigDecimal(amount.get()));
        orders.setPhone(byId.getPhone());
        orders.setConsignee(addressBook.getConsignee());
        orders.setAddress(concatAddress(addressBook.getProvinceName(),addressBook.getCityName(),addressBook.getDistrictName(),addressBook.getDetail()));

        int insert = orderMapper.insert(orders);
        if(insert <= 0) {
            return Result.error("支付失败");
        }

        boolean b = orderDetailService.saveBatch(orderDetails);
        if(!b) {
            return Result.error("支付失败");
        }

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,currentId);
        boolean remove = shoppingCartService.remove(queryWrapper);
        if(!remove) {
            return Result.error("支付失败");
        }

        return Result.success("支付成功");
    }

    @Override
    public Result<String> againSubmit(Map<String, String> map) {
        String ids = map.get("id");
        long id = Long.parseLong(ids);
        Orders orders = orderMapper.selectById(id);
        LambdaQueryWrapper<OrderDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderDetail::getOrderId,orders.getNumber());
        List<OrderDetail> orderDetailList = orderDetailService.list(queryWrapper);

        shoppingCartService.cleanShoppingCart();

        Long currentId = BaseContext.getCurrentId();
        List<ShoppingCart> shoppingCartList = orderDetailList.stream().map((item) -> {
            ShoppingCart shoppingCart = new ShoppingCart();
            shoppingCart.setUserId(currentId);
            shoppingCart.setImage(item.getImage());
            Long dishId = item.getDishId();
            Long setmealId = item.getSetmealId();
            if(dishId != null) {
                shoppingCart.setDishId(dishId);
            } else {
                shoppingCart.setSetmealId(setmealId);
            }
            shoppingCart.setName(item.getName());
            shoppingCart.setDishFlavor(item.getDishFlavor());
            shoppingCart.setNumber(item.getNumber());
            shoppingCart.setAmount(item.getAmount());
            shoppingCart.setCreateTime(LocalDateTime.now());
            return shoppingCart;
        }).collect(Collectors.toList());

        boolean b = shoppingCartService.saveBatch(shoppingCartList);
        if(!b) {
            return Result.error("操作失败");
        }

        return Result.success("操作成功");
    }
}
