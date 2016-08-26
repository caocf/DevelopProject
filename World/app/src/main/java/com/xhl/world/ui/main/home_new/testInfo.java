package com.xhl.world.ui.main.home_new;

import com.xhl.world.R;
import com.xhl.world.model.AdvHTest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Summer on 2016/8/26.
 */
public class testInfo {

    public static List<AdvHTest> getAdv() {

        List<AdvHTest> ads = new ArrayList<>();

        ads.add(getProduct("", R.drawable.ad_1_1, R.drawable.ad_2_1,R.drawable.ad_1_1));
        ads.add(getImage(0));//caidan
        ads.add(getImage(R.drawable.ad_2_1));
        ads.add(getProduct("门店体验", R.drawable.ad_3_1, R.drawable.ad_3_2));
        ads.add(getProduct("汇买上新", R.drawable.ad_4_1, R.drawable.ad_4_2, R.drawable.ad_4_3));
        ads.add(getProduct("限时购买", R.drawable.ad_5_1, R.drawable.ad_5_2, R.drawable.ad_5_3, R.drawable.ad_5_4));
        ads.add(getProduct("季节专项", R.drawable.ad_6_1, R.drawable.ad_6_2, R.drawable.ad_6_3, R.drawable.ad_6_4, R.drawable.ad_6_5, R.drawable.ad_6_6));
        ads.add(getProduct("茶果饮料", R.drawable.ad_7_1, R.drawable.ad_7_2, R.drawable.ad_7_3));
        ads.add(getProduct("坚果谷物", R.drawable.ad_8_1, R.drawable.ad_8_2, R.drawable.ad_8_3));
        ads.add(getProduct("米面油脂", R.drawable.ad_8_1, R.drawable.ad_8_2, R.drawable.ad_8_3));
        ads.add(getProduct("年份果酒", R.drawable.ad_10_1, R.drawable.ad_10_2, R.drawable.ad_10_3));
        ads.add(getProduct("烘培烹饪", R.drawable.ad_8_1, R.drawable.ad_8_2, R.drawable.ad_8_3));
        return ads;

    }

    private static AdvHTest getImage(int resId) {
        return new AdvHTest(null, resId);
    }

    private static AdvHTest getProduct(String name, int... resId) {
        AdvHTest advHTest = new AdvHTest(name, 0);
        advHTest.setList(getImages(resId));
        return advHTest;
    }

    private static List<AdvHTest> getImages(int... resId) {
        List<AdvHTest> ids = new ArrayList<>();
        for (int id : resId) {
            ids.add(getImage(id));
        }
        return ids;
    }

}
