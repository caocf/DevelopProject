package com.xhl.world.model;

import com.xhl.world.config.NetWorkConfig;

import java.util.List;

/**
 * Created by Sum on 16/2/2.
 */
public class LogisticsModel {
    private String company;
    private String flag;
    private String logisticsNo;
    private String productPic;//商品图片，主动添加
    private List<LogisticsDelivery> delivery;

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getProductPic() {
        return NetWorkConfig.imageHost + productPic;
    }

    public void setProductPic(String productPic) {
        this.productPic = productPic;
    }

    public List<LogisticsDelivery> getDelivery() {
        return delivery;
    }

    public String getLogisticsNo() {
        return logisticsNo;
    }
}
