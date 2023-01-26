package com.john.springbootmall.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.john.springbootmall.constant.impl.UserRole;
import com.john.springbootmall.dto.UserRegisterRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    public User(UserRegisterRequest userRegisterRequest) {
        this.setEmail(userRegisterRequest.getEmail());
        this.setPassword(userRegisterRequest.getPassword());
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Integer userId;

    @Column(name = "email")
    private String email;

    @JsonIgnore
    @Column(name = "password")
    private String password;

    @Column(name = "enabled")
    private Boolean enabled = true;

    @Column(name = "role")
    @Convert(converter = UserRole.Coverter.class)
    private UserRole role;

    @Column(name = "created_date", updatable = false)
    private Date createdDate;

    @Column(name = "last_modified_date")
    private Date lastModifiedDate;
}
