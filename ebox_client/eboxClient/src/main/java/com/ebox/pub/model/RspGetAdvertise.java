package com.ebox.pub.model;

import com.ebox.ex.network.model.base.type.AdvertiseClass;

/**
 * Created by mafeng on 2015/6/10.
 * 获取adv.json 解析后的数据结构
 */
public class RspGetAdvertise {

    private AdvertiseClass[] adverClass;

    public void setAdverClass(AdvertiseClass[] adverClass){
        this.adverClass = adverClass;
    }

    public AdvertiseClass[] getAdverClass(){
        return adverClass;
    }

}
