package com.itheima.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.common.BaseContext;
import com.itheima.common.Result;
import com.itheima.domain.ShoppingCart;
import com.itheima.exception.CustomException;
import com.itheima.mapper.ShoppingCartMapper;
import com.itheima.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SuppressWarnings("all")
@Service
@Slf4j
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Override
    public Result<List<ShoppingCart>> getShoppingCartList() {
        Long currentId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,currentId);
        queryWrapper.gt(ShoppingCart::getNumber,0);
        List<ShoppingCart> shoppingCarts = shoppingCartMapper.selectList(queryWrapper);
        return Result.success(shoppingCarts);
    }

    @Override
    public Result<ShoppingCart> add(ShoppingCart shoppingCart) {
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);
        Long dishId = shoppingCart.getDishId();
        Long setmealId = shoppingCart.getSetmealId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,currentId);
        if(dishId == null && setmealId == null) {
            throw new CustomException("添加失败");
        }

        if(dishId != null) {
            queryWrapper.eq(ShoppingCart::getDishId,dishId);
        }

        if(setmealId != null) {
            queryWrapper.eq(ShoppingCart::getSetmealId,setmealId);
        }

        ShoppingCart shoppingCartOne = shoppingCartMapper.selectOne(queryWrapper);
        if(shoppingCartOne != null) {
            Integer number = shoppingCartOne.getNumber();
            shoppingCartOne.setNumber(number + 1);
            shoppingCartMapper.updateById(shoppingCartOne);
        } else {
            shoppingCart.setNumber(1);
            shoppingCartMapper.insert(shoppingCart);
            shoppingCartOne = shoppingCart;
        }
        return Result.success(shoppingCartOne);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> sub(ShoppingCart shoppingCart) {
        log.info("dishId:{},setmealId:{}",shoppingCart.getDishId(),shoppingCart.getSetmealId());
        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        if(dishId != null) {
            queryWrapper.eq(ShoppingCart::getDishId,dishId);
        } else {
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }

        ShoppingCart shoppingCartOne = shoppingCartMapper.selectOne(queryWrapper);
        Integer number = shoppingCartOne.getNumber();
        if(number >= 1) {
            shoppingCartOne.setNumber(number - 1);
            shoppingCartMapper.updateById(shoppingCartOne);
        }

        if(number == 1) {
            shoppingCartMapper.delete(queryWrapper);
        }
        return Result.success("操作成功");
    }

    @Override
    public Result<String> cleanShoppingCart() {
        Long currentId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,currentId);
        int delete = shoppingCartMapper.delete(queryWrapper);
        if(delete <= 0) {
            return Result.error("操作失败");
        }
        return Result.success("操作成功");
    }

    @Override
    public List<ShoppingCart> getUserShoppingCart(Long id) {
        return shoppingCartMapper.getUserShoppingCart(id);
    }
}
