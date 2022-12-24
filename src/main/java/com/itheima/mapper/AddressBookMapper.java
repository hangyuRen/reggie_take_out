package com.itheima.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.domain.AddressBook;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {
    @Select("select * from address_book where user_id = #{id} and is_default = 1")
    AddressBook getDefaultAddressByUserId(Long id);
    int chancelDefaultAddress(@Param("id") Long id);
    int setDefaultAddress(@Param("id") Long id);
}
