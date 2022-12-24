package com.itheima.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.common.Result;
import com.itheima.domain.Category;
import com.itheima.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 添加菜品
     * @param category
     * @return
     */
    @PostMapping
    public Result<String> add(@RequestBody Category category) {
        return categoryService.add(category);
    }

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public Result<Page<Category>> selectByPage(int page, int pageSize) {
        return categoryService.selectByPage(page,pageSize);
    }

    /**
     * 根据id删除分类
     * @param ids
     * @return
     */
    @DeleteMapping
    public Result<String> deleteById(Long ids) {
        return categoryService.deleteById(ids);
    }

    /**
     * 修改分类信息
     * @param category
     * @return
     */
    @PutMapping
    public Result<String> updateCategory(@RequestBody Category category) {
        return categoryService.updateCategory(category);
    }

    /**
     * 根据条件查询分类数据
     * @param category
     * @return
     */
    @GetMapping("/list")
    public Result<List<Category>> list(Category category) {
        return categoryService.myList(category);
    }
}
