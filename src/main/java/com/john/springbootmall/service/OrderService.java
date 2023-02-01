package com.john.springbootmall.service;

import com.john.springbootmall.dto.CreateOrderRequest;
import com.john.springbootmall.dto.OrderQueryParams;
import com.john.springbootmall.entity.Order;
import org.springframework.data.domain.Page;

import javax.security.auth.message.AuthException;
import java.util.List;
import java.util.Map;

public interface OrderService {

    Map<String, Object> getOrders(OrderQueryParams orderQueryParams, String au) throws AuthException;

    Order createOrder(String au, CreateOrderRequest createOrderRequest) throws AuthException;
}
