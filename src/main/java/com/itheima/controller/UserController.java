package com.itheima.controller;

import com.itheima.common.Result;
import com.itheima.domain.User;
import com.itheima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 短信发送
     * @param user
     * @param httpSession
     * @return
     */
    @PostMapping("/sendMsg")
    public Result<String> sendMessage(@RequestBody User user, HttpSession httpSession) {
        return userService.sendMessage(user,httpSession);
    }

    /**
     * 用户登录
     * @param map
     * @param httpSession
     * @return
     */
    @PostMapping("/login")
    public Result<User> login(@RequestBody Map map, HttpSession httpSession) {
        return userService.login(map,httpSession);
    }

    /**
     * 用户退出
     * @param httpSession
     * @return
     */
    @PostMapping("/loginout")
    public Result<String> logout(HttpSession httpSession) {
        return userService.logout(httpSession);
    }

}
