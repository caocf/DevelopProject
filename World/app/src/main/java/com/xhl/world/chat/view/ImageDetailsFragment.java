package com.xhl.world.chat.view;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xhl.world.R;
import com.xhl.xhl_library.Base.Sum.SumFragment;
import com.xhl.xhl_library.network.ImageLoadManager;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by Sum on 16/1/3.
 */

@ContentView(R.layout.fragment_image_details)
public class ImageDetailsFragment extends SumFragment {

    @ViewInject(R.id.ll_content)
    private LinearLayout linearLayout;

    private String mUrl;
    private PhotoView mScaleView;

    @Override
    protected void initParams() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mScaleView = new PhotoView(getContext());
        mScaleView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.app_black));
        mScaleView.setScaleType(ImageView.ScaleType.CENTER);

        mScaleView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));

        mScaleView.setZoomable(true);

        return mScaleView;
    }

    @Override
    public void onEnter(Object data) {
        if (data != null && data instanceof String) {
            mUrl = (String) data;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

       /* PhotoView view = new PhotoView(getContext());

        view.setScaleType(ImageView.ScaleType.FIT_XY);

        view.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));

        view.setZoomable(true);

        linearLayout.addView(view);
*/
        ImageLoadManager.instance().LoadImage(mScaleView, mUrl);
    }
}
