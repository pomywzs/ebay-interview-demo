package com.ebay.interview.demo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
public class UserResourceRequest {
    private Integer userId;
    private Set<String> endpoint;

    @Tolerate
    public UserResourceRequest() {}
}
