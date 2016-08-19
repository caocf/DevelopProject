package com.xhl.bqlh.view.ui.recyclerHolder;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xhl.bqlh.Api.ApiControl;
import com.xhl.bqlh.R;
import com.xhl.bqlh.model.ShopModel;
import com.xhl.bqlh.model.base.ResponseModel;
import com.xhl.bqlh.view.base.Common.DefaultCallback;
import com.xhl.bqlh.view.helper.DialogMaker;
import com.xhl.bqlh.view.helper.EventHelper;
import com.xhl.bqlh.view.helper.pub.Callback.RecycleViewCallBack;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;

import org.xutils.x;

/**
 * Created by Summer on 2016/7/18.
 */
public class CollectionShopDataHolder extends RecyclerDataHolder<ShopModel> {
    private RecycleViewCallBack callBack;

    public CollectionShopDataHolder(ShopModel data, RecycleViewCallBack callBack) {
        super(data);
        this.callBack = callBack;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, ViewGroup parent, int position) {
        View view = View.inflate(context, R.layout.item_collection_shop, null);
        return new Shop(view);
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, ShopModel data) {
        Shop shop = (Shop) vHolder;
        shop.onBindData(data);
    }

    class Shop extends RecyclerViewHolder implements View.OnClickListener {
        private ShopModel mData;

        private ImageView image;

        private TextView name;

        public Shop(View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.iv_image);
            name = (TextView) view.findViewById(R.id.tv_name);

            view.setOnClickListener(this);
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    longClick();
                    return true;
                }
            });
        }

        public void onBindData(ShopModel data) {
            mData = data;
            name.setText(data.getShopName());
            x.image().bind(image, data.getShopLogo());
        }

        @Override
        public void onClick(View v) {
            if (mData != null) {
                EventHelper.postShop(mData.getCollectionShopId());
            }
        }

        private void longClick() {
            AlertDialog.Builder dialog = DialogMaker.getDialog(mContext);
            dialog.setTitle("删除店铺");
            dialog.setMessage("您确定删除收藏的店铺吗?");
            dialog.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onDelete();
                }
            });
            dialog.setNegativeButton(R.string.dialog_cancel, null);
            dialog.show();
        }

        private void onDelete() {
            ApiControl.getApi().collectDelete(mData.getId(), new DefaultCallback<ResponseModel<Object>>() {
                @Override
                public void success(ResponseModel<Object> result) {
                    if (callBack != null) {
                        callBack.onItemClick(getAdapterPosition(), null);
                    }
                }

                @Override
                public void finish() {

                }
            });
        }
    }
}
