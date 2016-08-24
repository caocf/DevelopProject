package com.xhl.world.chat;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Sum on 16/2/24.
 */
@Table(name = "push_message")
public class PushMessage {

    @Column(name = "id", isId = true)
    private int id;
    /**
     * 1：基本消息，2：商品详情，3：店铺详情，4：广告Url
     */
    @Column(name = "pushType")
    private String pushType;


    @Column(name = "pushTitle")
    private String pushTitle;//标题

    @Column(name = "pushTicker")
    private String pushTicker;//Ticker标题

    @Column(name = "pushContent")
    private String pushContent;//内容

    @Column(name = "pushTime")
    private String pushTime;//时间
    /**
     * 推送的扩展属性，根据pushType填写不通的值
     * 1:可不写
     * 2：商品的id
     * 3：店铺的id
     * 4：广告的url
     */
    @Column(name = "pushAttr")
    private String pushAttr;

    @Column(name = "data1")
    private String data1;//扩展字段

    @Column(name = "data2")
    private String data2;//扩展字段

    @Column(name = "data3")
    private String data3;//扩展字段


    public String getPushType() {
        return pushType;
    }

    public void setPushType(String pushType) {
        this.pushType = pushType;
    }

    public String getPushTitle() {
        return pushTitle;
    }

    public void setPushTitle(String pushTitle) {
        this.pushTitle = pushTitle;
    }

    public String getPushContent() {
        return pushContent;
    }

    public void setPushContent(String pushContent) {
        this.pushContent = pushContent;
    }

    public String getPushAttr() {
        return pushAttr;
    }

    public void setPushAttr(String pushAttr) {
        this.pushAttr = pushAttr;
    }

    public String getPushTime() {
        return pushTime;
    }

    public void setPushTime(String pushTime) {
        this.pushTime = pushTime;
    }

    public String getPushTicker() {
        return pushTicker;
    }

    public void setPushTicker(String pushTicker) {
        this.pushTicker = pushTicker;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
