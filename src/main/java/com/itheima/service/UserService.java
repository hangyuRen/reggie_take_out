package com.itheima.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.common.Result;
import com.itheima.domain.User;

import javax.servlet.http.HttpSession;
import java.util.Map;

public interface UserService extends IService<User> {
    Result<String> sendMessage(User user, HttpSession httpSession);
    Result<User> login(Map map, HttpSession httpSession);
    Result<String> logout(HttpSession httpSession);
}
