package com.xhl.bqlh.view.ui.bar;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.TextView;

import com.xhl.bqlh.Api.ApiControl;
import com.xhl.bqlh.R;
import com.xhl.bqlh.model.GarbageModel;
import com.xhl.bqlh.model.OperatorMenu;
import com.xhl.bqlh.model.base.ResponseModel;
import com.xhl.bqlh.model.event.AdEvent;
import com.xhl.bqlh.utils.ToastUtil;
import com.xhl.bqlh.view.base.BaseBar;
import com.xhl.bqlh.view.base.Common.DefaultCallback;
import com.xhl.bqlh.view.helper.EventHelper;
import com.xhl.xhl_library.ui.recyclerview.RecyclerAdapter;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;
import com.xhl.xhl_library.ui.recyclerview.layoutManager.FullGridViewManager;
import com.zhy.autolayout.utils.AutoUtils;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Summer on 2016/7/19.
 */
public class HomeOperatorBar extends BaseBar {

    private RecyclerView mRecyclerView;
    private boolean mHasLoad = false;

    public HomeOperatorBar(Context context) {
        super(context);
    }

    @Override
    protected boolean autoInject() {
        return false;
    }


    @Override
    protected void initParams() {
        mRecyclerView = _findViewById(R.id.recycler_view);
    }

    public void onBindData() {
        if (mHasLoad) {
            return;
        }
        ApiControl.getApi().homeMenu(new DefaultCallback<ResponseModel<GarbageModel>>() {
            @Override
            public void success(ResponseModel<GarbageModel> result) {
                mHasLoad = true;
                List homeMenu = result.getObj().getHomeMenu();
                if (homeMenu == null || homeMenu.size() == 0) {
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            nullDo();
                        }
                    }, 200);
                } else {
                    loadMenu(homeMenu);
                }
            }

            @Override
            public void finish() {

            }
        });
    }

    private void nullDo() {
        MarginLayoutParams layoutParams = (MarginLayoutParams) getLayoutParams();
        layoutParams.topMargin = 0;
        layoutParams.height = 0;
        setLayoutParams(layoutParams);
    }

    private void loadMenu(List<OperatorMenu> data) {
        mRecyclerView.setLayoutManager(new FullGridViewManager(mContext, 4));
        List<RecyclerDataHolder> holders = new ArrayList<>();
        for (OperatorMenu menu : data) {
            holders.add(new MenuDataHolder(menu));
        }
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(new RecyclerAdapter(mContext, holders));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.bar_home_operator;
    }

    static class MenuDataHolder extends RecyclerDataHolder<OperatorMenu> {
        public MenuDataHolder(OperatorMenu data) {
            super(data);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(Context context, ViewGroup parent, int position) {
            View content = View.inflate(context, R.layout.item_home_menu_operator, null);
            content.setLayoutParams(new ViewGroup.LayoutParams(-1, 160));
            AutoUtils.auto(content);

            return new MenuView(content);
        }

        @Override
        public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, OperatorMenu data) {
            MenuView h = (MenuView) vHolder;
            h.onBindData(data);
        }

        static class MenuView extends RecyclerViewHolder implements OnClickListener {

            private ImageView image;
            private TextView name;

            private OperatorMenu menu;

            public MenuView(View view) {
                super(view);
                view.setOnClickListener(this);
                image = (ImageView) view.findViewById(R.id.menu_icon);
                name = (TextView) view.findViewById(R.id.menu_name);
            }

            public void onBindData(OperatorMenu menu) {
                this.menu = menu;
                x.image().bind(image, menu.getMenuPicture());
                name.setText(menu.getMenuName());
            }

            @Override
            public void onClick(View v) {
                if (menu != null) {
                    if (!URLUtil.isNetworkUrl(menu.getMenuUrl())) {
                        ToastUtil.showToastShort(R.string.building);
                        return;
                    }
                    AdEvent e = new AdEvent();
                    e.adType = AdEvent.type_web;
                    e.data = menu.getMenuUrl();
                    EventHelper.postDefaultEvent(e);
                }
            }
        }
    }
}
