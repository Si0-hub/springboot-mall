package com.john.springbootmall.service;

import com.john.springbootmall.dto.ProductRequest;
import com.john.springbootmall.entity.Product;

public interface ProductService {

    Product getProductById(Integer productId);

    Product createProduct(ProductRequest productRequest);

    void updateProduct(Integer productId, ProductRequest productRequest);

    void deleteProductById(Integer productId);

//    Integer countProduct(ProductQueryParams productQueryParams);
//
//    List<Product> getProducts(ProductQueryParams productQueryParams);
}
