package com.ebay.interview.demo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    private Integer userId;
    private String accountName;
    private Role role;

    public void setRole(String roleName) {
        this.role = Role.parseRole(roleName);
    }

    @Tolerate
    public User() {}
}
