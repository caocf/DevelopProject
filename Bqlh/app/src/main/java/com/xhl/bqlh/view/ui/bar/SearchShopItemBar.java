package com.xhl.bqlh.view.ui.bar;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xhl.bqlh.Api.ApiControl;
import com.xhl.bqlh.R;
import com.xhl.bqlh.model.GarbageModel;
import com.xhl.bqlh.model.ProductModel;
import com.xhl.bqlh.model.ShopModel;
import com.xhl.bqlh.model.base.ResponseModel;
import com.xhl.bqlh.view.base.BaseBar;
import com.xhl.bqlh.view.base.Common.DefaultCallback;
import com.xhl.bqlh.view.custom.LifeCycleImageView;
import com.xhl.bqlh.view.helper.EventHelper;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * Created by Summer on 2016/7/18.
 */
public class SearchShopItemBar extends BaseBar {

    public SearchShopItemBar(Context context) {
        super(context);
    }

    @ViewInject(R.id.tv_shop_name)
    private TextView tv_shop_name;

    @ViewInject(R.id.iv_1)
    private ImageView iv1;

    @ViewInject(R.id.iv_2)
    private ImageView iv2;

    @ViewInject(R.id.iv_3)
    private ImageView iv3;

    @Event(value = {R.id.iv_1, R.id.iv_2, R.id.iv_3})
    private void onImageClick(View view) {
        Object tag = view.getTag();
        if (tag != null && tag instanceof ProductModel) {
            ProductModel pro = (ProductModel) tag;
            EventHelper.postProduct(pro.getId());
        }
    }

    @Event(R.id.ll_shop)
    private void onShopClick(View view) {
        EventHelper.postShop(mShop.getShopId());
    }

    private ShopModel mShop;

    @Override
    protected void initParams() {

    }

    public void onBindData(ShopModel shop) {
        if (mShop == shop) {
            return;
        }
        mShop = shop;
        tv_shop_name.setText(shop.getShopName());
        ApiControl.getApi().searchShopNewPro(shop.getShopId(), new DefaultCallback<ResponseModel<GarbageModel>>() {
            @Override
            public void success(ResponseModel<GarbageModel> result) {
                GarbageModel obj = result.getObj();
                List<ProductModel> list = obj.getProductList();
                loadImage(list);
            }

            @Override
            public void finish() {

            }
        });
    }

    private void loadImage(List<ProductModel> data) {
        if (data == null || data.size() != 3) {
            return;
        }
        iv1.setTag(data.get(0));
        x.image().bind(iv1, data.get(0).getProductPic(), LifeCycleImageView.imageOptions);

        iv2.setTag(data.get(1));
        x.image().bind(iv2, data.get(1).getProductPic(), LifeCycleImageView.imageOptions);

        iv3.setTag(data.get(2));
        x.image().bind(iv3, data.get(2).getProductPic(), LifeCycleImageView.imageOptions);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.bar_search_shop_item;
    }
}
