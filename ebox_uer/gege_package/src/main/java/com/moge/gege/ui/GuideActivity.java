package com.moge.gege.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.moge.gege.AppApplication;
import com.moge.gege.R;
import com.moge.gege.model.enums.LoginFromType;
import com.moge.gege.network.ErrorCode;
import com.moge.gege.ui.helper.UIHelper;
import com.moge.gege.util.widget.photoview.HackyViewPager;

public class GuideActivity extends BaseActivity implements OnPageChangeListener
{
    private Context mContext;
    private HackyViewPager mPhotoViewPager;
    private ImagePagerAdapter mAdapter;

    private boolean mEnableClick = true;

    private static int[] mGuideResArray = { R.drawable.guide_1,
            R.drawable.guide_2, R.drawable.guide_3, R.drawable.guide_4,
            R.drawable.guide_5 };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        mContext = GuideActivity.this;
        initView();
    }

    @Override
    protected void initView()
    {
        super.initView();

        mPhotoViewPager = (HackyViewPager) this
                .findViewById(R.id.photoViewPager);
        mAdapter = new ImagePagerAdapter();
        mPhotoViewPager.setAdapter(mAdapter);
        mPhotoViewPager.setOnPageChangeListener(this);
        mAdapter.notifyDataSetChanged();
    }

    private void startUseApp()
    {
        if (!mEnableClick)
        {
            return;
        }

        mEnableClick = false;

        if (!AppApplication.isCookieEmpty())
        {
            doSignin(this, LoginFromType.FROM_APP_START);
        }
        else
        {
            gotoHomePage();
        }
    }

    @Override
    protected void onLoginResult(int from, int result)
    {
        mEnableClick = true;

        super.onLoginResult(from, result);
        gotoHomePage();
    }

    private void gotoHomePage()
    {
        UIHelper.showHomePageActivity(mContext);
        finish();
    }

    private class ImagePagerAdapter extends PagerAdapter
    {
        private ImagePagerAdapter()
        {
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object)
        {
            container.removeView((View) object);
        }

        @Override
        public int getCount()
        {
            return mGuideResArray.length;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position)
        {
            View guideView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_guide, container, false);
            ImageView guideImage = (ImageView) guideView
                    .findViewById(R.id.guideImage);
            guideImage.setImageResource(mGuideResArray[position]);

            Button nextButton = (Button) guideView.findViewById(R.id.nextBtn);

            View.OnClickListener clickListener = new View.OnClickListener()
            {
                public void onClick(View paramAnonymousView)
                {
                    if (position != mGuideResArray.length - 1)
                    {
                        return;
                    }

                    startUseApp();
                }
            };

            nextButton.setOnClickListener(clickListener);
            container.addView(guideView);
            return guideView;
        }

        @Override
        public boolean isViewFromObject(View view, Object object)
        {
            return view == object;
        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0)
    {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2)
    {

    }

    @Override
    public void onPageSelected(int arg0)
    {
    }
}
