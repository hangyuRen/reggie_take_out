package com.itheima.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.common.Result;
import com.itheima.domain.ShoppingCart;

import java.util.List;

public interface ShoppingCartService extends IService<ShoppingCart> {
    Result<List<ShoppingCart>> getShoppingCartList();
    Result<ShoppingCart> add(ShoppingCart shoppingCart);
    Result<String> sub(ShoppingCart shoppingCart);
    Result<String> cleanShoppingCart();
    List<ShoppingCart> getUserShoppingCart(Long id);
}
