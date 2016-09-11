package com.ebox.ex.network.model;

import com.ebox.ex.network.model.base.type.BoxInfoType;

import java.util.List;

/**
 * Created by Android on 2015/10/24.
 */
public class FasterSelectMode {

    private List<BoxInfoType> box_infos;
    private int box_count;//个数
    private String box_type;//文字描述

    private int box_state;//0：正常 1：选中 3.不可用
    //背景图片
    private int box_image_normal;
    private int box_image_select;
    private int box_image_disable;
    //背景色
    private int box_bg_normal;
    private int box_bg_select;
    private int box_bg_disable;
    //
    private int box_text_normal;
    private int box_text_select;
    private int box_text_disable;

    private String money;
    private int money_color_normal;
    private int money_color_select;



    public List<BoxInfoType> getBox_infos() {
        return box_infos;
    }

    public void setBox_infos(List<BoxInfoType> box_infos) {
        this.box_infos = box_infos;
    }

    public String getBox_type() {
        return box_type;
    }

    public void setBox_type(String box_type) {
        this.box_type = box_type;
    }

    public void setBox_image_nomal(int box_image_nomall) {
        this.box_image_normal = box_image_nomall;
    }

    public void setBox_image_select(int box_image_select) {
        this.box_image_select = box_image_select;
    }

    public void setBox_image_disable(int box_image_disable) {
        this.box_image_disable = box_image_disable;
    }

    public int getBox_count() {
        return box_count;
    }

    public void setBox_count(int box_count) {
        this.box_count = box_count;
    }

    public void setBox_bg_normal(int box_bg_normal) {
        this.box_bg_normal = box_bg_normal;
    }


    public void setBox_bg_select(int box_bg_select) {
        this.box_bg_select = box_bg_select;
    }

    public void setBox_bg_disable(int box_bg_disable) {
        this.box_bg_disable = box_bg_disable;
    }

    public void setBox_state(int box_state) {
        this.box_state = box_state;
    }

    public void setBox_text_normal(int box_text_normal) {
        this.box_text_normal = box_text_normal;
    }

    public void setBox_text_select(int box_text_select) {
        this.box_text_select = box_text_select;
    }

    public void setBox_text_disable(int box_text_disable) {
        this.box_text_disable = box_text_disable;
    }

    public int getImage()
    {
        if (box_state==0)
        {
            return box_image_normal;
        }
        if (box_state==1)
        {
            return box_image_select;
        }
        return box_image_disable;
    }

    public int getBoxBg(){
        if (box_state==0)
        {
            return box_bg_normal;
        }
        if (box_state==1)
        {
            return box_bg_select;
        }
        return box_bg_disable;
    }

    public int getTextColor(){
        if (box_state==0)
        {
            return box_text_normal;
        }
        if (box_state==1)
        {
            return box_text_select;
        }
        return box_text_disable;
    }

    public int getMoneyColor() {
        if (box_state != 1) {
            return money_color_normal;
        }else {
            return money_color_select;
        }
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }


    public void setMoney_color_select(int money_color_select) {
        this.money_color_select = money_color_select;
    }

    public void setMoney_color_normal(int money_color_normal) {
        this.money_color_normal = money_color_normal;
    }
}
