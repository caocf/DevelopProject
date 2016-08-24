package com.xhl.world.ui.event;

import com.xhl.world.model.AddressModel;

/**
 * Created by Sum on 15/12/28.
 */
public class AddressEvent {

    public static final int Type_Default_address = 0; // 默认选中事件
    public static final int Type_delete_address = 1;//删除事件
    public static final int Type_edit_address = 2;//编辑事件
    public static final int Type_select_address = 3;//选中一项事件

    public int position;
    public int actionType = -1;
    public AddressModel model;

}
