package com.john.springbootmall.dao;

import com.john.springbootmall.entity.Order;
import com.john.springbootmall.entity.OrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface OrderDao extends JpaRepository<Order, Integer> {

    Page<Order> findAllByUserId(Integer userId, Pageable pageable);
}
