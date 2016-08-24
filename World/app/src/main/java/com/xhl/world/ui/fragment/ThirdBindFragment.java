package com.xhl.world.ui.fragment;

import android.view.View;
import android.widget.TextView;

import com.xhl.world.R;
import com.xhl.world.ui.view.LifeCycleImageView;
import com.xhl.xhl_library.ui.view.RippleView;
import com.xhl.xhl_library.utils.log.Logger;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.HashMap;

/**
 * Created by Sum on 16/2/20.
 */
@ContentView(R.layout.fragment_third_bind_count)
public class ThirdBindFragment extends BaseAppFragment {

    private HashMap<String, String> mData;


    @ViewInject(R.id.title_name)
    private TextView title_name;

    @ViewInject(R.id.iv_my_pic)
    private LifeCycleImageView iv_my_pic;//头像

    @ViewInject(R.id.tv_name_hint)
    private TextView tv_name_hint;//用户提示

    @ViewInject(R.id.tv_name)
    private TextView tv_name;//用户名称

    @Event(value = R.id.title_back, type = RippleView.OnRippleCompleteListener.class)
    private void onBackClick(View view) {
        getSumContext().popTopFragment(null);
    }

    @Event(value = R.id.ripple_add_connect, type = RippleView.OnRippleCompleteListener.class)
    private void ripple_add_connect(View view) {//添加关联
        getSumContext().pushFragmentToBackStack(ThirdBindLoginFragment.class, mData);
    }

    @Event(value = R.id.ripple_register, type = RippleView.OnRippleCompleteListener.class)
    private void onRegisterClick(View view) {//快速注册
        getSumContext().pushFragmentToBackStack(ThirdBindRegisterFragment.class, mData);
    }

    @Override
    protected void initParams() {
        title_name.setText("联合登陆");
        //{is_yellow_year_vip=0, vip=0, level=0, province=江苏, yellow_vip_level=0, is_yellow_vip=0,
        // gender=男, screen_name=test, msg=, profile_image_url=http://q.qlogo.cn/qqapp/100424468/04A6EE6B66D2F9E392DBFF6356677007/100, city=南京}
        //{unionid=okQY3wztXRoiV_OKaGwsuedd2p2E, third_login_hint=亲爱的微信用户：, country=中国, nickname=de磊, city=南京, province=江苏, language=zh_CN,
        // headimgurl=http://wx.qlogo.cn/mmopen/yqaUdpUcHrkxIiaY23R2LIega99MyI1ydrggm4KlVryZCnqXiaMvqWRO1UkwgOsTTCF5Z42tZAUUtxbXxXYaoqYFbjAMibkL52B/0, sex=1, third_login_type=wx, openid=oDxrfw-oKL8tS5phEQLsKKH0RWdk}

        Logger.e("" + mData.toString());

        String type = mData.get("third_login_type");
        String avatar,name;
        if (type.equals("wx")) {
            avatar = mData.get("headimgurl");
            name = mData.get("nickname");
        } else {
            avatar = mData.get("profile_image_url");
            name = mData.get("screen_name");
        }
        //设置数据
        iv_my_pic.LoadDrawable(avatar);
        //用户名称
        tv_name.setText(name);
        //提示
        tv_name_hint.setText(mData.get("third_login_hint"));
    }

    @Override
    public void onEnter(Object data) {
        if (data != null && data instanceof HashMap) {
            mData = (HashMap<String, String>) data;
        }
    }
}
