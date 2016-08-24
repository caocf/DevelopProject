package com.xhl.world.ui.main.classify.recyclerHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xhl.world.R;
import com.xhl.world.model.ClassifyItemModel;
import com.xhl.world.ui.event.EventBusHelper;
import com.xhl.world.ui.event.SearchItemEvent;
import com.xhl.world.ui.view.LifeCycleImageView;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * Created by Sum on 15/12/2.
 */
public class ClassifyDetailsItemDataHolder extends RecyclerDataHolder {

    public ClassifyDetailsItemDataHolder(ClassifyItemModel data) {
        super(data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_classify_details_child, null);

        int margin = context.getResources().getDimensionPixelOffset(R.dimen.px_dimen_20);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -2);
        params.leftMargin = margin;
        params.topMargin = margin;
        params.rightMargin = margin;

        view.setLayoutParams(params);

        AutoUtils.auto(view);

        return new ClassifyDetailsItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, Object data) {
        ClassifyDetailsItemViewHolder holder = (ClassifyDetailsItemViewHolder) vHolder;
        final ClassifyItemModel model = (ClassifyItemModel) data;
        holder.onBindData(model);
    }

    static class ClassifyDetailsItemViewHolder extends RecyclerViewHolder implements View.OnClickListener {


        public TextView tv_classify_item_child_name;
        public LifeCycleImageView iv_classify_item_child_image;
        private ClassifyItemModel mData;

        public ClassifyDetailsItemViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            iv_classify_item_child_image = (LifeCycleImageView) view.findViewById(R.id.iv_classify_item_child_image);
            tv_classify_item_child_name = (TextView) view.findViewById(R.id.tv_classify_item_child_name);
        }

        public void onBindData(ClassifyItemModel data) {
            if (data == null) {
                return;
            }
            mData = data;
            iv_classify_item_child_image.bindImageUrl(data.getImgUrl());
            tv_classify_item_child_name.setText(data.getCategoryName());
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            //搜索分类id
            EventBusHelper.postSearchContent(SearchItemEvent.Type_Classify, mData.getId());
        }
    }

}
