package com.john.springbootmall.controller;

import com.john.springbootmall.constant.ProductCategory;
import com.john.springbootmall.dto.ProductQueryParams;
import com.john.springbootmall.dto.ProductRequest;
import com.john.springbootmall.entity.Product;
import com.john.springbootmall.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.HashMap;
import java.util.Map;

@Validated
@RestController
@RequestMapping("/product")
public class ProductController {

    private ProductService productService;

    @Resource(name = "productServiceImpl")
    private void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<Product> getProduct(@PathVariable Integer productId) {
        Product product = productService.getProductById(productId);

        if (product != null) {
            return ResponseEntity.status(HttpStatus.OK).body(product);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(@RequestBody @Valid ProductRequest productRequest) {
        Product product = productService.createProduct(productRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @PutMapping("/products/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Integer productId,
                                                 @RequestBody @Valid ProductRequest productRequest) {
        // 檢查該資料是否存在
        Product product = productService.getProductById(productId);

        if (product == null) {
            System.out.println("Not found product");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // 修改商品的數據
        productService.updateProduct(productId, productRequest);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<Product> deleteProductById(@PathVariable Integer productId) {
        productService.deleteProductById(productId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/products")
    public ResponseEntity<Map<String, Object>> getProducts(
            @RequestParam(required = false) ProductCategory category,
            @RequestParam(defaultValue = "createdDate") String orderBy,
            @RequestParam(defaultValue = "DESC") String sort,
            @RequestParam(defaultValue = "0") @Max(1000) @Min(0) Integer page,
            @RequestParam(defaultValue = "5") @Max(100) @Min(0) Integer size
    ) {
        ProductQueryParams productQueryParams = new ProductQueryParams();
        productQueryParams.setCategory(category);
        productQueryParams.setPage(page);
        productQueryParams.setSize(size);
        productQueryParams.setOrderBy(orderBy);
        productQueryParams.setSort(sort.toUpperCase());

        // 取得 商品的列表
        Page<Product> productPage = productService.getProducts(productQueryParams);


        // 回傳集
        Map<String, Object> response = new HashMap<>();
        response.put("dataList", productPage.getContent());
        response.put("totalPages", productPage.getTotalPages());
        response.put("totalData", productPage.getTotalElements());
        response.put("nowPage", productPage.getNumber());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}