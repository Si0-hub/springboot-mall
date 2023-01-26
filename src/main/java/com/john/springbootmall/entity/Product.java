package com.john.springbootmall.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.john.springbootmall.constant.impl.ProductCategory;
import com.john.springbootmall.dto.ProductRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
public class Product {

    public Product(ProductRequest productRequest) {
        this.setProductName(productRequest.getProductName());
        this.setCategory(productRequest.getCategory());
        this.setImageUrl(productRequest.getImageUrl());
        this.setPrice(productRequest.getPrice());
        this.setStock(productRequest.getStock());
        this.setDescription(productRequest.getDescription());
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Integer productId;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private ProductCategory category;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "price")
    private Integer price;

    @Column(name = "stock")
    private Integer stock;

    @Column(name = "description")
    private String description;

    @Column(name = "created_date", updatable = false)
    private Date createdDate;

    @Column(name = "last_modified_date")
    private Date lastModifiedDate;
}
