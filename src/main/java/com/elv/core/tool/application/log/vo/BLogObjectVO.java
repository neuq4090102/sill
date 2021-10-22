package com.elv.core.tool.application.log.vo;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author lxh
 * @since 2021-08-16
 */
public class BLogObjectVO {

    private Object oldObject;
    private Object newObject;
    private List<Field> allFields;
    private Map<String, Method> oldGetterMap;
    private Map<String, Method> newGetterMap;

    public Object getOldObject() {
        return oldObject;
    }

    public void setOldObject(Object oldObject) {
        this.oldObject = oldObject;
    }

    public Object getNewObject() {
        return newObject;
    }

    public void setNewObject(Object newObject) {
        this.newObject = newObject;
    }

    public List<Field> getAllFields() {
        return allFields;
    }

    public void setAllFields(List<Field> allFields) {
        this.allFields = allFields;
    }

    public Map<String, Method> getOldGetterMap() {
        oldGetterMap = Optional.ofNullable(oldGetterMap).orElse(new HashMap<>());
        return oldGetterMap;
    }

    public void setOldGetterMap(Map<String, Method> oldGetterMap) {
        this.oldGetterMap = oldGetterMap;
    }

    public Map<String, Method> getNewGetterMap() {
        newGetterMap = Optional.ofNullable(newGetterMap).orElse(new HashMap<>());
        return newGetterMap;
    }

    public void setNewGetterMap(Map<String, Method> newGetterMap) {
        this.newGetterMap = newGetterMap;
    }

    public Class<?> getOldClass() {
        return oldObject.getClass();
    }

    public Class<?> getNewClass() {
        return newObject.getClass();
    }

    public Map<String, Field> getFieldMap() {
        return Optional.ofNullable(getAllFields()).orElse(new ArrayList<>()).stream()
                .collect(Collectors.toMap(key -> key.getName(), val -> val, (v1, v2) -> v1));
    }

}
