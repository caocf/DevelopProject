package com.xhl.world.ui.main.classify.bar;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.TextView;

import com.xhl.world.R;
import com.xhl.world.api.ApiControl;
import com.xhl.world.model.Base.ResponseModel;
import com.xhl.world.model.ClassifyItemModel;
import com.xhl.world.ui.main.classify.recyclerHolder.ClassifyDetailsItemDataHolder;
import com.xhl.world.ui.view.pub.BaseBar;
import com.xhl.xhl_library.ui.recyclerview.RecyclerAdapter;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.layoutManager.FullGridViewManager;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sum on 15/12/2.
 */
public class ClassifyDetailsItemBar extends BaseBar {


    @ViewInject(R.id.bar_classify_title)
    private TextView mTvTitle;

    @ViewInject(R.id.bar_classify_recycler_view)
    private RecyclerView mRecyclerView;

    private RecyclerAdapter mRecyclerAdapter;

    private ClassifyItemModel mClassifyItemModel;

    private boolean mIsLoadFinish = false;//首次加载完成表示
    private boolean mIsLoading = false;

    public ClassifyDetailsItemBar(Context context) {
        super(context);
    }

    public ClassifyDetailsItemBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initParams() {

        GridLayoutManager manager = new FullGridViewManager(getContext(), 3);
        manager.offsetChildrenHorizontal(20);
        manager.offsetChildrenVertical(20);
        mRecyclerAdapter = new RecyclerAdapter(getContext());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mRecyclerAdapter);
    }

    /**
     * 详情页面一项数据结构设置
     *
     * @param model
     */
    public void setClassifyItemData(ClassifyItemModel model) {

        if (mClassifyItemModel == model) {
            return;
        }

        mClassifyItemModel = model;
        if (!mIsLoadFinish && !mIsLoading) {
            mIsLoading = true;
            loadParentClassifyData();
        }

        mTvTitle.setText(model.getCategoryName());

    }

    public void reBindData() {
        if (!mIsLoadFinish && !mIsLoading ) {
            mIsLoading = true;
            loadParentClassifyData();
        }
    }


    //获得子树数据
    private void loadParentClassifyData() {

        ApiControl.getApi().categoryQueryList(mClassifyItemModel.getId(), new Callback.CommonCallback<ResponseModel<List<ClassifyItemModel>>>() {
            @Override
            public void onSuccess(ResponseModel<List<ClassifyItemModel>> result) {
                if (result.isSuccess()) {
                    List<ClassifyItemModel> models = result.getResultObj();
                    createRecyclerData(models);
                    mIsLoadFinish = true;
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                mIsLoading = false;
            }
        });
    }

    private List<RecyclerDataHolder> createRecyclerData(List<ClassifyItemModel> data) {

        List<RecyclerDataHolder> holders = new ArrayList<>();

        for (ClassifyItemModel mode : data) {
            ClassifyDetailsItemDataHolder holder = new ClassifyDetailsItemDataHolder(mode);
            holders.add(holder);
        }
        mRecyclerAdapter.setDataHolders(holders);

        return holders;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.bar_classify_item_details;
    }


}
