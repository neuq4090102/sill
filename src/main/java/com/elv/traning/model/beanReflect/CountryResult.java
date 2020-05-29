package com.elv.traning.model.beanReflect;

/**
 * @author lxh
 * @date 2020-04-09
 */
public class CountryResult {

    public String name;

    private int populationSize;

    private long earthArea;

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

    public long getEarthArea() {
        return earthArea;
    }

    public void setEarthArea(long earthArea) {
        this.earthArea = earthArea;
    }

    private void privateMethod() {
        System.out.println("这是私有方法");
    }
}
