package com.ebox.st.model;

import java.util.ArrayList;

/**
 * Created by mafeng on 2015/6/25.
 */
public class TakeCertificationRsp extends BaseRspModel {
    public ArrayList<TakeCertificationRspModel> getResult() {
        return result;
    }

    public void setResult(ArrayList<TakeCertificationRspModel> result) {
        this.result = result;
    }

    private  ArrayList<TakeCertificationRspModel> result;
}
