package com.itheima.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName(value = "category")
public class Category implements Serializable {

    @TableField(exist = false)
    private static final Long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Integer type;

    private String name;

    private Integer sort;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

}
