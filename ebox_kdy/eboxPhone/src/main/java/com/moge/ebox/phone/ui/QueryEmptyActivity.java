package com.moge.ebox.phone.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.moge.ebox.phone.R;
import com.moge.ebox.phone.bettle.base.EboxApplication;
import com.moge.ebox.phone.bettle.network.http.ApiClient;
import com.moge.ebox.phone.bettle.network.http.ApiClient.ClientCallback;
import com.moge.ebox.phone.bettle.tools.Logger;
import com.moge.ebox.phone.bettle.tools.UIHelper;
import com.moge.ebox.phone.model.BigEmptyInfoModel;
import com.moge.ebox.phone.model.EmptyInfoModel;
import com.moge.ebox.phone.model.MiddleEmptyInfoModel;
import com.moge.ebox.phone.model.SmallEmptyInfoModel;
import com.moge.ebox.phone.ui.adapter.BaseListAdapter;
import com.moge.ebox.phone.ui.adapter.KeyValues;
import com.moge.ebox.phone.ui.customview.Head;
import com.moge.ebox.phone.ui.customview.Head.HeadData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QueryEmptyActivity extends BaseActivity {

    public static final String TAG = "QueryEmptyActivity";
    private Head mHead;
    private PullToRefreshListView mRefreshListView;
    private Context mContext;
    private EmptyBoxAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_empty);
        mContext = QueryEmptyActivity.this;
        initView();
        initData();
    }

    private void initData() {
        mAdapter = new EmptyBoxAdapter();
        mRefreshListView.setAdapter(mAdapter);
        mRefreshListView.setMode(Mode.DISABLED);
        mRefreshListView.setScrollingWhileRefreshingEnabled(false);
        loadData();
    }

    private void initView() {
        mRefreshListView = findviewById_(R.id.refresh_listview);
        initHead();
    }

    private void initHead() {
        mHead = findviewById_(R.id.title);
        HeadData data = mHead.new HeadData();
        data.backVisibility = 1;
        data.tvVisibility = true;
        data.tvContent = getResources().getString(R.string.empty_query);
        mHead.setData(data, this);
    }

    private void loadData() {

        final ProgressDialog progress = UIHelper.showProgress(this, null,
                "空箱查询中,请稍等...", false);

        Map params = new HashMap();
        String phone = EboxApplication.getInstance().getLoginPhone();
        params.put(KeyValues.user_tel, phone);

        /**
         * 空箱查询
         */
        ApiClient.queryEmptyBox(EboxApplication.getInstance(), params,
                new ClientCallback() {

                    @Override
                    public void onSuccess(JSONArray data, int code) {
                        UIHelper.dismissProgress(progress);
                        mRefreshListView.onRefreshComplete();
                        Logger.i("QueryEmpty:" + data.toString());
//						JSONArray dataArray = null;
//							dataArray = data.getJSONObject(0).getJSONArray("box");
                        showData1(data);
                    }

                    @Override
                    public void onFailed(byte[] data, int code) {
                        UIHelper.dismissProgress(progress);
                        mRefreshListView.onRefreshComplete();
                        mRefreshListView.setEmptyView(getLoadFailedView());
                    }
                });
    }

    private JSONArray mQueryJsonArray;

    //    protected void showData3(JSONArray data){
//        JSONObject object;
//        try {
//            object = data.getJSONObject(0);
////			mTotal = object.getInt("total");
//            JSONArray list = object.getJSONArray("items");
//            mQueryJsonArray=list;
//            EmptyInfoModel model = new EmptyInfoModel();
//            ArrayList<EmptyInfoModel> parseList = model.parseList(list
//                    .toString());
//            if (list.length() == 0) {
//                mAdapter.setData(parseList,this.number);
//                ToastUtil.showToastShort("没有查询到相关信息");
//                // mRefreshListView.setEmptyView(getLoadFailedView());
//            } else {
//                if (type==0) {
//                    mAdapter.setData(parseList,this.number);
//                }else {
//                    mAdapter.addData(parseList,this.number);
//                }
//            }
//        } catch (AppException e) {
//            e.printStackTrace();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//    protected void showData(JSONArray data) {
//        ArrayList<EmptyInfoModel> arrayList = new ArrayList<EmptyInfoModel>();
//        for (int i = 0; i < data.length(); i += 3) {
//            try {
//                EmptyInfoModel model = new EmptyInfoModel();
//                for (int j = i; j < i + 3; j++) {
//                    JSONObject object = data.getJSONObject(j);
//                    String total = object.getString(KeyValues.empty_total);
//                    String name = object
//                            .getString(KeyValues.empty_terminal_name);
//                    String type = object.getString(KeyValues.empty_box_type);
//                    model.setTerminal_name(name);
//                    if (type.contains("小")) {
//                        model.setBox_small(total);
//                    } else if (type.contains("中")) {
//                        model.setBox_medium(total);
//                    } else {
//                        model.setBox_big(total);
//                    }
//                }
//                arrayList.add(model);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        if (arrayList.size() > 0) {
//            mAdapter.addAll(arrayList);
//            mAdapter.notifyDataSetChanged();
//        } else {
//            mRefreshListView.setEmptyView(getLoadFailedView());
//        }
//    }
//
    protected void showData1(JSONArray data) {
        Logger.i("QueryEmpty:" + data.toString());
        ArrayList<EmptyInfoModel> arrayList = new ArrayList<EmptyInfoModel>();
        Logger.i("QueryEmpty:"+data.length());

        for (int i = 0; i < data.length(); i++) {
            try {
                JSONObject object = data.getJSONObject(i);
                EmptyInfoModel model = new EmptyInfoModel();

                BigEmptyInfoModel bigModel = new BigEmptyInfoModel();
                MiddleEmptyInfoModel middleModel = new MiddleEmptyInfoModel();
                SmallEmptyInfoModel smallModle = new SmallEmptyInfoModel();
                Logger.i("QueryEmpty:"+object.toString());



                if (object.getJSONObject("box").toString().contains("big")){
                    bigModel.setBox_count(object.getJSONObject("box").getJSONObject("big").getInt("box_count"));
                }else{
                    bigModel.setBox_count(0);
                }

                if (object.getJSONObject("box").toString().contains("middle")){
                    middleModel.setBox_count(object.getJSONObject("box").getJSONObject("middle").getInt("box_count"));
                }else{
                    bigModel.setBox_count(0);
                }

                if (object.getJSONObject("box").toString().contains("small")){
                    smallModle.setBox_count(object.getJSONObject("box").getJSONObject("small").getInt("box_count"));
                }else{
                    bigModel.setBox_count(0);
                }


                model.setBig(bigModel);
                model.setSmall(smallModle);
                model.setMiddle(middleModel);


                model.setTerminal_name(object.getString("terminal_name"));

                arrayList.add(model);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (arrayList.size() > 0) {
            mAdapter.addAll(arrayList);
            mAdapter.notifyDataSetChanged();
        } else {
            mRefreshListView.setEmptyView(getLoadFailedView());
        }
    }

    private class EmptyBoxAdapter extends BaseListAdapter<EmptyInfoModel> {

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_query_empty,
                        null);
            }
            TextView tv_area = ViewHolder.get(convertView, R.id.tv_area);
            TextView tv_box_samll = ViewHolder.get(convertView,
                    R.id.tv_box_samll);
            TextView tv_box_medium = ViewHolder.get(convertView,
                    R.id.tv_box_medium);
            TextView tv_box_big = ViewHolder.get(convertView, R.id.tv_box_big);

            EmptyInfoModel model = list.get(position);
            tv_area.setText(model.getTerminal_name());
            tv_box_samll.setText(check(String.valueOf(model.getSmall().getBox_count())));
            tv_box_medium.setText(check(String.valueOf(model.getMiddle().getBox_count())));
            tv_box_big.setText(check(String.valueOf(model.getBig().getBox_count())));

            return convertView;
        }
    }

    public CharSequence check(String string) {

        if (TextUtils.isEmpty(string)) {
            return "0个";
        }
        return string + "个";
    }
}
