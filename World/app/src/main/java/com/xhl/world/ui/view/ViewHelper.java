package com.xhl.world.ui.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.xhl.world.R;
import com.xhl.world.ui.event.EventBusHelper;
import com.xhl.world.ui.utils.MDDialogHelper;
import com.xhl.xhl_library.ui.swipyrefresh.MaterialProgressDrawable;

/**
 * Created by Sum on 15/11/26.
 */
public class ViewHelper {

    private static final int MAX_ALPHA = 255;

    public static class LoadView {
        public MaterialProgressDrawable mProgress;
        public View mParent;
    }

    private static LoadView getLoadView() {
        return new LoadView();
    }

    public static LoadView getLoadingView(Context context) {
        LoadView loadView = getLoadView();

        View view = LayoutInflater.from(context).inflate(R.layout.pub_loading_view, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.loading);

        View viewParent = view.findViewById(R.id.load_content);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-1, -1);
        view.setLayoutParams(params);
        loadView.mParent = view;

        MaterialProgressDrawable drawable = new MaterialProgressDrawable(context, viewParent);
        int color1 = ContextCompat.getColor(context, R.color.colorAccent);
        int color = MDDialogHelper.resolveColor(context, R.attr.main_theme_color);
        drawable.setColorSchemeColors(color1, color);
        imageView.setImageDrawable(drawable);

        drawable.setAlpha(255);
        loadView.mProgress = drawable;

        drawable.start();
        return loadView;
    }

    private static Animation loadingAnimation(Context context) {
        return AnimationUtils.loadAnimation(context, R.anim.pub_loading_animation);
    }

    /**
     * 首次加载错误现实
     *
     * @param context
     * @return
     */
    public static View getNetWorkFirstErrorView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.pub_first_error_view, null);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-1, -1);

        view.setLayoutParams(params);
        return view;
    }

    /**
     * 完成首次加载的错误提示
     *
     * @param context
     * @return
     */
    public static View getNetWorkErrorView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.pub_error_view, null);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-1, -1);

        view.findViewById(R.id.error_reload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBusHelper.postReloadData();
            }
        });

        view.setLayoutParams(params);
        return view;
    }

}
