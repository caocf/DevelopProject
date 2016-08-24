package com.xhl.world.ui.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xhl.world.R;
import com.xhl.world.ui.adapter.FragmentViewPagerAdapter;
import com.xhl.world.ui.fragment.ScaleImageFragment;
import com.xhl.world.ui.view.pub.DepthPageTransformer;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sum on 16/1/13.
 */
@ContentView(R.layout.activity_image_details)
public class ImageDetailsActivity extends BaseAppActivity {


    @ViewInject(R.id.viewpager)
    private ViewPager mViewPager;

    @ViewInject(R.id.tv_num_cur)
    private TextView tv_num_cur;

    @ViewInject(R.id.tv_num_max)
    private TextView tv_num_max;

    @ViewInject(R.id.ll_num)
    private LinearLayout ll_num;

    private ScaleImageFragment mCurFragment;

    @Event(R.id.back)
    private void onBackClick(View view) {
        this.finish();
    }

    //资源路径
    public static final String Image_source = "image_source";
    //资源类型
    public static final String Image_type = "image_type";
    //本地文件
    public static final int Image_file = 1;
    //url
    public static final int Image_url = 2;

    public static final int Image_urls = 3;

    private List<String> mImageUrls;

    private String mImageSource;
    private int mImageType;

    private List<ScaleImageFragment> mImageDatas;

    FragmentViewPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mImageType = getIntent().getIntExtra(Image_type, 0);

        if (mImageType == 3) {
            mImageUrls = getIntent().getStringArrayListExtra(Image_source);
        } else {
            mImageSource = getIntent().getStringExtra(Image_source);
        }
    }

    @Override
    protected void initParams() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        mImageDatas = new ArrayList<>();

        if (mImageType != 3) {
            singImageLoad();
        } else {
            for (String url : mImageUrls) {
                mImageDatas.add(ScaleImageFragment.instance(url, Image_url));
            }
            mAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager());

            mAdapter.setFragments(mImageDatas);

            mViewPager.setAdapter(mAdapter);
            //切换动画
            mViewPager.setPageTransformer(true, new DepthPageTransformer());

            ll_num.setVisibility(View.VISIBLE);

            tv_num_cur.setText("1");

            tv_num_max.setText(String.valueOf(mImageDatas.size()));
            if (mImageDatas.size() >= 0) {
                mCurFragment = mImageDatas.get(0);
            }

            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    tv_num_cur.setText(String.valueOf(position + 1));
                    if (position < mImageDatas.size()) {
                        mCurFragment = mImageDatas.get(position);
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
    }

    private void singImageLoad() {
        mCurFragment = ScaleImageFragment.instance(mImageSource, mImageType);
        getSupportFragmentManager().beginTransaction().add(R.id.fl_content, mCurFragment).commit();
    }

    @Override
    public void onBackPressed() {
        if (mCurFragment != null && mCurFragment.phoneHasScale()) {
            mCurFragment.resetScale();
        } else {
            super.onBackPressed();
        }
    }
}
