package com.john.springbootmall.controller;

import com.john.springbootmall.dto.ProductRequest;
import com.john.springbootmall.entity.Product;
import com.john.springbootmall.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

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

//    @GetMapping("/products")
//    public ResponseEntity<Page<Product>> getProducts(
//            @RequestParam(required = false) ProductCategory category,
//            @RequestParam(required = false) String search,
//            @RequestParam(defaultValue = "created_date") String orderBy,
//            @RequestParam(defaultValue = "desc") String sort,
//            @RequestParam(defaultValue = "5") @Max(1000) @Min(0) Integer limit,
//            @RequestParam(defaultValue = "0") @Min(0) Integer offset
//    ) {
//        ProductQueryParams productQueryParams = new ProductQueryParams();
//        productQueryParams.setCategory(category);
//        productQueryParams.setSearch(search);
//        productQueryParams.setOrderBy(orderBy);
//        productQueryParams.setSort(sort);
//        productQueryParams.setLimt(limit);
//        productQueryParams.setOffset(offset);
//
//        // 取得 商品的列表
//        List<Product> productList = productService.getProducts(productQueryParams);
//
//        // 取得 商品總比數
//        Integer total = productService.countProduct(productQueryParams);
//
//        // 分頁
//        Page<Product> page = new Page<>();
//        page.setLimit(limit);
//        page.setOffset(offset);
//        page.setTotal(total);
//        page.setResult(productList);
//
//        return ResponseEntity.status(HttpStatus.OK).body(page);
//    }
}