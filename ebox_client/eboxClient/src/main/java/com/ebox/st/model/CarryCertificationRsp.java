package com.ebox.st.model;

import java.util.ArrayList;

/**
 * Created by mafeng on 2015/6/24.
 */
public class CarryCertificationRsp extends BaseRspModel {
    public ArrayList<CarryCertificationRspModel> getResult() {
        return result;
    }

    public void setResult(ArrayList<CarryCertificationRspModel> result) {
        this.result = result;
    }

    private ArrayList<CarryCertificationRspModel> result;
}
