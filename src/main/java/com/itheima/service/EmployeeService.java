package com.itheima.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.common.Result;
import com.itheima.domain.Employee;
import javax.servlet.http.HttpServletRequest;

public interface EmployeeService extends IService<Employee> {
    Result<Employee> login(HttpServletRequest request, Employee employee);
    Result<String> logout(HttpServletRequest request);
    Result<String> add(HttpServletRequest request, Employee employee);
    Result<Page<Employee>> mybatisPlusSelectByPage(int page, int pageSize, String name);
    Result<Page<Employee>> selectByPage(int page,int pageSize,String name);
    Result<String> update(HttpServletRequest request, Employee employee);
    Result<Employee> selectById(Long id);
}
