package com.xhl.bqlh.view.custom.viewPager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.xhl.bqlh.R;
import com.xhl.bqlh.view.custom.viewPager.autoScrollViewPager.AutoScrollViewPager;
import com.xhl.bqlh.view.custom.viewPager.autoScrollViewPager.ViewPagerAdapter;
import com.xhl.xhl_library.network.ImageLoadManager;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zhy.autolayout.utils.AutoUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ImagePageView extends AutoRelativeLayout

{
    private Context mContext;
    private ImagePageViewListener mListener;

    private AutoScrollViewPager mViewPager;
    private ViewPagerAdapter mAdapter;
    private ArrayList<View> mPageViews = new ArrayList<>();
    private List<? extends ImageModel> mDataList;

    private LinearLayout mCursorLayout;
    private ArrayList<ImageView> mPointViews = new ArrayList<>();
    private long mInterVal = -1;//自动滚动时间

    public ImagePageView(Context context) {
        super(context);
        this.mContext = context;
        onCreate();
    }

    public ImagePageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        onCreate();
    }

    public ImagePageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        onCreate();
    }


    public interface ImagePageViewListener {
        void onImagePageClick(ImageModel model);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //  onCreate();
    }

    private void onCreate() {
        initView();
        initViewPager();
        initCursor();
        initData();
    }

    public void setDataSource(List<? extends ImageModel> dataList) {
        if (dataList == null || dataList.size() == 0) {
            return;
        }

        mDataList = dataList;

        initViewPager();
        initCursor();
        initData();
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.pub_auto_view_pager, this, true);

        mViewPager = (AutoScrollViewPager) findViewById(R.id.image_auto_scroll);
        mCursorLayout = (LinearLayout) findViewById(R.id.cursor_layout);
    }

    private void initViewPager() {
        if (mDataList == null || mDataList.size() == 0) {
            this.setVisibility(View.INVISIBLE);
            return;
        } else {
            if (getVisibility() != VISIBLE) {
                this.setVisibility(VISIBLE);
            }
        }

        mPageViews.clear();

        for (int i = 0; i < mDataList.size(); i++) {
            String url = mDataList.get(i).getImageUrl();
            ImageView imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            if (!TextUtils.isEmpty(url)) {
                //绑定图片
                ImageLoadManager.instance().LoadImage(imageView, url);
                //绑定事件
                imageView.setOnClickListener(new ImageClickListener(i));

                mPageViews.add(imageView);
            }
        }
    }

    public void setListener(ImagePageViewListener listener) {
        mListener = listener;
    }

    /**
     * 一次展示时间.
     */
    public void setInterVal(long interVal) {
        mInterVal = interVal;
        mViewPager.setInterval(interVal);
    }

    public void startScroll() {
        // set viewpage height
        mViewPager.setInterval(mInterVal == -1 ? 3000L : mInterVal);
        mViewPager.setStopScrollWhenTouch(true);
        mViewPager.setCycle(true);
        //延迟滚动
        setViewPagerScrollDuration(mViewPager, 800);
    }

    public static void setViewPagerScrollDuration(ViewPager paramViewPager,
                                                  final int paramInt) {
        try {
            Field localField = ViewPager.class.getDeclaredField("mScroller");
            localField.setAccessible(true);
            localField.set(paramViewPager,
                    new Scroller(paramViewPager.getContext()) {
                        public void startScroll(int paramAnonymousInt1,
                                                int paramAnonymousInt2, int paramAnonymousInt3,
                                                int paramAnonymousInt4) {
                            super.startScroll(paramAnonymousInt1,
                                    paramAnonymousInt2, paramAnonymousInt3,
                                    paramAnonymousInt4, paramInt);
                        }

                        public void startScroll(int paramAnonymousInt1,
                                                int paramAnonymousInt2, int paramAnonymousInt3,
                                                int paramAnonymousInt4, int paramAnonymousInt5) {
                            super.startScroll(paramAnonymousInt1,
                                    paramAnonymousInt2, paramAnonymousInt3,
                                    paramAnonymousInt4, paramInt);
                        }
                    });
            return;
        } catch (Throwable localThrowable) {
        }
    }

    public class ImageClickListener implements OnClickListener {
        int index;

        ImageClickListener(int index) {
            this.index = index;
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onImagePageClick(mDataList.get(index));
            }
        }
    }

    private void initCursor() {
        if (mDataList == null || mDataList.size() <= 1) {
            return;
        }

        mPointViews.clear();
        mCursorLayout.removeAllViews();

        ImageView imageView;
        for (int i = 0; i < mPageViews.size(); i++) {
            imageView = new ImageView(mContext);
            imageView.setBackgroundResource(R.drawable.bg_blue_point);

            LinearLayout.LayoutParams layoutParams = new AutoLinearLayout.LayoutParams(14, 14);
            layoutParams.leftMargin = 8;
            layoutParams.rightMargin = 8;
            imageView.setLayoutParams(layoutParams);

            AutoUtils.auto(imageView);

            mCursorLayout.addView(imageView);
            if (i == 0) {
                imageView
                        .setBackgroundResource(
                                R.drawable.bg_blue_point);
            }
            mPointViews.add(imageView);
        }
    }

    private void initData() {
        if (mDataList == null || mDataList.size() == 0) {
            return;
        }

        mViewPager.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                drawPoint(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });

        mViewPager.stopAutoScroll();
        mAdapter = new ViewPagerAdapter(mPageViews);
        mViewPager.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mViewPager.startAutoScroll(AutoScrollViewPager.SLIDE_BORDER_MODE_CYCLE);

    }

    private void drawPoint(int index) {
        for (int i = 0; i < mPointViews.size(); i++) {
            if (index == i) {
                mPointViews.get(i).setBackgroundResource(
                        R.drawable.bg_blue_point);
            } else {
                mPointViews.get(i).setBackgroundResource(
                        R.drawable.bg_black_point);
            }
        }
    }
}
