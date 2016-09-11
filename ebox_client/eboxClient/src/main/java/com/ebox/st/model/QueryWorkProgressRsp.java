package com.ebox.st.model;

import java.util.ArrayList;

/**
 * Created by mafeng on 2015/6/25.
 */
public class QueryWorkProgressRsp extends BaseRspModel {
    private ArrayList<StateModel> result;

    public ArrayList<StateModel> getResult() {
        return result;
    }

    public void setResult(ArrayList<StateModel> result) {
        this.result = result;
    }
}
