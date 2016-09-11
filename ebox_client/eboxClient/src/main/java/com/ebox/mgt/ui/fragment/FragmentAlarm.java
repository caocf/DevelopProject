package com.ebox.mgt.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.ebox.R;
import com.ebox.mgt.ui.adapter.FragAlarmAdapter;
import com.ebox.pub.database.alarm.Alarm;
import com.ebox.pub.database.alarm.AlarmOp;
import com.ebox.pub.utils.MGViewUtil;

import java.util.ArrayList;

public class FragmentAlarm extends Fragment {

    private ListView list;
    private TextView tv_hint;
    private FragAlarmAdapter mAdapter;
    private ArrayList<Alarm> mData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mData = AlarmOp.getAllAlarm();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = initView(inflater);

        return view;
    }

    private View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.mgt_fragment_sqlite_query, null);
        MGViewUtil.scaleContentView((ViewGroup) view);
        list = (ListView) view.findViewById(R.id.list);
        tv_hint = (TextView) view.findViewById(R.id.tv_hint);
        mAdapter = new FragAlarmAdapter(getActivity());
        mAdapter.addAll(mData);
        list.setAdapter(mAdapter);
        if (mData == null || mData.size() == 0) {
            tv_hint.setVisibility(View.VISIBLE);
        } else {
            tv_hint.setVisibility(View.GONE);
        }

        return view;
    }

}
