package com.ebox.ex.network.model.base;

import com.ebox.pub.utils.LogUtil;

/**
 * Created by Android on 2015/8/31.
 */
public class BaseRsp implements ErrorCode{

    private int status;

    private String msg;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess(){

        boolean isSuccess=false;

        if (status==Code_Success)
        {
            isSuccess=true;
        }
        else if (status==Code_200)
        {
            LogUtil.e("BaseRsp","code 200 逻辑错误");
        }
        else if (status==Code_300)
        {
            LogUtil.e("BaseRsp","code 300 没用权限");
        }
        else if (status==Code_400)
        {
            LogUtil.e("BaseRsp","code 400 服务未找到");
        }
        else if (status==Code_500)
        {
            LogUtil.e("BaseRsp","code 500 服务器错误");
        }
        return  isSuccess;
    }
}
