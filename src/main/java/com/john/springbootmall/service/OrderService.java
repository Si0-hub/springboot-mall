package com.john.springbootmall.service;

import com.john.springbootmall.dto.CreateOrderRequest;
import com.john.springbootmall.dto.OrderQueryParams;
import com.john.springbootmall.entity.Order;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface OrderService {

    Map<String, Object> getOrders(OrderQueryParams orderQueryParams);

    Order createOrder(Integer userId, CreateOrderRequest createOrderRequest);
}
