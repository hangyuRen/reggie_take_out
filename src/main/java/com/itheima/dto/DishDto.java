package com.itheima.dto;

import com.itheima.domain.Dish;
import com.itheima.domain.DishFlavor;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
