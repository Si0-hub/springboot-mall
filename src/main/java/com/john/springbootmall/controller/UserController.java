package com.john.springbootmall.controller;

import com.john.springbootmall.dto.UserLoginRequest;
import com.john.springbootmall.dto.UserRegisterRequest;
import com.john.springbootmall.entity.User;
import com.john.springbootmall.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

@Api(tags = "使用者相關api")
@RestController
public class UserController {


    private UserService userService;

    @Resource(name = "userServiceImpl")
    private void setProductService(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation("註冊")
    @PostMapping("/users/register")
    public ResponseEntity<User> register(@RequestBody @Valid UserRegisterRequest userRegisterRequest) {
        User user = userService.register(userRegisterRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @ApiOperation("登入")
    @PostMapping("/users/login")
    public ResponseEntity<User> login(@RequestBody @Valid UserLoginRequest userLoginRequest) {
        User user = userService.login(userLoginRequest);

        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
}
