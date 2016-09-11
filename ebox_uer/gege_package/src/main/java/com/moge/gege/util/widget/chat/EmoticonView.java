package com.moge.gege.util.widget.chat;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.moge.gege.AppApplication;
import com.moge.gege.R;
import com.moge.gege.util.widget.MyGridView;

public class EmoticonView extends RelativeLayout implements OnItemClickListener
{
    private Context mContext;
    private OnEmoticonListener mListener;

    private ViewPager mEmoticonsViewPager;
    private ArrayList<View> mPageViews;
    private List<List<EmoticonEntity>> emojis;
    private List<EmoticonAdapter> mEmoticonAdapterList;

    private LinearLayout mCursorLayout;
    private ArrayList<ImageView> mPointViews;
    private int mCurrentIndex = 0;

    public EmoticonView(Context context)
    {
        super(context);
        this.mContext = context;
    }

    public EmoticonView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.mContext = context;
    }

    public EmoticonView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        this.mContext = context;
    }

    public void setListener(OnEmoticonListener listener)
    {
        mListener = listener;
    }

    public interface OnEmoticonListener
    {
        void onEmoticonClick(CharSequence cs);

        void onEmoticonDelete();
    }

    @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();

        if (EmoticonUtil.instance().isEmoticonEmpty())
        {
            new Thread()
            {
                public void run()
                {
                    EmoticonUtil.instance().init(
                            AppApplication.instance().getApplicationContext());
                }
            }.start();

            new Handler().postDelayed(new Runnable()
            {
                public void run()
                {
                    onCreate();
                }
            }, 1000);
        }
        else
        {
            onCreate();
        }
    }

    private void onCreate()
    {
        emojis = EmoticonUtil.instance().getEmoticonDataSource();

        initView();
        initViewPager();
        initCursor();
        initData();
    }

    private void initView()
    {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.custom_emoticon, this, true);
        mEmoticonsViewPager = (ViewPager) findViewById(R.id.emoticonViewPager);
        mCursorLayout = (LinearLayout) findViewById(R.id.cursorLayout);
    }

    private void initViewPager()
    {
        mPageViews = new ArrayList<View>();

        // left empty view
        View nullView1 = new View(mContext);
        nullView1.setBackgroundColor(Color.TRANSPARENT);
        mPageViews.add(nullView1);

        // middle emoticons
        mEmoticonAdapterList = new ArrayList<EmoticonAdapter>();
        for (int i = 0; i < emojis.size(); i++)
        {
            MyGridView view = new MyGridView(mContext);
            view.setOnItemClickListener(this);
            view.setNumColumns(7);
            view.setBackgroundColor(Color.TRANSPARENT);
            view.setHorizontalSpacing(1);
            view.setVerticalSpacing(1);
            view.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
            view.setCacheColorHint(0);
            view.setPadding(5, 0, 5, 0);
            view.setSelector(new ColorDrawable(Color.TRANSPARENT));
            view.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
                    LayoutParams.WRAP_CONTENT));
            view.setGravity(Gravity.CENTER);
            mPageViews.add(view);

            EmoticonAdapter adapter = new EmoticonAdapter(mContext);
            adapter.addAll(emojis.get(i));
            view.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            mEmoticonAdapterList.add(adapter);
        }

        // right empty view
        View nullView2 = new View(mContext);
        nullView2.setBackgroundColor(Color.TRANSPARENT);
        mPageViews.add(nullView2);
    }

    private void initCursor()
    {
        mPointViews = new ArrayList<ImageView>();
        ImageView imageView;
        for (int i = 0; i < mPageViews.size(); i++)
        {
            imageView = new ImageView(mContext);
            imageView.setBackgroundResource(R.drawable.d1);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT,
                            LayoutParams.WRAP_CONTENT));
            layoutParams.leftMargin = 10;
            layoutParams.rightMargin = 10;
            layoutParams.width = 8;
            layoutParams.height = 8;
            mCursorLayout.addView(imageView, layoutParams);
            if (i == 0 || i == mPageViews.size() - 1)
            {
                imageView.setVisibility(View.GONE);
            }
            if (i == 1)
            {
                imageView.setBackgroundResource(R.drawable.d2);
            }
            mPointViews.add(imageView);
        }
    }

    private void initData()
    {
        mEmoticonsViewPager.setAdapter(new ViewPagerAdapter(mPageViews));
        mEmoticonsViewPager.setCurrentItem(1);
        mCurrentIndex = 0;
        mEmoticonsViewPager.setOnPageChangeListener(new OnPageChangeListener()
        {
            @Override
            public void onPageSelected(int arg0)
            {
                mCurrentIndex = arg0 - 1;
                drawPoint(arg0);

                if (arg0 == mPointViews.size() - 1 || arg0 == 0)
                {
                    if (arg0 == 0)
                    {
                        mEmoticonsViewPager.setCurrentItem(arg0 + 1);
                        mPointViews.get(1).setBackgroundResource(R.drawable.d2);
                    }
                    else
                    {
                        mEmoticonsViewPager.setCurrentItem(arg0 - 1);
                        mPointViews.get(arg0 - 1).setBackgroundResource(
                                R.drawable.d2);
                    }
                }
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
    }

    public void drawPoint(int index)
    {
        for (int i = 1; i < mPointViews.size(); i++)
        {
            if (index == i)
            {
                mPointViews.get(i).setBackgroundResource(R.drawable.d2);
            }
            else
            {
                mPointViews.get(i).setBackgroundResource(R.drawable.d1);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
    {
        EmoticonEntity entity = (EmoticonEntity) mEmoticonAdapterList.get(
                mCurrentIndex).getItem(arg2);

        if (entity.getResId() == R.drawable.face_del_icon)
        {
            if (mListener != null)
            {
                mListener.onEmoticonDelete();
            }
            return;
        }

        if (!TextUtils.isEmpty(entity.getResName()))
        {
            if (mListener != null)
            {
                mListener.onEmoticonClick(EmoticonUtil.instance()
                        .getEmoticonString(mContext, entity.getResId(),
                                entity.getName()));
            }
        }

    }
}
