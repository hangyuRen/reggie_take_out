package com.itheima.controller;

import com.itheima.common.Result;
import com.itheima.domain.AddressBook;
import com.itheima.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@SuppressWarnings("all")
@RestController
@RequestMapping("/addressBook")
public class AddressBookController {
    @Autowired
    AddressBookService addressBookService;

    /**
     * 获取当前用户的地址信息
     * @return
     */
    @GetMapping("/list")
    public Result<List<AddressBook>> getUserAddressBookById() {
        return addressBookService.getUserAddressBookById();
    }

    /**
     * 添加地址
     * @param addressBook
     * @return
     */
    @PostMapping
    public Result<String> save(@RequestBody AddressBook addressBook) {
        return addressBookService.addAddress(addressBook);
    }

    /**
     * 更改默认地址
     * @param addressBook
     * @return
     */
    @PutMapping("/default")
    public Result<String> modifyDefaultAddress(@RequestBody AddressBook addressBook) {
        return addressBookService.modifyDefaultAddress(addressBook.getId());
    }

    /**
     * 修改时信息回写
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<AddressBook> getAddressById(@PathVariable("id") Long id) {
        return addressBookService.getAddressById(id);
    }

    /**
     * 修改地址信息
     * @param addressBook
     * @return
     */
    @PutMapping
    public Result<String> update(@RequestBody AddressBook addressBook) {
        return addressBookService.updateAddress(addressBook);
    }

    /**
     * 删除地址
     * @param ids
     * @return
     */
    @DeleteMapping
    public Result<String> deleteAddress(@RequestParam Long ids) {
        return addressBookService.deleteAddress(ids);
    }

    /**
     * 获取用户默认地址
     * @return
     */
    @GetMapping("/default")
    public Result<AddressBook> getDefaultAddress() {
        return addressBookService.getDefaultAddress();
    }
}
