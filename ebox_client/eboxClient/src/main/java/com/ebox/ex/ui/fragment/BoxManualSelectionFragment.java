package com.ebox.ex.ui.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ebox.R;
import com.ebox.ex.network.model.base.type.BoxInfoType;
import com.ebox.ex.network.model.enums.RackType;
import com.ebox.ex.ui.adapter.ManualSelectRecyclerAdapter;
import com.ebox.ex.utils.BoxInfoHelper;
import com.ebox.ex.utils.SharePreferenceHelper;
import com.ebox.pub.service.global.GlobalField;
import com.ebox.pub.utils.MGViewUtil;

import java.util.ArrayList;

public class BoxManualSelectionFragment extends Fragment implements View.OnClickListener, ManualSelectRecyclerAdapter.onItemClickListener {

    private OnFragmentSelectionListener mListener;

    private RecyclerView mRecycler;
    private ManualSelectRecyclerAdapter mAdapter;
    private GridLayoutManager mGridLayoutManager;

    private TextView tv_seq;
    private Button bt_last;
    private Button bt_next;
    private int rackIndex = 0;

    public static BoxManualSelectionFragment newInstance() {
        BoxManualSelectionFragment fragment = new BoxManualSelectionFragment();
        return fragment;
    }

    public BoxManualSelectionFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = initView(inflater, container);

        MGViewUtil.scaleContentView((ViewGroup) view);

        initData();
        return view;
    }

    private void initData() {

        mAdapter = new ManualSelectRecyclerAdapter(null);

        // mRecycler.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));

        mGridLayoutManager = new GridLayoutManager(getActivity(), 4);

        mRecycler.setLayoutManager(mGridLayoutManager);

        mRecycler.setAdapter(mAdapter);

        setListener();

        changeRack();
    }

    private void setListener() {
        mAdapter.setOnItemClickListener(this);
    }

    private View initView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.ex_fragment_box_manual_select, container, false);
        mRecycler = (RecyclerView) view.findViewById(R.id.recyclerView);
        tv_seq = (TextView) view.findViewById(R.id.tv_seq);
        bt_last = (Button) view.findViewById(R.id.bt_last);
        bt_last.setOnClickListener(this);
        bt_next = (Button) view.findViewById(R.id.bt_next);
        bt_next.setOnClickListener(this);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentSelectionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentSelectionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public void lastNextShow() {
        if (rackIndex > 0) {
            bt_last.setBackgroundResource(R.drawable.ex_rack_box_last);
        } else {
            bt_last.setBackgroundResource(R.drawable.ex_last_disabled);
        }

        if (rackIndex < (GlobalField.boxInfoLocal.size() - 1)) {
            bt_next.setBackgroundResource(R.drawable.ex_rack_box_next);
        } else {
            bt_next.setBackgroundResource(R.drawable.ex_next_disabled);
        }
    }

    private void changeRack() {
        lastNextShow();
        int rackNumb = GlobalField.boxInfoLocal.get(rackIndex).getBoxes().length;
        ArrayList<BoxInfoType> boxData = BoxInfoHelper.getBoxDataByRack(rackIndex);

        if (boxData == null) {
            return;
        }

        int rackNum = SharePreferenceHelper.getRackNum(rackIndex + 1);
        mGridLayoutManager.setSpanCount(rackNum);

        StringBuilder builder=new StringBuilder();
        if (rackIndex < 9)
        {
            builder.append("0");
        }
        builder.append(rackIndex + 1);

        builder.append(" 组").append(" ").append(" ").append(" ");
        BoxInfoType boxInfoType = boxData.get(0);

        if (boxInfoType.getRackType() == RackType.box_bw)
        {
            builder.append("保温柜");
            tv_seq.setTextColor(getResources().getColor(R.color.ex_box_bw));
        }
        else if (boxInfoType.getRackType() == RackType.box_sx)
        {
            builder.append("生鲜柜");
            tv_seq.setTextColor(getResources().getColor(R.color.ex_box_sx));
        } else {
            builder.append("快递柜");
            tv_seq.setTextColor(getResources().getColor(R.color.ex_box_kd));
        }

        tv_seq.setText(builder.toString());

        mAdapter.setData(boxData,rackNumb);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.bt_last:
                if (rackIndex > 0) {
                    rackIndex--;
                    changeRack();
                }
                break;
            case R.id.bt_next:
                if (rackIndex < (GlobalField.boxInfoLocal.size() - 1)) {
                    rackIndex++;
                    changeRack();
                }
                break;
        }

    }

    @Override
    public void onBoxItemClick(BoxInfoType infoType, int pos) {

        if (mListener != null) {
            mListener.onBoxItemSelection(infoType);
        }
    }


    public interface OnFragmentSelectionListener {

         void onBoxItemSelection(BoxInfoType infoType);

    }

}
