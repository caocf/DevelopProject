package com.xhl.bqlh.business.view.helper;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.xhl.xhl_library.utils.DisplayUtil;

/**
 * Created by Sum on 16/3/23.
 */
public class AddCarAnimHelper {

    private ViewGroup mDecor;//动画父容器

    private FrameLayout mScene;//动画场景

    private Activity mActivity;

    private int[] mTargetPos;//目标点
    private int[] mStartPos;//开始点

    private View mStartView;
    private View mTargetView;//目标view
    private int mTargetXOffset = 0;
    private int mTargetYOffset = 0;

    private static final int mDuration = 500;

    public AddCarAnimHelper(Activity activity) {
        mActivity = activity;
        init();
    }

    public AddCarAnimHelper setStartPos(int[] startPos) {
        mStartPos = startPos;
        return this;
    }

    public AddCarAnimHelper setTargetView(View mTargetView) {
        this.mTargetView = mTargetView;
        mTargetView.getLocationInWindow(mTargetPos);
//        mTargetXOffset = mTargetView.getMeasuredWidth() / 3;
        mTargetYOffset = mTargetView.getMeasuredHeight() / 4;
        return this;
    }

    public AddCarAnimHelper setStartView(View mStartView) {
        this.mStartView = mStartView;
        return this;
    }

    public AddCarAnimHelper setDefaultView(Drawable drawable) {
        ImageView imageView = new ImageView(mActivity);
        imageView.setImageDrawable(drawable);
        this.mStartView = imageView;
        return this;
    }


    private void init() {
        mTargetPos = new int[2];
        mDecor = (ViewGroup) mActivity.getWindow().getDecorView();
        mScene = new FrameLayout(mActivity);
        mScene.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        mScene.setBackgroundResource(android.R.color.transparent);
        //添加动画层级
        mDecor.addView(mScene);
    }

    //添加动画view
    private View addViewToScene(View view, int[] startPos) {
        int x = startPos[0];
        int y = startPos[1];

        int len = DisplayUtil.dip2px(mActivity, 50);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(len, len);
        params.leftMargin = x;
        params.topMargin = y;
        view.setLayoutParams(params);
//        view.setPadding(5, 5, 5, 5);

        mScene.addView(view);

        return view;
    }

    public void startProperty() {

        if (mStartView == null) {
            throw new NullPointerException("anim view is null");
        }
        if (mStartPos == null) {
            throw new NullPointerException("anim view start location is null");
        }
        if (mTargetView == null) {
            throw new NullPointerException("target view is null");
        }

        View animView = addViewToScene(mStartView, mStartPos);
        if (animView == null) {
            return;
        }

        animView.setAlpha(0.8f);

        int endX = mTargetPos[0] - mStartPos[0] - mTargetXOffset;

//        Logger.v("targetX:" + mTargetPos[0] + " mTargetXOffset" + mTargetXOffset + " startPosX:" + mStartPos[0]);

        int endY = mTargetPos[1] - mStartPos[1] - mTargetYOffset;


        ObjectAnimator scaleX = ObjectAnimator.ofFloat(mStartView, View.SCALE_X, 1f, 0.5f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(mStartView, View.SCALE_Y, 1f, 0.5f);

        ObjectAnimator translateX = ObjectAnimator.ofFloat(mStartView, View.TRANSLATION_X, 0f, endX);

        ObjectAnimator translateY = ObjectAnimator.ofFloat(mStartView, View.TRANSLATION_Y, 0f, endY);
        translateY.setInterpolator(new AccelerateInterpolator());

        ObjectAnimator alpha = ObjectAnimator.ofFloat(mStartView, View.ALPHA, 0.82f, 0.4f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX, scaleY, translateX, translateY, alpha);
        animatorSet.setDuration(mDuration);
        animatorSet.addListener(new AnimatorListener(mDecor, mStartView));
        animatorSet.start();
    }

    static class AnimatorListener implements Animator.AnimatorListener {

        private ViewGroup mParentView;
        private View mAnimView;

        public AnimatorListener(ViewGroup mParentView, View mAnimView) {
            this.mAnimView = mAnimView;
            this.mParentView = mParentView;
        }


        @Override
        public void onAnimationStart(Animator animation) {
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            //隐藏动画View
            mAnimView.setVisibility(View.GONE);
//            mParentView.removeView(mAnimView);
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }

}
