package com.moge.gege.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import com.moge.gege.R;
import com.moge.gege.util.PackageUtil;
import org.w3c.dom.Text;

public class AboutUsFragment extends BaseFragment
{
    public Context mContext;
    private TextView versionText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        final View layout = inflater.inflate(R.layout.fragment_aboutus,
                container, false);
        initView(layout);
        return layout;
    }

    @Override
    protected void initView(View v)
    {
        super.initView(v);

        mContext = getActivity();
        this.setHeaderLeft(R.string.about_us, R.drawable.icon_back);

        versionText = (TextView) v.findViewById(R.id.versionText);
        versionText.setText("V" + PackageUtil.getVersionName(mContext));
    }

    @Override
    protected void onHeaderLeftClick()
    {
        getActivity().finish();
    }
}
