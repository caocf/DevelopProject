package com.moge.gege.util.widget.chat;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class ViewPagerAdapter extends PagerAdapter
{

    private List<? extends View> pageViews;

    public ViewPagerAdapter(List<? extends View> pageViews)
    {
        super();
        this.pageViews = pageViews;
    }

    public void setData(List<? extends View> pageViews)
    {
        this.pageViews = pageViews;
    }

    @Override
    public int getCount()
    {
        return pageViews == null ? 0 : pageViews.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1)
    {
        return arg0 == arg1;
    }

    @Override
    public int getItemPosition(Object object)
    {
        return super.getItemPosition(object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
        if(position < pageViews.size()) {
            ((ViewPager) container).removeView(pageViews.get(position));
        }
    }

    /***
     * 获取每一个item�?类于listview中的getview
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
        ((ViewPager) container).addView(pageViews.get(position));
        return pageViews.get(position);
    }

}