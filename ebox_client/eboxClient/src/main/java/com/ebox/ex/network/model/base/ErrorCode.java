package com.ebox.ex.network.model.base;

/**
 * Created by Android on 2015/9/6.
 */
public interface ErrorCode {

    /*0   成功
    200	逻辑错误
    300	没有权限
    400	未找到服务
    500	服务器错误*/

    /**
     * 成功
     */
    public static final int Code_Success = 0;
    /**
     * 逻辑错误
     */
    public static final int Code_200 = 200;
    /**
     * 没有权限
     */
    public static final int Code_300 = 300;
    /**
     * 服务未找到
     */
    public static final int Code_400 = 400;
    /**
     * 服务器错误
     */
    public static final int Code_500 = 500;
}
