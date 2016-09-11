package com.ebox.ex.ui.bar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ebox.R;
import com.ebox.ex.network.model.FasterSelectMode;
import com.ebox.ex.network.model.base.type.BoxInfoType;
import com.ebox.ex.ui.BaseDeliveryActivity;
import com.ebox.ex.ui.adapter.FasterSelectRecyclerAdapter;
import com.ebox.ex.ui.fragment.OpDeliveryFragment;
import com.ebox.ex.utils.BoxInfoHelper;
import com.ebox.pub.boxctl.BoxUtils;

import java.util.List;

/**
 * Created by Android on 2015/10/23.
 */
public class SelectBoxBar extends RelativeLayout implements FasterSelectRecyclerAdapter.OnFasterSelectListener{

    private Activity mContext;

    private RecyclerView recyclerView;
    private FasterSelectRecyclerAdapter adapter;
    private FastSelectBoxListener listener;
    private int choosePos;
    private TextView tv_box;

    public SelectBoxBar(Context context) {
        super(context);
        initViews(context);
    }

    public SelectBoxBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public SelectBoxBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews(context);
    }

    private void initViews(Context context)
    {
        mContext= (Activity) context;
        //手动选择项目
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.ex_bar_selector_box, this, true);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        tv_box = (TextView) findViewById(R.id.tv_box);
        //手动选择箱门
        findViewById(R.id.bt_manual).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startManualActivity();
            }
        });

        List<FasterSelectMode> faster = BoxInfoHelper.getFaster();

        //ItemDecoration
        adapter = new FasterSelectRecyclerAdapter(faster,this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 4);

        //布局管理
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.left = 8;
                outRect.right = 8;
                outRect.top = 0;
                outRect.bottom = 8;
            }
        });
        //适配器
        recyclerView.setAdapter(adapter);
    }

    public void setBox(String boxCode) {
        if (boxCode!=null) {
            tv_box.setText(BoxUtils.getBoxDesc(boxCode));
        }else {
            tv_box.setText("");
        }
    }

    private void startManualActivity() {

       Intent intent= new Intent(mContext, BaseDeliveryActivity.class);
        intent.putExtra("tag", BaseDeliveryActivity.title_manual_select);
        mContext.startActivityForResult(intent, OpDeliveryFragment.REQUEST_CODE_MANUAL_SELECT);
    }

    @Override
    public void onBoxItemClick(BoxInfoType infoType, int pos) {
        if (listener != null)
        {
            listener.fasterSelect(infoType);
            choosePos=pos;
        }
    }

    public void clearCachePos(){
        choosePos=-1;
    }

    public void refreshChoose(BoxInfoType infoType, int action)
    {
        adapter.refreshChoose(infoType, choosePos, action);
    }

    public void setListener(FastSelectBoxListener listener) {
        this.listener = listener;
    }

    public interface  FastSelectBoxListener
    {
        void fasterSelect(BoxInfoType infoType);
    }
}
