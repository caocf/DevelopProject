package com.moge.gege.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.moge.gege.AppApplication;
import com.moge.gege.R;
import com.moge.gege.config.GlobalConfig;
import com.moge.gege.data.dao.ShopcartDAO;
import com.moge.gege.model.RespTradingListModel;
import com.moge.gege.model.TradingListModel;
import com.moge.gege.model.TradingModel;
import com.moge.gege.network.ErrorCode;
import com.moge.gege.network.request.TradingListRequest;
import com.moge.gege.network.request.TradingPromotionListRequest;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;
import com.moge.gege.ui.adapter.TradingListAdapter;
import com.moge.gege.ui.helper.UIHelper;
import com.moge.gege.util.LogUtil;
import com.moge.gege.util.ToastUtil;

public class TradingListActivity extends BaseActivity implements
        TradingListAdapter.TradingListListener
{
    private Context mContext;
    private PullToRefreshGridView mRefreshTopicGridView;
    private GridView mGridView;
    private TradingListAdapter mAdapter;
    private String mNextCursor = "";

    private boolean mIsPromotion = false;
    private String mId = "";
    private String mName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tradinglist);

        // receive external params
        mIsPromotion = getIntent().getBooleanExtra("is_promotion", false);
        mId = getIntent().getStringExtra("id");
        if (mId == null)
        {
            mId = "";
        }

        mName = getIntent().getStringExtra("name");
        if(TextUtils.isEmpty(mName))
        {
            mName = "";
        }

        mContext = TradingListActivity.this;
        initView();
        initData();
    }

    @Override
    public void onResume()
    {
        super.onResume();

        checkShoppingCart();
    }

    @Override
    protected void initView()
    {
        super.initView();

        this.setHeaderLeftImage(R.drawable.icon_back);
        this.setHeaderLeftTitle(mName);
        this.setHeaderRightImage(R.drawable.icon_shoppingcart);

        mRefreshTopicGridView = (PullToRefreshGridView) this
                .findViewById(R.id.tradingGridView);
        mGridView = mRefreshTopicGridView.getRefreshableView();
        mAdapter = new TradingListAdapter(mContext);
        mAdapter.setListener(this);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(mAdapter);
        mRefreshTopicGridView.setMode(Mode.BOTH);
        //        mRefreshTopicGridView.setRefreshing();
        mRefreshTopicGridView.setEmptyView(getLoadingView());
        mRefreshTopicGridView

                .setOnRefreshListener(new OnRefreshListener2<GridView>()
                {

                    @Override
                    public void onPullDownToRefresh(
                            PullToRefreshBase<GridView> refreshView)
                    {
                        initData();
                    }

                    @Override
                    public void onPullUpToRefresh(
                            PullToRefreshBase<GridView> refreshView)
                    {
                        loadMoreData();
                    }
                });
    }

    private void initData()
    {
        showLoadingView();
        mNextCursor = "";
        loadMoreData();
    }

    @Override
    protected void onHeaderRightClick()
    {
        this.startActivity(new Intent(mContext, ShoppingCartActivity.class));
    }

    private void loadMoreData()
    {
        if (mIsPromotion)
        {
            doTradingPromotionList(mNextCursor, mId);
        }
        else
        {
            doTradingListRequest(mNextCursor, "", mId);
        }
    }

    private void doTradingListRequest(String cursor, String sortType,
            String categoryId)
    {
        TradingListRequest request = new TradingListRequest(mNextCursor,
                GlobalConfig.getLongitude(), GlobalConfig.getLatitude(),
                sortType, categoryId,
                new ResponseEventHandler<RespTradingListModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespTradingListModel> request,
                            RespTradingListModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            TradingListModel listModel = result.getData();

                            if (mNextCursor.equals(""))
                            {
                                mAdapter.clear();
                            }

                            if (listModel.getTradings() != null
                                    && listModel.getTradings().size() > 0)
                            {
                                mAdapter.addAll(listModel.getTradings());
                                mNextCursor = listModel.getNext_cursor();
                                hideLoadingView();
                            }
                            else
                            {
                                ToastUtil.showToastShort(R.string.no_more_data);
                                showLoadEmptyView();
                            }

                            mAdapter.notifyDataSetChanged();
                        }
                        else
                        {
                            showLoadFailView(result.getMsg());
                        }

                        mRefreshTopicGridView.onRefreshComplete();
                    }

                    @Override
                    public void onResponseError(VolleyError error)
                    {
                        LogUtil.i(error.getMessage());
                        mRefreshTopicGridView.onRefreshComplete();
                        showLoadFailView(null);
                    }

                });
        executeRequest(request);
    }

    private void doTradingPromotionList(String cursor, String promotionId)
    {
        TradingPromotionListRequest request = new TradingPromotionListRequest(
                cursor, GlobalConfig.getLongitude(),
                GlobalConfig.getLatitude(), promotionId,
                new ResponseEventHandler<RespTradingListModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespTradingListModel> request,
                            RespTradingListModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            TradingListModel listModel = result.getData();

                            if (mNextCursor.equals(""))
                            {
                                mAdapter.clear();
                            }

                            if (listModel.getTradings() != null
                                    && listModel.getTradings().size() > 0)
                            {
                                mAdapter.addAll(listModel.getTradings());
                                mNextCursor = listModel.getNext_cursor();
                            }
                            else
                            {
                                ToastUtil.showToastShort(R.string.no_more_data);
                                showLoadEmptyView();
                            }

                            mAdapter.notifyDataSetChanged();
                        }
                        else
                        {
                            showLoadFailView(result.getMsg());
                        }

                        mRefreshTopicGridView.onRefreshComplete();
                    }

                    @Override
                    public void onResponseError(VolleyError error)
                    {
                        LogUtil.i(error.getMessage());
                        mRefreshTopicGridView.onRefreshComplete();
                        showLoadFailView(null);
                    }

                });
        executeRequest(request);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK)
        {
            return;
        }

        switch (requestCode)
        {
            case GlobalConfig.INTENT_PUBLISH_TOPIC:
            case GlobalConfig.INTENT_TOPIC_DETAIL:
                break;
            default:
                break;
        }
    }

    @Override
    public void onShoppingCartClick(TradingModel model)
    {
        int leftNum = model.getNum() - model.getSale_num();
        if(leftNum < 1)
        {
            ToastUtil.showToastShort(R.string.no_enough_good);
            return;
        }

        leftNum -= ShopcartDAO.instance().getGoodBuyNum(AppApplication.getLoginId(), model.get_id());
        if(leftNum < 1)
        {
            ToastUtil.showToastShort(R.string.no_left_good);
            return;
        }

        model.setSelected(true);
        model.setBuyNum(1);
        if (ShopcartDAO.instance().insertShopcart(model,
                AppApplication.getLoginId()))
        {
            checkShoppingCart();
            ToastUtil.showToastShort(R.string.add_to_shoppingcard_success);
        }
        else
        {
            ToastUtil.showToastShort(R.string.add_to_shoppingcard_failed);
        }
    }

    @Override
    public void onTradingItemClick(TradingModel model)
    {
        UIHelper.showTradingDetailActivity(mContext, model.get_id());
    }

    private void checkShoppingCart()
    {
        if (ShopcartDAO.instance().getShopcartSize(AppApplication.getLoginId())
                > 0)
        {
            this.setHeaderRightImage(R.drawable.icon_shoppingcart_new);
        }
        else
        {
            this.setHeaderRightImage(R.drawable.icon_shoppingcart);
        }
    }

}
