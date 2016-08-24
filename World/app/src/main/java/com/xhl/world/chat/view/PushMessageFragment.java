package com.xhl.world.chat.view;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.xhl.world.R;
import com.xhl.world.chat.DBHelper;
import com.xhl.world.chat.PushMessage;
import com.xhl.world.chat.recyclerHolder.PushMessageDataHolder;
import com.xhl.world.ui.fragment.BaseAppFragment;
import com.xhl.xhl_library.ui.recyclerview.RecyclerAdapter;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sum on 16/2/24.
 */
@ContentView(R.layout.chat_fragment_push)
public class PushMessageFragment extends BaseAppFragment {

    @ViewInject(R.id.chat_message_recycler_view)
    private RecyclerView mRecyclerView;

    private LinearLayoutManager mLayoutManager;

    private RecyclerAdapter mAdapter;

    @Override
    protected void initParams() {
        mAdapter = new RecyclerAdapter(getContext());
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void createData(List<PushMessage> data) {

        if (null == data || data.size() <= 0) {
            return;
        }

        List<RecyclerDataHolder> holders = new ArrayList<>();
        for (PushMessage msg : data) {
            PushMessageDataHolder holder = new PushMessageDataHolder(msg);
            holders.add(holder);
        }

        mAdapter.setDataHolders(holders);
    }

    @Override
    public void onResume() {
        super.onResume();
        createData(DBHelper.getInstance().queryAllMessage());
    }
}
