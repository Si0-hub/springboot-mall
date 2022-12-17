package com.john.springbootmall.dao.impl;

import com.john.springbootmall.dao.UserDao;
import com.john.springbootmall.dto.UserRegisterRequest;
import com.john.springbootmall.model.Product;
import com.john.springbootmall.model.User;
import com.john.springbootmall.rowmapper.ProductRowMapper;
import com.john.springbootmall.rowmapper.UserRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserDaoImpl implements UserDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Integer createUser(UserRegisterRequest userRegisterRequest) {

        String sql = "INSERT INTO user (email, password, created_date, last_modified_date) VALUES (:email, :password, :createDate, :lastModifiedDate)";
        Map<String, Object> insertMap = new HashMap<>();

        insertMap.put("email", userRegisterRequest.getEmail());
        insertMap.put("password", userRegisterRequest.getPassword());

        Date today = new Date();
        insertMap.put("createDate", today);
        insertMap.put("lastModifiedDate", today);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(insertMap), keyHolder);

        int userId = keyHolder.getKey().intValue();

        return userId;
    }

    @Override
    public User getUserById(Integer userId) {
        String sql = "SELECT user_id, email, password, created_date, last_modified_date FROM user WHERE user_id = :userId";

        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("userId", userId);

        List<User> userLsit = namedParameterJdbcTemplate.query(sql, queryMap, new UserRowMapper());

        if (userLsit.size() > 0) {
            return userLsit.get(0);
        } else {
            return null;
        }
    }

    @Override
    public User getUserByEmail(String email) {
        String sql = "SELECT user_id, email, password, created_date, last_modified_date FROM user WHERE email = :email";

        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("email", email);

        List<User> userLsit = namedParameterJdbcTemplate.query(sql, queryMap, new UserRowMapper());

        if (userLsit.size() > 0) {
            return userLsit.get(0);
        } else {
            return null;
        }
    }
}
