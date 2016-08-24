package com.xhl.world.mvp.presenters;

import com.xhl.world.model.FlashSaleItemDetailsModel;
import com.xhl.world.model.Base.ResponseModel;
import com.xhl.world.mvp.views.FlashSaleView;
import com.xhl.world.mvp.domain.FlashSaleUserCase;
import com.xhl.world.mvp.domain.back.ActionCallBack;
import com.xhl.world.mvp.domain.back.ErrorEvent;

/**
 * Created by Sum on 15/12/12.
 */
public class FlashSalePresenter extends Presenter implements ActionCallBack<ResponseModel<FlashSaleItemDetailsModel>> {

    private FlashSaleUserCase flashSaleUserCase;

    private FlashSaleView<FlashSaleItemDetailsModel> flashSaleView;

    private boolean isRestData = true;

    public FlashSalePresenter(FlashSaleUserCase flashSaleUserCase) {
        this.flashSaleUserCase = flashSaleUserCase;

        this.flashSaleUserCase.callBack(this);
    }

    public void attachView(FlashSaleView flashSaleView) {
        this.flashSaleView = flashSaleView;
    }

    public void refreshTop() {
        isRestData = true;
        flashSaleUserCase.refreshTop();
        flashSaleUserCase.execute();
    }

    public void refreshBottom() {
        isRestData = false;
        //设置参数
        flashSaleUserCase.refreshBottom();
        //执行请求
        flashSaleUserCase.execute();
    }

    @Override
    public void onStart() {

        if (flashSaleView == null) {
            throw new RuntimeException("attach view is null");
        }

        if (flashSaleView.isFirstLoading()) {

            flashSaleView.showLoadingView();

            flashSaleUserCase.execute();
        }
    }

    @Override
    public void onStop() {

    }

    /**
     *  用户动作成功回调
     *
     * @param data
     */
    @Override
    public void onSuccess(ResponseModel<FlashSaleItemDetailsModel> data) {

        if (flashSaleView.isFirstLoading()) {
            //隐藏加载View
            flashSaleView.hideLoadingView();
        }
      /*  if (isRestData) {
            flashSaleView.setLoadData(data.getObjList());
        } else {
            flashSaleView.appendLoadData(data.getObjList());
        }*/
    }

    /**
     * 用户动作失败回调
     *
     * @param errorType
     * @param errorMsg
     */
    @Override
    public void onFailed(String errorType, String errorMsg) {

        if (flashSaleView.isFirstLoading()) {
            //隐藏加载View
            flashSaleView.hideLoadingView();
        }

        if (errorType.equals(ErrorEvent.NETWORK_ERROR)) {
            //网络异常显示
            flashSaleView.showReLoadView();
        }
    }
}
