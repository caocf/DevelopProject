package com.xhl.world.model;

/**
 * Created by Sum on 16/1/7.
 */
public class ScoreModel {
    private String userId;//用户id

    private String integral;//获得或使用的积分数

    private String getOrUse;//1获得 2使用

    private String source;//来源 或 用途 1：购买获得 2：购买使用

    private String createTime; //获得或使用的时间

    private String nowIntegral;//当前剩余积分

    private String totalIntegral; //当前总共的积分


    public String getScoreRes() {
        return source + "\n" + createTime;
    }

    public String getScoreNum() {
       /* if (getOrUse.equals("1")) {
            return "+" + integral;
        } else {
            return "-" + integral;
        }*/
        return integral;
    }

    public String getScoreLef() {
        return nowIntegral;
    }
}
