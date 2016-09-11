package com.ebox.mgt.ui.fragment.pollingfg;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ebox.AppApplication;
import com.ebox.R;
import com.ebox.mgt.ui.fragment.pollingfg.model.PollType;
import com.ebox.mgt.ui.fragment.pollingfg.model.PollingStore;
import com.ebox.pub.ui.customview.view.MaterialButton;

/**
 * 网络测试
 */
public class FragmentNetTest extends BasepollFragment {

    private View view;
    private MaterialButton netCheckBT;
    private WebView showWV;
    private TextView showTV;
    private String mUrl = "http://www.baidu.com";

    private LinearLayout showLL;
    private Bundle bundle;


    private RadioGroup groute, gline, gnet;
    private Button bt_ok, bt_error;

    private RadioButton rb_ok, rb_error, rb_ok_line, rb_error_line, rb_ok_net, rb_error_net;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_net_test, null);
        bundle = getArguments();
        initViews();
        return view;
    }

    private void initViews() {

        rb_ok = (RadioButton) view.findViewById(R.id.rb_ok);
        rb_error = (RadioButton) view.findViewById(R.id.rb_error);
        rb_ok_line = (RadioButton) view.findViewById(R.id.rb_ok_line);
        rb_error_line = (RadioButton) view.findViewById(R.id.rb_error_line);
        rb_ok_net = (RadioButton) view.findViewById(R.id.rb_ok_net);
        rb_error_net = (RadioButton) view.findViewById(R.id.rb_error_net);

        PollingStore.storePoll("netTest",PollType.TEST_OK);

        String netRouteModuleState = PollingStore.getStore("netRouteModuleState");
        String netLineState = PollingStore.getStore("netLineState");
        String netRouteState = PollingStore.getStore("netRouteState");

        if (netRouteModuleState.equals(PollType.OK)) {
            rb_ok.setChecked(true);
        } else if (netRouteModuleState.equals(PollType.ERROR)) {
            rb_error.setChecked(true);
        }else{
            PollingStore.storePoll("netRouteModuleState",PollType.OK);
        }

        if (netLineState.equals(PollType.OK)) {
            rb_ok_line.setChecked(true);
        } else if (netLineState.equals(PollType.ERROR)) {
            rb_error_line.setChecked(true);
        }else{
            PollingStore.storePoll("netLineState",PollType.OK);
        }

        if (netRouteState.equals(PollType.OK)) {
            rb_ok_net.setChecked(true);
        } else if (netRouteState.equals(PollType.ERROR)) {
            rb_error_net.setChecked(true);
        }else{
            PollingStore.storePoll("netRouteState",PollType.OK);
        }


        bt_ok = (Button) view.findViewById(R.id.bt_ok);
        bt_error = (Button) view.findViewById(R.id.bt_error);

        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PollingStore.storePoll("netTest", PollType.TEST_OK);
                Toast.makeText(AppApplication.getInstance(), "网络测试成功", Toast.LENGTH_SHORT).show();
            }
        });

        bt_error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PollingStore.storePoll("netTest", PollType.TEST_ERROR);
                Toast.makeText(AppApplication.getInstance(), "网络测试失败", Toast.LENGTH_SHORT).show();
            }
        });


        groute = (RadioGroup) view.findViewById(R.id.rg_route);
        gline = (RadioGroup) view.findViewById(R.id.rg_line);
        gnet = (RadioGroup) view.findViewById(R.id.rg_net);


        groute.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.rb_ok:
                       PollingStore.storePoll("netRouteModuleState",PollType.OK);
                        break;
                    case R.id.rb_error:
                        PollingStore.storePoll("netRouteModuleState", PollType.ERROR);
                        break;
                }
            }
        });

        gline.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.rb_ok_line:
                        PollingStore.storePoll("netLineState", PollType.OK);
                        break;
                    case R.id.rb_error_line:
                        PollingStore.storePoll("netLineState", PollType.ERROR);
                        break;
                }
            }
        });

        gnet.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.rb_ok_net:
                        PollingStore.storePoll("netRouteState", PollType.OK);
                        break;
                    case R.id.rb_error_net:
                        PollingStore.storePoll("netRouteState", PollType.ERROR);
                        break;
                }
            }
        });


        netCheckBT = (MaterialButton) view.findViewById(R.id.mgt_bt_fnt_nettest);
        showWV = (WebView) view.findViewById(R.id.mgt_wv_fnt_nettest);
        showLL = (LinearLayout) view.findViewById(R.id.mgt_ll_showstate);
        showTV = (TextView) view.findViewById(R.id.mgt_tv_fnt_nettest);

        if (bundle != null && bundle.getInt("arg") == 100) {
            //如果需要显示
            showLL.setVisibility(View.VISIBLE);
        } else {
            showLL.setVisibility(View.INVISIBLE);
        }

        netCheckBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initWebView();
            }
        });
    }


    private void initWebView() {
        showWV.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView paramAnonymousWebView,
                                       String paramAnonymousString) {

            }

            public void onPageStarted(WebView paramAnonymousWebView,
                                      String paramAnonymousString, Bitmap paramAnonymousBitmap) {
                super.onPageStarted(paramAnonymousWebView,
                        paramAnonymousString, paramAnonymousBitmap);
            }

            public void onReceivedError(WebView paramAnonymousWebView,
                                        int paramAnonymousInt, String paramAnonymousString1,
                                        String paramAnonymousString2) {
                super.onReceivedError(paramAnonymousWebView, paramAnonymousInt,
                        paramAnonymousString1, paramAnonymousString2);
            }

            public boolean shouldOverrideUrlLoading(
                    WebView paramAnonymousWebView, String paramAnonymousString) {
                Uri localUri = Uri.parse(paramAnonymousString);
                String str = localUri.getScheme();
                if ((TextUtils.isEmpty(str)) || ("http".equals(str))
                        || ("https".equals(str))) {
                    showWV.loadUrl(paramAnonymousString);
                    return false;
                }
                return true;
            }

        });

        showTV.setVisibility(View.INVISIBLE);
        showWV.loadUrl(mUrl);
    }


}
