package com.xhl.bqlh.business.view.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ExpandableListView;

import com.xhl.bqlh.business.Api.ApiControl;
import com.xhl.bqlh.business.Model.Base.ResponseModel;
import com.xhl.bqlh.business.Model.BusinessTypeModel;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.utils.SnackUtil;
import com.xhl.bqlh.business.view.base.BaseAppActivity;
import com.xhl.bqlh.business.view.ui.adapter.ExpandAdapter;
import com.xhl.xhl_library.ui.SwipeRefresh.SwipeRefreshLayout;
import com.xhl.xhl_library.ui.SwipeRefresh.SwipeRefreshLayoutDirection;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by Sum on 16/4/16.
 */
@ContentView(R.layout.activity_shop_select_type)
public class SelectShopTypeActivity extends BaseAppActivity {

    @ViewInject(R.id.ex_list_view)
    private ExpandableListView listView;

    @ViewInject(R.id.swipe_refresh_layout)
    private SwipeRefreshLayout mRefresh;

    private List<BusinessTypeModel> mTypes;

    @Override
    protected void initParams() {
        super.initToolbar();
        setTitle(R.string.customer_shop_type);
        super.initRefreshStyle(mRefresh, SwipeRefreshLayoutDirection.TOP);
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                if (mTypes != null) {
                    BusinessTypeModel model = mTypes.get(groupPosition).getChildren().get(childPosition);
                    Intent intent = new Intent();
                    intent.putExtra("id", model.getId());
                    intent.putExtra("name", model.getName());
                    setResult(RESULT_OK, intent);
                    SelectShopTypeActivity.this.finish();
                }
                return true;
            }
        });
        loadTypes();
    }

    @Override
    public void onRefreshLoadData() {
        loadTypes();
    }

    private void loadTypes() {

        ApiControl.getApi().registerBusinessType(new Callback.CommonCallback<ResponseModel<BusinessTypeModel>>() {
            @Override
            public void onSuccess(ResponseModel<BusinessTypeModel> result) {
                if (result.isSuccess()) {
                    mTypes = result.getObjList();
                    listView.setAdapter(new ExpandAdapter(SelectShopTypeActivity.this, mTypes));
                } else {
                    SnackUtil.shortShow(mToolbar, result.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                SnackUtil.shortShow(mToolbar, ex.getMessage());
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
