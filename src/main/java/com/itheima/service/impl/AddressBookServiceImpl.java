package com.itheima.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.common.BaseContext;
import com.itheima.common.Result;
import com.itheima.domain.AddressBook;
import com.itheima.exception.CustomException;
import com.itheima.mapper.AddressBookMapper;
import com.itheima.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SuppressWarnings("all")
@Service
@Slf4j
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
    @Autowired
    private AddressBookMapper addressBookMapper;
    @Override
    public AddressBook getDefaultAddressByUserId(Long id) {
        return addressBookMapper.getDefaultAddressByUserId(id);
    }

    @Override
    public Result<List<AddressBook>> getUserAddressBookById() {
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        List<AddressBook> addressBooks = addressBookMapper.selectList(queryWrapper);
        return Result.success(addressBooks);
    }

    @Override
    public Result<String> addAddress(AddressBook addressBook) {
        log.info("address:{}",addressBook.toString());
        addressBook.setUserId(BaseContext.getCurrentId());
        int insert = addressBookMapper.insert(addressBook);
        if(insert <= 0) {
            return Result.error("添加失败");
        }

        return Result.success("添加成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> modifyDefaultAddress(Long id) {
        int chancelDefaultAddress = addressBookMapper.chancelDefaultAddress(BaseContext.getCurrentId());
        if(chancelDefaultAddress <= 0) {
            throw new CustomException("修改失败");
        }
        int setDefaultAddress = addressBookMapper.setDefaultAddress(id);
        if(setDefaultAddress <= 0) {
            throw new CustomException("修改失败");
        }
        return Result.success("修改成功");
    }

    @Override
    public Result<AddressBook> getAddressById(Long id) {
        AddressBook addressBook = addressBookMapper.selectById(id);
        if(addressBook == null) {
            throw new CustomException("查询出错");
        }
        return Result.success(addressBook);
    }

    @Override
    public Result<String> updateAddress(AddressBook addressBook) {
        int i = addressBookMapper.updateById(addressBook);
        if(i <= 0) {
            return Result.error("修改失败");
        }
        return Result.success("修改成功");
    }

    @Override
    public Result<String> deleteAddress(Long ids) {
        int i = addressBookMapper.deleteById(ids);
        if(i <= 0) {
            return Result.error("删除失败");
        }
        return Result.success("删除成功");
    }

    @Override
    public Result<AddressBook> getDefaultAddress() {
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId,BaseContext.getCurrentId());
        queryWrapper.eq(AddressBook::getIsDefault,1);
        AddressBook addressBook = addressBookMapper.selectOne(queryWrapper);
        if(addressBook == null) {
            return Result.error("获取地址出错");
        }

        return Result.success(addressBook);
    }
}
