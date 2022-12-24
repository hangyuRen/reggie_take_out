package com.itheima.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.common.Result;
import com.itheima.domain.AddressBook;

import java.util.List;

public interface AddressBookService extends IService<AddressBook> {
    AddressBook getDefaultAddressByUserId(Long id);
    Result<List<AddressBook>> getUserAddressBookById();
    Result<String> addAddress(AddressBook addressBook);
    Result<String> modifyDefaultAddress(Long id);
    Result<AddressBook> getAddressById(Long id);
    Result<String> updateAddress(AddressBook addressBook);
    Result<String> deleteAddress(Long ids);
    Result<AddressBook> getDefaultAddress();
}
