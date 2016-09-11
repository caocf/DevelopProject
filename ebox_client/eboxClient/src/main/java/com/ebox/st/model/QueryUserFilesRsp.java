package com.ebox.st.model;

import java.util.List;

public class QueryUserFilesRsp extends BaseRspModel {
    private List<IdentiOrderModel> result;



    public List<IdentiOrderModel> getOrder() {
        return result;
    }

    public void setOrder(List<IdentiOrderModel> order) {
        this.result= order;
    }
}
