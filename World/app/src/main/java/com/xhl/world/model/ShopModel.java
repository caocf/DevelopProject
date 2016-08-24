package com.xhl.world.model;

import android.text.TextUtils;

import com.xhl.world.config.NetWorkConfig;

/**
 * Created by Sum on 16/1/9.
 */
public class ShopModel {

    /**
     * "sid": "ef83638efd4f46f3a792a8b79914f6ee",
     * "state": null,
     * "typeId": null,
     * "uid": "91cbfffcb6b14483a5bed0d7a324d375",
     * "templateId": null,
     * "templateNo": "previewShop",
     * "shopName": "test",
     * "shopLogo": "/v1/tfs/T1bahTBydT1RCvBVdK",
     * "shopStar": 1,
     * "applyTime": null,
     * "info": null,
     * "shopUrl": "http://58.213.106.82:9090/SHZC/shops/shopIndex?dealerId=f1c132c4133b45b1b4f433932184c217&shopId=ef83638efd4f46f3a792a8b79914f6ee",
     * "createTime": null,
     * "endTime": null,
     * "remark": null,
     * "bail": 0,
     * "operateStatus": null,
     * "createTimeUp": null,
     * "createTimeDown": null,
     * "industyRegisterName": null,
     * "liableName": null,
     * "liablePhone": null,
     * "liableMail": null,
     * "companyAddress": null,
     * "area": null,
     * "region": null,
     * "businessLicence": null,
     * "taxRegistrationCertificate": null,
     * "organizationCode": null,
     * "processId": null,
     * "page": null
     */
    private String sid;
    private String state;
    private String uid;
    private String shopName;
    private String shopLogo;
    private String shopStar;
    private String shopUrl;
    private String info;//店铺介绍
    private String applyTime;//店铺申请时间
    private String createTime;//店铺创建时间
    private String endTime;//店铺关闭时间
    private String operateStatus;//
    private String bail;//保证金 0:未交 1：已交

    public String getShopId(){
        return sid;
    }

    public String getShopLogo() {
        return NetWorkConfig.imageHost + shopLogo;
    }

    public String getShopName() {
        if (TextUtils.isEmpty(shopName))
            return "";
        return shopName;
    }

    public String getShopUrl() {
        return shopUrl;
    }

}
