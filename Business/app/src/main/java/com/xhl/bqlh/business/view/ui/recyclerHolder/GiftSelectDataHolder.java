package com.xhl.bqlh.business.view.ui.recyclerHolder;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.xhl.bqlh.business.Model.GiftModel;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.utils.ToastUtil;
import com.xhl.bqlh.business.view.custom.LifeCycleImageView;
import com.xhl.bqlh.business.view.helper.DialogMaker;
import com.xhl.bqlh.business.view.helper.ViewHelper;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by Summer on 2016/7/27.
 */
public class GiftSelectDataHolder extends RecyclerDataHolder<GiftModel> {

    public GiftSelectDataHolder(GiftModel data) {
        super(data);
    }

    @Override
    public int getType() {
        return 2;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, ViewGroup parent, int position) {

        View view = View.inflate(context, R.layout.item_gift_product_select, null);

        return new GiftViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, GiftModel data) {
        GiftViewHolder holder = (GiftViewHolder) vHolder;
        holder.onBindData(data);
    }

    static class GiftViewHolder extends RecyclerViewHolder {

        @ViewInject(R.id.iv_image)
        private LifeCycleImageView iv_image;

        @ViewInject(R.id.tv_product_name)
        private TextView tv_product_name;

        @ViewInject(R.id.tv_product_max)
        private TextView tv_product_max;

        @ViewInject(R.id.tv_product_numb)
        private TextView tv_product_numb;

        @ViewInject(R.id.btn_reduce)
        private View btn_reduce;


        @Event(R.id.tv_product_numb)
        private void onNumClick(View view) {
            inputNum();
        }

        @Event(R.id.btn_reduce)
        private void onReduceClick(View view) {
            int giftNum = mData.getGiftNum();
            if (giftNum > 0) {
                mData.setGiftNum(--giftNum);
                update();
            }
        }

        @Event(R.id.btn_add)
        private void onAddClick(View view) {
            int giftNum = mData.getGiftNum();
            if (giftNum < mData.getStock()) {
                mData.setGiftNum(++giftNum);
                update();
            }
        }

        private GiftModel mData;

        public GiftViewHolder(View view) {
            super(view);
            x.view().inject(this, view);
        }

        public void onBindData(GiftModel data) {
            mData = data;
            iv_image.bindImageUrl(data.getImage());
            tv_product_name.setText(data.getGiftName());
            String num = "剩余数量:" + data.getStock();
            tv_product_max.setText(num);
            update();
        }

        private void update() {
            tv_product_numb.setText(String.valueOf(mData.getGiftName()));
            if (mData.getGiftNum() == 0) {
                ViewHelper.setViewGone(btn_reduce, true);
                ViewHelper.setViewGone(tv_product_numb, true);
            } else {
                ViewHelper.setViewGone(btn_reduce, false);
                ViewHelper.setViewGone(tv_product_numb, false);
            }
        }

        private void inputNum() {
            AlertDialog.Builder dialog = DialogMaker.getDialog(mContext);
            dialog.setTitle("修改数量");
            View content = View.inflate(mContext, R.layout.dialog_input_num, null);

            final EditText input = (EditText) content.findViewById(R.id.ed_num);
            final TextView tv_num = (TextView) content.findViewById(R.id.tv_product_stock);

            tv_num.setText(String.valueOf(mData.getStock()));
            String num = String.valueOf(mData.getGiftNum());
            input.setText(num);
            input.setSelection(num.length());

            dialog.setView(content);
            dialog.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String num = input.getText().toString();
                    if (!TextUtils.isEmpty(num)) {
                        Integer valueOf = Integer.valueOf(num);
                        if (valueOf > mData.getStock()) {
                            ToastUtil.showToastLong("超出赠品数量");
                        } else {
                            mData.setGiftNum(valueOf);
                            update();
                        }
                    }
                }
            });
            dialog.setNegativeButton(R.string.dialog_cancel, null);
            dialog.setCancelable(false);
            dialog.show();
        }
    }
}
