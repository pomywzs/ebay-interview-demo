package com.ebay.interview.demo.controller;


import com.ebay.interview.demo.domain.*;
import com.ebay.interview.demo.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.ebay.interview.demo.constant.CommonConstant.HEADER_USER_INFO;

@Slf4j
@RestController
public class UserManagerController {

    @Autowired
    RequestContextInfo requestContextInfo;

    @Autowired
    FileService fileService;


    @ModelAttribute
    public String setUserContext(HttpServletRequest request, HttpServletResponse response) throws MissingServletRequestParameterException {
        String userInfo = Optional.ofNullable(request)
                .map(req -> req.getHeader(HEADER_USER_INFO))
                .map(info -> new String(Base64.getDecoder().decode(info), StandardCharsets.UTF_8))
                .orElse(StringUtils.EMPTY);
        log.info("userInfo: {}", userInfo);
        if (StringUtils.isEmpty(userInfo)) {
            throw new MissingServletRequestParameterException(HEADER_USER_INFO, "header");
        }
        requestContextInfo.setUserInfo(userInfo);
        return userInfo;
    }

    /**
     * 添加用户资源
     */
    @RequestMapping(value = "/admin/addUser", method = RequestMethod.POST)
    public ResponseVO<String> addResources(@RequestBody UserResourceRequest resourceRequest) throws Exception {
        String msg;
        if (resourceRequest == null) {
            msg = "UserResource 不能为空";
            log.warn(msg);
            return ResponseVO.badRequest(msg);
        }
        boolean isAdmin = Optional.ofNullable(requestContextInfo.getRole()).map(r -> r == Role.admin).orElse(false);
        if (!isAdmin) {
            msg = "没有管理员权限";
            log.warn("添加用户资源失败: {}", msg);
            return ResponseVO.unAuthorized(msg);
        }
        if(ObjectUtils.isEmpty(resourceRequest)){
            return ResponseVO.badRequest("用户信息不存在");
        }else {
            UserResourceDTO userResourceDTO = new UserResourceDTO();
            BeanUtils.copyProperties(resourceRequest,userResourceDTO);
            boolean writeSuccess = fileService.addUserToFile(userResourceDTO);
            return writeSuccess ? ResponseVO.ok() : ResponseVO.serverError("用户添加失败");
        }

    }

    /**
     * 查看用户是否可访问资源
     *
     * @return 用户是否有访问资源权限
     */
    @RequestMapping(value = "/user/{resource}", method = RequestMethod.POST)
    public ResponseVO<Boolean> accessResource(@PathVariable String resource) throws Exception {
        String msg;
        if (Objects.isNull(resource)) {
            msg = "资源为空";
            log.warn(msg);
            return ResponseVO.badRequest(msg);
        }
        Map<Integer, UserResourceDTO> userResMap = fileService.readUserResourceGroupById();
        boolean haveAccess = Optional.ofNullable(userResMap.get(requestContextInfo.getUserId()))
                .map(userAccess -> Optional.ofNullable(userAccess.getEndpoint()).map(endpoint -> endpoint.contains(resource)).orElse(false))
                .orElse(false);
        return ResponseVO.ok(haveAccess);
    }





}
