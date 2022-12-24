package com.itheima.common;

import lombok.Data;
import java.util.HashMap;
import java.util.Map;

@Data
public class Result<T> {

    private Integer code;

    private String msg;

    private T data;

    private Map map = new HashMap<>();

    public static <T> Result<T> success(T object) {
        Result<T> result = new Result<>();
        result.code = 1;
        result.data = object;
        return result;
    }

    public static <T> Result<T> error(String msg) {
        Result<T> result = new Result<>();
        result.code = 0;
        result.msg = msg;
        return result;
    }

    public Result<T> add(String key,Object value) {
        map.put(key,value);
        return this;
    }
}
