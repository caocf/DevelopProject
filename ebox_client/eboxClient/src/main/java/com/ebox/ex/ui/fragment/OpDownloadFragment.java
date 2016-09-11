package com.ebox.ex.ui.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ImageView;

import com.ebox.Anetwork.RequestManager;
import com.ebox.R;
import com.ebox.ex.ui.base.BaseOpFragment;
import com.ebox.pub.service.global.GlobalField;
import com.ebox.pub.utils.LogUtil;
import com.ebox.pub.utils.QRImageUtil;

/**
 * Created by Android on 2015/10/22.
 * 快递员APP下载
 */
public class OpDownloadFragment extends BaseOpFragment {

    private ImageView iv_weixin;
    private ImageView iv_app;

    public final static String APP_URL = "http://www.aimoge.com/app/download/kuaidi";


    public static BaseOpFragment newInstance() {
        BaseOpFragment fragment = new OpDownloadFragment();
        return fragment;
    }

    @Override
    protected int getViewId() {
        return R.layout.ex_fragment_op_download;
    }

    @Override
    protected void initView(View view) {
        iv_weixin = (ImageView) view.findViewById(R.id.ex_iv_fod_weixin);
        iv_app = (ImageView) view.findViewById(R.id.ex_iv_fod_app);

        Bitmap ios_bitmap = QRImageUtil.createQRImage(APP_URL, BitmapFactory.decodeResource(getResources(), R.drawable.ex_icon_delivery_app));
        iv_app.setImageBitmap(ios_bitmap);

//        if (GlobalField.showConfig == null || GlobalField.showConfig.dot_name.equals("魔格")) {
//            return;
//        }
        String url = GlobalField.showConfig.weixin;
        LogUtil.i("OpDownloadFragment  +"+url);
        if (!TextUtils.isEmpty(url) && URLUtil.isValidUrl(url)) {
            RequestManager.loadImage(iv_weixin, url, 0);
        }


    }


}
