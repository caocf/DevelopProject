package com.ebox.ex.ui.bar;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ebox.Anetwork.RequestManager;
import com.ebox.R;
import com.ebox.pub.service.global.GlobalField;
import com.ebox.pub.utils.MGViewUtil;

/**
 * Created by Android on 2015/10/28.
 */
public class Logo extends FrameLayout {

    private Context context;
    private ImageView iv_logo;
    private TextView tv_phone;
    public Logo(Context context) {
        super(context);
        initSrc(context);
    }

    public Logo(Context context, AttributeSet attrs) {
        super(context, attrs);
        initSrc(context);
    }

    public Logo(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initSrc(context);
    }

    private void initSrc(Context context)
    {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.ex_logo, this, true);
        iv_logo= (ImageView) view.findViewById(R.id.iv_logo);
        tv_phone= (TextView) view.findViewById(R.id.tv_phone);
        MGViewUtil.scaleContentView(this);
        initLogo();
    }

    public void initLogo() {
        if (GlobalField.showConfig == null||GlobalField.showConfig.dot_name.equals("魔格")) {
            return;
        }
        String url = GlobalField.showConfig.logo;
        if (!TextUtils.isEmpty(url) && URLUtil.isValidUrl(url)) {
            RequestManager.loadImage(iv_logo, url, 0);
        }

        String phone = GlobalField.showConfig.call_center;
        if (!TextUtils.isEmpty(phone)) {
            tv_phone.setText(phone);
        }
    }

}
