package com.elv.traning.designMode.responsibilityChain;

/**
 * 策略结果存储
 *
 * @author lxh
 * @date 2020-05-20
 */
public class StrategyStore {

    private String name;

    public StrategyStore() {
    }

    public StrategyStore(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
