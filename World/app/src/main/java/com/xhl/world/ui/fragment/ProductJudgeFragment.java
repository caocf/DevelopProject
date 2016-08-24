package com.xhl.world.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xhl.world.R;
import com.xhl.world.api.ApiControl;
import com.xhl.world.model.Base.Response;
import com.xhl.world.model.Base.ResponseModel;
import com.xhl.world.model.EvaluateModel;
import com.xhl.world.ui.recyclerHolder.ProductJudgeItemDataHolder;
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
 * Created by Sum on 16/1/25.
 */
@ContentView(R.layout.fragment_product_judge)
public class ProductJudgeFragment extends BaseAppFragment implements RecyclerViewScrollBottomListener {

    @ViewInject(R.id.title_name)
    private TextView title_name;

    @ViewInject(R.id.rg_content)
    private RadioGroup rg_content;

    @ViewInject(R.id.rb_judge_all)
    private RadioButton rb_judge_all;

    @ViewInject(R.id.rb_judge_pic)
    private RadioButton rb_judge_pic;

    @ViewInject(R.id.rl_null_show)
    private RelativeLayout rl_null_show;

    @Event(value = R.id.title_back, type = RippleView.OnRippleCompleteListener.class)
    private void onBackClick(View view) {
        getSumContext().popTopFragment(null);
    }

    @ViewInject(R.id.judge_recycler_view)
    private RecyclerView mRecyclerView;

    private LinearLayoutManager mManager;
    private RecyclerAdapter mAdapter;
    private String mProductId;

    private boolean isExistMore = true;
    private int mPageSize = 20;
    private int mPageNum = 0;
    private String mCurType = "1";

    @Override
    protected void initParams() {
        title_name.setText("商品评价");

        mAdapter = new RecyclerAdapter(getContext());
        mManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);

        //添加滚动到底部监听
        PageScrollListener scrollListener = new PageScrollListener(mManager);
        scrollListener.addBottomListener(this);
        mRecyclerView.addOnScrollListener(scrollListener);

        rg_content.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_judge_all:
                        mCurType = "1";
                        break;
                    case R.id.rb_judge_pic:
                        mCurType = "2";
                        break;
                }
                mPageNum = 0;
                changeJudgeContent();

            }
        });
        changeJudgeContent();
    }


    private void changeJudgeContent() {
        if (TextUtils.isEmpty(mProductId)) {
            return;
        }
        showLoadingDialog();

        ApiControl.getApi().productEvaluate(mProductId, mCurType, mPageNum, mPageSize, new Callback.CommonCallback<ResponseModel<Response<EvaluateModel>>>() {
            @Override
            public void onSuccess(ResponseModel<Response<EvaluateModel>> result) {
                if (result.isSuccess()) {

                    if (mCurType.equals("1")) {
                        rb_judge_all.setText(getString(R.string.product_judge_state_1, result.getResultObj().getTotal()));
                    } else if (mCurType.equals("2")) {
                        rb_judge_pic.setText(getString(R.string.product_judge_state_2, result.getResultObj().getTotal()));
                    }

                    List<EvaluateModel> rows = result.getResultObj().getRows();
                    if (rows.size() < mPageSize) {
                        isExistMore = false;
                    }
                    //加载数据
                    loadEvaluate(rows);
                } else {
                    SnackMaker.shortShow(title_name, result.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                SnackMaker.shortShow(title_name, ex.getMessage());
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

    //加载评价数据
    private void loadEvaluate(List<EvaluateModel> data) {

        if (data.size() <= 0 && mPageNum == 0) {
//            SnackMaker.shortShow(title_name, "没有更多商品评价了哦");
            rl_null_show.setVisibility(View.VISIBLE);
            return;
        }

        rl_null_show.setVisibility(View.GONE);

        List<RecyclerDataHolder> holders = new ArrayList<>();
        int imageNum = 0;
        for (EvaluateModel evaluate : data) {
            ProductJudgeItemDataHolder holder = new ProductJudgeItemDataHolder(evaluate);
            holders.add(holder);
            if (!TextUtils.isEmpty(evaluate.getRateImg())) {
                imageNum++;
            }
        }
        //设置有图数量
        rb_judge_pic.setText(getString(R.string.product_judge_state_2, imageNum));

        mRecyclerView.getRecycledViewPool().setMaxRecycledViews(0, 15);
        if (mPageNum == 0) {
            mAdapter.setDataHolders(holders);
        } else {
            mAdapter.addDataHolder(holders);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onEnter(Object data) {
        if (data != null && data instanceof String) {
            mProductId = (String) data;
        }
    }

    @Override
    public void onScrollBottom() {
        if (!isExistMore) {
            SnackMaker.shortShow(title_name, R.string.bottom_tip);
            return;
        }
        mPageNum++;
    }
}
