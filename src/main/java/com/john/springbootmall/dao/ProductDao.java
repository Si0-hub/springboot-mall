package com.john.springbootmall.dao;

import com.john.springbootmall.entity.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductDao extends CrudRepository<Product, Integer > {

    Product findByProductId(Integer productId);
}
