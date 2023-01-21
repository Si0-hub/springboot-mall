package com.john.springbootmall.dao;

import com.john.springbootmall.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends CrudRepository<User, Integer> {
    User findByEmail(String email);
}
