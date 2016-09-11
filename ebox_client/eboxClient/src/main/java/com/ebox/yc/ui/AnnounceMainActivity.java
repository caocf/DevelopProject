package com.ebox.yc.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;

import com.android.volley.VolleyError;
import com.ebox.R;
import com.ebox.ex.ui.CommonActivity;
import com.ebox.Anetwork.RequestManager;
import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.pub.service.global.GlobalField;
import com.ebox.pub.ui.customview.Title;
import com.ebox.pub.ui.customview.Title.TitleData;
import com.ebox.pub.utils.DialogUtil;
import com.ebox.pub.utils.LogUtil;
import com.ebox.pub.utils.MGViewUtil;
import com.ebox.pub.utils.Tip;
import com.ebox.yc.model.Content;
import com.ebox.yc.model.ReqAnnounce;
import com.ebox.yc.model.RspAnnounce;
import com.ebox.yc.model.enums.Channel;
import com.ebox.yc.network.request.QryContents;
import com.ebox.yc.ui.adapter.AnnounceAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;


public class AnnounceMainActivity extends CommonActivity implements OnClickListener {
    private PullToRefreshListView gglist;
    private Tip tip;

    private AnnounceAdapter mAdapter;
    private ArrayList<Content> pList = new ArrayList<Content>();

    private Button btn_left;
    private Button btn_right;

    private Title title;
    private TitleData titleData;
    private DialogUtil dialogUtil;
    private Context mContext;

    private int pageIndex = 0;
    private int pageSize = 5;
    private int total = 0;
    private Boolean hasNext = false;

    private static final int ACTION_LEFT = 0;
    private static final int ACTION_RIGHT = 1;
    private static final int ACTION_FIRST = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = AnnounceMainActivity.this;
        setContentView(R.layout.yc_activity_announce);
        MGViewUtil.scaleContentView(this, R.id.rootView);
        initView();
        initData(ACTION_FIRST);
    }


    @SuppressLint("SetJavaScriptEnabled")
    private void initView() {
        title = (Title) findViewById(R.id.title);
        titleData = title.new TitleData();
        titleData.backVisibility = 1;
        titleData.tvVisibility = true;
        titleData.tvContent = getResources().getString(R.string.yc_announce);
        title.setData(titleData, this);


        dialogUtil = new DialogUtil();
        dialogUtil.createProgressDialog(this, true);
        gglist = (PullToRefreshListView) findViewById(R.id.gglist);
        btn_left = (Button) findViewById(R.id.btn_left);
        btn_right = (Button) findViewById(R.id.btn_right);
        btn_right.setOnClickListener(this);
        btn_left.setOnClickListener(this);
        mAdapter = new AnnounceAdapter(this);
        mAdapter.addAll(pList);
        gglist.setAdapter(mAdapter);
        gglist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Content info = pList.get(position - 1);
                startDeal(info);
            }
        });
        gglist.setMode(PullToRefreshBase.Mode.DISABLED);

        setLeftState(false);
    }

    private void startDeal(Content info) {
        Intent intent = new Intent(this, AnnounceDetailActivity.class);
        intent.putExtra("conent_id", info.getConentId()+"");
        startActivity(intent);
    }

    private void initData(int type) {
        dialogUtil.showProgressDialog();
        qryContents(type);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (tip != null) {
            tip.closeTip();
        }
        if (dialogUtil != null) {
            dialogUtil.closeProgressDialog();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        title.showTimer();
    }

    @Override
    public void onPause() {
        super.onPause();
        title.stopTimer();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
                setLeftBtn();
                break;
            case R.id.btn_right:
                setRightBtn();
                break;
        }
    }

    private void showPrompt(int resId) {
        tip = new Tip(mContext, getResources().getString(
                resId), null);
        tip.show(0);
    }

    private Boolean checkParam() {
        if (GlobalField.config.getCommunityId() == null || GlobalField.config.getCommunityId().equals("")) {
            showPrompt(R.string.community_id_not_exist);
            return false;
        }
        return true;
    }

    private void setLeftBtn() {
        setRightState(true);
        pageIndex--;
        initData(ACTION_LEFT);

        if (pageIndex == 0) {
            setLeftState(false);
        }
    }

    private void setLeftState(boolean show) {
        if (show) {
            btn_left.setBackgroundResource(R.drawable.yc_btn_left);
        } else {
            btn_left.setBackgroundResource(R.drawable.yc_left_pressed);
        }
        btn_left.setClickable(show);
    }

    private void setRightBtn() {
        if (hasNext) {
            setLeftState(true);
            pageIndex++;
            initData(ACTION_RIGHT);
        } else {
            tip = new Tip(mContext, "已经是最后一页", null);
            tip.show(0);
            setRightState(false);
        }
    }

    private void setRightState(boolean show) {
        if (show) {
            btn_right.setBackgroundResource(R.drawable.yc_btn_right);
        } else {
            btn_right.setBackgroundResource(R.drawable.yc_right_pressed);
        }
        btn_right.setClickable(show);
    }

    private void qryContents(final int type) {

        if (checkParam()) {
            ReqAnnounce req = new ReqAnnounce();
            req.setCcId(Long.parseLong(GlobalField.config.getCommunityId()));
            req.setChannel(Channel.Kd);
            req.setNodeId("B");
            req.setPageIndex(pageIndex);
            req.setPageSize(pageSize);
            QryContents request = new QryContents(req, new ResponseEventHandler<RspAnnounce>() {

                @Override
                public void onResponseSuccess(RspAnnounce result) {
                    dialogUtil.closeProgressDialog();
                    if (result.getResult() == 0)
                    {
                        if (type!=ACTION_FIRST&&(result.getRowCount() / pageSize) < pageIndex) {

                            tip = new Tip(mContext, "已经是最后一页", null);
                            tip.show(0);
                            if (type == ACTION_RIGHT) {
                                setRightState(false);
                                --pageIndex;
                            }
                        } else {
                            if (result.getAnounceList() == null || result.getAnounceList().size() == 0) {
                                tip = new Tip(mContext, "没有数据！", null);
                                tip.show(0);
                                hasNext = false;
                                if (type == ACTION_RIGHT) {
                                    setRightState(false);
                                    --pageIndex;
                                }
                                return;
                            }
                            pList.clear();
                            int page = result.getRowCount() / 5;
                            if (pageIndex < page) {
                                hasNext = true;
                            } else {
                                hasNext = false;
                            }

                            for (int i = 0; i < result.getAnounceList().size(); i++) {
                                pList.add(result.getAnounceList().get(i));
                            }
                            mAdapter.clear();
                            mAdapter.addAll(pList);
                            mAdapter.notifyDataSetChanged();
                        }
                        total = result.getRowCount();
                    } else {
                        tip = new Tip(mContext, result.getResultMsg(), null);
                        tip.show(0);
                    }
                }

                @Override
                public void onResponseError(VolleyError error) {
                    dialogUtil.closeProgressDialog();
                    LogUtil.i("查询公告失败" + error.getMessage());

                }

            });

            RequestManager.addRequest(request, this);
        }


    }


}
