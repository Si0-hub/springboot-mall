package com.john.springbootmall.dao;

import com.john.springbootmall.dto.OrderQueryParams;
import com.john.springbootmall.model.Order;
import com.john.springbootmall.model.OrderItem;

import java.util.List;

public interface OrderDao {

    Integer countOrder(OrderQueryParams orderQueryParams);

    List<Order> getOrders(OrderQueryParams orderQueryParams);

    Order getOrderById(Integer orderId);

    List<OrderItem> getOrderItemsByOrderId(Integer orderId);

    Integer createOder(Integer userId, int totalAmount);

    void  createOrderItem(Integer orderId, List<OrderItem> orderItemList);
}
