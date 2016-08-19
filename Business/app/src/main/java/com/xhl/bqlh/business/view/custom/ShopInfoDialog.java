package com.xhl.bqlh.business.view.custom;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;

import com.xhl.bqlh.business.Api.ApiControl;
import com.xhl.bqlh.business.AppConfig.GlobalParams;
import com.xhl.bqlh.business.Model.Base.ResponseModel;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.utils.ToastUtil;
import com.xhl.bqlh.business.view.helper.DialogMaker;
import com.xhl.bqlh.business.view.ui.activity.CustomerUpdateActivity;

import org.xutils.common.Callback;

/**
 * Created by Sum on 16/5/30.
 */
public class ShopInfoDialog {

    private Context context;
    private BottomSheetDialog dialog;
    private String shopId;

    public ShopInfoDialog(Context context, String shopId) {
        this.context = context;
        this.shopId = shopId;
        dialog = new BottomSheetDialog(context);
        initView();
    }

    private void initView() {
        View rootView = View.inflate(context, R.layout.dialog_shop_info, null);
        View shopInfo = rootView.findViewById(R.id.btn_shop_info_error);
        shopInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shopUpdate();
            }
        });

        View shopClose = rootView.findViewById(R.id.btn_shop_close);
        shopClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shopClose();
            }
        });

        View dialogClose = rootView.findViewById(R.id.btn_dialog_close);
        dialogClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });

        dialog.setContentView(rootView);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
    }

    private void shopUpdate() {
        Intent intent = new Intent(context, CustomerUpdateActivity.class);
        intent.putExtra(CustomerUpdateActivity.TAG_UPDATE_TYPE, CustomerUpdateActivity.TYPE_GET_INFO);
        intent.putExtra(GlobalParams.Intent_shop_id, shopId);
        context.startActivity(intent);
    }

    private void shopClose() {
        dialogCloseShop();
    }

    private void dialogCloseShop() {
        AlertDialog.Builder dialog = DialogMaker.getDialog(context);
        dialog.setTitle(R.string.menu_shop_close);
        dialog.setMessage("您确定该店铺已经关闭?");
        dialog.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                closeShop();
            }
        });
        dialog.setNegativeButton(R.string.dialog_cancel, null);
        dialog.show();
    }

    private void closeShop() {
        if (!TextUtils.isEmpty(shopId)) {
            ApiControl.getApi().shopClose(shopId, new Callback.CommonCallback<ResponseModel<String>>() {
                @Override
                public void onSuccess(ResponseModel<String> result) {
                    if (result.isSuccess()) {
                        ToastUtil.showToastShort(result.getObj());
                        close();
                    } else {
                        ToastUtil.showToastShort(result.getMessage());
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    ToastUtil.showToastShort(ex.getMessage());
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {

                }
            });
        }

    }

    public void show() {
        if (dialog != null) {
            dialog.show();
        }
    }

    public void close() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
