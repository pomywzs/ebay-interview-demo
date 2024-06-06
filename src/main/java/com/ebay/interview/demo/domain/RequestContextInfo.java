package com.ebay.interview.demo.domain;

import org.springframework.stereotype.Component;
import com.ebay.interview.demo.util.JsonUtil;

import java.util.Optional;

@Component
public class RequestContextInfo {

    private final ThreadLocal<User> threadLocal = ThreadLocal.withInitial(User::new);

    public void setUserInfo(String userInfo) {
        Optional.ofNullable(JsonUtil.jsonStringToObject(userInfo, User.class)).ifPresent(threadLocal::set);
    }

    public Role getRole() {
        return threadLocal.get().getRole();
    }

    public void clear() {
        threadLocal.remove();
    }

    public Integer getUserId() {
        return threadLocal.get().getUserId();
    }

}
