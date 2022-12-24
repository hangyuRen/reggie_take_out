package com.itheima.controller;

import com.itheima.common.Result;
import com.itheima.domain.ShoppingCart;
import com.itheima.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SuppressWarnings("all")
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 购物车展示
     * @return
     */
    @GetMapping("/list")
    public Result<List<ShoppingCart>> list() {
        return shoppingCartService.getShoppingCartList();
    }

    /**
     * 添加菜品到购物车
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public Result<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart) {
        return shoppingCartService.add(shoppingCart);
    }

    /**
     * 减少购物车中指定菜品的数量
     * @param shoppingCart
     * @return
     */
    @PostMapping("/sub")
    public Result<String> sub(@RequestBody ShoppingCart shoppingCart) {
        return shoppingCartService.sub(shoppingCart);
    }

    /**
     * 清空购物车
     * @return
     */
    @DeleteMapping("/clean")
    public Result<String> cleanShoppingCart() {
        return shoppingCartService.cleanShoppingCart();
    }
}
