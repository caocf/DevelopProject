package com.xhl.world.model;

import java.util.List;

/**
 * Created by Sum on 16/2/3.
 */
public class AdvListModel {
    //按楼层排序
    private List<AdvModel> one;
    private List<AdvModel> two;
    private List<AdvModel> three;
    private List<AdvModel> four;
    private List<AdvModel> five;
    private List<AdvModel> six;
    private AdvModel name;

    public List<AdvModel> getOne() {
        return one;
    }

    public List<AdvModel> getTwo() {
        return two;
    }

    public List<AdvModel> getThree() {
        return three;
    }

    public List<AdvModel> getFour() {
        return four;
    }

    public List<AdvModel> getFive() {
        return five;
    }

    public List<AdvModel> getSix() {
        return six;
    }

    public AdvModel getName() {
        return name;
    }
}
