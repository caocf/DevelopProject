package com.xhl.bqlh.business.view.ui.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.xhl.bqlh.business.AppConfig.NetWorkConfig;
import com.xhl.bqlh.business.Model.ShopDisplayModel;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.view.base.BaseAppFragment;
import com.xhl.bqlh.business.view.ui.adapter.FragmentViewPagerAdapter;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sum on 16/6/3.
 */
@ContentView(R.layout.fragment_shop_display_detail)
public class ShopDisplayDetailsFragment extends BaseAppFragment {

    @ViewInject(R.id.view_pager)
    private ViewPager view_pager;

    @ViewInject(R.id.tv_page)
    private TextView tv_page;

    @ViewInject(R.id.tv_time)
    private TextView tv_time;

    @ViewInject(R.id.tv_remark)
    private TextView tv_remark;

    @ViewInject(R.id.tv_location)
    private TextView tv_location;

    @Event(R.id.iv_back)
    private void onBackClick(View view) {
        getSumContext().popTopFragment(null);
    }

    @Override
    public boolean processBackPressed() {
        if (mCurFragment != null && mCurFragment.ImageHasScale()) {
            mCurFragment.ResetScale();
            return true;
        }
        return super.processBackPressed();
    }

    private ShopDisplayModel model;
    private List<ImageDetailsFragment> mFragments;
    private ImageDetailsFragment mCurFragment;

    public static ShopDisplayDetailsFragment instance(ShopDisplayModel model) {
        ShopDisplayDetailsFragment fragment = new ShopDisplayDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", model);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initParams() {
        super.initToolbar();

        Bundle arguments = getArguments();
        if (arguments != null) {
            model = (ShopDisplayModel) arguments.getSerializable("data");
        }
        if (model != null) {
            tv_time.setText(model.getShowTime());
            tv_remark.setText(model.getShowRemark());
            tv_location.setText(model.getAddress());
            List<String> imageUrlList = model.getImageUrlList();
            mFragments = new ArrayList<>();
            for (String url : imageUrlList) {
                mFragments.add(ImageDetailsFragment.instance(NetWorkConfig.imageHost + url));
            }
            FragmentViewPagerAdapter adapter = new FragmentViewPagerAdapter(getChildFragmentManager());
            adapter.setFragments(mFragments);
            view_pager.setAdapter(adapter);
            view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    showImage(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            showImage(0);
        }
    }

    private void showImage(int position) {
        int size = model.getImageUrlList().size();
        if (size == 0) {
            mToolbar.setTitle("没有图片");
        } else {
            mToolbar.setTitle(getString(R.string.image_num, position + 1, size));
        }
        if (mFragments.size() > position) {
            mCurFragment = mFragments.get(position);
        }
    }

    @Override
    public void onEnter(Object data) {
        if (data != null && data instanceof ShopDisplayModel) {
            model = (ShopDisplayModel) data;
        }
    }
}
