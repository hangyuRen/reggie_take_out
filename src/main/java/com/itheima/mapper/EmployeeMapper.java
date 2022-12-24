package com.itheima.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.domain.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
    List<Employee>  selectByPage(@Param("page") int page, @Param("pageSize") int pageSize,@Param("name") String name);
    int getTotal(@Param("name") String name);
    Employee getById(@Param("id") Long id);
}
