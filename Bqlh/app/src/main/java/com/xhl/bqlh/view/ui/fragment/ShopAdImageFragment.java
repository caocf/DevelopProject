package com.xhl.bqlh.view.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xhl.bqlh.model.AdInfoModel;
import com.xhl.bqlh.view.base.BaseAppFragment;
import com.xhl.bqlh.view.custom.LifeCycleImageView;

import org.xutils.x;

/**
 * Created by Sum on 16/1/22.
 */
public class ShopAdImageFragment extends BaseAppFragment {

    private AdInfoModel mInfo;
    public static ShopAdImageFragment instance(AdInfoModel ad) {
        ShopAdImageFragment fragment = new ShopAdImageFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", ad);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initParams() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        if (bundle != null) {
            mInfo = (AdInfoModel) bundle.getSerializable("data");
        }

        ImageView imageView = new ImageView(getContext());

        imageView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));

        x.image().bind(imageView, mInfo.getImageUrl(), LifeCycleImageView.imageOptions);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInfo != null) {
                    AdInfoModel.postEvent(mInfo);
                }
            }
        });

        return imageView;
    }
}
