package com.john.springbootmall.service;

import com.john.springbootmall.dto.ProductQueryParams;
import com.john.springbootmall.dto.ProductRequest;
import com.john.springbootmall.entity.Product;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {

    Product getProductById(Integer productId);

    Product createProduct(ProductRequest productRequest);

    void updateProduct(Integer productId, ProductRequest productRequest);

    void deleteProductById(Integer productId);

    Page<Product> getProducts(ProductQueryParams productQueryParams);
}
