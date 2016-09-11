package com.ebox.st.model;

import java.util.ArrayList;

/**
 * Created by mafeng on 2015/6/24.
 */
public class QueryWorkInfoRsp extends BaseRspModel {
    public ArrayList<QueryWorkInfoRspModel> getResult() {
        return result;
    }

    public void setResult(ArrayList<QueryWorkInfoRspModel> result) {
        this.result = result;
    }

    private ArrayList<QueryWorkInfoRspModel> result;

}
