package com.xhl.bqlh.business.view.ui.recyclerHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xhl.bqlh.business.Model.ShopDisplayModel;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.view.custom.LifeCycleImageView;
import com.xhl.bqlh.business.view.helper.ViewHelper;
import com.xhl.bqlh.business.view.helper.pub.Callback.RecycleViewCallBack;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;

/**
 * Created by Sum on 16/6/3.
 */
public class ShopDisplayDataHolder extends RecyclerDataHolder<ShopDisplayModel> {

    private RecycleViewCallBack callBack;

    public ShopDisplayDataHolder(ShopDisplayModel data) {
        super(data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, ViewGroup parent, int position) {
        return new DisplayViewHolder(View.inflate(context, R.layout.item_shop_display, null));
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, ShopDisplayModel data) {
        DisplayViewHolder holder = (DisplayViewHolder) vHolder;
        holder.onBindData(data, position);
    }

    public void setCallBack(RecycleViewCallBack callBack) {
        this.callBack = callBack;
    }

    class DisplayViewHolder extends RecyclerViewHolder implements View.OnClickListener {

        private LifeCycleImageView imageView;
        private TextView tv_time;
        private TextView tv_remark;

        private int baseHeight;

        public DisplayViewHolder(View view) {
            super(view);
            imageView = (LifeCycleImageView) view.findViewById(R.id.image);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            tv_remark = (TextView) view.findViewById(R.id.tv_remark);
            baseHeight = mContext.getResources().getDimensionPixelOffset(R.dimen.display_default_height);
            view.setOnClickListener(this);
        }

        public void onBindData(ShopDisplayModel displayModel, int pos) {
            if (displayModel != null) {
                String imageUrl = displayModel.getImageUrl();
                if (!TextUtils.isEmpty(imageUrl)) {
                    ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
                    layoutParams.height = baseHeight + (pos % 3) * 60;
                    imageView.setLayoutParams(layoutParams);
                    imageView.bindImageUrl(imageUrl);
                }
                tv_time.setText(displayModel.getShowTime());
                String showRemark = displayModel.getShowRemark();
                if (!TextUtils.isEmpty(showRemark)) {
                    ViewHelper.setViewGone(tv_remark, false);
                    tv_remark.setText(showRemark);
                } else {
                    ViewHelper.setViewGone(tv_remark, true);
                }
            }
        }

        @Override
        public void onClick(View v) {
            if (callBack != null) {
                callBack.onItemClick(getAdapterPosition());
            }
        }
    }

}
