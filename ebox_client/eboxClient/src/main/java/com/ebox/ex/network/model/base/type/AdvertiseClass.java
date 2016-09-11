package com.ebox.ex.network.model.base.type;

import java.util.List;

public class AdvertiseClass {

    private Integer class_id;
    private Integer type;
    private String name;
    private StrategyType strategy;

    private List<Advertise> advertise;
    public Integer getClass_id() {
        return class_id;
    }

    public void setClass_id(Integer class_id) {
        this.class_id = class_id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public StrategyType getStrategy() {
        return strategy;
    }

    public void setStrategy(StrategyType strategy) {
        this.strategy = strategy;
    }

    public List<Advertise> getAdvertise() {
        return advertise;
    }

    public void setAdvertise(List<Advertise> advertise) {
        this.advertise = advertise;
    }



}
