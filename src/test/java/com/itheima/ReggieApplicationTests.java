package com.itheima;

import com.itheima.domain.Dish;
import com.itheima.mapper.DishMapper;
import com.itheima.service.DishService;
import com.itheima.service.impl.DishServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
class ReggieApplicationTests {

    @Autowired
    private DishMapper dishMapper;
    @Test
    void contextLoads() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
        System.out.println(list);
        list = list.stream().map(item -> {
            item++;
            return item;
        }).collect(Collectors.toList());

        System.out.println(list);
    }

    @Test
    void testLogicDeleted() {
        Dish dish = dishMapper.selectById(new BigDecimal("1413385247889891330"));
        System.out.println(dish);
    }
}
