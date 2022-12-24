package com.itheima.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.common.Result;
import com.itheima.domain.Employee;
import com.itheima.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public Result<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        return employeeService.login(request,employee);
    }

    /**
     * 员工退出
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public Result<String> logout(HttpServletRequest request) {
        return employeeService.logout(request);
    }

    /**
     * 添加员工
     * @param employee
     * @return
     */
    @PostMapping
    public Result<String> add(HttpServletRequest request, @RequestBody Employee employee) {
        return employeeService.add(request,employee);
    }

    /**
     * MP分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    //@GetMapping("/page")
    public Result<Page<Employee>> mybatisPlusSelectByPage(int page, int pageSize, String name) {
        return employeeService.mybatisPlusSelectByPage(page,pageSize,name);
    }

    /**
     * 自定义分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public Result<Page<Employee>> selectByPage(int page,int pageSize,String name) {
        return employeeService.selectByPage(page,pageSize,name);
    }

    /**
     * 更新
     * @param request
     * @param employee
     * @return
     */
    @PutMapping
    public Result<String> update(HttpServletRequest request, @RequestBody Employee employee) {
        return employeeService.update(request,employee);
    }

    /**
     * 单个查询
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<Employee> selectById(@PathVariable("id") Long id) {
        return employeeService.selectById(id);
    }
}
