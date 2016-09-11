package com.moge.gege.model;

import java.io.Serializable;

public class TradingBaseModel implements Serializable
{
    private static final long serialVersionUID = 1L;

    private AttachmentModel attachments;
    private int discount_price;
    private int num;
    private String title;
    private int price;
    private String _id;
    private String author_uid;
    private String promotion_id;

    // persional
    private boolean isSelected;
    private int buyNum;

    // for order list
    private int status;
    private String statusDesc;
    private String orderTime;
    private boolean multiGoods;

    // for shoppingcart
    private int sale_num;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public AttachmentModel getAttachments() {
        return attachments;
    }

    public void setAttachments(AttachmentModel attachments) {
        this.attachments = attachments;
    }

    public int getDiscount_price() {
        return discount_price;
    }

    public void setDiscount_price(int discount_price) {
        this.discount_price = discount_price;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getAuthor_uid() {
        return author_uid;
    }

    public void setAuthor_uid(String author_uid) {
        this.author_uid = author_uid;
    }

    public String getPromotion_id() {
        return promotion_id;
    }

    public void setPromotion_id(String promotion_id) {
        this.promotion_id = promotion_id;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public int getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(int buyNum) {
        this.buyNum = buyNum;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public boolean isMultiGoods() {
        return multiGoods;
    }

    public void setMultiGoods(boolean multiGoods) {
        this.multiGoods = multiGoods;
    }

    public int getSale_num() {
        return sale_num;
    }

    public void setSale_num(int sale_num) {
        this.sale_num = sale_num;
    }
}
