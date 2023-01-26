package com.john.springbootmall.dto;

import com.john.springbootmall.constant.impl.ProductCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductQueryParams {

    private ProductCategory category;
    private String prouductName;
    private String price;
    private String orderBy;
    private String sort;
    private Integer page;
    private Integer size;
}
