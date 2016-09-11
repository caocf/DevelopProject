package com.ebox.ex.network.model.req;

import com.ebox.ex.network.model.base.BaseRsp;
import com.ebox.ex.network.model.base.type.NotesType;

/**
 * Created by prin on 2015/11/2.
 */
public class RspGetNote extends BaseRsp {

    private NotesType data;

    public NotesType getData() {
        return data;
    }

    public void setData(NotesType data) {
        this.data = data;
    }
}
