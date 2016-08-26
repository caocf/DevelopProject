package com.xhl.xhl_library.ui.viewPager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.xhl.xhl_library.R;
import com.xhl.xhl_library.network.ImageLoadManager;
import com.xhl.xhl_library.ui.viewPager.autoScrollViewPager.AutoScrollViewPager;
import com.xhl.xhl_library.ui.viewPager.autoScrollViewPager.ViewPagerAdapter;
import com.zhy.autolayout.AutoRelativeLayout;

import org.xutils.common.util.LogUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ImagePageView extends AutoRelativeLayout

{
    private Context mContext;
    private ImagePageViewListener mListener;

    private AutoScrollViewPager mViewPager;
    private ViewPagerAdapter mAdapter;
    private ArrayList<View> mPageViews = new ArrayList<View>();
    private List<? extends ImageModel> mDataList;

    private LinearLayout mCursorLayout;
    private ArrayList<ImageView> mPointViews = new ArrayList<ImageView>();
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

    public void setListener(ImagePageViewListener listener) {
        mListener = listener;
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
        inflater.inflate(R.layout.imagepage_view, this, true);

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
            } else if (mDataList.get(i).getImageRes() != -1) {
                //绑定事件
                imageView.setOnClickListener(new ImageClickListener(i));
                imageView.setImageResource(mDataList.get(i).getImageRes());
                mPageViews.add(imageView);
            }
        }
    }

    /**
     * 一次展示时间.
     *
     * @param interVal
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
        LogUtil.v("auto start scroll");
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
            LogUtil.e("ViewPager set duration failed");
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
            imageView.setBackgroundResource(R.drawable.icon_indicator_normal);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT,
                            LayoutParams.WRAP_CONTENT));
            layoutParams.leftMargin = 10;
            layoutParams.rightMargin = 10;
            layoutParams.width = 14;
            layoutParams.height = 14;
            mCursorLayout.addView(imageView, layoutParams);
            if (i == 0) {
                imageView
                        .setBackgroundResource(
                                R.drawable.icon_indicator_select);
            }
            mPointViews.add(imageView);
        }
    }

    private void initData() {
        if (mDataList == null || mDataList.size() == 0) {
            return;
        }

        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
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
                        R.drawable.icon_indicator_select);
            } else {
                mPointViews.get(i).setBackgroundResource(
                        R.drawable.bg_black_point);
            }
        }
    }
}
