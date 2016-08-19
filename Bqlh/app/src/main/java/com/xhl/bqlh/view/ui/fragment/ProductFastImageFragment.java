package com.xhl.bqlh.view.ui.fragment;

import android.os.Bundle;
import android.view.View;

import com.xhl.bqlh.R;
import com.xhl.bqlh.model.event.ImageEvent;
import com.xhl.bqlh.view.base.BaseAppFragment;
import com.xhl.bqlh.view.custom.LifeCycleImageView;
import com.xhl.bqlh.view.helper.EventHelper;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by Sum on 16/1/22.
 */
@ContentView(R.layout.fragment_product_image)
public class ProductFastImageFragment extends BaseAppFragment {

    @ViewInject(R.id.product_image)
    private LifeCycleImageView product_image;

    private String mImageUrl;

    public static ProductFastImageFragment instance(String imageUrl) {
        ProductFastImageFragment fragment = new ProductFastImageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("imageUrl", imageUrl);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onEnter(Object data) {
        if (data != null && data instanceof String) {
            mImageUrl = (String) data;
        }
    }

    @Override
    protected void initParams() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mImageUrl = bundle.getString("imageUrl");
        }

        product_image.bindImageUrl(mImageUrl);
        product_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventHelper.postDefaultEvent(new ImageEvent(0));
            }
        });
    }
}
