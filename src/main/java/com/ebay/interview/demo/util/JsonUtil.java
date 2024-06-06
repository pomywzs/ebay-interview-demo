package com.ebay.interview.demo.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonUtil {

    public static final ObjectMapper mapper = new ObjectMapper();

    public static String objectToJsonString(Object o) {
        try {
            return mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            log.error("JsonUtil.objectToJsonString error",e);
        }
        return null;
    }

    public static <T> T jsonStringToObject(String str, Class<T> clazz) {
        try {
            return mapper.readValue(str, clazz);
        } catch (Exception e) {
            log.error("JsonUtil.jsonStringToObject error",e);
        }
        return null;
    }


}
