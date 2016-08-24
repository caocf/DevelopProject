package com.xhl.world.ui.fragment;

import android.content.DialogInterface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.xhl.world.R;
import com.xhl.world.api.ApiControl;
import com.xhl.world.model.AddressModel;
import com.xhl.world.model.Base.Response;
import com.xhl.world.model.Base.ResponseModel;
import com.xhl.world.ui.event.AddressEvent;
import com.xhl.world.ui.recyclerHolder.AddressItemDataHolder;
import com.xhl.world.ui.utils.DialogMaker;
import com.xhl.world.ui.utils.SnackMaker;
import com.xhl.xhl_library.ui.recyclerview.RecyclerAdapter;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.swipyrefresh.SwipyRefreshLayout;
import com.xhl.xhl_library.ui.swipyrefresh.SwipyRefreshLayoutDirection;
import com.xhl.xhl_library.ui.view.RippleView;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sum on 15/12/28.
 */
@ContentView(R.layout.fragment_my_address)
public class AddressMyFragment extends BaseAppFragment {

    @ViewInject(R.id.title_name)
    private TextView title_name;

    @ViewInject(R.id.address_recycler_view)
    private RecyclerView mRecyclerView;

    @ViewInject(R.id.swipy_refresh_layout)
    private SwipyRefreshLayout mSwipRefresh;

    @Event(value = R.id.title_back, type = RippleView.OnRippleCompleteListener.class)
    private void onBackClick(View view) {
        getSumContext().popTopFragment(null);
    }

    @Event(value = R.id.ripple_add_address, type = RippleView.OnRippleCompleteListener.class)
    private void onAddAdressClick(View view) {
        getSumContext().pushFragmentToBackStack(AddressEditFragment.class, null);
    }

    private LinearLayoutManager mManager;
    private RecyclerAdapter mAdapter;
    private boolean isFirstLoad = true;
    private boolean isLoading = false;
    private int mEnterTag = 0;

    @Override
    protected void initParams() {
        title_name.setText("我的收货地址");

        mManager = new LinearLayoutManager(getContext());
        mAdapter = new RecyclerAdapter(getContext());

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mManager);
        mRecyclerView.setAdapter(mAdapter);

        initPullRefresh();

        loadAddressData();
    }

    private void initPullRefresh() {
        mSwipRefresh.setColorSchemeResources(R.color.app_green, R.color.app_blue);
        mSwipRefresh.setDirection(SwipyRefreshLayoutDirection.TOP);
        mSwipRefresh.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {

                if (direction == SwipyRefreshLayoutDirection.TOP) {
                    loadAddressData();
                }
            }
        });
    }

    private void loadAddressData() {
        if (isFirstLoad) {
            showLoadingDialog();
        }
        if (isLoading) {
            return;
        }
        isLoading = true;
        ApiControl.getApi().addressQuery(new Callback.CommonCallback<ResponseModel<Response<AddressModel>>>() {
            @Override
            public void onSuccess(ResponseModel<Response<AddressModel>> result) {
                if (result.isSuccess()) {
                    List<AddressModel> rows = result.getResultObj().getRows();
                    if (rows.size() == 0) {

                        mAdapter.setDataHolders(null);
                        SnackMaker.shortShow(title_name,"还没有您的地址,赶快添加吧");

                    } else {
                        createShowData(rows);
                    }
                } else {
                    SnackMaker.shortShow(title_name,result.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                SnackMaker.shortShow(title_name,ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                mSwipRefresh.setRefreshing(false);
                isFirstLoad = false;
                isLoading = false;
                hideLoadingDialog();
            }
        });

    }

    private void createShowData(List<AddressModel> data) {
        List<RecyclerDataHolder> h = new ArrayList<>();
        for (AddressModel model : data) {
            AddressItemDataHolder holder = new AddressItemDataHolder(model);
            h.add(holder);
        }
        mAdapter.setDataHolders(h);
    }

    @Override
    public void onEnter(Object data) {
        if (data != null && data instanceof Integer) {
            mEnterTag = (Integer) data;
        }
    }

    @Override
    public void onBackWithData(Object data) {
        //在编辑添加界面返回后下拉地址数据
        loadAddressData();
    }

    //删除地址
    private void onDeleteAddress(final AddressModel addressModel) {
        String title = getString(R.string.dialog_title);
        String msg = "您确定删除该地址？";
        DialogMaker.showAlterDialog(getContext(), title, msg, null, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteAddress(addressModel);
            }
        });
    }

    private void deleteAddress(AddressModel addressModel) {
        showLoadingDialog();

        ApiControl.getApi().addressDelete(addressModel.getId(), new Callback.CommonCallback<ResponseModel<AddressModel>>() {
            @Override
            public void onSuccess(ResponseModel<AddressModel> result) {
                if (result.isSuccess()) {
                    loadAddressData();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                SnackMaker.shortShow(title_name,R.string.network_error);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                hideLoadingDialog();
            }
        });
    }

    //设置默认地址
    private void onSetDefault(AddressModel addressModel) {

        showLoadingDialog();

        ApiControl.getApi().addressUpdateDefault(addressModel.getId(), new Callback.CommonCallback<ResponseModel<AddressModel>>() {
            @Override
            public void onSuccess(ResponseModel<AddressModel> result) {
                if (result.isSuccess()) {
                    loadAddressData();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                SnackMaker.shortShow(title_name,R.string.network_error);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                hideLoadingDialog();
            }
        });

    }

    public void onEvent(AddressEvent event) {
        switch (event.actionType) {
            case AddressEvent.Type_Default_address:
                onSetDefault(event.model);
                break;

            case AddressEvent.Type_delete_address:
                onDeleteAddress(event.model);
                break;

            case AddressEvent.Type_edit_address:
                getSumContext().pushFragmentToBackStack(AddressEditFragment.class, event.model);
                break;

            case AddressEvent.Type_select_address:
                if (mEnterTag == 0) {//默认点击退出当前界面
                    getSumContext().popTopFragment(null);
                }

                break;

        }
    }
}
