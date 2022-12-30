package com.john.springbootmall.rowmapper;

import com.john.springbootmall.model.OrderItem;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderItemRowMapper implements RowMapper<OrderItem> {

    @Override
    public OrderItem mapRow(ResultSet resultSet, int i) throws SQLException {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderItemId(resultSet.getInt("order_item_id"));
        orderItem.setOrderId(resultSet.getInt("order_id"));
        orderItem.setProductId(resultSet.getInt("product_id"));
        orderItem.setAmount(resultSet.getInt("amount"));
        orderItem.setQuantity(resultSet.getInt("quantity"));

        orderItem.setProductName(resultSet.getString("product_name"));
        orderItem.setImageUrl(resultSet.getString("image_url"));

        return orderItem;
    }
}
