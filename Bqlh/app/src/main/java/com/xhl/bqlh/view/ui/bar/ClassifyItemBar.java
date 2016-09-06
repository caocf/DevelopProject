package com.xhl.bqlh.view.ui.bar;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xhl.bqlh.AppConfig.GlobalParams;
import com.xhl.bqlh.R;
import com.xhl.bqlh.model.ClassifyModel;
import com.xhl.bqlh.view.base.BaseBar;
import com.xhl.bqlh.view.custom.LifeCycleImageView;
import com.xhl.bqlh.view.ui.activity.SearchProductResActivity;
import com.xhl.xhl_library.ui.recyclerview.RecyclerAdapter;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;
import com.xhl.xhl_library.ui.recyclerview.layoutManager.FullGridViewManager;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sum on 16/7/2.
 */
public class ClassifyItemBar extends BaseBar {
    public ClassifyItemBar(Context context) {
        super(context);
    }

    public ClassifyItemBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private TextView mClassifyName;

    private RecyclerView mRecyclerView;
    private RecyclerAdapter mRecyclerAdapter;

    private ClassifyModel mClassify;

    @Override
    protected boolean autoInject() {
        return false;
    }

    @Override
    protected void initParams() {
        mClassifyName = _findViewById(R.id.classify_title);
        mRecyclerView = _findViewById(R.id.recycler_view);

        GridLayoutManager manager = new FullGridViewManager(getContext(), 3);
        mRecyclerAdapter = new RecyclerAdapter(getContext());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mRecyclerAdapter);
    }

    //使用RecyclerView缓存来显示数据，不会重复加载
    public void onBindData(ClassifyModel classify) {
        if (classify == null) {
            return;
        }
        //相同数据返回
        if (mClassify != null && mClassify == classify) {
            return;
        }
        mClassify = classify;
        String categoryAppName = classify.getCategoryAppName();
        if (!TextUtils.isEmpty(categoryAppName)) {
            mClassifyName.setText(categoryAppName);
        }
        //三级分类
        ArrayList<ClassifyModel> children = classify.getChildren();
        List<RecyclerDataHolder> holders = new ArrayList<>();
        for (int i = 0; i < children.size(); i++) {
            ClassifyModel model = children.get(i);
            model.shopId = classify.shopId;
            holders.add(new ItemProductDataHolder(model, i));
        }
        mRecyclerAdapter.setDataHolders(holders);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.bar_classify_item;
    }


    private static class ItemProductDataHolder extends RecyclerDataHolder<ClassifyModel> {
        private int type = 0;

        public ItemProductDataHolder(ClassifyModel data, int type) {
            super(data);
            this.type = type;
        }

        @Override
        public int getType() {
            return 10;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(Context context, ViewGroup parent, int position) {
            View view = View.inflate(context, R.layout.item_classify_child_info, null);
            view.setLayoutParams(new ViewGroup.LayoutParams(-1, 180));
            AutoUtils.auto(view);
            return new IProductView(view);
        }

        @Override
        public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, ClassifyModel data) {
            IProductView view = (IProductView) vHolder;
            view.onBindData(data);
        }

        static class IProductView extends RecyclerViewHolder implements OnClickListener {

            private ImageView image;
            private TextView name;
            ClassifyModel mC;

            public IProductView(View view) {
                super(view);
                image = (ImageView) view.findViewById(R.id.iv_image);
                view.setOnClickListener(this);
                image.setOnClickListener(this);
                name = (TextView) view.findViewById(R.id.tv_name);
            }

            public void onBindData(ClassifyModel data) {
                mC = data;
//                image.bindImageUrl(data.getImgUrl());
                LifeCycleImageView.LoadImageView(image, data.getImgUrl());
                if (!TextUtils.isEmpty(data.getCategoryAppName())) {
                    name.setText(data.getCategoryAppName());
                }
            }

            @Override
            public void onClick(View v) {
                if (mC != null && !TextUtils.isEmpty(mC.getId())) {
                    Intent intent = new Intent(mContext, SearchProductResActivity.class);
                    intent.putExtra(SearchProductResActivity.SEARCH_TYPE, SearchProductResActivity.SEARCH_TYPE_CATEGORY);
                    intent.putExtra(SearchProductResActivity.SEARCH_PARAMS, mC.getId());
                    intent.putExtra(GlobalParams.Intent_shop_id, mC.shopId);
                    mContext.startActivity(intent);
                }
            }
        }
    }
}
