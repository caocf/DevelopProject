package com.xhl.bqlh.model;

import android.text.TextUtils;

/**
 * Created by Summer on 2016/7/20.
 */
public class UserAccount {
    private String id;
    private String uid;
    private String accountNum;
    private String zhiFBAccount;
    private String accountBank;
    private String name;
    private String accountType;

    public String getAccountNum() {
        if (TextUtils.isEmpty(accountNum)) {
            return "";
        }
        return accountNum;
    }

    public String getZhiFBAccount() {
        if (TextUtils.isEmpty(zhiFBAccount)) {
            return "";
        }
        return zhiFBAccount;
    }
}
