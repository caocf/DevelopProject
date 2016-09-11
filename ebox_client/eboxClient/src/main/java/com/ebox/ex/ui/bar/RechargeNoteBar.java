package com.ebox.ex.ui.bar;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.ebox.Anetwork.RequestManager;
import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.R;
import com.ebox.ex.network.model.req.RspGetNote;
import com.ebox.ex.network.request.RequestGetNote;
import com.ebox.ex.ui.adapter.RechargeNoteAdapter;
import com.ebox.ex.utils.OperatorHelper;
import com.ebox.pub.utils.DialogUtil;
import com.ebox.pub.utils.LogUtil;
import com.ebox.pub.utils.Tip;

import java.util.HashMap;

/**
 * Created by prin on 2015/10/29.
 * 充值记录界面
 */
public class RechargeNoteBar extends LinearLayout {

    private ListView lv_note;


    private RechargeNoteAdapter mAdapter;
    private DialogUtil dialogUtil;
    private Activity context;
    private int page,page_size;
    private boolean isLast; //数据是否结束

    public RechargeNoteBar(Context context) {
        super(context);
        this.context= (Activity) context;
        initView(context);
    }

    public RechargeNoteBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public RechargeNoteBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    private void initView(Context context) {
        page_size=10;
        page=0;
        isLast=false;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.ex_bar_note, this, true);

        lv_note= (ListView) findViewById(R.id.lv_ebn_note);

        mAdapter=new RechargeNoteAdapter(context);
        lv_note.setAdapter(mAdapter);
        lv_note.setOnScrollListener(listener);

        dialogUtil=new DialogUtil();
        dialogUtil.createProgressDialog((Activity) context);
        
        initData();
    }

    ListView.OnScrollListener listener=new AbsListView.OnScrollListener() {
        private int lastpos;
        @Override
        public void onScrollStateChanged(AbsListView absListView, int i) {
            //SCROLL_STATE_IDLE  屏幕停止滚动

            //停止滚动  并且数据标识并未结束则请求下页数据
            if (i==SCROLL_STATE_IDLE&&!isLast){
                int itemCount=lv_note.getAdapter().getCount();
                if (lastpos+1>=itemCount){
                    getNote();
                }

            }
        }

        @Override
        public void onScroll(AbsListView absListView, int i, int i1, int i2) {
            lastpos=lv_note.getLastVisiblePosition();
        }
    };


    private void initData() {
        dialogUtil.showProgressDialog();
        if (OperatorHelper.cacheCookie!=null){
            getNote();
        }else{
            OperatorHelper.silenceLogin(new OperatorHelper.LoginResponseListener() {
                @Override
                public void success() {
                    getNote();
                }

                @Override
                public void failed() {
                    dialogUtil.closeProgressDialog();
                    new Tip(context,getResources().getString(R.string.pub_connect_failed),null).show(0);
                }
            });
        }
    }

    /**
     * 获得充值记录
     */
    private void getNote() {
        HashMap<String,Object> params=new HashMap<String, Object>();
        params.put("page",page);
        params.put("page_size",page_size);

        RequestGetNote request=new RequestGetNote(params, new ResponseEventHandler<RspGetNote>() {
            @Override
            public void onResponseSuccess(RspGetNote result) {
                dialogUtil.closeProgressDialog();
                if (result.isSuccess()){
                    page++;
                    //返回的集合数小于个数表示已经到最后
                    int size=result.getData().getItems().size();
                    if (size>0){
                        mAdapter.addAll(result.getData().getItems());
                        mAdapter.notifyDataSetChanged();
                    }else{
                        {
                            new Tip(context, "没有查询到更多数据", null).show(0);
                        }
                        isLast=true;
                    }
                }else{
                    {
                        new Tip(context, "没有查询到更多数据", null).show(0);
                    }
                }

            }

            @Override
            public void onResponseError(VolleyError error) {
                dialogUtil.closeProgressDialog();
                new Tip(context, context.getString(R.string.pub_connect_failed), null).show(0);
                LogUtil.e(error.getMessage());
            }
        });

        RequestManager.addRequest(request, null);

    }


}
