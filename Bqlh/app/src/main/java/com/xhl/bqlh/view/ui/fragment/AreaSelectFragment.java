package com.xhl.bqlh.view.ui.fragment;

import android.view.View;
import android.widget.ExpandableListView;

import com.xhl.bqlh.Api.ApiControl;
import com.xhl.bqlh.R;
import com.xhl.bqlh.data.PreferenceData;
import com.xhl.bqlh.model.AreaModel;
import com.xhl.bqlh.model.GarbageModel;
import com.xhl.bqlh.model.event.CommonEvent;
import com.xhl.bqlh.view.base.BaseAppFragment;
import com.xhl.bqlh.view.helper.EventHelper;
import com.xhl.bqlh.view.ui.adapter.ExpandAdapter;
import com.xhl.xhl_library.ui.SwipeRefresh.SwipeRefreshLayout;
import com.xhl.xhl_library.ui.SwipeRefresh.SwipeRefreshLayoutDirection;
import com.xhl.xhl_library.utils.log.Logger;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by Summer on 2016/7/29.
 */
@ContentView(R.layout.fragment_area_select)
public class AreaSelectFragment extends BaseAppFragment {
    @ViewInject(R.id.ex_list_view)
    private ExpandableListView listView;

    @ViewInject(R.id.swipe_refresh_layout)
    private SwipeRefreshLayout mRefresh;

    private List<AreaModel> mTypes;

    @Override
    protected void initParams() {

        super.initBackBar("选择区域", true, false);

        super.initRefreshStyle(mRefresh, SwipeRefreshLayoutDirection.TOP);

        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                if (mTypes != null) {
                    AreaModel areaModel = mTypes.get(groupPosition).getChildren().get(childPosition);
                    PreferenceData.getInstance().areaId(areaModel.getId());
                    PreferenceData.getInstance().areaName(areaModel.getAreaName());
                    EventHelper.postCommonEvent(CommonEvent.ET_RELOAD_ADS);
                    getActivity().finish();
                }
                return true;
            }
        });
        mRefresh.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefresh.setRefreshing(true);
                onRefreshLoadData();
            }
        }, 300);
    }


    @Override
    public void onRefreshLoadData() {
        ApiControl.getApi().homeArea(new Callback.CommonCallback<GarbageModel>() {
            @Override
            public void onSuccess(GarbageModel result) {
                mTypes = result.getCityList();
                listView.setAdapter(new ExpandAdapter(getContext(), mTypes));
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Logger.e(ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                mRefresh.setRefreshing(false);
            }
        });

    }
}
