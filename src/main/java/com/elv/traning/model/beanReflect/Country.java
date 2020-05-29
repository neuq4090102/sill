package com.elv.traning.model.beanReflect;

import java.util.List;

/**
 * @author lxh
 * @date 2020-04-09
 */
public class Country extends Earth implements ILand {

    public String name;

    private int populationSize;

    private List<City> cities;

    public Country() {
    }

    public Country(String name, int populationSize) {
        this.name = name;
        this.populationSize = populationSize;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPopulationSize() {
        return populationSize;
    }

    public void setPopulationSize(int populationSize) {
        this.populationSize = populationSize;
    }

    private Void privateMethod() {
        System.out.println("这是私有方法");
        return null;
    }
}
