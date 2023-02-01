package com.john.springbootmall.controller;

import com.john.springbootmall.dto.CreateOrderRequest;
import com.john.springbootmall.dto.OrderQueryParams;
import com.john.springbootmall.entity.Order;
import com.john.springbootmall.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.security.auth.message.AuthException;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "購物清單相關api")
@Validated
@RestController
@RequestMapping("/order")
public class OrderController {

    private OrderService orderService;

    @Resource(name = "orderServiceImpl")
    private void setProductService(OrderService orderService) {
        this.orderService = orderService;
    }

    @ApiOperation("取得該使用者的購買清單")
    @GetMapping("/users/getorders")
    public  ResponseEntity<Map<String, Object>> getOrders(
            @RequestHeader("Authorization") String au,
            @RequestParam(defaultValue = "0") @Max(1000) @Min(0) Integer page,
            @RequestParam(defaultValue = "5") @Max(100) @Min(0) Integer size) throws AuthException {

        OrderQueryParams orderQueryParams = new OrderQueryParams();
        orderQueryParams.setPage(page);
        orderQueryParams.setSize(size);

        // 取得 order List
        Map<String, Object> response = orderService.getOrders(orderQueryParams, au);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @ApiOperation("創建購買清單")
    @PostMapping("/users/createorders")
    public ResponseEntity<?> createOrder(@RequestHeader("Authorization") String au,
                                         @RequestBody @Valid  CreateOrderRequest createOrderRequest) throws AuthException {
        Order order = orderService.createOrder(au, createOrderRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

}
