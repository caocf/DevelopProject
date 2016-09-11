package com.moge.gege.ui.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.android.volley.VolleyError;
import com.moge.gege.R;
import com.moge.gege.model.BaseOptionModel;
import com.moge.gege.model.RespCategoryListModel;
import com.moge.gege.model.enums.TradingSortType;
import com.moge.gege.network.ErrorCode;
import com.moge.gege.network.request.TradingCategoryRequest;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.RequestManager;
import com.moge.gege.network.util.ResponseEventHandler;
import com.moge.gege.ui.adapter.TradingCategoryAdapter;
import com.moge.gege.ui.adapter.TradingCategoryAdapter.TradingCategoryListener;
import com.moge.gege.util.LogUtil;

public class TradingCategoryPopupWindow implements TradingCategoryListener
{
    private Context mContext;
    private TradingCategoryPopWinListener mListener;

    private ListView mListView;
    private TradingCategoryAdapter mAdapter;

    private PopupWindow mPopup;
    private List<BaseOptionModel> mDataList = new ArrayList<BaseOptionModel>();

    /**
     * 
     * @param context
     * @param callback
     * @param id
     * @param view
     */
    public TradingCategoryPopupWindow(Context context,
            TradingCategoryPopWinListener listener)
    {
        mContext = context;
        mListener = listener;
        initPopupWindow();
        // initData();
    }

    private void initPopupWindow()
    {
        View layout = LayoutInflater.from(mContext).inflate(
                R.layout.pop_trading_category, null);
        mPopup = new PopupWindow(layout, LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT, true);
        mPopup.setBackgroundDrawable(new BitmapDrawable());
        mPopup.setFocusable(true);
        mPopup.setOutsideTouchable(true);
        // mPopup.setAnimationStyle(R.style.popwin_anim_style);

        mListView = (ListView) layout.findViewById(R.id.categoryList);
        mAdapter = new TradingCategoryAdapter(mContext);
        mAdapter.setListener(this);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(mAdapter);
    }

    private void initData()
    {
        doTradingCategoryRequest();
    }

    private void doTradingCategoryRequest()
    {
        TradingCategoryRequest request = new TradingCategoryRequest(

        new ResponseEventHandler<RespCategoryListModel>()
        {
            @Override
            public void onResponseSucess(
                    BaseRequest<RespCategoryListModel> request,
                    RespCategoryListModel result)
            {
                if (result.getStatus() == ErrorCode.SUCCESS)
                {
                    mDataList.clear();
                    mDataList.addAll(result.getData().getCategorys());
                    mDataList.add(new BaseOptionModel(0, mContext
                            .getResources().getString(R.string.more),
                            TradingSortType.MORE));

                    mAdapter.clear();
                    mAdapter.addAll(mDataList);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onResponseError(VolleyError error)
            {
                LogUtil.i(error.getMessage());
            }
        });

        RequestManager.addRequest(request, this);
    }

    public void showPopupWindow(View v, int x, int y)
    {
        if (mPopup != null)
        {
            // reload data when data empty
            if (mAdapter.getCount() == 0)
            {
                initData();
            }
            mPopup.showAsDropDown(v, x, y);
        }
    }

    public static abstract interface TradingCategoryPopWinListener
    {
        public abstract void onTradingCategoryItemClick(BaseOptionModel model);
    }

    @Override
    public void onCategoryItemClick(BaseOptionModel model)
    {
        mPopup.dismiss();
        if (mListener != null)
        {
            mListener.onTradingCategoryItemClick(model);
        }
    }

}
