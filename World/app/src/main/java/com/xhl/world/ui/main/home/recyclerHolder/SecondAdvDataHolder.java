package com.xhl.world.ui.main.home.recyclerHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.xhl.world.R;
import com.xhl.world.model.AdvModel;
import com.xhl.world.ui.event.EventBusHelper;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;
import com.xhl.xhl_library.ui.viewPager.ImageModel;
import com.xhl.xhl_library.ui.viewPager.ImagePageView;

import java.util.List;

/**
 * Created by Sum on 15/12/2.
 */
public class SecondAdvDataHolder extends RecyclerDataHolder {

    private List<? extends ImageModel> mData;
    private boolean isFirstLoad = false;

    public SecondAdvDataHolder(List<? extends ImageModel> data) {
        super(data);
        mData = data;
    }

    @Override
    public int getType() {
        return 11;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, int position) {

        View view = LayoutInflater.from(context).inflate(R.layout.bar_home_second_adv, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -2);
        view.setLayoutParams(params);

        return new SecondAdvViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, Object data) {

        if (vHolder.getItemViewType() == getType() && !isFirstLoad) {
            isFirstLoad = true;
            SecondAdvViewHolder holder = (SecondAdvViewHolder) vHolder;
            holder.setData(mData);
        }

    }

    /**
     * Created by Sum on 15/12/5.
     */
    static class SecondAdvViewHolder extends RecyclerViewHolder implements ImagePageView.ImagePageViewListener {

        public ImagePageView imagePageView;

        public SecondAdvViewHolder(View view) {
            super(view);
            imagePageView = (ImagePageView) view.findViewById(R.id.sec_image_page_view);
        }

        public void setData(List<? extends ImageModel> data) {
            if (data != null && data.size() >= 0) {
                imagePageView.setDataSource(data);
                imagePageView.setInterVal(12000);
                imagePageView.setListener(this);
                imagePageView.startScroll();
                imagePageView.setListener(this);
            }
        }

        @Override
        public void onImagePageClick(ImageModel model) {
            if (model instanceof AdvModel) {
                EventBusHelper.postAdv((AdvModel) model);
            }
        }
    }
}
