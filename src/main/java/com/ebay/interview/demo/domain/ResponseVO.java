package com.ebay.interview.demo.domain;

import lombok.Builder;
import lombok.Data;
import org.apache.catalina.connector.Response;

@Builder
@Data
public class ResponseVO<T> {

    private int code;
    private T data;
    private String msg;

    public static <T> ResponseVO<T> serverError(String msg) {
        return ResponseVO.<T>builder().code(Response.SC_INTERNAL_SERVER_ERROR).data(null).msg(msg).build();
    }

    public static <T> ResponseVO<T> unAuthorized(String msg) {
        return ResponseVO.<T>builder().code(Response.SC_UNAUTHORIZED).data(null).msg(msg).build();
    }

    public static <T> ResponseVO<T> badRequest(String msg) {
        return ResponseVO.<T>builder().code(Response.SC_BAD_REQUEST).data(null).msg(msg).build();
    }

    public static <T> ResponseVO<T> ok() {
        return ResponseVO.<T>builder().code(Response.SC_OK).data(null).msg("success").build();
    }

    public static <T> ResponseVO<T> ok(T data) {
        return ResponseVO.<T>builder().code(Response.SC_OK).data(data).msg("success").build();
    }
}
