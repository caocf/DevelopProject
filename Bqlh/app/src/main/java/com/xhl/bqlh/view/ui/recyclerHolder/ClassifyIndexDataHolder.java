package com.xhl.bqlh.view.ui.recyclerHolder;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xhl.bqlh.R;
import com.xhl.bqlh.model.ClassifyModel;
import com.xhl.bqlh.view.helper.ViewHelper;
import com.xhl.bqlh.view.helper.pub.Callback.RecycleViewCallBack;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * Created by Sum on 16/4/7.
 * 商品分类导航
 */
public class ClassifyIndexDataHolder extends RecyclerDataHolder<ClassifyModel> {

    private RecycleViewCallBack mCallBack;
    public ClassifyIndexDataHolder(ClassifyModel data) {
        super(data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, ViewGroup parent, int position) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_classify_index, null);
        view.setLayoutParams(new ViewGroup.LayoutParams(-1, 96));
        AutoUtils.auto(view);
        return new ProductParentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, ClassifyModel data) {
        ProductParentViewHolder holder = (ProductParentViewHolder) vHolder;
        String categoryName = data.getCategoryAppName();
        holder.mTvName.setText(categoryName);

      if (getHolderState() == HOLDER_SELECT) {
            holder.mTvName.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
            holder.mBgView.setBackgroundColor(ContextCompat.getColor(context, R.color.app_background));
            ViewHelper.setViewGone(holder.mLine, true);
        } else {
            holder.mBgView.setBackgroundColor(ContextCompat.getColor(context, R.color.app_while));
            holder.mTvName.setTextColor(ContextCompat.getColor(context, R.color.text_light_color));
            ViewHelper.setViewGone(holder.mLine, false);
        }
    }

    public void setCallBack(RecycleViewCallBack callBack) {
        mCallBack = callBack;
    }

    private class ProductParentViewHolder extends RecyclerViewHolder implements View.OnClickListener {

        public TextView mTvName;
        public View mBgView;
        public View mLine;

        public ProductParentViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            mTvName = (TextView) view.findViewById(R.id.tv_product_classify_name);
            mBgView = view.findViewById(R.id.ll_content);
            mLine = view.findViewById(R.id.line_ver);
        }

        @Override
        public void onClick(View v) {
            if (mCallBack != null) {
                mCallBack.onItemClick(getAdapterPosition(),null);
            }
        }
    }
}
