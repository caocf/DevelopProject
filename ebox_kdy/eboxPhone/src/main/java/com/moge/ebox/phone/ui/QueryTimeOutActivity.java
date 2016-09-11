package com.moge.ebox.phone.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.moge.ebox.phone.R;
import com.moge.ebox.phone.bettle.base.EboxApplication;
import com.moge.ebox.phone.bettle.network.http.ApiClient;
import com.moge.ebox.phone.bettle.network.http.ApiClient.ClientCallback;
import com.moge.ebox.phone.bettle.tools.Logger;
import com.moge.ebox.phone.bettle.tools.UIHelper;
import com.moge.ebox.phone.fragment.FragmentDetailsOrder;
import com.moge.ebox.phone.model.OrderDetailsModel;
import com.moge.ebox.phone.ui.adapter.QueryOrderAdapter;
import com.moge.ebox.phone.ui.customview.Head;
import com.moge.ebox.phone.ui.customview.Head.HeadData;
import com.moge.ebox.phone.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import tools.AppException;

public class QueryTimeOutActivity extends BaseActivity {

    private PullToRefreshListView mRefreshListView;
    private QueryOrderAdapter mAdapter;
    private Context mContext;
    private Head mHead;
    private int mTotal;
    private int pageNum;
    private int pageSize;

    private String cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.i("QueryTimeOutActivity   Oncreate1");
        setContentView(R.layout.activity_query_time_out);
        Logger.i("QueryTimeOutActivity   Oncreate2");
        mContext = this;
//		pageNum = 0;
        cursor = "";
        pageSize = 10;
        initView();
        Logger.i("QueryTimeOutActivity   Oncreate3");
        iniData();
        Logger.i("QueryTimeOutActivity   Oncreate4");
        query(0);
        Logger.i("QueryTimeOutActivity   Oncreat5");
    }

    private void iniData() {
        mAdapter = new QueryOrderAdapter(mContext);
        mRefreshListView.setAdapter(mAdapter);
        mRefreshListView.setMode(Mode.PULL_FROM_END);
        mRefreshListView
                .setOnRefreshListener(new OnRefreshListener2<ListView>() {

                    @Override
                    public void onPullDownToRefresh(
                            PullToRefreshBase<ListView> refreshView) {
//						pageNum = 0;
                        cursor = "";
                        query(0);
                    }

                    @Override
                    public void onPullUpToRefresh(
                            PullToRefreshBase<ListView> refreshView) {
//						pageNum=mAdapter.getCount();
                        query(1);
                    }
                });

        mRefreshListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (!isShow) {
                    isShow = true;
                    showDeatils(position - 1);
                }
            }
        });
    }

    private JSONArray mQueryJsonArray;

    protected void showDeatils(int position) {
        if (mQueryJsonArray == null) {
            ToastUtil.showToastShort("查询数据错误");
            return;
        }
        if (mQueryJsonArray.length() > 0) {
            OrderDetailsModel item = mAdapter.getItem(position);
            startDeatils(item);
        }

    }

    private void startDeatils(OrderDetailsModel jsonObject) {

        if (jsonObject == null) {
            return;
        }

        FragmentManager manager = getSupportFragmentManager();

        FragmentTransaction transaction = manager.beginTransaction();

        transaction
                .setCustomAnimations(R.anim.right_in, 0, 0, R.anim.right_out);

        transaction
                .replace(R.id.rl_details,
                        FragmentDetailsOrder.getInstance(jsonObject))
                .addToBackStack(null).commit();
    }

    private void initView() {
        mRefreshListView = findviewById_(R.id.refresh_listview);
        mHead = findviewById_(R.id.title);
        initHead();
    }

    private void initHead() {
        HeadData data = mHead.new HeadData();
        data.backVisibility = 1;
        data.tvVisibility = true;
        data.tvContent = getResources().getString(R.string.time_out_kd);
        mHead.setData(data, this);
    }

    private void query(final int type) {
        if (check(type)) {
            return;
        }
        final ProgressDialog progress = UIHelper.showProgress(this, null,
                "超期快递查询中,请稍等...", false);
        Logger.i("QueryTimeOutActivity   开始query");
        Map params = new HashMap();
//		params.put("tel", EboxApplication.getInstance().getLoginPhone());
//		params.put("page", pageNum);
        params.put("cursor", cursor);
        params.put("page_size", pageSize);

        /**
         * 查询超时快递信息
         */
        ApiClient.queryTimeOutExpress(EboxApplication.getInstance(), params,
                new ClientCallback() {

                    @Override
                    public void onSuccess(JSONArray data, int code) {
                        Logger.i("TimeOut:onSuccess " + data.toString());
                        UIHelper.dismissProgress(progress);
                        mRefreshListView.onRefreshComplete();
                        showData(data, type);
                    }

                    @Override
                    public void onFailed(byte[] data, int code) {
                        UIHelper.dismissProgress(progress);
                        mRefreshListView.onRefreshComplete();
                        Logger.i("TimeOut:onfailue ");
                        //	mRefreshListView.setEmptyView(getLoadFailedView());
                    }
                });
    }

    private JSONArray list;

    protected void showData(JSONArray data, int type) {
        JSONObject object;
        try {
            object = data.getJSONObject(0);
//			mTotal = object.getInt("total");
            list = object.getJSONArray("items");
            cursor = object.getString("next_cursor");
            mQueryJsonArray = list;
            OrderDetailsModel model = new OrderDetailsModel();
            Logger.i("QueryHistory获得历史件成功");
            ArrayList<OrderDetailsModel> parseList = model.parseList(list
                    .toString());
            if (type == 1 && list.length() == 0) {
                //说明是翻页查询的
                mRefreshListView.post(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showToastShort("当前已经是最后一页");
                        mRefreshListView.onRefreshComplete();
                    }
                });
            } else {
                if (list.length() == 0) {
                    mAdapter.setData(parseList);
                    ToastUtil.showToastShort("没有查询到相关信息");
                    //mRefreshListView.setEmptyView(getLoadFailedView());
                } else {
                    if (type == 0) {
                        mAdapter.setData(parseList);
                    } else {
                        mAdapter.addData(parseList);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (AppException e) {
            e.printStackTrace();
        }
    }

    private boolean check(int type) {
//		if (mAdapter != null && mAdapter.getCount() >= mTotal) {
        if (list != null) {
            if (list.length() < pageSize) {
                if (type == 1) {
                    mRefreshListView.post(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showToastShort("当前已经是最后一页");
                            mRefreshListView.onRefreshComplete();
                        }
                    });
                    return true;
                }
            }
        }
        return false;
    }

}
