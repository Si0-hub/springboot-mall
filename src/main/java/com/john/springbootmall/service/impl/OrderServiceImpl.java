package com.john.springbootmall.service.impl;

import com.john.springbootmall.dao.OrderDao;
import com.john.springbootmall.dao.ProductDao;
import com.john.springbootmall.dao.UserDao;
import com.john.springbootmall.dto.BuyItem;
import com.john.springbootmall.dto.CreateOrderRequest;
import com.john.springbootmall.model.Order;
import com.john.springbootmall.model.OrderItem;
import com.john.springbootmall.model.Product;
import com.john.springbootmall.model.User;
import com.john.springbootmall.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private ProductDao productDao;

    private final static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Transactional
    @Override
    public Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest) {
        // 檢查user是否存在
        User user = userDao.getUserById(userId);

        if (user == null) {
            log.warn("該userId {} 不存在!!", userId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        int totalAmount = 0;
        List<OrderItem> orderItemList = new ArrayList<>();


        for (BuyItem buyItem : createOrderRequest.getBuyItemList()) {
            Product product = productDao.getProdictById(buyItem.getProductId());

            if (product == null) {
                log.warn("商品 {} 不存在!!", buyItem.getProductId());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            } else if (product.getStock() < buyItem.getQuantity()) {
                log.warn("商品 {} 庫存數量不足，無法購買。剩餘庫存 {}，欲購買數量 {}",
                        buyItem.getProductId(), product.getStock(), buyItem.getQuantity());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }

            // 扣除商品庫存
            productDao.updateStock(product.getProductId(), product.getStock() - buyItem.getQuantity());

            int amount = buyItem.getQuantity() * product.getPrice();
            totalAmount = totalAmount + amount;

            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(buyItem.getProductId());
            orderItem.setQuantity(buyItem.getQuantity());
            orderItem.setAmount(amount);

            orderItemList.add(orderItem);
        }


        // 創建訂單
        Integer orderId = orderDao.createOder(userId, totalAmount);

        // 創建訂單明細
        orderDao.createOrderItem(orderId, orderItemList);

        return orderId;
    }

    @Override
    public Order getOrderById(Integer orderId) {
        Order order = orderDao.getOrderById(orderId);

        List<OrderItem> orderItemList = orderDao.getOrderItemsByOrderId(order.getOrderId());

        order.setOrderItemList(orderItemList);

        return order;
    }
}
