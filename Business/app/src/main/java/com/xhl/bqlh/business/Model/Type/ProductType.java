package com.xhl.bqlh.business.Model.Type;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

/**
 * Created by Summer on 2016/8/8.
 */
public class ProductType {

    public static final int PRODUCT_GIFT = 0x100;

    public static SpannableString GetGiftTitle(String giftName) {
        SpannableString hint = new SpannableString("赠品:" + giftName);
        hint.setSpan(new ForegroundColorSpan(Color.RED), 0, 3, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return hint;
    }

}
