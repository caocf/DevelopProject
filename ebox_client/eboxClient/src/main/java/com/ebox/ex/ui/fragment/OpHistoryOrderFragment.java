package com.ebox.ex.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;
import com.ebox.Anetwork.RequestManager;
import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.R;
import com.ebox.ex.network.model.RspOperatorQueryItem;
import com.ebox.ex.network.request.RequestGetHistory;
import com.ebox.ex.network.request.RequestGetHistoryByInput;
import com.ebox.ex.ui.bar.EboxKeyboard;
import com.ebox.ex.ui.bar.RecycleOrderBar;
import com.ebox.ex.ui.base.BaseOpFragment;
import com.ebox.pub.utils.DialogUtil;
import com.ebox.pub.utils.LogUtil;
import com.ebox.pub.utils.Tip;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Android on 2015/10/22.
 */
public class OpHistoryOrderFragment extends BaseOpFragment implements CompoundButton.OnCheckedChangeListener {

    private EditText ed_item_id;
    private RecycleOrderBar recycle_order;
    private RadioButton rb_today, rb_week, rb_month;
    private RadioButton rb_order_all, rb_order_unpick;
    private RelativeLayout rl_input;
    private EboxKeyboard keyboard;

    private String start_time;
    private String end_time;
    private String query_type;//100: 所有  0: 未取 4: 快递员 5: 用户 6: 管理员
    private boolean isQueryByTime;
    private int page,page_size;
    private String next_cursor;
    private String item_id;
    private DialogUtil dialogUtil;
    private boolean isLast ,isFirstQuery;
    private SimpleDateFormat sdf;

    public static BaseOpFragment newInstance() {
        BaseOpFragment fragment = new OpHistoryOrderFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        page_size=10;
    }

    @Override
    protected int getViewId() {
        return R.layout.ex_fragment_op_history_order;
    }

    @Override
    protected void initView(View view) {
        dialogUtil = new DialogUtil();
        dialogUtil.createProgressDialog(context);

        view.findViewById(R.id.btn_query).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query();
            }
        });
        rl_input = (RelativeLayout) view.findViewById(R.id.rl_input);
        rl_input.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideInput();
                return true;
            }
        });
        keyboard = (EboxKeyboard) view.findViewById(R.id.keyboard);
        keyboard.hideAdv();
        recycle_order = (RecycleOrderBar) view.findViewById(R.id.recycle_order);
        recycle_order.initRecycler(false);
        recycle_order.setScrollListener(listener);
        rb_today = (RadioButton) view.findViewById(R.id.rb_today);
        rb_today.setOnCheckedChangeListener(this);
        rb_week = (RadioButton) view.findViewById(R.id.rb_week);
        rb_week.setOnCheckedChangeListener(this);
        rb_month = (RadioButton) view.findViewById(R.id.rb_month);
        rb_month.setOnCheckedChangeListener(this);
        rb_order_all = (RadioButton) view.findViewById(R.id.rb_order_all);
        rb_order_all.setOnCheckedChangeListener(this);
        rb_order_unpick = (RadioButton) view.findViewById(R.id.rb_order_unpick);
        rb_order_unpick.setOnCheckedChangeListener(this);

        ed_item_id = (EditText) view.findViewById(R.id.ed_item_id);
        //选择状态
        rb_today.setChecked(true);
        rb_order_unpick.setChecked(true);

        ed_item_id.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ed_item_id.requestFocus();
                showInput();
                return true;
            }
        });
        keyboard.setEditText(ed_item_id);

        //查询默认选项
        query_by_time();
    }

    private void hideInput() {
        if (rl_input.getVisibility() == View.VISIBLE) {
            rl_input.setVisibility(View.INVISIBLE);
        }
    }

    private void showInput() {
        if (rl_input.getVisibility() != View.VISIBLE) {
            rl_input.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        hideInput();
        end_time = getTime(new Date());
        recycle_order.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        hideInput();
    }

    private String getTime(Date format) {
        return sdf.format(format);
    }

    private boolean checkSelect() {
        if (start_time.length() > 0 && end_time.length() > 0 && query_type != null) {
            return true;
        } else {
            return false;
        }
    }

    private void query() {

        hideInput();
        item_id= ed_item_id.getText().toString();
        //清理缓存数据
        page = 0;
        next_cursor = null;
        isLast=false;
        isFirstQuery=false;
        recycle_order.clearData();

        if (item_id.length() > 0)//订单模糊查询
        {
            isQueryByTime = false;
            query_by_item();
            ed_item_id.getText().clear();
            return;
        }
        //日期查询
        if (checkSelect()) {
            Log.d("query", "query history start_time:" + start_time + " end_time:" + end_time + " query_type:" + query_type);
            isQueryByTime = true;
            query_by_time();
        } else {
            new Tip(context, "查询日期错误,请重新选择", null).show(0);
        }
    }

    private void query_by_time() {
        dialogUtil.showProgressDialog();
        HashMap<String, Object> prams = new HashMap<String, Object>();
        prams.put("page", page);
        prams.put("page_size", page_size);
        prams.put("start_date", start_time);
        prams.put("end_date", end_time);
        if (next_cursor!=null && next_cursor.length()>0) {
            prams.put("cursor", next_cursor);
        }
        prams.put("state", query_type);

        RequestGetHistory requestGetHistory = new RequestGetHistory(prams, new ResponseEventHandler<RspOperatorQueryItem>() {
            @Override
            public void onResponseSuccess(RspOperatorQueryItem result) {
                dialogUtil.closeProgressDialog();
                if (result.isSuccess())
                {
                    page++;
                    next_cursor = result.getData().getNext_cursor();
                    //返回的集合数小于个数表示已经到最后
                    int size = result.getData().getItems().size();
                    if (size > 0)
                    {
                        recycle_order.addDatas(result.getData().getItems());
                    } else
                    {
                        {
                            new Tip(context, "没有查询到更多数据", null).show(0);
                        }
                        isLast = true;
                    }
                } else {
                    new Tip(context, result.getMsg(), null).show(0);
                }

                recycle_order.onResume();
            }

            @Override
            public void onResponseError(VolleyError error) {
                dialogUtil.closeProgressDialog();
                new Tip(context, context.getString(R.string.pub_connect_failed), null).show(0);
                LogUtil.e(error.getMessage());
            }
        });

        RequestManager.addRequest(requestGetHistory, null);

    }

    private void query_by_item() {
        HashMap<String, Object> prams = new HashMap<String, Object>();
        prams.put("page", page);
        prams.put("page_size",page_size);
        prams.put("search", item_id);
        if (next_cursor!=null && next_cursor.length()>0) {
            prams.put("cursor", next_cursor);
        }

        RequestGetHistoryByInput req = new RequestGetHistoryByInput(prams, new ResponseEventHandler<RspOperatorQueryItem>() {
            @Override
            public void onResponseSuccess(RspOperatorQueryItem result) {
                if (result.isSuccess()) {
                    page++;
                    next_cursor = result.getData().getNext_cursor();
                    //返回的集合数小于个数表示已经到最后
                    int size = result.getData().getItems().size();
                    if (size > 0) {
                        recycle_order.addDatas(result.getData().getItems());
                    } else
                    {
                        new Tip(context, "没有查询到更多数据", null).show(0);
                        isLast=true;
                    }
                } else {
                    new Tip(context, result.getMsg(), null).show(0);
                }
                recycle_order.onResume();
            }

            @Override
            public void onResponseError(VolleyError error) {
                new Tip(context, context.getString(R.string.pub_connect_failed), null).show(0);
                LogUtil.e(error.getMessage());
            }
        });

        RequestManager.addRequest(req, null);

    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Calendar calendar;
        hideInput();
        ed_item_id.getText().clear();
        switch (buttonView.getId()) {

            case R.id.rb_today:
                if (isChecked) {
                    calendar = Calendar.getInstance();
                    calendar.add(Calendar.DAY_OF_YEAR, -1);
                    start_time = getTime(calendar.getTime());
                }
                break;
            case R.id.rb_week:
                if (isChecked) {
                    calendar = Calendar.getInstance();
                    calendar.add(Calendar.DAY_OF_YEAR, -7);
                    start_time = getTime(calendar.getTime());
                }
                break;
            case R.id.rb_month:
                if (isChecked) {
                    calendar = Calendar.getInstance();
                    calendar.add(Calendar.MONTH, -1);
                    start_time = getTime(calendar.getTime());
                }
                break;
            case R.id.rb_order_all:
                if (isChecked) {
                    query_type = "100";
                }
                break;
            case R.id.rb_order_unpick:
                if (isChecked) {
                    query_type = "0";
                }
                break;
        }
    }

    RecyclerView.OnScrollListener listener=new RecyclerView.OnScrollListener() {

        private int lastpos;
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            //停止滚动
            if (newState == RecyclerView.SCROLL_STATE_IDLE && !isLast)
            {
                int itemCount = recyclerView.getAdapter().getItemCount();
                if ( lastpos + 1 >= itemCount) {
                    if (isQueryByTime)
                    {
                        query_by_time();
                    }else {
                        query_by_item();
                    }
                }
            }

        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
            if (manager instanceof  LinearLayoutManager) {
                lastpos= ((LinearLayoutManager) manager).findLastVisibleItemPosition();
            }
        }
    };

}
