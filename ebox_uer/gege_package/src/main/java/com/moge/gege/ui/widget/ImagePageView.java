package com.moge.gege.ui.widget;

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
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.moge.gege.R;
import com.moge.gege.model.ImageModel;
import com.moge.gege.model.StyleInfoModel;
import com.moge.gege.model.TradingPromotionModel;
import com.moge.gege.model.enums.PromotionStyleType;
import com.moge.gege.ui.widget.TradingPromotionView.TradingPromotionListener;
import com.moge.gege.util.LogUtil;
import com.moge.gege.util.ViewUtil;
import com.moge.gege.util.widget.autoscrollviewpager.AutoScrollViewPager;
import com.moge.gege.util.widget.chat.ViewPagerAdapter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ImagePageView extends RelativeLayout implements
        TradingPromotionListener
{
    private Context mContext;
    private ImagePageViewListener mListener;

    private AutoScrollViewPager mViewPager;
    private ViewPagerAdapter mAdapter;
    private ArrayList<View> mPageViews = new ArrayList<View>();
    private List<? extends ImageModel> mDataList;

    private LinearLayout mCursorLayout;
    private ArrayList<ImageView> mPointViews = new ArrayList<ImageView>();
    private int mCurrentIndex = 0;
    private boolean mStartRun = true;

    public ImagePageView(Context context)
    {
        super(context);
        this.mContext = context;
    }

    public ImagePageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.mContext = context;
    }

    public ImagePageView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        this.mContext = context;
    }

    public void setListener(ImagePageViewListener listener)
    {
        mListener = listener;
    }

    public interface ImagePageViewListener
    {
        void onImagePageClick(ImageModel model);

        void onImagePromotionUnStart(int hour, int minute, int second);

        void onImagePromotionIng(int hour, int minute, int second);

        void onImagePromotionFinish();
    }

    @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();
        onCreate();
    }

    private void onCreate()
    {
        initView();
        initViewPager();
        initCursor();
        initData();
    }

    public void setDataSource(List<? extends ImageModel> dataList)
    {
        if (dataList == null || dataList.size() == 0)
        {
            return;
        }

        mDataList = dataList;

        initViewPager();
        initCursor();
        initData();
    }

    private void initView()
    {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.custom_imagepage, this, true);
        mViewPager = (AutoScrollViewPager) findViewById(R.id.imageViewPager);
        mCursorLayout = (LinearLayout) findViewById(R.id.cursorLayout);
    }

    private void initViewPager()
    {
        if (mDataList == null || mDataList.size() == 0)
        {
            return;
        }

        mPageViews.clear();

        for (int i = 0; i < mDataList.size(); i++)
        {
            TradingPromotionView promotionView = new TradingPromotionView(
                    mContext);
            promotionView.setOnClickListener(new ImageClickListener(i));
            promotionView.setListener(this);

            ImageModel model = mDataList.get(i);
            promotionView.setImageUrl(model.getImage());

            if (model instanceof TradingPromotionModel)
            {
                TradingPromotionModel promotionInfo = (TradingPromotionModel) model;
                StyleInfoModel styleInfo = promotionInfo.getStyle_info();

                if(styleInfo != null)
                {
                    if (promotionInfo.getStyle().equalsIgnoreCase(
                            PromotionStyleType.SECOND_KILL))
                    {
                        promotionView.setPromotionInfo((int) styleInfo.getX(),
                                (int) styleInfo.getY(),
                                (long) (styleInfo.getStart_time() * 1000),
                                (long) (styleInfo.getEnd_time() * 1000));
                    }
                    else if(promotionInfo.getStyle().equalsIgnoreCase(
                            PromotionStyleType.CUSTOM_SECOND_KILL))
                    {
                        promotionView.setFixPromotionInfo(
                                (long) (styleInfo.getStart_time() * 1000),
                                (long) (styleInfo.getEnd_time() * 1000));
                    }
                }
            }

            mPageViews.add(promotionView);
        }

        // set viewpage height
        mViewPager.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                getImageHeight(mDataList.get(0).getImage())));
        mViewPager.setInterval(3000L);
        mViewPager.setStopScrollWhenTouch(true);
        mViewPager.setCycle(true);

        setViewPagerScrollDuration(mViewPager, 800);
    }

    public static void setViewPagerScrollDuration(ViewPager paramViewPager,
            final int paramInt)
    {
        try
        {
            Field localField = ViewPager.class.getDeclaredField("mScroller");
            localField.setAccessible(true);
            localField.set(paramViewPager,
                    new Scroller(paramViewPager.getContext())
                    {
                        public void startScroll(int paramAnonymousInt1,
                                int paramAnonymousInt2, int paramAnonymousInt3,
                                int paramAnonymousInt4)
                        {
                            super.startScroll(paramAnonymousInt1,
                                    paramAnonymousInt2, paramAnonymousInt3,
                                    paramAnonymousInt4, paramInt);
                        }

                        public void startScroll(int paramAnonymousInt1,
                                int paramAnonymousInt2, int paramAnonymousInt3,
                                int paramAnonymousInt4, int paramAnonymousInt5)
                        {
                            super.startScroll(paramAnonymousInt1,
                                    paramAnonymousInt2, paramAnonymousInt3,
                                    paramAnonymousInt4, paramInt);
                        }
                    });
            return;
        }
        catch (Throwable localThrowable)
        {
            LogUtil.e("ViewPager set duration failed");
        }
    }

    // FpOW5AfBglQ8Fudf3A1zh8UWr_EJ_800x533_147479.jpeg
    private int getImageHeight(String imagename)
    {
        int defaultHeight = 140;

        if (TextUtils.isEmpty(imagename) || imagename.length() <= 29)
        {
            return defaultHeight;
        }

        String leftStr = imagename.substring(29, imagename.indexOf("_", 29));

        String[] pxArray = leftStr.split("x");
        if (pxArray != null && pxArray.length >= 2)
        {
            int imageWidth;
            int imageHeight;

            imageWidth = Integer.parseInt(pxArray[0]);
            imageHeight = Integer.parseInt(pxArray[1]);
            defaultHeight = (int) (ViewUtil.getWidth() * 1.0 / imageWidth
                    * imageHeight);
        }

        return defaultHeight;
    }

    public class ImageClickListener implements OnClickListener
    {
        int index;

        ImageClickListener(int index)
        {
            this.index = index;
        }

        @Override
        public void onClick(View v)
        {
            if (mListener != null)
            {
                mListener.onImagePageClick(mDataList.get(index));
            }
        }
    }

    private void initCursor()
    {
        if (mDataList == null || mDataList.size() == 0)
        {
            return;
        }

        mPointViews.clear();
        mCursorLayout.removeAllViews();

        ImageView imageView;
        for (int i = 0; i < mPageViews.size(); i++)
        {
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
            // if (i == 0 || i == mPageViews.size() - 1)
            // {
            // imageView.setVisibility(View.GONE);
            // }
            if (i == 0)
            {
                imageView
                        .setBackgroundResource(
                                R.drawable.icon_indicator_select);
            }
            mPointViews.add(imageView);
        }
    }

    private void initData()
    {
        if (mDataList == null || mDataList.size() == 0)
        {
            return;
        }

        mViewPager.setOnPageChangeListener(new OnPageChangeListener()
        {
            @Override
            public void onPageSelected(int position)
            {
                mCurrentIndex = position;
                drawPoint(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2)
            {

            }

            @Override
            public void onPageScrollStateChanged(int arg0)
            {

            }
        });

        mViewPager.stopAutoScroll();
        mAdapter = new ViewPagerAdapter(mPageViews);
        mViewPager.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mViewPager.startAutoScroll(AutoScrollViewPager.SLIDE_BORDER_MODE_CYCLE);
    }

    private void drawPoint(int index)
    {
        for (int i = 0; i < mPointViews.size(); i++)
        {
            if (index == i)
            {
                mPointViews.get(i).setBackgroundResource(
                        R.drawable.icon_indicator_select);
            }
            else
            {
                mPointViews.get(i).setBackgroundResource(
                        R.drawable.bg_black_point);
            }
        }
    }

    @Override
    public void onPromotionFinish()
    {
        if (mListener != null)
        {
            mListener.onImagePromotionFinish();
        }
    }

    @Override
    public void onPromotionUnStart(int hour, int minute, int second)
    {
        if (mListener != null)
        {
            mListener.onImagePromotionUnStart(hour, minute, second);
        }
    }

    @Override
    public void onPromotionIng(int hour, int minute, int second)
    {
        if (mListener != null)
        {
            mListener.onImagePromotionIng(hour, minute, second);
        }
    }
}
