package com.xhl.bqlh.business.Model.Type;

/**
 * Created by Summer on 2016/7/28.
 */
public interface OrderReturnType {

    //1-待审核；2-仓管预审通过；3-仓管预审不通过；4-仓管确认入库；5-仓库拒绝入库；6-财务审核通过；7-财务审核不通过

    public static final int TYPE_APPLY = 1;//待审核
    public static final int TYPE_HANDLE = 2;//处理中
    public static final int TYPE_FINISH = 3;//完成

}
