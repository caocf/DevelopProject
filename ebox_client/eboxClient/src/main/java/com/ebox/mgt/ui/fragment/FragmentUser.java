package com.ebox.mgt.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.ebox.R;
import com.ebox.ex.database.operator.OperatorInfo;
import com.ebox.ex.database.operator.OperatorOp;
import com.ebox.mgt.ui.adapter.FragUserAdapter;
import com.ebox.pub.utils.MGViewUtil;

import java.util.ArrayList;

public class FragmentUser extends Fragment {

    private ListView list;
    private FragUserAdapter mAdapter;
    private ArrayList<OperatorInfo> mInfos;
    private TextView tv_hint;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInfos = OperatorOp.getAllOperator();
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
        mAdapter = new FragUserAdapter(getActivity());
        mAdapter.addAll(mInfos);
        list.setAdapter(mAdapter);
        if (mInfos == null || mInfos.size() == 0) {
            tv_hint.setVisibility(View.VISIBLE);
        } else {
            tv_hint.setVisibility(View.GONE);
        }
        return view;
    }

}
