package com.itheima.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.annotation.MyLog;
import com.itheima.common.Result;
import com.itheima.domain.User;
import com.itheima.mapper.UserMapper;
import com.itheima.service.UserService;
import com.itheima.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("all")
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${loginMail.from}")
    private String mailFrom;
    @Value("${loginMail.to}")
    private String mailTo;
    @Value("${loginMail.subject}")
    private String mailSubject;
    @Value("${loginMail.text}")
    private String text;
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    @MyLog
    public Result<String> sendMessage(User user, HttpSession httpSession) {
        String phone = user.getPhone();
        log.info("phone:{}",phone);
        if(StringUtils.isNotEmpty(phone)) {
            String code = ValidateCodeUtils.generateValidateCode(4).toString();

            //发送邮箱代码段
            log.info("code:{}",code);
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(mailFrom);
            simpleMailMessage.setTo(mailTo);
            simpleMailMessage.setSubject(mailSubject);
            simpleMailMessage.setText(text + code);
            javaMailSender.send(simpleMailMessage);

            redisTemplate.opsForValue().set(phone,code,1, TimeUnit.MINUTES);

            return Result.success("发送成功");
        }

        return Result.error("发送失败");
    }

    @Override
    @MyLog
    public Result<User> login(Map map, HttpSession httpSession) {
        String phone = (String) map.get("phone");
        String code = (String) map.get("code");

        Object codeInSession = redisTemplate.opsForValue().get(phone);
        if(codeInSession != null && codeInSession.equals(code)) {
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,phone);
            User user = this.getOne(queryWrapper);
            if(user == null) {
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                this.save(user);
            }
            httpSession.setAttribute("user",user.getId());
            redisTemplate.delete(phone);
            return Result.success(user);
        }
        return Result.error("短信发送失败");
    }

    @Override
    @MyLog
    public Result<String> logout(HttpSession httpSession) {
        httpSession.removeAttribute("user");
        return Result.success("退出成功");
    }
}
