package com.moge.ebox.phone.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.moge.ebox.camera.CaptureActivity;
import com.moge.ebox.phone.R;
import com.moge.ebox.phone.bettle.base.EboxApplication;
import com.moge.ebox.phone.bettle.network.http.ApiClient;
import com.moge.ebox.phone.bettle.network.http.ApiClient.ClientCallback;
import com.moge.ebox.phone.bettle.tools.Logger;
import com.moge.ebox.phone.bettle.tools.UIHelper;
import com.moge.ebox.phone.bettle.utils.ViewUtils;
import com.moge.ebox.phone.config.GlobalConfig;
import com.moge.ebox.phone.fragment.FragmentDetailsOrder;
import com.moge.ebox.phone.model.OrderDetailsModel;
import com.moge.ebox.phone.ui.adapter.BaseListAdapter;
import com.moge.ebox.phone.ui.customview.Head;
import com.moge.ebox.phone.ui.customview.Head.HeadData;
import com.moge.ebox.phone.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tools.AppException;

/**
 * 快递查询
 */
public class QuerykdActivity extends BaseActivity implements OnClickListener {
    public static final String TAG = "QuerykdActivity";
    private PullToRefreshListView mRefreshListView;
    private ImageView mIvScand;
    private QueryItemAdapter mAdapter;
    private Head mHead;
    private Context mContext;
    private Button mBtnQuery;
    private EditText mEtInput;
    private JSONArray mQueryJsonArray;
    private String number;
    private int mTotal;
    private int pageNum;
    private int pageSize;

    private String cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_fast);
        mContext = this;
        pageSize = 10;
        initView();
        initData();
    }

    private void initData() {
        mAdapter = new QueryItemAdapter();
        mRefreshListView.setMode(Mode.PULL_FROM_END);
        mRefreshListView.setAdapter(mAdapter);
        setListener();
    }

    private void setListener() {

        mRefreshListView
                .setOnRefreshListener(new OnRefreshListener2<ListView>() {

                    @Override
                    public void onPullDownToRefresh(
                            PullToRefreshBase<ListView> refreshView) {
                        cursor = "";
                        if (checkNumber()) {
                            String number = mEtInput.getText().toString()
                                    .trim();
                            queryResult(number, 0);
                        }
                    }

                    @Override
                    public void onPullUpToRefresh(
                            PullToRefreshBase<ListView> refreshView) {
                        Logger.i("queryKD:" + cursor);
                        if (checkNumber()) {
                            String number = mEtInput.getText().toString()
                                    .trim();
                            queryResult(number, 1);
                        }
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
        mEtInput = findviewById_(R.id.et_input);
        mBtnQuery = findviewById_(R.id.btn_query);
        mIvScand = findviewById_(R.id.iv_scand);
        mIvScand.setOnClickListener(this);
        mBtnQuery.setOnClickListener(this);
        initHead();
    }

    private void initHead() {
        mHead = findviewById_(R.id.title);
        HeadData data = mHead.new HeadData();
        data.backVisibility = 1;
        data.tvVisibility = true;
        data.tvContent = getResources().getString(R.string.fast_query);
        mHead.setData(data, this);
    }

    @SuppressLint("NewApi")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case GlobalConfig.INTENT_SCAND:
                String scanResult = data.getStringExtra("SCAN_RESULT");
                mEtInput.setText(scanResult);
                mBtnQuery.callOnClick();
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_query:
                query();
                break;

            case R.id.iv_scand:
                Intent intent = new Intent(QuerykdActivity.this,
                        CaptureActivity.class);
                startActivityForResult(intent, GlobalConfig.INTENT_SCAND);
                break;
            default:
                break;
        }
    }

    private void query() {
        ViewUtils.closeInput(this, mEtInput);
        if (checkNumber()) {
            cursor = "";
            String number = mEtInput.getText().toString().trim();
            queryResult(number, 0);
        }
    }

    private boolean checkNumber() {
        String number = mEtInput.getText().toString().trim();
        if (TextUtils.isEmpty(number) || number.length() == 0) {
            ToastUtil.showToastShort(R.string.query_hint1);
            mRefreshListView.post(new Runnable() {
                @Override
                public void run() {
                    mRefreshListView.onRefreshComplete();
                }
            });
            return false;
        }
        return true;
    }

    private boolean check(int type) {
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

    private void queryResult(String number, final int type) {
        if (check(type)) {
            return;
        }
        final ProgressDialog progress = UIHelper.showProgress(mContext, null,
                "快递查询中,请稍等...", false);
        this.number = number;
        Map params = new HashMap();
        params.put("search", number);
        params.put("cursor", cursor);
        params.put("page_size", pageSize);
        /**
         * 查询快递订单状态
         */
        ApiClient.queryExpressOrderState(EboxApplication.getInstance(), params,
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
                        //mRefreshListView.setEmptyView(getLoadFailedView());
                    }
                });

    }

    private JSONArray list;

    protected void showData(JSONArray data, int type) {
        JSONObject object;
        try {
            object = data.getJSONObject(0);
            list = object.getJSONArray("items");
            cursor = object.getString("next_cursor");
            mQueryJsonArray = list;
            OrderDetailsModel model = new OrderDetailsModel();
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
                    mAdapter.setData(parseList, this.number);
                    ToastUtil.showToastShort("没有查询到相关信息");
                    // mRefreshListView.setEmptyView(getLoadFailedView());
                } else {
                    if (type == 0) {
                        mAdapter.setData(parseList, this.number);
                    } else {
                        mAdapter.addData(parseList, this.number);
                    }
                }
            }
        } catch (AppException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        ViewUtils.closeInput(this, mEtInput);
        return super.dispatchTouchEvent(ev);
    }

    private class QueryItemAdapter extends BaseListAdapter<OrderDetailsModel> {

        private String match;

        public void setData(List<OrderDetailsModel> data, String match) {
            this.match = match;
            if (list.size() > 0) {
                list.clear();
            }
            list.addAll(data);
            notifyDataSetChanged();
        }

        public void addData(List<OrderDetailsModel> data, String match) {
            this.match = match;
            list.addAll(data);
            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_query_kd,
                        null);
            }
            TextView tv_item_id = ViewHolder.get(convertView, R.id.tv_item_id);
            TextView tv_phone = ViewHolder.get(convertView, R.id.tv_phone);
            TextView tv_state = ViewHolder.get(convertView, R.id.tv_state);

            OrderDetailsModel detailsModel = list.get(position);
            if (!TextUtils.isEmpty(match)) {
                hightColor(tv_item_id, match, detailsModel.getItem_id());
            } else {
                tv_item_id.setText(detailsModel.getItem_id());
            }
            if (!TextUtils.isEmpty(match)) {
                hightColor(tv_phone, match, detailsModel.getMsisdn());
            } else {
                tv_phone.setText(detailsModel.getMsisdn());
            }
            tv_state.setText(ViewUtils.checkState(detailsModel.getState()));
            return convertView;
        }
    }
}
