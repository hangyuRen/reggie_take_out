package com.itheima.common;

import com.itheima.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice(annotations = {RestController.class, Controller.class})  //添加了这些注解的类会被处理
@ResponseBody
@Slf4j
public class GlobalException {
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public Result<String> exceptionHandler(SQLIntegrityConstraintViolationException ex) {
        log.error(ex.getMessage());
        if(ex.getMessage().contains("Duplicate entry")) {
            String[] split = ex.getMessage().split(" ");
            return Result.error("账号" + split[2] + "已存在");
        }
        return Result.error("未知错误");
    }

    @ExceptionHandler(CustomException.class)
    public Result<String> customExceptionHandler(CustomException ex) {
        log.error(ex.getMessage());
        return Result.error(ex.getMessage());
    }
}
