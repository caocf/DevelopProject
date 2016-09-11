package com.moge.gege.ui.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.URLSpan;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import com.moge.gege.AppApplication;
import com.moge.gege.R;
import com.moge.gege.config.GlobalConfig;
import com.moge.gege.model.MerchantListModel;
import com.moge.gege.model.OrderModel;
import com.moge.gege.model.enums.TopicType;
import com.moge.gege.ui.*;
import com.moge.gege.util.LogUtil;
import com.moge.gege.util.ToastUtil;

import java.util.ArrayList;
import java.util.Map;

public class UIHelper
{
    private final static String TAG = "UIHelper";

    public static boolean showUrlRedirectBySpannable(Context context,
            String spannableStr)
    {
        Spanned span = Html.fromHtml(spannableStr);
        URLSpan[] urls = span.getSpans(0, spannableStr.length(), URLSpan.class);
        if (urls.length == 0)
        {
            return false;
        }

        return UIHelper.showUrlRedirect(context, urls[0].getURL());
    }

    /**
     * url跳转
     *
     * @param context
     * @param url
     */
    public static boolean showUrlRedirect(Context context, String url)
    {
        if(TextUtils.isDigitsOnly(url))
        {
            UIHelper.showDeliveryQueryActivity(context, url, "");
            return true;
        }

        URLs urls = URLs.parseURL(url);
        if (urls != null && urls.getUrlType() != URLs.URL_WEBVIEW)
        {
            return showLinkRedirect(context, urls.getUrlType(),
                    urls.getParams());
        }
        else
        {
            return showWebPageActivity(context, url);
        }
    }

    public static boolean showLinkRedirect(Context context, int urlType,
            Map<String, String> paramsMap)
    {
        switch (urlType)
        {
            case URLs.URL_HOME:
                showHomePageActivity(context);
                break;
            case URLs.URL_PERSIONAL_CENTER:
                showUserCenterActivity(context, paramsMap.get("uid"));
                break;
            case URLs.URL_TOPIC_LIST:
                showTopicListActivity(context, paramsMap.get("bid"));
                break;
            case URLs.URL_TOPIC_DETAIL:
                showTopicDetailActivity(context, "", paramsMap.get("tid"),
                        TopicType.GENERAL_TOPIC);
                break;
            case URLs.URL_TRADING_LIST:
                showTradingListActivity(context, false,
                        paramsMap.get("category"), "");
                break;
            case URLs.URL_TRADING_PROMOTION_LIST:
                showTradingListActivity(context, true,
                        paramsMap.get("promotions_id"), "");
                break;
            case URLs.URL_TRADING_DETAIL:
                showTradingDetailActivity(context, paramsMap.get("trading_id"));
                break;
            case URLs.URL_ACTIVITY_LIST:
                showActivityList(context);
                break;
            case URLs.URL_ACTIVITY_DETAIL:
                showTopicDetailActivity(context, "",
                        paramsMap.get("activity_id"), TopicType.ACTIVITY_TOPIC);
                break;
            case URLs.URL_SERVICE_LIST:
                showServiceList(context,
                        Integer.parseInt(paramsMap.get("service_type")) + 1);
                break;
            case URLs.URL_SERVICE_DETAIL:
                showTopicDetailActivity(context, "",
                        paramsMap.get("service_id"),
                        Integer.parseInt(paramsMap.get("service_type"))
                                + TopicType.CATEGORY_TOPIC);
                break;
            case URLs.URL_CONVENIENCE_SERVICE_DETAIL:
                showConvenienceDetail(context, paramsMap.get("service_id"), "");
                break;
            default:
                return false;
        }

        return true;
    }

    /**
     * 打开浏览器
     *
     * @param context
     * @param url
     */
    public static boolean openBrowser(Context context, String url)
    {
        try
        {
            Uri uri = Uri.parse(url);
            Intent it = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(it);
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            ToastUtil.showToastShort("无法浏览此网页");
            return false;
        }
    }

    /**
     * 获取webviewClient对象
     *
     * @return
     */
    public static WebViewClient getWebViewClient()
    {
        return new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                showUrlRedirect(view.getContext(), url);
                return true;
            }
        };
    }

    public static void showHomePageActivity(Context context)
    {
        // context.startActivity(new Intent(context, HomeActivity.class));
        final Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setClass(context, HomeActivity.class);
        context.startActivity(intent);
    }

    public static void logout(Context context)
    {
        HomeActivity.instance.finish();

        HomeActivity.setCurTabPage(HomeActivity.INDEX_WAREHOUSE);

        final Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setClass(context, HomeActivity.class);
        context.startActivity(intent);
    }

    public static void showTopicListActivity(Context context, String boardId)
    {
        Intent intent;
        intent = new Intent(context, TopicListActivity.class);
        intent.putExtra("board_id", boardId);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void showTopicDetailActivity(Context context, String boardId,
            String topicId, int topicType)
    {
        if (topicType != TopicType.BUSINESS_TOPIC)
        {
            Intent intent = new Intent(context, TopicDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("board_id", boardId);
            bundle.putString("topic_id", topicId);
            bundle.putInt("topic_type", topicType);
            intent.putExtras(bundle);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ((Activity) context).startActivityForResult(intent,
                    GlobalConfig.INTENT_TOPIC_DETAIL);
        }
        else
        {
            showTradingDetailActivity(context, topicId);
        }
    }

    public static void showUserCenterActivity(Context context, String uid)
    {
        Intent intent;
        if (uid.equals(AppApplication.getLoginId()))
        {
            intent = new Intent(context, MyCenterActivity.class);
        }
        else
        {
            intent = new Intent(context, OtherCenterActivity.class);
            intent.putExtra("uid", uid);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void showTradingDetailActivity(Context context,
            String tradingId)
    {
        Intent intent = new Intent(context, TradingDetailActivity.class);
        intent.putExtra("trading_id", tradingId);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void showActivityList(Context context)
    {
        Intent intent = new Intent(context, ActivityListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void showServiceList(Context context, int serviceType)
    {
        Intent intent = new Intent(context, ServiceListActivity.class);
        intent.putExtra("service_type", serviceType);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void showConvenienceDetail(Context context,
            String convenienceId, String name)
    {
        Intent intent = new Intent(context, ConvenienceDetailActivity.class);
        intent.putExtra("cid", convenienceId);
        intent.putExtra("name", name);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static boolean showWebPageActivity(Context context, String url)
    {
        Intent intent = new Intent(context, WebPageActivity.class);
        intent.putExtra("web_url", url);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        return true;
    }

    public static boolean showTradingListActivity(Context context,
            boolean isPromotion, String id, String name)
    {
        Intent intent = new Intent(context, TradingListActivity.class);
        intent.putExtra("is_promotion", isPromotion);
        intent.putExtra("id", id);
        if (!TextUtils.isEmpty(name))
        {
            intent.putExtra("name", name);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        return true;
    }

    public static void showMyRelatedActivity(Context context, int operateType)
    {
        Intent intent = new Intent(context, MyRelatedActivity.class);
        intent.putExtra("operateType", operateType);
        context.startActivity(intent);
    }

    public static void showChatActivity(Context context, String uid)
    {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("uid", uid);
        context.startActivity(intent);
    }

    public static void showPhotoGalleryActivity(Context context,
            ArrayList<String> photoList, int curIndex)
    {
        Intent intent = new Intent(context, PhotoGalleryActivity.class);
        intent.putExtra("photolist", photoList);
        intent.putExtra("index", curIndex);
        context.startActivity(intent);
    }

    public static void showAddressListActivity(Activity context,
            String selAddressId)
    {
        Intent intent = new Intent(context, AddressListActivity.class);
        intent.putExtra("address_id", selAddressId);
        context.startActivityForResult(intent,
                GlobalConfig.INTENT_SELECT_ADDRESS);
    }

    public static void showCouponListActivity(Activity context,
            String couponId, String couponCode, int couponFee)
    {
        Intent intent = new Intent(context, CouponListActivity.class);
        intent.putExtra("user_coupon_id", couponId);
        intent.putExtra("coupon_code", couponCode);
        intent.putExtra("coupon_fee", couponFee);
        context.startActivityForResult(intent,
                GlobalConfig.INTENT_SELECT_COUPON);
    }

    public static void showGuideActivity(Context context)
    {
        Intent intent = new Intent(context, GuideActivity.class);
        context.startActivity(intent);
    }

    public static void showGiftListActivity(Context context, String uid,
            int operType)
    {
        Intent intent = new Intent(context, GiftListActivity.class);
        intent.putExtra("uid", uid);
        intent.putExtra("opertype", operType);
        context.startActivity(intent);
    }

    public static void showPayTypeActivity(Activity context, MerchantListModel listModel)
    {
        Intent intent = new Intent(context, TradingPayTypeActivity.class);
        intent.putExtra("more_order", listModel);
        context.startActivityForResult(intent,
                GlobalConfig.INTENT_SELECT_PAYTYPE);
    }

    public static void showPayTypeActivity(Activity context, OrderModel model)
    {
        Intent intent = new Intent(context, TradingPayTypeActivity.class);
        intent.putExtra("one_order", model);
        context.startActivityForResult(intent,
                GlobalConfig.INTENT_SELECT_PAYTYPE);
    }

    public static void showPayResultActivity(Context context,
            ArrayList<String> orderIdList, int payResult)
    {
        Intent intent = new Intent(context, TradingPayResultActivity.class);
        intent.putExtra("orderlist", orderIdList);
        intent.putExtra("result", payResult);
        context.startActivity(intent);
    }

    public static void showLoginActivity(Activity context, int from)
    {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra("from", from);
        context.startActivityForResult(intent, GlobalConfig.INTENT_LOGIN);
    }

    public static void showUserPoints(TextView textView, int points)
    {
        textView.setText(Html.fromHtml(String.format(
                AppApplication.instance().getString(R.string.points),
                "<font color='#68b5f5'>"
                        + points
                        + "</font>")));
    }

    public static void showDeliveryQueryActivity(Context context, String deliveryNumber, String deliveryCompany)
    {
        Intent intent = new Intent(context, DeliverySearchDetailActivity.class);
        intent.putExtra("delivery_number", deliveryNumber);
        intent.putExtra("delivery_company", deliveryCompany);
        context.startActivity(intent);
    }

    public static void showScanOpenActivity(Context context, String query_order)
    {
        Intent intent = new Intent(context, ScanOpenDeliveryListActivity.class);
        intent.putExtra("query_order", query_order);
        context.startActivity(intent);
    }
}
