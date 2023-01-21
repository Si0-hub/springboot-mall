package com.john.springbootmall.service.impl;

import com.john.springbootmall.dao.UserDao;
import com.john.springbootmall.dto.UserLoginRequest;
import com.john.springbootmall.dto.UserRegisterRequest;
import com.john.springbootmall.entity.User;
import com.john.springbootmall.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private UserDao userDao;

    @Resource(name = "userDao")
    private void setProductService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User register(UserRegisterRequest userRegisterRequest) {
        // 檢查註冊的email
        Optional<User> user = userDao.findByEmail(userRegisterRequest.getEmail());

        if (user.isPresent()) {
            log.warn("該Email {} 已經被註冊了!!", userRegisterRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        String hashedPassword = DigestUtils.md5DigestAsHex(userRegisterRequest.getPassword().getBytes());
        userRegisterRequest.setPassword(hashedPassword);

        User registerUser = new User(userRegisterRequest);

        Date today = new Date();
        registerUser.setCreatedDate(today);
        registerUser.setLastModifiedDate(today);

        return userDao.save(registerUser);
    }

    @Override
    public String login(UserLoginRequest userLoginRequest) {
        Optional<User> user = userDao.findByEmail(userLoginRequest.getEmail());


        // 檢查user 是否存在
        if (!user.isPresent()) {
            log.warn("該 email {} 尚未註冊", userLoginRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        String hashedPassword = DigestUtils.md5DigestAsHex(userLoginRequest.getPassword().getBytes());

        if (user.get().getPassword().equals(hashedPassword)) {
            // 設定30min過期
            Date expireDate = new Date(System.currentTimeMillis()+ 30 * 60 * 1000);
            String jwtToken = Jwts.builder()
                            .setSubject(user.get().getEmail()) // 以email當subject
                            .setExpiration(expireDate)
                            .signWith(SignatureAlgorithm.HS512,"MySecret") // MySecret是自訂的私鑰，HS512是自選的演算法，可以任意改變
                            .compact();
            return jwtToken;
        } else {
            log.warn("email {} 的密碼不正確", userLoginRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
