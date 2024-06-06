package com.ebay.interview.demo;


import com.ebay.interview.demo.domain.Role;
import com.ebay.interview.demo.domain.User;
import com.ebay.interview.demo.domain.UserResourceRequest;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.ebay.interview.demo.util.JsonUtil;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.ebay.interview.demo.constant.CommonConstant.HEADER_USER_INFO;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class UserManagerTest {

    @Autowired
    private MockMvc mockMvc;

    private String encodedAdminUserInfo;

    private String encodedUserInfo;


    /**
     * 数据初始化
     */
    @Before
    public void dataInitialize() {
        // 管理员
        String adminUserInfo = Optional.ofNullable(JsonUtil.objectToJsonString(User.builder().userId(1).role(Role.admin).accountName("管理员1").build()))
                .orElse(StringUtils.EMPTY);
        // 普通用户
        String userInfo = Optional.ofNullable(JsonUtil.objectToJsonString(User.builder().userId(2).role(Role.user).accountName("普通用户1").build()))
                .orElse(StringUtils.EMPTY);

        System.out.println("加密管理员用户信息: " + adminUserInfo);
        encodedAdminUserInfo = new String(Base64.getEncoder().encode(adminUserInfo.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
        System.out.println("解密管理员用户信息: " + encodedAdminUserInfo);

        System.out.println("加密普通用户信息: " + userInfo);
        encodedUserInfo = new String(Base64.getEncoder().encode(userInfo.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
        System.out.println("解密普通用户信息: " + encodedUserInfo);
    }

    /**
     * 管理员添加用户测试
     */
    @Test
    public void adminUserAddUserTest() throws Exception {
        //用户资源
        Set<String> userResources = new HashSet<>();
        userResources.add("resourceA");
        userResources.add("resourceB");
        userResources.add("resourceC");

        String requestBody = Optional.ofNullable(
                        JsonUtil.objectToJsonString(UserResourceRequest.builder()
                                .userId(1)
                                .endpoint(userResources)
                                .build())
                )
                .orElse(StringUtils.EMPTY);
        mockMvc.perform(MockMvcRequestBuilders.post("/admin/addUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .accept(MediaType.APPLICATION_JSON)
                .header(HEADER_USER_INFO, encodedAdminUserInfo)
        ).andExpect(MockMvcResultMatchers.status().isOk());
        System.out.println("adminUserAddUserTest end");
    }

    /**
     * 普通用户添加用户测试
     */
    @Test
    public void userAddUserTest() throws Exception {
        //用户资源
        Set<String> userResources = new HashSet<>();
        userResources.add("resourceA");
        userResources.add("resourceB");
        userResources.add("resourceC");

        String requestBody = Optional.ofNullable(
                        JsonUtil.objectToJsonString(UserResourceRequest.builder()
                                .userId(2)
                                .endpoint(userResources)
                                .build())
                )
                .orElse(StringUtils.EMPTY);
        mockMvc.perform(MockMvcRequestBuilders.post("/admin/addUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .accept(MediaType.APPLICATION_JSON)
                .header(HEADER_USER_INFO, encodedUserInfo)
        ).andExpect(MockMvcResultMatchers.status().isOk());
        System.out.println("userAddUserTest end");
    }

    /**
     * 测试可访问资源
     */
    @Test
    public void accessResourceTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/user/resourceA")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(HEADER_USER_INFO, encodedAdminUserInfo)
        ).andExpect(MockMvcResultMatchers.status().isOk());
        System.out.println("accessResourceTest end");
    }


    /**
     * 测试不可访问资源
     */
    @Test
    public void unAccessResourceTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/user/resourceD")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER_USER_INFO, encodedAdminUserInfo)
                ).andExpect(MockMvcResultMatchers.status().isOk());
        System.out.println("unAccessResourceTest end");
    }

}
