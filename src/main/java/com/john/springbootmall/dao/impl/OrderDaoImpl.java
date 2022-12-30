package com.john.springbootmall.dao.impl;

import com.john.springbootmall.dao.OrderDao;
import com.john.springbootmall.model.Order;
import com.john.springbootmall.model.OrderItem;
import com.john.springbootmall.rowmapper.OrderItemRowMapper;
import com.john.springbootmall.rowmapper.OrderRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OrderDaoImpl implements OrderDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Order getOrderById(Integer orderId) {

        String sql = "SELECT order_id, user_id, total_amount, created_date, last_modified_date FROM `order` WHERE order_id = :orderId";

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("orderId", orderId);

        List<Order> orderList = namedParameterJdbcTemplate.query(sql, paramMap, new OrderRowMapper());

        if (orderList.size() > 0) {
            return orderList.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<OrderItem> getOrderItemsByOrderId(Integer orderId) {
        String sql = "SELECT  A.order_item_id, A.order_id, A.product_id, A.quantity, A.amount, B.product_name, B.image_url "
                   + "FROM order_item A "
                   + "LEFT JOIN product B on A.product_id = B.product_id "
                   + "WHERE A.order_id = :orderId";

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("orderId", orderId);

        List<OrderItem> orderItemList = namedParameterJdbcTemplate.query(sql, paramMap, new OrderItemRowMapper());


        return orderItemList;
    }

    @Override
    public Integer createOder(Integer userId, int totalAmount) {

        String sql = "INSERT INTO `order` (user_id, total_amount, created_date, last_modified_date) VALUES (:userId, :totalAmount, :createdDate, :last_modified_date)";

        Map<String, Object> insertMap = new HashMap<>();
        insertMap.put("userId", userId);
        insertMap.put("totalAmount", totalAmount);

        Date now = new Date();
        insertMap.put("createdDate", now);
        insertMap.put("last_modified_date", now);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(insertMap), keyHolder);

        int orderId = keyHolder.getKey().intValue();

        return orderId;
    }

    @Override
    public void createOrderItem(Integer orderId, List<OrderItem> orderItemList) {

        String sql = "INSERT INTO order_item (order_id, product_id, quantity, amount) VALUES (:orderId, :productId, :quantity, :amount)";

        // batchUpdate做法 一次寫入多筆資料到資料庫
        MapSqlParameterSource[] parameterSources = new MapSqlParameterSource[orderItemList.size()];

        for (int i = 0; i < orderItemList.size(); i++) {
            parameterSources[i] = new MapSqlParameterSource();
            parameterSources[i].addValue("orderId", orderId);
            parameterSources[i].addValue("productId", orderItemList.get(i).getProductId());
            parameterSources[i].addValue("quantity", orderItemList.get(i).getQuantity());
            parameterSources[i].addValue("amount", orderItemList.get(i).getAmount());
        }

        namedParameterJdbcTemplate.batchUpdate(sql, parameterSources);
    }
}
