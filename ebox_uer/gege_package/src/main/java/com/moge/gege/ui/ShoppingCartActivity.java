package com.moge.gege.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.android.volley.VolleyError;
import com.moge.gege.AppApplication;
import com.moge.gege.R;
import com.moge.gege.data.dao.ShopcartDAO;
import com.moge.gege.model.RespTradingDetailModel;
import com.moge.gege.model.TradingBaseModel;
import com.moge.gege.model.TradingDetailModel;
import com.moge.gege.network.ErrorCode;
import com.moge.gege.network.request.TradingDetailRequest;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;
import com.moge.gege.ui.adapter.ShoppingCartListAdapter;
import com.moge.gege.ui.helper.UIHelper;
import com.moge.gege.util.DialogUtil;
import com.moge.gege.util.FunctionUtils;
import com.moge.gege.util.LogUtil;
import com.moge.gege.util.widget.CustomDialog;

import java.util.*;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ShoppingCartActivity extends BaseActivity implements
        OnClickListener, OnCheckedChangeListener, ShoppingCartListAdapter.ShoppingCartListListener {
    private Activity mContext;

    @InjectView(R.id.goodsList)
    ListView mGoodsListView;
    @InjectView(R.id.allSelectCheckBox)
    CheckBox mAllSelectCheckBox;
    @InjectView(R.id.totalMoneyTextView)
    TextView mTotalMoneyTextView;
    @InjectView(R.id.checkoutBtn)
    Button mCheckoutBtn;
    @InjectView(R.id.deleteBtn)
    Button mDeleteBtn;

    private ShoppingCartListAdapter mAdapter;
    private List<TradingBaseModel> mListGoods;
    private Map<String, TradingBaseModel> mSynGoodsMap = new HashMap<String, TradingBaseModel>();

    private int mTotalBuyNum = 0;

    private Dialog mProgressDialog;
    private int mSynTotalNum = 0;
    private int mSynSuccessNum = 0;
    private int mSynFailNum = 0;
    private boolean mSynIng = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoppingcard);
        ButterKnife.inject(this);

        mContext = ShoppingCartActivity.this;
        initView();
        // initData();
    }

    @Override
    protected void initView() {
        super.initView();

        this.setHeaderLeftImage(R.drawable.icon_back);
        this.setHeaderLeftTitle(R.string.shoppingcart);
//        this.setHeaderRightTitle(R.string.edit);

        mAdapter = new ShoppingCartListAdapter(this);
        mAdapter.setListener(this);
        mGoodsListView.setAdapter(mAdapter);
        mGoodsListView.setOnItemClickListener(mAdapter);

        mAllSelectCheckBox.setOnCheckedChangeListener(this);
        mCheckoutBtn.setOnClickListener(this);
        mDeleteBtn.setOnClickListener(this);
    }

    private void initData() {
        mListGoods = ShopcartDAO.instance().getShopcart(
                AppApplication.getLoginId());

        // synchronize shoppingcart
        if (mListGoods != null && mListGoods.size() > 0 && AppApplication
                .shouldToSynShoppingCart()) {
            // synchronize ing
            if (mSynIng) {
                return;
            }

            if (mProgressDialog == null) {
                mProgressDialog = DialogUtil.createProgressDialog(mContext,
                        mContext.getResources()
                                .getString(R.string.syn_shoppingcart_ing));
            }

            synShoppingCart(mListGoods);
        } else {
            refreshUI();
        }
    }

    // refresh adapter and total fee
    private void refreshUI() {
        mAdapter.clear();
        mAdapter.addAll(mListGoods);
        mAdapter.notifyDataSetChanged();

        updateTotalInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    @Override
    protected void onHeaderLeftClick() {
        Intent intent = new Intent();
        intent.putExtra("shoppingcart_num", ShopcartDAO.instance()
                .getShopcartSize(AppApplication.getLoginId()));
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onHeaderRightClick() {
//        if (mAdapter.getMode() == ShoppingCartMode.Normal)
//        {
//            this.setHeaderRightTitle(R.string.finish);
//            mAdapter.setMode(ShoppingCartMode.Eidt);
//
//            mAllSelectCheckBox.setText(getString(R.string.select_all));
//            mTotalMoneyTextView.setVisibility(View.GONE);
//            mCheckoutBtn.setVisibility(View.GONE);
//            mDeleteBtn.setVisibility(View.VISIBLE);
//        }
//        else
//        {
//            this.setHeaderRightTitle(R.string.edit);
//            mAdapter.setMode(ShoppingCartMode.Normal);
//
//            mAllSelectCheckBox.setText("");
//            mTotalMoneyTextView.setVisibility(View.VISIBLE);
//            mCheckoutBtn.setVisibility(View.VISIBLE);
//            mDeleteBtn.setVisibility(View.GONE);
//        }
//
//        mAdapter.notifyDataSetChanged();
    }

    private void updateTotalInfo() {

        double totalMoney = 0;
        mTotalBuyNum = 0;

        for (TradingBaseModel model : mAdapter.getAll()) {

            totalMoney += model.getDiscount_price() * model.getBuyNum();
            mTotalBuyNum += model.getBuyNum();

        }

        SpannableString ss = new SpannableString(getString(R.string.total_money)
                + FunctionUtils.getDouble(totalMoney * 1.0 / 100));
        ss.setSpan(new AbsoluteSizeSpan(17, true), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTotalMoneyTextView.setText(ss);

//        mTotalMoneyTextView.setText(getString(R.string.total_money)
//                + FunctionUtils.getDouble(totalMoney * 1.0 / 100));
    }

    private void selectAllGoods(List<TradingBaseModel> listModel,
                                boolean isSelected) {
        ShopcartDAO.instance().selectAllGoods(AppApplication.getLoginId(),
                isSelected);

        for (TradingBaseModel model : listModel) {
            model.setSelected(isSelected);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.checkoutBtn:
                onCheckOut();
                break;
            case R.id.deleteBtn:
                onDeleteGoods();
                break;
            default:
                break;
        }
    }

    private ArrayList<TradingBaseModel> getSelectTradingList() {
        ArrayList<TradingBaseModel> selectList = new ArrayList<TradingBaseModel>();
        for (TradingBaseModel model : mAdapter.getAll()) {
                selectList.add(model);
        }

        return selectList;
    }

    private void onCheckOut() {
        if (mTotalBuyNum == 0) {
            return;
        }

        if (!AppApplication.checkLoginState(mContext)) {
            return;
        }

        Intent intent = new Intent(mContext, TradingPayActivity.class);
        intent.putExtra("goods_list", getSelectTradingList());
        this.startActivity(intent);
    }

    private void onDeleteGoods() {
        if (mTotalBuyNum == 0) {
            return;
        }

        new CustomDialog.Builder(mContext)
                .setTitle(R.string.general_tips)
                .setMessage(getString(R.string.delete_goods_tips, mTotalBuyNum))
                .setPositiveButton(R.string.general_confirm,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                List<TradingBaseModel> selectList = getSelectTradingList();
                                mListGoods.removeAll(selectList);
                                refreshUI();

                                ShopcartDAO.instance().deleteGoods(selectList,
                                        AppApplication.getLoginId());

                                dialog.dismiss();
                            }

                        })
                .setNegativeButton(R.string.general_cancel,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                            }

                        }).create().show();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        selectAllGoods(mListGoods, isChecked);
        updateTotalInfo();
    }

    @Override
    public void onTradingNumChange() {
        updateTotalInfo();
    }

    @Override
    public void onTradingDetailItemClick(TradingBaseModel model) {
        UIHelper.showTradingDetailActivity(mContext, model.get_id());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onHeaderLeftClick();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onLoginResult(int from, int result) {
        if (result == ErrorCode.SUCCESS) {
            // synchronize shoppingcart data
            ShopcartDAO.instance().updateUserGoods(AppApplication.getLoginId());

            // synchronize and refresh shoppingcart
            AppApplication.setSynShoppingCartTime(0);
            initData();
        }
    }

    private void synShoppingCart(List<TradingBaseModel> tradingList) {
        mSynTotalNum = tradingList.size();
        mSynSuccessNum = 0;
        mSynFailNum = 0;
        mSynGoodsMap.clear();
        mSynIng = true;

        mProgressDialog.show();
        for (TradingBaseModel trading : tradingList) {
            doTradingDetailRequest(trading.get_id());
        }
    }

    private void isSynShoppingCartFinish() {
        if (mSynSuccessNum + mSynFailNum >= mSynTotalNum) {
            // syn database
            Iterator itr = mListGoods.iterator();
            while (itr.hasNext()) {
                TradingBaseModel baseModel = (TradingBaseModel) itr.next();
                TradingBaseModel existModel = mSynGoodsMap
                        .get(baseModel.get_id());
                if (existModel != null && (baseModel.getNum() - baseModel.getSale_num() > 0)) {
                    existModel.setBuyNum(baseModel.getBuyNum());
                    existModel.setSale_num(baseModel.getSale_num());
                    ShopcartDAO.instance().updateGoods(existModel,
                            AppApplication.getLoginId());
                } else {
                    itr.remove();
                    ShopcartDAO.instance().deleteGoods(baseModel,
                            AppApplication.getLoginId());
                }
            }

            // update ui
            mListGoods = ShopcartDAO.instance().getShopcart(
                    AppApplication.getLoginId());
            refreshUI();

            // reset value
            mSynSuccessNum = 0;
            mSynFailNum = 0;
            mSynGoodsMap.clear();
            mSynIng = false;

            AppApplication.setSynShoppingCartTime(System.currentTimeMillis());
            mProgressDialog.dismiss();
        }
    }

    private void doTradingDetailRequest(String tradingId) {
        TradingDetailRequest request = new TradingDetailRequest(tradingId,
                new ResponseEventHandler<RespTradingDetailModel>() {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespTradingDetailModel> request,
                            RespTradingDetailModel result) {
                        if (result.getStatus() == ErrorCode.SUCCESS) {
                            TradingDetailModel model = result.getData()
                                    .getTrading();
                            if (model != null) {
                                mSynGoodsMap.put(model.get_id(), model);
                            }
                            mSynSuccessNum++;
                        } else {
                            mSynFailNum++;
                        }

                        isSynShoppingCartFinish();
                    }

                    @Override
                    public void onResponseError(VolleyError error) {
                        LogUtil.i(error.getMessage());
                        mSynFailNum++;
                        isSynShoppingCartFinish();
                    }
                });
        executeRequest(request);
    }

}
