package com.john.springbootmall.dao;

import com.john.springbootmall.constant.ProductCategory;
import com.john.springbootmall.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductDao extends JpaRepository<Product, Integer> {

    Product findByProductId(Integer productId);

    Page<Product> findAllByCategory(ProductCategory category, Pageable pageable);
}
