/**
 * wechatdonal
 */
package com.moge.ebox.phone.bettle.model;

import tools.AppContext;

/**
 * Ebox
 *
 * @author John
 */
public class UserInfo extends BaseEntity<UserInfo> {

    public String operator_id;
    public String operator_name;
    public String telephone;
    public String orgnization_id;
    public String orgnization_name;
    public String username;
    public String password;
    public String status;
    public String balance;
    public String reserve_status;
    public String dot_id;//网点
    public String account_id;
    public String sessionId;
    public String path;//头像路径
    public String identity;//身份证
    public String app_status;
    public String channel;
    public String create_at;
    public String face_id;
    public String region_id;
    public String remark;
    public String site_loc;
    public String site_name;
    public String station_id;
    public String update_at;
//    public GegeInfo gege;

    public String getSrc(AppContext appContext) {
        return this.getFieldValue(appContext, "path");
    }

    public String getTelephone(AppContext appContext) {
        return this.getFieldValue(appContext, "telephone");
    }

    public String getUsername(AppContext appContext) {
        return this.getFieldValue(appContext, "username");
    }

    public String getPassword(AppContext appContext) {
        return this.getFieldValue(appContext, "password");
    }

    public String getOperatorId(AppContext appContext) {
        return this.getFieldValue(appContext, "operator_id");
    }

    public String getOperatorName(AppContext appContext) {
        return this.getFieldValue(appContext, "operator_name");
    }

    public String getBalance(AppContext appContext) {
        return this.getFieldValue(appContext, "balance");
    }

    public String getReserveStatus(AppContext appContext) {
        return this.getFieldValue(appContext, "reserve_status");
    }
}
