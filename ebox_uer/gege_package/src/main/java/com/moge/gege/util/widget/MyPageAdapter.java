package com.moge.gege.util.widget;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

public class MyPageAdapter extends FragmentStatePagerAdapter
{
    private List<? extends Fragment> mFragmentList;

    public MyPageAdapter(FragmentManager fm)
    {
        super(fm);
    }

    public MyPageAdapter(FragmentManager fm,
            List<? extends Fragment> fragmentList)
    {
        super(fm);
        this.mFragmentList = fragmentList;
    }

    @Override
    public int getCount()
    {
        return mFragmentList.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
        super.destroyItem(container, position, object);
    }

    @Override
    public Fragment getItem(int arg0)
    {
        return mFragmentList.get(arg0);
    }
}