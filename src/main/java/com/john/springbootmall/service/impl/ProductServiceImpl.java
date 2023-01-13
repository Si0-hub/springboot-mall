package com.john.springbootmall.service.impl;

import com.john.springbootmall.constant.ProductCategory;
import com.john.springbootmall.dao.ProductDao;
import com.john.springbootmall.dto.ProductQueryParams;
import com.john.springbootmall.dto.ProductRequest;
import com.john.springbootmall.entity.Product;
import com.john.springbootmall.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        Product product = productDao.findByProductId(productId);
        if (product != null) {
            productDao.deleteById(productId);
        }

    }

    @Override
    public Page<Product> getProducts(ProductQueryParams productQueryParams) {
        ProductCategory productCategory = productQueryParams.getCategory();

        // 判斷前端sort方式
        Sort sort = null;
        if ("DESC".equals(productQueryParams.getSort())) {
            sort = Sort.by(Sort.Direction.DESC, productQueryParams.getOrderBy());
        } else {
            sort = Sort.by(Sort.Direction.ASC, productQueryParams.getOrderBy());
        }

        Pageable pageable = PageRequest.of(productQueryParams.getPage(), productQueryParams.getSize(), sort);

        Page<Product>  productPage;
        if (productCategory == null) {
            productPage = productDao.findAll(pageable);
        } else {
            productPage = productDao.findAllByCategory(productCategory, pageable);
        }

        return productPage;
    }
}
