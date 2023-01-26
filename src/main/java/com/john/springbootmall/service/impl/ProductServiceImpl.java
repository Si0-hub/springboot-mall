package com.john.springbootmall.service.impl;

import com.john.springbootmall.constant.impl.ProductCategory;
import com.john.springbootmall.dao.ProductDao;
import com.john.springbootmall.dto.ProductQueryParams;
import com.john.springbootmall.dto.ProductRequest;
import com.john.springbootmall.entity.Product;
import com.john.springbootmall.service.ProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        // 判斷前端sort方式
        Sort sort = null;
        if ("DESC".equals(productQueryParams.getSort())) {
            sort = Sort.by(Sort.Direction.DESC, productQueryParams.getOrderBy());
        } else {
            sort = Sort.by(Sort.Direction.ASC, productQueryParams.getOrderBy());
        }

        Pageable pageable = PageRequest.of(productQueryParams.getPage(), productQueryParams.getSize(), sort);
        Specification<Product> specification = buildCondition(productQueryParams);
        Page<Product>  productPage = productDao.findAll(specification, pageable);
        return productPage;
    }

    private Specification<Product> buildCondition(ProductQueryParams productQueryParams) {
        return (Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicateList = new ArrayList<>();

            if (StringUtils.isNotBlank(productQueryParams.getProuductName())) {
                // 本处我都转为小写，进行模糊匹配
                predicateList.add(cb.like(cb.lower(root.get("productName").as(String.class)), "%" + productQueryParams.getProuductName().toLowerCase() + "%"));
            }

            if (productQueryParams.getCategory() != null) {
                predicateList.add(cb.equal(root.get("category").as(ProductCategory.class), productQueryParams.getCategory()));
            }

            if (StringUtils.isNotBlank(productQueryParams.getPrice())) {
                String[] priceRang = productQueryParams.getPrice().split("-");
                predicateList.add(cb.between(root.get("price").as(Integer.class),Integer.parseInt(priceRang[0]), Integer.parseInt(priceRang[1])));
            }

            return cb.and(predicateList.toArray(new Predicate[0]));
        };
    }
}
