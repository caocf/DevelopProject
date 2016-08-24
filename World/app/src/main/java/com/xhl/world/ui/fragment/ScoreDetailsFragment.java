package com.xhl.world.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.xhl.world.R;
import com.xhl.world.api.ApiControl;
import com.xhl.world.model.Base.Response;
import com.xhl.world.model.Base.ResponseModel;
import com.xhl.world.model.ScoreModel;
import com.xhl.world.ui.recyclerHolder.ScoreDetailDataHolder;
import com.xhl.world.ui.utils.SnackMaker;
import com.xhl.world.ui.view.pub.PageScrollListener;
import com.xhl.world.ui.view.pub.callback.RecyclerViewScrollBottomListener;
import com.xhl.xhl_library.ui.recyclerview.RecyclerAdapter;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.view.RippleView;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sum on 16/1/7.
 */
@ContentView(R.layout.fragment_score_details)
public class ScoreDetailsFragment extends BaseAppFragment implements RecyclerViewScrollBottomListener {

    @ViewInject(R.id.title_name)
    private TextView title_name;

    @ViewInject(R.id.score_recycler_view)
    private RecyclerView mRecyclerView;


    @Event(value = R.id.title_back, type = RippleView.OnRippleCompleteListener.class)
    private void onBackClick(View view) {
        getSumContext().popTopFragment(null);
    }

    private LinearLayoutManager mLayoutManager;
    private RecyclerAdapter mAdapter;

    private int mCurPage = 0;
    private int mPageSize = 100;

    @Override
    protected void initParams() {
        title_name.setText("积分明细");

        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new RecyclerAdapter(getContext());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        PageScrollListener listener = new PageScrollListener(mLayoutManager);
        listener.addBottomListener(this);
        mRecyclerView.addOnScrollListener(listener);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadData();
    }

    private void loadData() {

        showLoadingDialog();

        ApiControl.getApi().integralList(mCurPage, mPageSize, new Callback.CommonCallback<ResponseModel<Response<ScoreModel>>>() {
            @Override
            public void onSuccess(ResponseModel<Response<ScoreModel>> result) {
                if (result.isSuccess()) {
                    List<ScoreModel> scoreModels = result.getResultObj().getRows();
                    showData(scoreModels);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                SnackMaker.shortShow(title_name,ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                hideLoadingDialog();
            }
        });
    }

    private void showData(List<ScoreModel> data) {

        if (data != null && data.size() > 0) {
            List<RecyclerDataHolder> holders = new ArrayList<>();
            for (ScoreModel s : data) {
                ScoreDetailDataHolder holder = new ScoreDetailDataHolder(s);
                holders.add(holder);
            }
            mAdapter.setDataHolders(holders);
        } else {
            SnackMaker.shortShow(title_name,"没有查询到您的积分信息");
        }
    }

    @Override

    public void onResume() {
        super.onResume();
    }

    @Override
    public void onScrollBottom() {

    }
}
