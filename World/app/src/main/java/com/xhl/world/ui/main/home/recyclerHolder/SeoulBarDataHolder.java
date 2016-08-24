package com.xhl.world.ui.main.home.recyclerHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xhl.world.R;
import com.xhl.world.model.AdvModel;
import com.xhl.world.ui.view.pub.callback.RecycleViewCallBack;
import com.xhl.xhl_library.network.ImageLoadManager;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * Created by Sum on 15/11/27.
 * <p/>
 * 一个数据对象
 * 仅显示一张图
 */
public class SeoulBarDataHolder extends RecyclerDataHolder {

    private RecycleViewCallBack callBack;

    public SeoulBarDataHolder(AdvModel data) {
        super(data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, int position) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_seoul_imageview, null);

        int height = context.getResources().getDimensionPixelOffset(R.dimen.seoul_image_view_height);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, height);
     /*  params.topMargin = 8;
        params.bottomMargin = 8;
        params.leftMargin = 8;
        params.rightMargin = 8;*/

        view.setLayoutParams(params);

        AutoUtils.auto(view);

        return new SeoulBarViewHolder(view, callBack);
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, Object data) {

        SeoulBarViewHolder holder = (SeoulBarViewHolder) vHolder;

        ImageLoadManager.instance().LoadImage(holder.imageView, ((AdvModel)data).getImageUrl());
    }

    /**
     * RecyclerView root view 事件回调
     *
     * @param callBack
     */
    public void setCallBack(RecycleViewCallBack callBack) {
        this.callBack = callBack;
    }

   static class SeoulBarViewHolder extends RecyclerViewHolder implements View.OnClickListener {

        private RecycleViewCallBack mCallBack;

        public ImageView imageView;

        public SeoulBarViewHolder(View view, RecycleViewCallBack callBack) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.iv_seoul);
            mCallBack = callBack;
            imageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int layoutPosition = getAdapterPosition();
            if (mCallBack != null) {
                mCallBack.onItemClick(layoutPosition);
            }
        }
    }
}
