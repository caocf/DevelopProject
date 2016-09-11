package com.ebox.st.model;

import java.util.ArrayList;

public class CheckWorkRsp extends BaseRspModel {

    private ArrayList<CheckWorkModel> result;

    public ArrayList<CheckWorkModel> getResult() {
        return result;
    }

    public void setResult(ArrayList<CheckWorkModel> result) {
        this.result = result;
    }

}
