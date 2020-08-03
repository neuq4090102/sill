package com.elv.core.model.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class BuildVO {

    private long id;
    private String name;
    private boolean next;
    private List<String> list;
    private Map<String, String> map;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isNext() {
        return next;
    }

    public void setNext(boolean next) {
        this.next = next;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    public void setVal(String name, boolean next) {
        this.name = name;
        this.next = next;
    }

    public void addList(String str) {
        this.list = Optional.ofNullable(this.list).orElse(new ArrayList<>());
        this.list.add(str);
    }

    public void addMap(String key, String val) {
        this.map = Optional.ofNullable(this.map).orElse(new HashMap<>());
        this.map.put(key, val);
    }
}
