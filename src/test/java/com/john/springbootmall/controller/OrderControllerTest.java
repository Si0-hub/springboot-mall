package com.john.springbootmall.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.john.springbootmall.dto.BuyItem;
import com.john.springbootmall.dto.CreateOrderRequest;
import com.john.springbootmall.dto.UserLoginRequest;
import com.john.springbootmall.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private UserService userService;

    @Resource(name = "userServiceImpl")
    private void setProductService(UserService userService) {
        this.userService = userService;
    }

    private ObjectMapper objectMapper = new ObjectMapper();

    // 創建訂單
    @Transactional
    @Test
    public void createOrder_success() throws Exception {
        UserLoginRequest userRegisterRequest = new UserLoginRequest();
        userRegisterRequest.setEmail("user1@gmail.com");
        userRegisterRequest.setPassword("123");

        String au = login(userRegisterRequest);

        CreateOrderRequest createOrderRequest = new CreateOrderRequest();
        List<BuyItem> buyItemList = new ArrayList<>();

        BuyItem buyItem1 = new BuyItem();
        buyItem1.setProductId(1);
        buyItem1.setQuantity(5);
        buyItemList.add(buyItem1);

        BuyItem buyItem2 = new BuyItem();
        buyItem2.setProductId(2);
        buyItem2.setQuantity(2);
        buyItemList.add(buyItem2);

        createOrderRequest.setBuyItemList(buyItemList);

        String json = objectMapper.writeValueAsString(createOrderRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/order/users/createorders")
                .servletPath("/order/users/createorders")
                .header("Authorization", au)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.userId", equalTo(1)))
                .andExpect(jsonPath("$.totalAmount", equalTo(750)))
                .andExpect(jsonPath("$.orderItemList", hasSize(2)))
                .andExpect(jsonPath("$.createdDate", notNullValue()))
                .andExpect(jsonPath("$.lastModifiedDate", notNullValue()));
    }

    @Transactional
    @Test
    public void createOrder_illegalArgument_emptyBuyItemList() throws Exception {
        UserLoginRequest userRegisterRequest = new UserLoginRequest();
        userRegisterRequest.setEmail("user1@gmail.com");
        userRegisterRequest.setPassword("123");

        String au = login(userRegisterRequest);

        CreateOrderRequest createOrderRequest = new CreateOrderRequest();
        List<BuyItem> buyItemList = new ArrayList<>();
        createOrderRequest.setBuyItemList(buyItemList);

        String json = objectMapper.writeValueAsString(createOrderRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/order/users/createorders")
                .servletPath("/order/users/createorders")
                .header("Authorization", au)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(400));
    }

    @Transactional
    @Test
    public void createOrder_productNotExist() throws Exception {
        UserLoginRequest userRegisterRequest = new UserLoginRequest();
        userRegisterRequest.setEmail("user1@gmail.com");
        userRegisterRequest.setPassword("123");

        String au = login(userRegisterRequest);

        CreateOrderRequest createOrderRequest = new CreateOrderRequest();
        List<BuyItem> buyItemList = new ArrayList<>();

        BuyItem buyItem1 = new BuyItem();
        buyItem1.setProductId(100);
        buyItem1.setQuantity(1);
        buyItemList.add(buyItem1);

        createOrderRequest.setBuyItemList(buyItemList);

        String json = objectMapper.writeValueAsString(createOrderRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/order/users/createorders", 1)
                .servletPath("/order/users/createorders")
                .header("Authorization", au)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(400));
    }

    @Transactional
    @Test
    public void createOrder_stockNotEnough() throws Exception {
        UserLoginRequest userRegisterRequest = new UserLoginRequest();
        userRegisterRequest.setEmail("user1@gmail.com");
        userRegisterRequest.setPassword("123");

        String au = login(userRegisterRequest);

        CreateOrderRequest createOrderRequest = new CreateOrderRequest();
        List<BuyItem> buyItemList = new ArrayList<>();

        BuyItem buyItem1 = new BuyItem();
        buyItem1.setProductId(1);
        buyItem1.setQuantity(10000);
        buyItemList.add(buyItem1);

        createOrderRequest.setBuyItemList(buyItemList);

        String json = objectMapper.writeValueAsString(createOrderRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/order/users/createorders", 1)
                .servletPath("/order/users/createorders")
                .header("Authorization", au)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(400));
    }

    // 查詢訂單列表
    @Test
    public void getOrders() throws Exception {
        UserLoginRequest userRegisterRequest = new UserLoginRequest();
        userRegisterRequest.setEmail("user1@gmail.com");
        userRegisterRequest.setPassword("123");

        String au = login(userRegisterRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/order/users/getorders", 1)
                .servletPath("/order/users/getorders")
                .header("Authorization", au);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPages", notNullValue()))
                .andExpect(jsonPath("$.nowPage", notNullValue()))
                .andExpect(jsonPath("$.totalData", notNullValue()))
                .andExpect(jsonPath("$.dataList", hasSize(2)))
                .andExpect(jsonPath("$.dataList[0].userId", equalTo(1)))
                .andExpect(jsonPath("$.dataList[0].totalAmount", equalTo(500690)))
                .andExpect(jsonPath("$.dataList[0].orderItemList", hasSize(3)))
                .andExpect(jsonPath("$.dataList[0].createdDate", notNullValue()))
                .andExpect(jsonPath("$.dataList[0].lastModifiedDate", notNullValue()))
                .andExpect(jsonPath("$.dataList[1].userId", equalTo(1)))
                .andExpect(jsonPath("$.dataList[1].totalAmount", equalTo(100000)))
                .andExpect(jsonPath("$.dataList[1].orderItemList", hasSize(1)))
                .andExpect(jsonPath("$.dataList[1].createdDate", notNullValue()))
                .andExpect(jsonPath("$.dataList[1].lastModifiedDate", notNullValue()));
    }

    @Test
    public void getOrders_pagination() throws Exception {
        UserLoginRequest userRegisterRequest = new UserLoginRequest();
        userRegisterRequest.setEmail("user1@gmail.com");
        userRegisterRequest.setPassword("123");

        String au = login(userRegisterRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/order/users/getorders", 1)
                .servletPath("/order/users/getorders")
                .header("Authorization", au)
                .param("page", "1")
                .param("size", "2");

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPages", notNullValue()))
                .andExpect(jsonPath("$.nowPage", notNullValue()))
                .andExpect(jsonPath("$.totalData", notNullValue()))
                .andExpect(jsonPath("$.dataList", hasSize(0)));
        ;
    }

    @Test
    public void getOrders_userHasNoOrder() throws Exception {
        UserLoginRequest userRegisterRequest = new UserLoginRequest();
        userRegisterRequest.setEmail("user2@gmail.com");
        userRegisterRequest.setPassword("123");

        String au = login(userRegisterRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/order/users/getorders", 2)
                .servletPath("/order/users/getorders")
                .header("Authorization", au);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPages", notNullValue()))
                .andExpect(jsonPath("$.nowPage", notNullValue()))
                .andExpect(jsonPath("$.totalData", notNullValue()))
                .andExpect(jsonPath("$.dataList", hasSize(0)));
    }

    private String login(UserLoginRequest userRegisterRequest) throws Exception {
        return userService.login(userRegisterRequest);
    }
}