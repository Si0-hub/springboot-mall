package com.john.springbootmall.service;

import com.john.springbootmall.dto.UserLoginRequest;
import com.john.springbootmall.dto.UserRegisterRequest;
import com.john.springbootmall.entity.User;

public interface UserService {

    User register(UserRegisterRequest userRegisterRequest);

    User login(UserLoginRequest userLoginRequest);
}
