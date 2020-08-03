package com.elv.core.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.elv.core.model.util.BuildVO;

/**
 * 通用构造器 - 支持函数式写法（含示例）
 *
 * @author lxh
 * @since 2020-05-13
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

    public T buildWithInit() {
        T object = this.build();
        MockUtil.init(object);
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
        System.out.println(JsonUtil.toJson(Builder.of(BuildVO::new).buildWithInit()));
    }
}


