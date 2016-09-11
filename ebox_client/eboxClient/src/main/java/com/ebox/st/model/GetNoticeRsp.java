package com.ebox.st.model;

import java.util.ArrayList;

public class GetNoticeRsp extends BaseRspModel {
    private ArrayList<GetNoticeRspModel> result ;
    public ArrayList<GetNoticeRspModel> getResult() {
        return result;
    }

    public void setResult(ArrayList<GetNoticeRspModel> order) {
        this.result= order;
    }

}
