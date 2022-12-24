package com.itheima.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.common.Result;
import com.itheima.domain.Category;

import java.util.List;

public interface CategoryService extends IService<Category> {
    Result<String> add(Category category);
    Result<Page<Category>> selectByPage(int page,int pageSize);
    Result<String> deleteById(Long id);
    Result<String> updateCategory(Category category);
    Result<List<Category>> myList(Category category);
}
