package com.ebox.ex.ui.bar;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.ebox.R;

/**
 * Created by prin on 2015/10/28.
 */
public class ResultTableBar extends LinearLayout {

    private Activity mContext;
    private RecyclerView recyclerView;


    public ResultTableBar(Context context) {
        this(context,null);
        initViews(context);
    }


    public ResultTableBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        initViews(context);
    }

    public ResultTableBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews(context);
    }


    private void initViews(Context context) {
        mContext= (Activity) context;

        //获得信息显示界面
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.ex_bar_result_table, this, true);


    }



}
