package com.xhl.world.ui.recyclerHolder;

import android.content.Context;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xhl.world.R;
import com.xhl.world.model.FlashSaleItemDetailsModel;
import com.xhl.world.ui.view.pub.callback.RecycleViewCallBack;
import com.xhl.world.ui.view.LifeCycleImageView;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;

import java.util.Random;

/**
 * Created by Sum on 15/12/12.
 */
public class FlashSaleItemDetailsDataHolder extends RecyclerDataHolder {

    private FlashSaleItemDetailsModel mData;
    private RecycleViewCallBack callBack;

    public FlashSaleItemDetailsDataHolder(FlashSaleItemDetailsModel data) {
        super(data);
        mData = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_flash_sale_details, null);

        return new FlashSaleItemDetailsViewHolder(view, callBack);
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, Object data) {
        FlashSaleItemDetailsViewHolder holder = (FlashSaleItemDetailsViewHolder) vHolder;
        holder.bindData((FlashSaleItemDetailsModel) data);
    }


    public void setCallBack(RecycleViewCallBack callBack) {
        this.callBack = callBack;
    }

    static class FlashSaleItemDetailsViewHolder extends RecyclerViewHolder implements View.OnClickListener {

        private RecycleViewCallBack callBack;
        private Context mContext;
        private LifeCycleImageView iv_item_search_icon;
        private LifeCycleImageView iv_item_search_country_icon;
        private TextView tv_item_search_title;
        private TextView tv_item_search_price;
        private TextView tv_item_search_price_old;
        private TextView tv_item_search_country;
        private TextView tv_goods_details;
        private ProgressBar sale_progressBar;

        public FlashSaleItemDetailsViewHolder(View view, RecycleViewCallBack callBack) {
            super(view);
            this.callBack = callBack;
            mContext = view.getContext();
            view.setOnClickListener(this);
            iv_item_search_icon = (LifeCycleImageView) view.findViewById(R.id.iv_item_search_icon);
            iv_item_search_country_icon = (LifeCycleImageView) view.findViewById(R.id.iv_item_search_country_icon);
            tv_item_search_title = (TextView) view.findViewById(R.id.tv_item_search_title);
            tv_item_search_price = (TextView) view.findViewById(R.id.tv_item_search_price);
            tv_item_search_price_old = (TextView) view.findViewById(R.id.tv_item_search_price_old);
            //设置中线
            tv_item_search_price_old.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);

            tv_item_search_country = (TextView) view.findViewById(R.id.tv_item_search_country);
            tv_goods_details = (TextView) view.findViewById(R.id.tv_goods_details);
            sale_progressBar = (ProgressBar) view.findViewById(R.id.sale_progressBar);
            sale_progressBar.setMax(100);

            random = new Random();
        }

        public void bindData(FlashSaleItemDetailsModel model) {
            if (model == null) {
                return;
            }
            tv_item_search_title.setText("测试标题"+getRandom());
            sale_progressBar.setProgress(getRandom());
            tv_item_search_price_old.setText(getRandom()+"");
            tv_item_search_price.setText("" + getRandom());
            iv_item_search_icon.bindImageUrl(getUrl());
            updateBuyText(getRandom());
        }

        private Random random;

        private int getRandom() {
            return random.nextInt(100);
        }
        private String getUrl(){
            int random = getRandom();
            if (random%2 == 0) {
                return "http://img2.imgtn.bdimg.com/it/u=2469637794,1231617530&fm=21&gp=0.jpg";
            }
            return "http://img0.imgtn.bdimg.com/it/u=1845283912,3248204034&fm=21&gp=0.jpg";
        }

        private void updateBuyText(int test) {
            if (test % 2 == 0) {
                tv_goods_details.setBackgroundResource(R.drawable.code_btn_light_red_bg);
                tv_goods_details.setTextColor(ContextCompat.getColor(mContext,R.color.app_while));
                tv_goods_details.setText(R.string.false_sale_buy);
            }else {
                tv_goods_details.setBackgroundResource(R.drawable.code_btn_light_red_bg_line);
                tv_goods_details.setTextColor(ContextCompat.getColor(mContext,R.color.app_light_red));
                tv_goods_details.setText(R.string.false_sale_more_policy);
            }
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            if (callBack != null) {
                callBack.onItemClick(getAdapterPosition());
            }
        }
    }
}
