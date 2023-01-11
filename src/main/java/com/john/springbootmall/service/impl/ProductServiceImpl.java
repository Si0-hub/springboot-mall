package com.john.springbootmall.service.impl;

import com.john.springbootmall.dao.ProductDao;
import com.john.springbootmall.dto.ProductRequest;
import com.john.springbootmall.entity.Product;
import com.john.springbootmall.service.ProductService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class ProductServiceImpl implements ProductService {


    private ProductDao productDao;

    @Resource(name = "productDao")
    private void setProductDao(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Override
    public Product getProductById(Integer productId) {
        return productDao.findByProductId(productId);
    }

    @Override
    public Product createProduct(ProductRequest productRequest) {
        Product product = new Product(productRequest);

        Date today = new Date();
        product.setCreatedDate(today);
        product.setLastModifiedDate(today);

        return productDao.save(product);
    }

    @Override
    public void updateProduct(Integer productId, ProductRequest productRequest) {
        Product product = new Product(productRequest);
        product.setProductId(productId);

        Date today = new Date();
        product.setLastModifiedDate(today);

        productDao.save(product);
    }

    @Override
    public void deleteProductById(Integer productId) {
        productDao.deleteById(productId);
    }

//    @Override
//    public Integer countProduct(ProductQueryParams productQueryParams) {
//        return productDao.countProduct(productQueryParams);
//    }
//
//    @Override
//    public List<Product> getProducts(ProductQueryParams productQueryParams) {
//        return productDao.getProducts(productQueryParams);
//    }
}
