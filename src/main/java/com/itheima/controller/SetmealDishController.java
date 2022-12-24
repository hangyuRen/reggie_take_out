package com.itheima.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.common.Result;
import com.itheima.domain.Setmeal;
import com.itheima.dto.SetmealDto;
import com.itheima.service.SetmealDishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@SuppressWarnings("all")
@RestController
@RequestMapping("/setmeal")
public class SetmealDishController {
    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * 新增套餐
     * @param setmealDto
     * @return
     */
    @PostMapping
    public Result<String> savaSetmeal(@RequestBody SetmealDto setmealDto) {
        return setmealDishService.saveSetmeal(setmealDto);
    }

    /**
     * 套餐分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public Result<Page<SetmealDto>> selectByPage(@RequestParam("page") int page,@RequestParam("pageSize") int pageSize,String name) {
        return setmealDishService.selectByPage(page,pageSize,name);
    }

    /**
     * 修改套餐 信息回写
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<SetmealDto> getByIdWithDishes(@PathVariable Long id) {
        return setmealDishService.getByIdWithDishes(id);
    }

    /**
     * 单个/批量 启售/停售
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public Result<String> modifyStatusForSingleOrBatch(@PathVariable("status") int status, @RequestParam List<Long> ids) {
        return setmealDishService.modifyStatusForSingleOrBatch(status,ids);
    }

    /**
     * update
     * @param setmealDto
     * @return
     */
    @PutMapping
    public Result<String> update(@RequestBody SetmealDto setmealDto) {
        return setmealDishService.updateSetmeal(setmealDto);
    }

    /**
     * 单个/批量删除
     * @param ids
     * @return
     */
    @DeleteMapping
    public Result<String> deleteSingleOrBatch(@RequestParam List<Long> ids) {
        return setmealDishService.deleteSingleOrBatch(ids);
    }

    /**
     * 购物车中套餐展示
     * @param categoryId
     * @param status
     * @return
     */
    @GetMapping("/list")
    public Result<List<Setmeal>> setmealList(@RequestParam("categoryId") Long categoryId,@RequestParam("status") Integer status) {
        return setmealDishService.setmealList(categoryId,status);
    }
}
