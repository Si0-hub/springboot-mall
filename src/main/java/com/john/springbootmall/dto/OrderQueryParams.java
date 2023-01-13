package com.john.springbootmall.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderQueryParams {

    private Integer userId;
    private Integer page;
    private Integer size;
}
