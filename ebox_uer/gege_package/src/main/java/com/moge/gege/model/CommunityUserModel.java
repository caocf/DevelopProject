package com.moge.gege.model;

import java.io.Serializable;

public class CommunityUserModel extends BaseCommunityModel implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private int user_count;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getUser_count() {
        return user_count;
    }

    public void setUser_count(int user_count) {
        this.user_count = user_count;
    }
}
