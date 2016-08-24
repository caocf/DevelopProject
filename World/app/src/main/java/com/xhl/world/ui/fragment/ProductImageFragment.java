package com.xhl.world.ui.fragment;

import android.os.Bundle;
import android.view.View;

import com.xhl.world.R;
import com.xhl.world.ui.event.EventBusHelper;
import com.xhl.world.ui.view.LifeCycleImageView;
import com.xhl.xhl_library.Base.BaseFragment;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by Sum on 16/1/22.
 */
@ContentView(R.layout.fragment_product_image)
public class ProductImageFragment extends BaseFragment {

    @ViewInject(R.id.product_image)
    private LifeCycleImageView product_image;

    private String mImageUrl;

    public static ProductImageFragment instance(String imageUrl) {
        ProductImageFragment fragment = new ProductImageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("imageUrl", imageUrl);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mImageUrl = getArguments().getString("imageUrl");
    }

    @Override
    protected void initParams() {
        product_image.bindImageUrl(mImageUrl);
        product_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBusHelper.postImageUrlsEvent(null);
            }
        });
    }
}
