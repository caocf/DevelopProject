package com.xhl.world.mvp.domain;

import com.xhl.world.model.Base.ResponseModel;
import com.xhl.world.mvp.domain.back.ActionCallBack;
import com.xhl.world.model.FlashSaleItemDetailsModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sum on 15/12/14.
 */
public class FlashSaleUserCaseController implements FlashSaleUserCase<ResponseModel<FlashSaleItemDetailsModel>> {

    private int mCurPage = 1;
    private final int mPageSize = 20;
    private boolean isLoading = false;

    private ActionCallBack<ResponseModel<FlashSaleItemDetailsModel>> mCallBack;

    public FlashSaleUserCaseController() {

    }

    @Override
    public void refreshTop() {
        mCurPage = 1;
    }

    @Override
    public void refreshBottom() {
        mCurPage += 1;
    }


    /**
     * 执行动作
     */
    @Override
    public void execute() {
        if (!isLoading) {
            isLoading = true;
            List<FlashSaleItemDetailsModel> testData = getTestData();

            ResponseModel<FlashSaleItemDetailsModel> mode = new ResponseModel<>();

            mCallBack.onSuccess(mode);
            isLoading = false;
        }
        //AppActionImpl.instance().getFlashSaleDetailsAction("",mCallBack);

    }


    private List<FlashSaleItemDetailsModel> getTestData() {
        ArrayList<FlashSaleItemDetailsModel> models = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            FlashSaleItemDetailsModel model = new FlashSaleItemDetailsModel();
            models.add(model);
        }
        return models;
    }

    @Override
    public void callBack(ActionCallBack<ResponseModel<FlashSaleItemDetailsModel>> callBack) {
        mCallBack = callBack;
    }
}
