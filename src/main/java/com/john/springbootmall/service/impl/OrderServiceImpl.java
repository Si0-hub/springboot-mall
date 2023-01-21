package com.john.springbootmall.service.impl;

import com.john.springbootmall.dao.OrderDao;
import com.john.springbootmall.dao.OrderItemDao;
import com.john.springbootmall.dao.ProductDao;
import com.john.springbootmall.dao.UserDao;
import com.john.springbootmall.dto.BuyItem;
import com.john.springbootmall.dto.CreateOrderRequest;
import com.john.springbootmall.dto.OrderQueryParams;
import com.john.springbootmall.entity.Order;
import com.john.springbootmall.entity.OrderItem;
import com.john.springbootmall.entity.Product;
import com.john.springbootmall.entity.User;
import com.john.springbootmall.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.Resource;
import java.util.*;

@Component
public class OrderServiceImpl implements OrderService {
    private OrderDao orderDao;
    private OrderItemDao orderItemDao;
    private UserDao userDao;
    private ProductDao productDao;
    @Resource(name = "orderDao")
    private void OrderDao(OrderDao orderDao) {
        this.orderDao = orderDao;
    }
    @Resource(name = "orderItemDao")
    private void OrderDao(OrderItemDao orderItemDao) {
        this.orderItemDao = orderItemDao;
    }
    @Resource(name = "userDao")
    private void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
    @Resource(name = "productDao")
    private void setProductDao(ProductDao productDao) {
        this.productDao = productDao;
    }

    private final static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public Map<String, Object> getOrders(OrderQueryParams orderQueryParams) {
        Pageable pageable = PageRequest.of(orderQueryParams.getPage(), orderQueryParams.getSize());
        Page<Order> orderPage = orderDao.findAllByUserId(orderQueryParams.getUserId(), pageable);

        // 查出每筆明細
        List<Order> orderList = orderPage.getContent();
        for (Order order : orderList) {
            Integer orderId = order.getOrderId();
            List<OrderItem> orderItemList = orderItemDao.findByOrderId(orderId);
            order.setOrderItemList(orderItemList);
        }

        Map<String, Object> rtn = new HashMap<>();
        rtn.put("dataList", orderList);
        rtn.put("totalPages", orderPage.getTotalPages());
        rtn.put("totalData", orderPage.getTotalElements());
        rtn.put("nowPage", orderPage.getNumber());

        return rtn;
    }

    @Transactional
    @Override
    public Order createOrder(Integer userId, CreateOrderRequest createOrderRequest) {
        // 檢查user是否存在
        Boolean userExist = userDao.existsById(userId);
        if (!userExist) {
            log.warn("該userId {} 不存在!!", userId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        int totalAmount = 0;
        List<OrderItem> orderItemList = new ArrayList<>();
        // 算總金額，以及檢核
        for (BuyItem buyItem : createOrderRequest.getBuyItemList()) {
            Product product = productDao.findByProductId(buyItem.getProductId());

            if (product == null) {
                log.warn("商品 {} 不存在!!", buyItem.getProductId());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            } else if (product.getStock() < buyItem.getQuantity()) {
                log.warn("商品 {} 庫存數量不足，無法購買。剩餘庫存 {}，欲購買數量 {}",
                        buyItem.getProductId(), product.getStock(), buyItem.getQuantity());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }

            product.setStock(product.getStock() - buyItem.getQuantity());
            product.setLastModifiedDate(new Date());
            // 扣除商品庫存
            productDao.save(product);

            int amount = product.getPrice() * buyItem.getQuantity();
            totalAmount = totalAmount + amount;

            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(buyItem.getProductId());
            orderItem.setQuantity(buyItem.getQuantity());
            orderItem.setAmount(amount);

            orderItemList.add(orderItem);
        }


        // 創建訂單
        Order creatOrder = new Order();
        creatOrder.setUserId(userId);
        creatOrder.setTotalAmount(totalAmount);
        creatOrder.setCreatedDate(new Date());
        creatOrder.setLastModifiedDate(new Date());

        Order order = orderDao.save(creatOrder);

        // 創建訂單明細
        orderItemList.forEach(orderItem -> orderItem.setOrderId(order.getOrderId()));
        orderItemDao.saveAll(orderItemList);

        order.setOrderItemList(orderItemList);
        return order;
    }
}
