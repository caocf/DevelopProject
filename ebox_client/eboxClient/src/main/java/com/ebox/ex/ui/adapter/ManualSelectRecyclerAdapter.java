package com.ebox.ex.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ebox.R;
import com.ebox.ex.network.model.base.type.BoxInfoType;
import com.ebox.ex.network.model.enums.BoxSizeType;
import com.ebox.ex.network.model.enums.BoxStateType;
import com.ebox.ex.network.model.enums.DoorStatusType;
import com.ebox.pub.utils.MGViewUtil;

import java.util.List;

/**
 * Created by Android on 2015/8/25.
 */
public class ManualSelectRecyclerAdapter extends RecyclerView.Adapter {


    private List<BoxInfoType> mDatas;
    private onItemClickListener mL;
    private Context mContext;
    public ManualSelectRecyclerAdapter(List<BoxInfoType> datas) {
        mDatas = datas;
    }
    private int rackNumber;

    public void setData(List<BoxInfoType> data,int rackNumber) {
        if (mDatas != null)
        {
            mDatas.clear();
        }
        this.rackNumber=rackNumber;
        mDatas = data;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(onItemClickListener l) {
        this.mL = l;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        mContext=viewGroup.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.ex_item_manual_select, null);
        MGViewUtil.scaleContentView(view);
        return new viewHolder(view, mL);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {

        viewHolder holder = (ManualSelectRecyclerAdapter.viewHolder) viewHolder;

        BoxInfoType boxInfoType = mDatas.get(i);

        holder.getTv_num().setText("" + (++i));

        holder.getTv_state().setText(checkState(holder.getTv_state(),boxInfoType));

    }

    private String checkState(View view,BoxInfoType infoType) {

        StringBuilder builder = new StringBuilder();

        if (infoType.getBoxStatus()== BoxStateType.empty
                &&infoType.getDoorStatus() != DoorStatusType.unknow)
        {
            view.setBackgroundResource(R.drawable.ex_code_rack);
            view.setEnabled(true);
            if (infoType.getBoxSize().equals(BoxSizeType.large))
            {
                builder.append("空闲").append("(").append("大").append(")");
            }
            else if (infoType.getBoxSize().equals(BoxSizeType.medium))
            {
                builder.append("空闲").append("(").append("中").append(")");
            }
            else if (infoType.getBoxSize().equals(BoxSizeType.tiny))
            {
                builder.append("空闲").append("(").append("微").append(")");
            }
            else
            {
                builder.append("空闲").append("(").append("小").append(")");
            }
            view.setBackgroundResource(R.drawable.ex_code_rack);
        }else
        {
            builder.append("已存");
            view.setBackgroundResource(R.drawable.ex_code_rack_disabled);
            view.setEnabled(false);
        }
        return builder.toString();
    }

    @Override
    public int getItemCount() {

        if (mDatas==null) {
            return 0;
        }
        if (mDatas.size() != rackNumber) {
            return rackNumber;
        }else
        return mDatas.size();
    }

    public interface onItemClickListener {

        void onBoxItemClick(BoxInfoType infoType, int pos);
    }


    class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tv_num;

        private TextView tv_state;

        private onItemClickListener mL;

        public viewHolder(View root, onItemClickListener listener) {
            super(root);
            this.mL = listener;
            root.setOnClickListener(this);
            tv_num = (TextView) root.findViewById(R.id.tv_num);
            tv_state = (TextView) root.findViewById(R.id.tv_state);

            MGViewUtil.scaleContentView((ViewGroup) root);
        }


        public TextView getTv_num() {
            return tv_num;
        }

        public TextView getTv_state() {
            return tv_state;
        }

        @Override
        public void onClick(View v) {

            int layoutPosition = getLayoutPosition();
            BoxInfoType boxInfoType = mDatas.get(layoutPosition);

            if (mL != null)
            {
                if ( boxInfoType.getDoorStatus() != DoorStatusType.unknow
                        && boxInfoType.getBoxStatus()==BoxStateType.empty)
                {
                    mL.onBoxItemClick(boxInfoType, layoutPosition);
                }
            }
        }

    }
}
