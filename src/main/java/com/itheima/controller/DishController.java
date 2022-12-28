package com.itheima.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.common.Result;
import com.itheima.domain.Dish;
import com.itheima.dto.DishDto;
import com.itheima.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;

    /**
     *新增菜品
     * @param dishDto
     * @return
     */
    @PostMapping
    public Result<String> save(@RequestBody DishDto dishDto) {
        return dishService.saveWithFlavor(dishDto);
    }

    /**
     *分页查询 菜品类别只有categoryId没有categoryName 需要连表查询
     * @param page
     * @param pageSize
     * @return
     */
    //@GetMapping("/page")
    public Result<Page<Dish>> selectByPage(int page,int pageSize,String name) {
        return dishService.selectByPage(page,pageSize,name);
    }

    /**
     * 分页查询 利用dishDto对象设置categoryName返回
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public Result<Page<DishDto>> select(@RequestParam("page") int page,@RequestParam("pageSize") int pageSize,String name) {
        return dishService.select(page,pageSize,name);
    }

    /**
     * 修改时 菜品信息回写
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<DishDto> get(@PathVariable("id") Long id) {
        return dishService.getByIdWithFlavor(id);
    }

    /**
     * 修改菜品信息
     * @param dishDto
     * @return
     */
    @PutMapping
    public Result<String> update(@RequestBody DishDto dishDto) {
        return dishService.updateWithFlavor(dishDto);
    }

    /**
     * 修改售卖状态
     * @param ids
     * @return
     */
    //@PostMapping("/status/{status}")
    public Result<String> changeStatus(@PathVariable int status,@RequestParam Long ids) {
        return dishService.changeStatus(status,ids);
    }

    /**
     * 批量修改状态 单个同样适用
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public Result<String> changeStatusForBatch(@PathVariable("status") int status, @RequestParam List<Long> ids) {
        return dishService.changeStatusForBatch(status,ids);
    }

    /**
     * 批量删除 单个同样适用
     * @param ids
     * @return
     */
    @DeleteMapping
    public Result<String> deleteBatches(@RequestParam List<Long> ids) {
        return dishService.deleteBatches(ids);
    }

    /**
     * 套餐中展示菜品选择
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    public Result<List<DishDto>> listDish(@RequestParam Long categoryId) {
        return dishService.listDish(categoryId);
    }
}
