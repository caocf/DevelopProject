package com.xhl.bqlh.view.ui.fragment;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xhl.bqlh.Api.ApiControl;
import com.xhl.bqlh.R;
import com.xhl.bqlh.model.SearchFastModel;
import com.xhl.bqlh.model.base.ResponseModel;
import com.xhl.bqlh.model.type.SearchTyp;
import com.xhl.bqlh.view.base.BaseAppFragment;
import com.xhl.bqlh.view.helper.EventHelper;
import com.xhl.xhl_library.ui.recyclerview.RecyclerAdapter;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sum on 16/7/5.
 */
@ContentView(R.layout.fragment_search_fast)
public class SearchFastFragment extends BaseAppFragment implements Callback.CommonCallback<ResponseModel<SearchFastModel>> {

    @ViewInject(R.id.recycler_view)
    private RecyclerView recyclerView;

    private RecyclerAdapter mAdapter;

    @Override
    protected void initParams() {

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mAdapter = new RecyclerAdapter(getContext());

        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(mAdapter);

    }

    public void showFastData(List<SearchFastModel> data) {
        if (data.size() > 0) {
            List<RecyclerDataHolder> holders = new ArrayList<>();
            for (SearchFastModel search : data) {
                search.searchTyp = mSearchType;
                holders.add(new SearchFastItemDataHolder(search));
            }
            mAdapter.setDataHolders(holders);
        }
    }

    public void hideSelf() {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.hide(this).commit();
    }

    private Cancelable mLast;

    private int mSearchType;

    public void search(String text, int searchType) {
        if (mLast != null && !mLast.isCancelled()) {
            mLast.cancel();
        }
        mSearchType = searchType;
        mLast = ApiControl.getApi().searchFast(String.valueOf(searchType), text, this);
    }

    private void changeVisible() {
        boolean isNeedShow = false;
        if (mAdapter.getItemCount() > 0) {
            isNeedShow = true;
        }
        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (isNeedShow && isHidden()) {
            transaction.show(this).commit();
        } else if (!isNeedShow && !isHidden()) {
            transaction.hide(this).commit();
        }
    }


    @Override
    public void onSuccess(ResponseModel<SearchFastModel> result) {
        if (result.isSuccess()) {
            List<SearchFastModel> objList = result.getObjList();
            showFastData(objList);
            changeVisible();
        }
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {

    }

    @Override
    public void onCancelled(CancelledException cex) {

    }

    @Override
    public void onFinished() {

    }

    private static class SearchFastItemDataHolder extends RecyclerDataHolder<SearchFastModel> {
        public SearchFastItemDataHolder(SearchFastModel data) {
            super(data);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(Context context, ViewGroup parent, int position) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_serach_fast, null);
            view.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
            return new SearchResViewHolder(view);
        }

        @Override
        public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, SearchFastModel data) {
            SearchResViewHolder holder = (SearchResViewHolder) vHolder;
            holder.onBindData(data);
        }

        static class SearchResViewHolder extends RecyclerViewHolder implements View.OnClickListener {
            TextView btn_search_res;
            private SearchFastModel mData;

            public SearchResViewHolder(View view) {
                super(view);
                btn_search_res = (TextView) view.findViewById(R.id.tv_search_res);
                view.setOnClickListener(this);
            }

            public void onBindData(SearchFastModel search) {
                if (search == null) {
                    return;
                }
                mData = search;
                btn_search_res.setText(search.getName());
            }

            @Override
            public void onClick(View v) {
                if (mData != null) {
                    if (mData.searchTyp == SearchTyp.search_shop) {
                        EventHelper.postShop(mData.getSid());
                    } else if (mData.searchTyp == SearchTyp.search_product) {
                        EventHelper.postProduct(mData.getId());
                    }
                }
            }
        }
    }

}

