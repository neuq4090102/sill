package com.elv.core.util;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author lxh
 * @date 2020-04-17
 */
public class JsonUtil {

    private static final Logger logger;
    private static final ObjectMapper mapper;

    static {
        logger = LoggerFactory.getLogger(JsonUtil.class);
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private JsonUtil() {
    }

    public static String toJson(Object object) {
        String result = null;
        try {
            result = mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            logger.error(" Failed to convert to json with {}.", object.toString(), e);
        }

        return result;
    }

    public static <T> T toObject(String json, Class<T> targetClass) {
        T result = null;
        try {
            result = mapper.readValue(json, targetClass);
        } catch (Exception e) {
            if (!isJson(json)) {
                logger.error(" It's not valid json string with {}.", json, e);
            } else {
                logger.error(" Failed to convert to Object with {}.", json, e);
            }
        }

        return result;
    }

    public static <T> T toObject(String json, TypeReference<T> typeReference) {
        T result = null;
        try {
            result = mapper.readValue(json, typeReference);
        } catch (IOException e) {
            if (!isJson(json)) {
                logger.error(" It's not valid json string with {}.", json, e);
            } else {
                logger.error(" Failed to convert to Object with {}.", json, e);
            }
        }

        return result;
    }

    public static <T> List<T> toList(String json, Class<T[]> targetClasses) {
        T[] results = toObject(json, targetClasses);
        if (results == null || results.length == 0) {
            return Collections.emptyList();
        }

        return Arrays.asList(results);
    }

    public static JsonNode toTree(String json) {
        JsonNode result = null;
        try {
            result = mapper.readTree(json);
        } catch (Exception e) {
            logger.error(" Failed to convert to Tree with {}.", json, e);
        }

        return result;
    }

    public static boolean isJson(String json) {
        if (json.startsWith("{") && json.endsWith("}")) {
            return true;
        } else if (json.startsWith("[{") && json.endsWith("}]")) {
            return true;
        } else if (json.startsWith("[") && json.endsWith("]")) {
            return true;
        }
        return false;
    }
}
