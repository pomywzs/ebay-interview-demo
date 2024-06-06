package com.ebay.interview.demo.Interceptor;


import com.ebay.interview.demo.domain.RequestContextInfo;
import com.ebay.interview.demo.domain.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.io.IOException;


/**
 * 拦截器异常处理
 **/
@Slf4j
@ControllerAdvice
public class CommonInterceptor implements ResponseBodyAdvice<Object> {

    @Autowired
    RequestContextInfo requestContextInfo;

    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType,
                                  Class clazz, ServerHttpRequest request, ServerHttpResponse response) {
        // 防止内存泄漏
        requestContextInfo.clear();
        return o;
    }

    /**
     * 全局异常
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ResponseVO<String> handler(Exception e) {
        String msg = "系统异常.";
        log.error(msg, e);
        return ResponseVO.serverError(msg);
    }

    /**
     * 参数缺失异常
     */
    @ResponseBody
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public ResponseVO<String> handlerMissingServletRequestParameterException(Exception e) {
        return ResponseVO.serverError("参数缺失");
    }

    /**
     * IO异常
     */
    @ResponseBody
    @ExceptionHandler(value = IOException.class)
    public ResponseVO<String> handlerIOException(IOException e) {
        return ResponseVO.serverError("IO异常");
    }

}
