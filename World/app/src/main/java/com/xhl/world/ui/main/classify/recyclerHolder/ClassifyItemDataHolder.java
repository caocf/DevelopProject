package com.xhl.world.ui.main.classify.recyclerHolder;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.xhl.world.R;
import com.xhl.world.model.ClassifyItemModel;
import com.xhl.world.ui.main.home.HomeItemType;
import com.xhl.world.ui.utils.ViewUtils;
import com.xhl.world.ui.view.pub.callback.RecycleViewCallBack;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * Created by Sum on 15/12/2.
 */
public class ClassifyItemDataHolder extends RecyclerDataHolder {

    private RecycleViewCallBack mCallBack;

    public ClassifyItemDataHolder(ClassifyItemModel data) {
        super(data);
    }

    @Override
    public int getType() {
        return HomeItemType.Type_Brand;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, int position) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_classify, null);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-1, -2);

        view.setLayoutParams(params);


        AutoUtils.autoSize(view);

        return new ClassifyItemViewHolder(view, mCallBack);
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, Object data) {

        ClassifyItemViewHolder holder = (ClassifyItemViewHolder) vHolder;
        ClassifyItemModel mode = (ClassifyItemModel) data;
        //分类名称
        holder.tv_classify_item_name.setText(mode.getCategoryName());
        if (getHolderState() == HOLDER_SELECT) {
            holder.tv_classify_item_name.setTextColor(ContextCompat.getColor(context, R.color.app_while));
            holder.rl_content.setBackgroundColor( ContextCompat.getColor(context, R.color.colorAccent));
            ViewUtils.changeViewVisible(holder.line_hor,false);
        } else {
            holder.rl_content.setBackgroundColor(ContextCompat.getColor(context, R.color.app_while));
            holder.tv_classify_item_name.setTextColor(ContextCompat.getColor(context, R.color.base_dark_text_color));
            ViewUtils.changeViewVisible(holder.line_hor,true);
        }
    }


    /**
     * RecyclerView root view 事件回调
     *
     * @param callBack
     */
    public void setCallBack(RecycleViewCallBack callBack) {
        mCallBack = callBack;
    }
}
