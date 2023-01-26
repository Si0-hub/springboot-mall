package com.john.springbootmall.constant.impl;

import com.john.springbootmall.constant.EnumBase;
import com.john.springbootmall.util.CoverterUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public enum UserRole implements EnumBase<Integer> {
    ADMIN(1),
    MEMBER(2)
    ;

    private final Integer value;

    public static class Coverter extends CoverterUtil<UserRole, Integer> {
        public Coverter() {
            super(UserRole.class);
        }
    }
}

