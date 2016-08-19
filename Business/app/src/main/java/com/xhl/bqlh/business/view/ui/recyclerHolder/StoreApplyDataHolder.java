package com.xhl.bqlh.business.view.ui.recyclerHolder;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xhl.bqlh.business.Api.ApiControl;
import com.xhl.bqlh.business.Model.ApplyModel;
import com.xhl.bqlh.business.Model.Base.ResponseModel;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.view.base.Common.DefaultCallback;
import com.xhl.bqlh.business.view.helper.DialogMaker;
import com.xhl.bqlh.business.view.helper.pub.Callback.RecycleViewCallBack;
import com.xhl.bqlh.business.view.ui.activity.StoreApplyUpdateActivity;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;

/**
 * Created by Sum on 16/4/26.
 */
public class StoreApplyDataHolder extends RecyclerDataHolder<ApplyModel> {

    private RecycleViewCallBack callBack;

    public StoreApplyDataHolder(ApplyModel data, RecycleViewCallBack callBack) {
        super(data);
        this.callBack = callBack;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, ViewGroup parent, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_store_apply, null);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final Context context, final int position, RecyclerView.ViewHolder vHolder, final ApplyModel data) {
        final View content = vHolder.itemView;

        TextView tv_time = (TextView) content.findViewById(R.id.tv_apply_time_hint);
        tv_time.setText(data.getCreateTime());

        TextView tv_money = (TextView) content.findViewById(R.id.tv_apply_money);
        tv_money.setText(context.getString(R.string.product_num1, data.getAllProNum()));

        TextView tv_state = (TextView) content.findViewById(R.id.tv_apply_state);
        tv_state.setText(data.getStateDesc());

        View bg = content.findViewById(R.id.rl_details);
        bg.setBackgroundResource(data.getStateBg());

        content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data != null) {
                    Intent intent = new Intent(context, StoreApplyUpdateActivity.class);
                    intent.putExtra("data", data);
                    context.startActivity(intent);
                }
            }
        });
        content.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (data.getShstate() != 1) {
                    delete(context, data, position);
                }
                return true;
            }
        });
    }

    private void delete(Context context, final ApplyModel data, final int pos) {

        AlertDialog.Builder dialog = DialogMaker.getDialog(context);
        dialog.setCancelable(false);
        dialog.setTitle("删除");
        dialog.setMessage("您确定删除该装车单吗?");
        dialog.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (callBack != null) {
                    commit(data, pos);
                }
            }
        });
        dialog.setNegativeButton(R.string.dialog_cancel, null);
        dialog.show();
    }

    private void commit(ApplyModel model, final int pos) {

        ApiControl.getApi().storeApplyUpdate("3", model.getId(), new DefaultCallback<ResponseModel<Object>>() {
            @Override
            public void success(ResponseModel<Object> result) {
                if (callBack != null) {
                    callBack.onItemClick(pos);
                }
            }

            @Override
            public void finish() {

            }
        });
    }

}
