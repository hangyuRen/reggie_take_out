package com.itheima.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.annotation.MyLog;
import com.itheima.common.Result;
import com.itheima.domain.Employee;
import com.itheima.mapper.EmployeeMapper;
import com.itheima.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public Result<Employee> login(HttpServletRequest request, Employee employee) {

        String password = DigestUtils.md5DigestAsHex(employee.getPassword().getBytes());
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeMapper.selectOne(queryWrapper);

        if(emp == null) {
            return Result.error("用户不存在");
        }

        if(!emp.getPassword().equals(password)) {
            return Result.error("密码错误");
        }

        if(emp.getStatus() == 0) {
            return Result.error("用户已禁用");
        }

        request.getSession().setAttribute("employee",emp.getId());
        return Result.success(emp);
    }

    @Override
    public Result<String> logout(HttpServletRequest request) {
        request.getSession().removeAttribute("employee");
        return Result.success("退出成功");
    }

    @Override
    @MyLog
    public Result<String> add(HttpServletRequest request, Employee employee) {

        //密码MD5加密 默认密码为123456
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

        /*employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());

        Long empId = (Long) request.getSession().getAttribute("employee");
        employee.setCreateUser(empId);
        employee.setUpdateUser(empId);*/

        log.info("新增员工:{}",employee);

        int insert = employeeMapper.insert(employee);

        if(insert > 0) {
            return Result.success("添加成功");
        }
        return Result.error("添加失败");
    }

    @Override
    @Cacheable(value = "employeeCache",key = "T(String).valueOf(#page).concat('-').concat(#pageSize)")
    public Result<Page<Employee>> mybatisPlusSelectByPage(int page, int pageSize, String name) {

        log.info("page = {},pageSize = {},name = {}",page,pageSize,name);

        Page<Employee> pages = new Page<>((long) (page - 1) * pageSize,pageSize);
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        employeeMapper.selectPage(pages,queryWrapper);
        return Result.success(pages);
    }

    @Override
    @Cacheable(value = "employeeCache",key = "T(String).valueOf(#page).concat('-').concat(#pageSize)")
    public Result<Page<Employee>> selectByPage(int page, int pageSize, String name) {

        log.info("page = {},pageSize = {},name = {}",page,pageSize,name);

        if(name != null && name.length() != 0) {
            name += "%";
        }
        Page<Employee> iPage = new Page<>();
        iPage.setTotal(employeeMapper.getTotal(name));
        List<Employee> employees = employeeMapper.selectByPage((page - 1) * pageSize, pageSize, name);
        iPage.setRecords(employees);
        return Result.success(iPage);
    }

    @Override
    @CacheEvict(value = "emloyeeCache",key = "#employee.id",condition = "#employee != null")
    public Result<String> update(HttpServletRequest request, Employee employee) {
        /*employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser((Long) request.getSession().getAttribute("employee"));*/
        employeeMapper.updateById(employee);
        return Result.success("员工信息修改成功");
    }

    @Override
    @Cacheable(value = "employeeCache",key = "#id",condition = "#id != null")
    public Result<Employee> selectById(Long id) {
        Employee employee = employeeMapper.getById(id);
        return Result.success(employee);
    }
}
