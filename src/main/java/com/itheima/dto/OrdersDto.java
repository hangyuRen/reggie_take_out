package com.itheima.dto;

import com.itheima.domain.OrderDetail;
import com.itheima.domain.Orders;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class OrdersDto extends Orders implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userName;

    private String phone;

    private String address;

    private String consignee;

    private List<OrderDetail> orderDetails;
	
}
