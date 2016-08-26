package com.xhl.world.model;

import com.xhl.xhl_library.ui.viewPager.ImageModel;

import java.util.List;

/**
 * Created by Summer on 2016/8/26.
 */
public class AdvHTest extends ImageModel {

    private String name;
    private int resId;

    private List<AdvHTest> list;

    public AdvHTest() {
    }

    public AdvHTest(String name, int resId) {
        this.name = name;
        this.resId = resId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }


    public List<AdvHTest> getList() {
        return list;
    }

    public void setList(List<AdvHTest> list) {
        this.list = list;
    }

    @Override
    public String getImageUrl() {
        return null;
    }

    @Override
    public int getImageRes() {
        return resId;
    }
}
