package com.ebay.interview.demo.domain;

import org.springframework.util.StringUtils;

import java.util.Optional;

public enum Role {

    admin("管理员"),
    user("普通用户"),

    un_login("未登录");

    private final String desc;

    Role(String desc) {
        this.desc = desc;
    }

    public static Role parseRole(String roleName) {
        return Optional.ofNullable(roleName).map(Role::valueOf).orElse(un_login);
    }
}
