package com.xhl.world.ui.recyclerHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xhl.world.R;
import com.xhl.world.model.FlashSaleTimeModel;
import com.xhl.world.ui.view.pub.callback.RecycleViewCallBack;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;

/**
 * Created by Sum on 15/12/11.
 */
public class FlashSaleDetailsTimeDataHolder extends RecyclerDataHolder {

    private RecycleViewCallBack mCallBack;
    private FlashSaleTimeModel mData;

    public FlashSaleDetailsTimeDataHolder(FlashSaleTimeModel data) {
        super(data);
        mData = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, int position) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_flash_sale_details_time, null);

        view.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));

        return new FlashSaleDetailsTimeViewHolder(view, mCallBack);
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, Object data) {
        FlashSaleDetailsTimeViewHolder holder = (FlashSaleDetailsTimeViewHolder) vHolder;
     //   LogUtil.e("position:" + position + " state:" + getHolderState());
        if (getHolderState() == HOLDER_SELECT) {//选中状态
            holder.rl_content.setBackgroundResource(R.color.app_light_red);
        } else {
            holder.rl_content.setBackgroundResource(R.color.transparent);
        }
        //设置时间段
        holder.tv_start_time.setText(mData.getShowTime());
        //设置抢购状态
        holder.tv_start_state.setText(mData.getStateDes()+","+position);
    }

    public void setCallBack(RecycleViewCallBack CallBack) {
        this.mCallBack = CallBack;
    }


    static class FlashSaleDetailsTimeViewHolder extends RecyclerViewHolder implements View.OnClickListener {

        private TextView tv_start_time;
        private TextView tv_start_state;
        private RecycleViewCallBack callBack;
        private RelativeLayout rl_content;

        public FlashSaleDetailsTimeViewHolder(View view, RecycleViewCallBack callBack) {
            super(view);
            this.callBack = callBack;
            view.setOnClickListener(this);
            tv_start_time = (TextView) view.findViewById(R.id.tv_flash_sale_time);
            tv_start_state = (TextView) view.findViewById(R.id.tv_flash_sale_state);
            rl_content = (RelativeLayout) view.findViewById(R.id.rl_content);
        }

        @Override
        public void onClick(View v) {
            if (callBack != null) {
                callBack.onItemClick(getAdapterPosition());
            }
        }
    }
}
