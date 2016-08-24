package com.xhl.world.ui.main.classify;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.xhl.world.R;
import com.xhl.world.api.ApiControl;
import com.xhl.world.model.Base.ResponseModel;
import com.xhl.world.model.ClassifyItemModel;
import com.xhl.world.ui.activity.SearchDetailsActivity;
import com.xhl.world.ui.event.SearchItemEvent;
import com.xhl.world.ui.fragment.BaseAppFragment;
import com.xhl.world.ui.main.classify.recyclerHolder.ClassifyDetailsDataHolder;
import com.xhl.world.ui.utils.SnackMaker;
import com.xhl.xhl_library.ui.recyclerview.RecyclerAdapter;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.swipyrefresh.SwipyRefreshLayout;
import com.xhl.xhl_library.ui.swipyrefresh.SwipyRefreshLayoutDirection;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sum on 15/11/26.
 */
@ContentView(R.layout.fragment_classify_item_details)
public class ClassifyItemDetailsFragment extends BaseAppFragment {

    private ClassifyItemModel mClassifyItem;
    @ViewInject(R.id.swipy_refresh_layout)
    private SwipyRefreshLayout mSwipRefresh;

    @ViewInject(R.id.bar_classify_item_details_recycler_view)
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private RecyclerAdapter mRecyclerAdapter;
    private String mId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mClassifyItem = (ClassifyItemModel) getArguments().getSerializable("model");
        if (mClassifyItem == null) {
            LogUtil.e("classify details is null");
        }
        mId = mClassifyItem.getId();

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mClassifyItem == null) {
            this.mClassifyItem = (ClassifyItemModel) getArguments().getSerializable("model");
        }
    }


    @Override
    protected void initParams() {

        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerAdapter = new RecyclerAdapter(getContext());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mRecyclerAdapter);

        initPullRefresh();
        loadParentClassifyData();
    }


    private void initPullRefresh() {
        mSwipRefresh.setColorSchemeResources(R.color.app_green, R.color.app_blue);
        mSwipRefresh.setDirection(SwipyRefreshLayoutDirection.TOP);
        mSwipRefresh.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {

                if (direction == SwipyRefreshLayoutDirection.TOP) {
                    loadParentClassifyData();
                }
            }
        });
    }

    //获取导航栏标签数据
    private void loadParentClassifyData() {

        ApiControl.getApi().categoryQueryList(mId, new Callback.CommonCallback<ResponseModel<List<ClassifyItemModel>>>() {
            @Override
            public void onSuccess(ResponseModel<List<ClassifyItemModel>> result) {
                if (result.isSuccess()) {
                    List<ClassifyItemModel> models = result.getResultObj();
                    createRecyclerData(models);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                SnackMaker.shortShow(mSwipRefresh, ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                mSwipRefresh.setRefreshing(false);
            }
        });
    }

    private void createRecyclerData(List<ClassifyItemModel> data) {
        List<RecyclerDataHolder> holders = new ArrayList<>();

        for (ClassifyItemModel mode : data) {

            ClassifyDetailsDataHolder holder = new ClassifyDetailsDataHolder(mode);
            holders.add(holder);
        }
        mRecyclerAdapter.setDataHolders(holders);
        mRecyclerView.postInvalidate();
    }

    public void onEvent(SearchItemEvent event) {

        if (event.search_type == SearchItemEvent.Type_Classify) {
            Intent intent = new Intent(getContext(), SearchDetailsActivity.class);
            intent.putExtra(SearchDetailsActivity.SEARCH_PARAMS, event.search_content);
            intent.putExtra(SearchDetailsActivity.SEARCH_TYPE, SearchDetailsActivity.SEARCH_TYPE_CATEGORY);
            getActivity().startActivity(intent);
        }
    }
}
