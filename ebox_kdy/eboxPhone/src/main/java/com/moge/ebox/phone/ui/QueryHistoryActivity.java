package com.moge.ebox.phone.ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

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
import com.moge.ebox.phone.bettle.utils.ViewUtils;
import com.moge.ebox.phone.fragment.FragmentDetailsOrder;
import com.moge.ebox.phone.model.OrderDetailsModel;
import com.moge.ebox.phone.ui.adapter.QueryOrderAdapter;
import com.moge.ebox.phone.ui.customview.Head;
import com.moge.ebox.phone.ui.customview.Head.HeadData;
import com.moge.ebox.phone.utils.TimePickerUtil;
import com.moge.ebox.phone.utils.TimePickerUtil.TimePickerClickListener;
import com.moge.ebox.phone.utils.TimeUtil;
import com.moge.ebox.phone.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import tools.AppException;

public class QueryHistoryActivity extends BaseActivity implements


        OnClickListener {

    private TextView mtvStartTime, mtvEndTime, tv_state;
    private RelativeLayout rl_state;
    private Button mbtnQuery;
    private PullToRefreshListView mRefreshListView;
    private QueryOrderAdapter mAdapter;
    private Head mHead;
    private String startTime, endTime;
    private int pageNum, pageSize;
    private int mTotal;

    private Spinner spinner_state;
    private ArrayAdapter<CharSequence> adapterState;
    private int mState;

    private String cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_query_history);
        cursor = "";
        pageSize = 10;
        mState = -1;
        initView();
        initData();
    }

    private void initData() {
        mtvEndTime.setText(TimeUtil.getCurrentTimeDay());
        endTime = TimeUtil.currentTime();
        mtvStartTime.setText(TimeUtil.getBefoSevenDay());
        startTime = TimeUtil.currentBef7DayTime();
        initState();
        setListener();
    }

    private void setListener() {
        mAdapter = new QueryOrderAdapter(this);
        mRefreshListView.setMode(Mode.PULL_FROM_END);
        mRefreshListView.setAdapter(mAdapter);
        mRefreshListView
                .setOnRefreshListener(new OnRefreshListener2<ListView>() {

                    @Override
                    public void onPullDownToRefresh(
                            PullToRefreshBase<ListView> refreshView) {
                        cursor = "";
                        query(0);
                    }

                    @Override
                    public void onPullUpToRefresh(
                            PullToRefreshBase<ListView> refreshView) {
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

    boolean b = false;

    private void initState() {
        adapterState = ArrayAdapter.createFromResource(this,
                R.array.order_state, android.R.layout.simple_spinner_item);
        adapterState
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_state.setAdapter(adapterState);
        spinner_state.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if (b) {
                    switch (position) {
                        case 0:// 全部
                            mState = 100;
                            break;
                        case 1:// 未取
                            mState = 0;// 查询order表中的数据
                            break;
                        case 2:// 自己取走
                            mState = 4;
                            break;
                        case 3:// 用户取走
                            mState = 5;
                            break;
                        case 4:// 管理员取出
                            mState = 6;
                            break;
                    }
                    cursor = "";
                }
                b = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        //默认未取走
        spinner_state.setVisibility(View.VISIBLE);
        spinner_state.setSelection(1, true);
        mState = 0;// 查询order表中的数据
    }

    private void initView() {
        mRefreshListView = findviewById_(R.id.refresh_listview);
        mtvStartTime = findviewById_(R.id.tv_start_time);
        mtvStartTime.setOnClickListener(this);
        mtvEndTime = findviewById_(R.id.tv_end_time);
        mtvEndTime.setOnClickListener(this);
        mbtnQuery = findviewById_(R.id.btn_query);
        mbtnQuery.setOnClickListener(this);
        tv_state = findviewById_(R.id.tv_state);
        rl_state = findviewById_(R.id.rl_state);
        rl_state.setOnClickListener(this);
        spinner_state = findviewById_(R.id.spinner_state);
        initHead();
    }

    private void initHead() {
        mHead = findviewById_(R.id.title);
        HeadData data = mHead.new HeadData();
        data.backVisibility = 1;
        data.tvVisibility = true;
        data.tvContent = getResources().getString(R.string.history_send);
        mHead.setData(data, this);
    }

    private TimePickerClickListener timePickeredStart = new TimePickerClickListener() {

        @Override
        public void onTimePickClick(int year, int month, int day, int hour,
                                    int min, int sec, String formatedDateStr) {
            startTime = year + "-" + month + "-" + day;
            mtvStartTime.setText(TimeUtil.getChooseTimeDay(formatedDateStr));
        }
    };

    private TimePickerClickListener timePickeredEnd = new TimePickerClickListener() {

        @Override
        public void onTimePickClick(int year, int month, int day, int hour,
                                    int min, int sec, String formatedDateStr) {
            endTime = year + "-" + month + "-" + day;
            mtvEndTime.setText(TimeUtil.getChooseTimeDay(formatedDateStr));
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_start_time:
                TimePickerUtil.getInstance().showTimePicker(startTime,
                        QueryHistoryActivity.this, timePickeredStart);
                break;

            case R.id.tv_end_time:
                TimePickerUtil.getInstance().showTimePicker(endTime,
                        QueryHistoryActivity.this, timePickeredEnd);
                break;

            case R.id.btn_query:
                cursor = "";
                query(0);
                break;
            case R.id.rl_state:
                showStateDialog();
                break;
        }
    }

    private void showStateDialog() {
        String title = getResources().getString(R.string.choose_state);
        final String[] array = getResources().getStringArray(R.array.order_state);

        ViewUtils.createChooseDialog(this, title, array,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:// 全部
                                mState = 100;
                                break;
                            case 1:// 未取
                                mState = 0;// 查询order表中的数据
                                break;
                            case 2:// 快递员取走
                                mState = 4;
                                break;
                            case 3:// 用户取走
                                mState = 5;
                                break;
                            case 4:// 管理员取出
                                mState = 6;
                                break;
                        }
                        tv_state.setText(array[which]);
                        cursor = "";
                    }
                }).show();
    }

    private boolean check(int type) {
//		if (mAdapter != null && mAdapter.getCount() >= mTotal) {
        Logger.i("QueryHis：check " + mAdapter.getCount());
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

    private boolean checkParmas() {
        if (TextUtils.isEmpty(startTime)) {
            ToastUtil.showToastShort("请选择开始日期");
            return false;
        }
        if (TextUtils.isEmpty(endTime)) {
            ToastUtil.showToastShort("请选择结束日期");
            return false;
        }
        if (mState == -1) {
            ToastUtil.showToastShort("请选择订单状态");
            return false;
        }
        return true;
    }

    private void query(final int type) {

        if (!checkParmas()) {
            return;
        }
        if (check(type)) {
            return;
        }
        final ProgressDialog progress = UIHelper.showProgress(this, null,
                "查询中,请稍等...", false);
        Map params = new HashMap();
        if (startTime.length() >= 10) {
            startTime = startTime.substring(0, 10);
        }
        if (endTime.length() >= 10) {
            endTime = endTime.substring(0, 10);
        }
        Logger.i("QueryHistory" + startTime + "   " + endTime);
        Logger.i("QueryHistory+type" + mState);
        params.put("start_date", startTime);
        params.put("end_date", endTime);
        params.put("cursor", cursor);
        params.put("page_size", pageSize);
        params.put("state", mState);
        /**
         * 查询历史派件
         */
        ApiClient.queryHistoryExpress(EboxApplication.getInstance(), params,
                new ClientCallback() {

                    @Override
                    public void onSuccess(JSONArray data, int code) {
                        UIHelper.dismissProgress(progress);
                        mRefreshListView.onRefreshComplete();
                        showData(data, type);
                    }

                    @Override
                    public void onFailed(byte[] data, int code) {
                        UIHelper.dismissProgress(progress);
                        mRefreshListView.onRefreshComplete();
                        // mRefreshListView.setEmptyView(getLoadFailedView());
                    }
                });
    }

    private JSONArray list;

    protected void showData(JSONArray data, int type) {
        JSONObject object;
        try {
            object = data.getJSONObject(0);
            list = object.getJSONArray("items");
            Logger.i("QueryHis" + list.length());
            cursor = object.getString("next_cursor");
            mQueryJsonArray = list;
            OrderDetailsModel model = new OrderDetailsModel();
            ArrayList<OrderDetailsModel> parseList = model.parseList(list
                    .toString());
            Logger.i("QueryHistory获得历史件成功");
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
                    // mRefreshListView.setEmptyView(getLoadFailedView());
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

}
