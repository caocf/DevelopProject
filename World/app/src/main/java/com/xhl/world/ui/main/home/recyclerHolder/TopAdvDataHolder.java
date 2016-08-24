package com.xhl.world.ui.main.home.recyclerHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.xhl.world.model.AdvModel;
import com.xhl.world.ui.event.EventBusHelper;
import com.xhl.world.ui.main.home.HomeItemType;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;
import com.xhl.xhl_library.ui.viewPager.ImageModel;
import com.xhl.xhl_library.ui.viewPager.ImagePageView;

import java.util.List;

/**
 * Created by Sum on 15/12/2.
 */
public class TopAdvDataHolder extends RecyclerDataHolder {

    private boolean isFirstLoad = false;

    public TopAdvDataHolder(List<? extends ImageModel> data) {
        super(data);
    }

    @Override
    public int getType() {
        return HomeItemType.Type_Top_adv;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, int position) {

        ImagePageView pageView = new ImagePageView(context);
        // int height = (int) context.getResources().getDimensionPixelSize(R.dimen.fragment_home_top_scrollview_height);
        // int height =160;
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(-1, -2);
        pageView.setLayoutParams(params);

        return new TopAdvViewHolder(pageView);
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, Object data) {
        if (vHolder.getItemViewType() == getType() && !isFirstLoad) {
            isFirstLoad = true;
            TopAdvViewHolder holder = (TopAdvViewHolder) vHolder;
            holder.setData((List<? extends ImageModel>) data);
        }
    }

    static class TopAdvViewHolder extends RecyclerViewHolder implements ImagePageView.ImagePageViewListener {

        public ImagePageView pageView;

        public TopAdvViewHolder(View view) {
            super(view);
            pageView = (ImagePageView) view;
        }

        public void setData(List<? extends ImageModel> data) {
            pageView.setDataSource(data);
            pageView.setListener(this);
            pageView.setInterVal(3000);
            pageView.startScroll();
        }

        @Override
        public void onImagePageClick(ImageModel model) {
            if (model instanceof AdvModel) {
                EventBusHelper.postAdv((AdvModel) model);
            }
        }
    }
}
