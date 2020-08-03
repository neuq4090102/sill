package com.elv.traning.jackson;

import com.elv.traning.model.jackson.JsonEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author lxh
 * @since 2020-04-16
 */
public class Jackson {

    private static final Logger logger;
    private static final ObjectMapper mapper;

    static {
        logger = LoggerFactory.getLogger(Jackson.class);

        mapper = new ObjectMapper();

        //在反序列化时忽略在json中存在但Java对象不存在的属性,有可能是空
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static void main(String[] args) {

        testToJson();

        // testToObject();

        // testToTree();

        // test();

        // testIsJson();

    }

    private static void testToJson() {

        System.out.println("list by digital to json" + toJson(Stream.of("1,3,5,7,9").collect(Collectors.toList())));

        JsonEntity jsonEntity = new JsonEntity();
        System.out.println(jsonEntity);
        System.out.println("object to json:" + toJson(jsonEntity));

        Map<String, String> map = new HashMap<>();
        map.put("a", "111");
        map.put("b", "222");
        map.put("c", "333");

        System.out.println("map to json:" + toJson(map));

        List<JsonEntity> jsonEntities = new ArrayList<>();
        // jsonEntities.add(JsonEntity.builder()._first_name("zhang").value(3).build());
        // jsonEntities.add(JsonEntity.builder()._first_name("li").value(4).build());
        // jsonEntities.add(JsonEntity.builder()._first_name("wang").value(5).build());

        System.out.println("list to json:" + toJson(jsonEntities));

        TreeSet<JsonEntity> treeSet = new TreeSet<>();
        // treeSet.add(JsonEntity.builder()._first_name("zhang").value(3).build());
        // treeSet.add(JsonEntity.builder()._first_name("zhang2").value(4).build());
        // treeSet.add(JsonEntity.builder()._first_name("zhang3").value(5).build());
        System.out.println("treeSet to json:" + toJson(treeSet));

        TreeMap<String, String> subTreeMap1 = new TreeMap<>();
        TreeMap<String, String> subTreeMap2 = new TreeMap<>();
        TreeMap<String, String> subTreeMap3 = new TreeMap<>();

        subTreeMap1.put("0101", "010101");
        subTreeMap1.put("0102", "010201");
        subTreeMap1.put("0103", "010301");

        subTreeMap2.put("0201", "020101");
        subTreeMap2.put("0202", "020201");
        subTreeMap2.put("0203", "020301");

        subTreeMap3.put("0301", "030101");
        subTreeMap3.put("0302", "030201");
        subTreeMap3.put("0303", "030301");

        TreeMap<String, TreeMap<String, String>> treeMap = new TreeMap<>();
        treeMap.put("01", subTreeMap1);
        treeMap.put("02", subTreeMap2);
        treeMap.put("03", subTreeMap3);

        System.out.println("treeMap to json:" + toJson(treeMap));

    }

    private static void testToObject() {
        //        String json = "{\"channelOrderNo\":null,\"channelPrice\":0,\"orderNo\":\"202002124419569448072\",\"invoicePrice\":500,\"orderRoomEntities\":null}";
        String json = "{\"value\":666,\"firstName\":\"Bob\"}";
        System.out.println(">>>>" + toObject(json, JsonEntity.class));
        String mapJson = "{\"a\":\"111\",\"b\":\"222\",\"c\":\"333\"}";

        TypeReference<Map<String, String>> mapTypeReference = new TypeReference<Map<String, String>>() {
        };
        Map<String, String> map = toObject(mapJson, mapTypeReference);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.out.println(entry.getKey() + "-->" + entry.getValue());
        }

        String listJson = "[{\"value\":3,\"firstName\":\"zhang\"},{\"value\":4,\"firstName\":\"li\"},{\"value\":5,\"firstName\":\"wang\"}]";

        List<JsonEntity> jsonEntities = toList(listJson, JsonEntity[].class);
        for (JsonEntity jsonEntity : jsonEntities) {
            System.out.println("list:" + jsonEntity);
        }
    }

    private static void testToTree() {
        String treeJson = "{\"01\":{\"0101\":\"010101\",\"0102\":\"010201\",\"0103\":\"010301\"},\"02\":{\"0201\":\"020101\",\"0202\":\"020201\",\"0203\":\"020301\"},\"03\":{\"0301\":\"030101\",\"0302\":\"030201\",\"0303\":\"030301\"}}";
        JsonNode jsonNode = toTree(treeJson);
        System.out.println(jsonNode.size());
        System.out.println(jsonNode.getNodeType().name());
        System.out.println(toJson(jsonNode.get("02")));
    }

    private static String toJson(Object object) {
        String result = null;
        try {
            result = mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            logger.error(" failed to convert to json with {}.", object.toString(), e);
        }

        return result;
    }

    private static <T> T toObject(String json, Class<T> targetClass) {
        if (!isJson(json)) {

        }
        T result = null;
        try {
            result = mapper.readValue(json, targetClass);
        } catch (IOException e) {
            logger.error(" failed to convert to Object with {}.", json, e);
        }

        return result;
    }

    private static <T> T toObject(String json, TypeReference<T> typeReference) {
        if (!isJson(json)) {

        }
        T result = null;
        try {
            result = mapper.readValue(json, typeReference);
        } catch (IOException e) {
            logger.error(" failed to convert to Object with {}.", json, e);
        }

        return result;
    }

    private static <T> List<T> toList(String json, Class<T[]> targetClasses) {
        T[] results = toObject(json, targetClasses);
        if (results == null || results.length == 0) {
            return Collections.emptyList();
        }

        return Arrays.asList(results);
    }

    private static JsonNode toTree(String json) {
        JsonNode result = null;
        try {
            result = mapper.readTree(json);
        } catch (JsonProcessingException e) {
            logger.error(" failed to convert to Tree with {}.", json, e);
        }

        return result;
    }

    private static void test() {

        String json = "{\"value\":666,\"firstName\":\"Bob\"}";
        JsonEntity jsonEntityOne = toObject(json, JsonEntity.class);

        JsonEntity jsonEntityTwo = mapper.convertValue(jsonEntityOne, JsonEntity.class);
        jsonEntityTwo.set_first_name("lv");

        System.out.println(jsonEntityTwo);
    }

    private static boolean isJson(String json) {
        return true;
    }

    private static void testIsJson() {
        List<String> jsonList = Arrays.asList(new String[] { "[\"1,3,5,7,9\"}", "[\"1,3,5,7,9\"]",
                "{\"value\":666,\"firstName\":\"B;o:b}r{f\"}", "{\"a\":\"111\",\"b\":\"222\",\"c\":\"333\"}",
                "[{\"value\":3,\"firstName\":\"zhang\"},{\"value\":4,\"firstName\":\"li\"},{\"value\":5,\"firstName\":\"wang\"}]",
                "[{\"value\":3,\"firstName\":\"zhang\"},{\"value\":4,\"firstName\":\"zhang2\"},{\"value\":5,\"firstName\":\"zhang3\"}]",
                "{\"01\":{\"0101\":\"010101\",\"0102\":\"010201\",\"0103\":\"010301\"},\"02\":{\"0201\":\"020101\",\"0202\":\"020201\",\"0203\":\"020301\"},\"03\":{\"0301\":\"030101\",\"0302\":\"030201\",\"0303\":\"030301\"}}" });
        for (int i = 0; i < jsonList.size(); i++) {
            //            if (i != 2) {
            //                continue;
            //            }
            String json = jsonList.get(i);
            System.out.println(json + "--->>>" + isJson(json));
        }
    }

    private static boolean isJson2(String json) {

        boolean containKeyValueType = json.contains(":");

        char[] chars = json.toCharArray();
        Stack<Character> stacks = new Stack<>();

        int beginIdx = 0;
        int endIdx = chars.length - 1;
        for (int i = 0; i < chars.length; i++) {
            char tmp = chars[i];
            if ('{' == tmp || '[' == tmp) {
                stacks.push(tmp);

                if (containKeyValueType && json.substring(i, endIdx + 1).contains(":")) {
                    beginIdx = i;
                    //                    endIdx = chars.length - 1;
                }
            }

            if ('}' == tmp || ']' == tmp) {
                if (stacks.isEmpty()) {
                    return false;
                } else if ('}' == tmp && stacks.pop() != '{') {
                    return false;
                } else if (']' == tmp && stacks.pop() != '[') {
                    return false;
                }

                if (containKeyValueType && json.substring(beginIdx, i + 1).contains(":")) {
                    endIdx = i;
                }
            }

            if (containKeyValueType && beginIdx > 0 && endIdx < chars.length) {
                String tmp2 = json.substring(beginIdx, endIdx + 1);
                System.out.println("beginIdx=" + beginIdx + ",endIdx=" + endIdx + ",str=" + tmp2);
            }

        }

        String tmp2 = json.substring(beginIdx, endIdx + 1);
        System.out.println("beginIdx=" + beginIdx + ",endIdx=" + endIdx + ",str=" + tmp2);

        return stacks.isEmpty();
    }

}
