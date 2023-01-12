package com.john.springbootmall.dto;

import com.john.springbootmall.constant.ProductCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestParam;

@Getter
@Setter
@NoArgsConstructor
public class ProductQueryParams {

    private ProductCategory category;
    private String search;
    private String orderBy;
    private String sort;
    private Integer page;
    private Integer size;
}
