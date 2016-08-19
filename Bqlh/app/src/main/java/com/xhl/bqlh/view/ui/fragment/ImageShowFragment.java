package com.xhl.bqlh.view.ui.fragment;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xhl.bqlh.R;
import com.xhl.bqlh.view.base.BaseAppFragment;
import com.xhl.bqlh.view.ui.adapter.FragmentViewPagerAdapter;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sum on 16/7/7.
 */
@ContentView(R.layout.fragment_images_show)
public class ImageShowFragment extends BaseAppFragment {


    @ViewInject(R.id.viewpager)
    private ViewPager mViewPager;

    @ViewInject(R.id.tv_num_cur)
    private TextView tv_num_cur;

    @ViewInject(R.id.tv_num_max)
    private TextView tv_num_max;

    @ViewInject(R.id.ll_num)
    private LinearLayout ll_num;

    private List<String> mImageUrls;

    private FragmentViewPagerAdapter mAdapter;

    private List<ImageDetailsFragment> mImageDatas;

    private ImageDetailsFragment mCur;

    @Event(R.id.back)
    private void onBackClick(View view) {
        getSumContext().popTopFragment(null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mImageDatas = null;
        mCur = null;
    }

    @Override
    protected void initParams() {
        mImageDatas = new ArrayList<>();
        if (mImageUrls == null) {
            return;
        }
        for (String url : mImageUrls) {
            mImageDatas.add(ImageDetailsFragment.instance(url));
        }
        mAdapter = new FragmentViewPagerAdapter(getChildFragmentManager());

        mAdapter.setFragments(mImageDatas);

        mViewPager.setAdapter(mAdapter);
        //切换动画
//        mViewPager.setPageTransformer(true, new DepthPageTransformer());

        ll_num.setVisibility(View.VISIBLE);

        tv_num_cur.setText("1");

        tv_num_max.setText(String.valueOf(mImageDatas.size()));

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                tv_num_cur.setText(String.valueOf(position + 1));
                mCur = mImageDatas.get(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onEnter(Object data) {
        if (data != null) {
            mImageUrls = (List<String>) data;
        }
    }


    @Override
    public boolean processBackPressed() {
        if (mCur != null) {
            return mCur.processBackPressed();
        }
        return super.processBackPressed();
    }

}
