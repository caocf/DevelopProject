package com.moge.gege.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moge.gege.R;

public class ContactUsFragment extends BaseFragment
{
    public Context mContext;
    private TextView phoneText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        final View layout = inflater.inflate(R.layout.fragment_contactus,
                container, false);
        initView(layout);
        return layout;
    }

    @Override
    protected void initView(View v)
    {
        super.initView(v);

        mContext = getActivity();
        this.setHeaderLeft(R.string.contact_us, R.drawable.icon_back);

        phoneText = (TextView)v.findViewById(R.id.phoneText);

        SpannableString ss =
                new SpannableString(getString(R.string.contact_by_phone));
        ss.setSpan(new URLSpan("tel:4008-123-280"),
                3, 15, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        phoneText.setText(ss);
        phoneText.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    protected void onHeaderLeftClick()
    {
        getActivity().finish();
    }
}
