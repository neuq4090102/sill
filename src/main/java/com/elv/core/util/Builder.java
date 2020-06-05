package com.elv.core.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 通用构造器 - 支持函数式写法（含示例）
 *
 * @author lxh
 * @date 2020-05-13
 */
public class Builder<T> {

    private final Supplier<T> supplier;
    private List<Consumer<T>> consumers = new ArrayList<>();

    private Builder(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public static <T> Builder<T> of(Supplier<T> supplier) {
        return new Builder<>(supplier);
    }

    @FunctionalInterface
    public interface Consumer1<T, P1> {
        void accept(T t, P1 p1);
    }

    @FunctionalInterface
    public interface Consumer2<T, P1, P2> {
        void accept(T t, P1 p1, P2 p2);
    }

    @FunctionalInterface
    public interface Consumer3<T, P1, P2, P3> {
        void accept(T t, P1 p1, P2 p2, P3 p3);
    }

    public <P1> Builder<T> with(Consumer1<T, P1> consumer, P1 p1) {
        Consumer<T> tmpConsumer = instance -> consumer.accept(instance, p1);
        consumers.add(tmpConsumer);
        return this;
    }

    public <P1, P2> Builder<T> with(Consumer2<T, P1, P2> consumer, P1 p1, P2 p2) {
        Consumer<T> tmpConsumer = instance -> consumer.accept(instance, p1, p2);
        consumers.add(tmpConsumer);
        return this;
    }

    public <P1, P2, P3> Builder<T> with(Consumer3<T, P1, P2, P3> consumer, P1 p1, P2 p2, P3 p3) {
        Consumer<T> tmpConsumer = instance -> consumer.accept(instance, p1, p2, p3);
        consumers.add(tmpConsumer);
        return this;
    }

    public T build() {
        T object = supplier.get();
        consumers.forEach(consumer -> consumer.accept(object));
        consumers.clear();
        return object;
    }

    public T build(boolean init) {
        T object = this.build();
        if (init) {
            // TODO：暂未实现
            // BeanUtil.init(object);
        }
        return object;
    }

    public static void main(String[] args) {
        // Builder用法示例
        BuildVO buildVO = Builder.of(BuildVO::new) //
                .with(BuildVO::setId, 441L) // 单个属性赋值
                .with(BuildVO::setVal, "艾泽拉斯", true) // 多个属性赋值
                .with(BuildVO::addMap, "1", "aaa") // 往map里赋值
                .with(BuildVO::addMap, "2", "bbb") //
                .with(BuildVO::addMap, "3", "ccc") //
                .with(BuildVO::addList, "java") // 往list中赋值
                .with(BuildVO::addList, "ruby") //
                .build();

        System.out.println(JsonUtil.toJson(buildVO));
        System.out.println(JsonUtil.toJson(Builder.of(BuildVO::new).build(true)));
    }
}

/**
 * 测试VO
 */
class BuildVO {
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
