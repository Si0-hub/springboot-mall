package com.john.springbootmall.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.john.springbootmall.constant.impl.ProductCategory;
import com.john.springbootmall.dto.ProductRequest;
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

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private UserService userService;

    @Resource(name = "userServiceImpl")
    private void setProductService(UserService userService) {
        this.userService = userService;
    }

    private ObjectMapper objectMapper = new ObjectMapper();

    // 查詢商品
    @Test
    public void getProduct_success() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/product/getProducts/{productId}", 1)
                .servletPath("/product/getProducts/1");

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName", equalTo("蘋果（澳洲）")))
                .andExpect(jsonPath("$.category", equalTo("FOOD")))
                .andExpect(jsonPath("$.imageUrl", notNullValue()))
                .andExpect(jsonPath("$.price", notNullValue()))
                .andExpect(jsonPath("$.stock", notNullValue()))
                .andExpect(jsonPath("$.description", notNullValue()))
                .andExpect(jsonPath("$.createdDate", notNullValue()))
                .andExpect(jsonPath("$.lastModifiedDate", notNullValue()));
    }

    @Test
    public void getProduct_notFound() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/product/getProducts/{productId}", 20000)
                .servletPath("/product/getProducts/20000");

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(404));
    }

    // 創建商品
    @Transactional
    @Test
    public void createProduct_success() throws Exception {
        String au = login();

        ProductRequest productRequest = new ProductRequest();
        productRequest.setProductName("test food product");
        productRequest.setCategory(ProductCategory.FOOD);
        productRequest.setImageUrl("http://test.com");
        productRequest.setPrice(100);
        productRequest.setStock(2);

        String json = objectMapper.writeValueAsString(productRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/product/products/create")
                .servletPath("/product/products/create")
                .header("Authorization", au)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.productName", equalTo("test food product")))
                .andExpect(jsonPath("$.category", equalTo("FOOD")))
                .andExpect(jsonPath("$.imageUrl", equalTo("http://test.com")))
                .andExpect(jsonPath("$.price", equalTo(100)))
                .andExpect(jsonPath("$.stock", equalTo(2)))
                .andExpect(jsonPath("$.description", nullValue()))
                .andExpect(jsonPath("$.createdDate", notNullValue()))
                .andExpect(jsonPath("$.lastModifiedDate", notNullValue()));
    }

    @Transactional
    @Test
    public void createProduct_illegalArgument() throws Exception {
        String au = login();

        ProductRequest productRequest = new ProductRequest();
        productRequest.setProductName("test food product");

        String json = objectMapper.writeValueAsString(productRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/product/products/create")
                .servletPath("/product/products/create")
                .header("Authorization", au)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(400));
    }

    // 更新商品
    @Transactional
    @Test
    public void updateProduct_success() throws Exception {
        String au = login();

        ProductRequest productRequest = new ProductRequest();
        productRequest.setProductName("test food product");
        productRequest.setCategory(ProductCategory.FOOD);
        productRequest.setImageUrl("http://test.com");
        productRequest.setPrice(100);
        productRequest.setStock(2);

        String json = objectMapper.writeValueAsString(productRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/product/products/{productId}", 3)
                .servletPath("/product/products/3")
                .header("Authorization", au)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(200));
    }

    @Transactional
    @Test
    public void updateProduct_illegalArgument() throws Exception {
        String au = login();

        ProductRequest productRequest = new ProductRequest();
        productRequest.setProductName("test food product");

        String json = objectMapper.writeValueAsString(productRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/product/products/{productId}", 3)
                .servletPath("/product/products/3")
                .header("Authorization", au)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(400));

    }

    @Transactional
    @Test
    public void updateProduct_productNotFound() throws Exception {
        String au = login();

        ProductRequest productRequest = new ProductRequest();
        productRequest.setProductName("test food product");
        productRequest.setCategory(ProductCategory.FOOD);
        productRequest.setImageUrl("http://test.com");
        productRequest.setPrice(100);
        productRequest.setStock(2);

        String json = objectMapper.writeValueAsString(productRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/product/products/{productId}", 20000)
                .servletPath("/product/products/20000")
                .header("Authorization", au)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(404));
    }

    // 刪除商品
    @Transactional
    @Test
    public void deleteProduct_success() throws Exception {
        String au = login();

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/product/products/{productId}", 1)
                .servletPath("/product/products/1")
                .header("Authorization", au);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(204));
    }

    @Transactional
    @Test
    public void deleteProduct_deleteNonExistingProduct() throws Exception {
        String au = login();

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/product/products/{productId}", 20000)
                .servletPath("/product/products/20000")
                .header("Authorization", au);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(204));
    }

    // 查詢商品列表
    @Test
    public void getProducts() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/product/getProducts")
                .servletPath("/product/getProducts");

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPages", notNullValue()))
                .andExpect(jsonPath("$.nowPage", notNullValue()))
                .andExpect(jsonPath("$.totalData", notNullValue()))
                .andExpect(jsonPath("$.dataList", hasSize(5)));
    }

    @Test
    public void getProducts_filtering() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/product/getProducts")
                .servletPath("/product/getProducts")
                .param("category", "CAR");

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPages", notNullValue()))
                .andExpect(jsonPath("$.nowPage", notNullValue()))
                .andExpect(jsonPath("$.totalData", notNullValue()))
                .andExpect(jsonPath("$.dataList", hasSize(4)));
    }

    @Test
    public void getProducts_sorting() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/product/getProducts")
                .servletPath("/product/getProducts")
                .param("orderBy", "price")
                .param("sort", "desc");

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPages", notNullValue()))
                .andExpect(jsonPath("$.nowPage", notNullValue()))
                .andExpect(jsonPath("$.totalData", notNullValue()))
                .andExpect(jsonPath("$.dataList", hasSize(5)))
                .andExpect(jsonPath("$.dataList[0].productName", equalTo("Benz")))
                .andExpect(jsonPath("$.dataList[1].productName", equalTo("BMW")))
                .andExpect(jsonPath("$.dataList[2].productName", equalTo("Tesla")))
                .andExpect(jsonPath("$.dataList[3].productName", equalTo("Toyota")))
                .andExpect(jsonPath("$.dataList[4].productName", equalTo("蘋果（日本北海道）")));
    }

    @Test
    public void getProducts_pagination() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/product/getProducts")
                .servletPath("/product/getProducts")
                .param("size", "2")
                .param("page", "2");

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPages", notNullValue()))
                .andExpect(jsonPath("$.nowPage", notNullValue()))
                .andExpect(jsonPath("$.totalData", notNullValue()))
                .andExpect(jsonPath("$.dataList", hasSize(2)))
                .andExpect(jsonPath("$.dataList[0].productName", equalTo("好吃又鮮甜的蘋果橘子")))
                .andExpect(jsonPath("$.dataList[1].productName", equalTo("蘋果（日本北海道）")));
    }

    private String login() throws Exception {
        UserLoginRequest userRegisterRequest = new UserLoginRequest();
        userRegisterRequest.setEmail("user1@gmail.com");
        userRegisterRequest.setPassword("123");
        return userService.login(userRegisterRequest);
    }
}