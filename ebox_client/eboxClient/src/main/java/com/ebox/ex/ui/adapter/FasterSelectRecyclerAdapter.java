package com.ebox.ex.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ebox.R;
import com.ebox.ex.network.model.FasterSelectMode;
import com.ebox.ex.network.model.base.type.BoxInfoType;
import com.ebox.ex.utils.BoxInfoHelper;
import com.ebox.ex.utils.OperatorHelper;
import com.ebox.pub.utils.MGViewUtil;

import java.util.List;
import java.util.Random;

/**
 * Created by Android on 2015/10/24.
 */
public class FasterSelectRecyclerAdapter extends RecyclerView.Adapter {


    private List<FasterSelectMode> mDatas;
    private OnFasterSelectListener mL;
    private Context mContext;

    public FasterSelectRecyclerAdapter(List<FasterSelectMode> data,OnFasterSelectListener listener) {
        mL=listener;
        mDatas = data;
    }

    private void updateState(FasterSelectMode mode, int action){

       int count= mode.getBox_infos().size();

        mode.setBox_count(count);

        if (count > 0)
        {
            if (action==0)//完成投递
            {
                mode.setBox_state(0);//正常使用
            }
            else if(action==1)//取消投递
            {
                mode.setBox_state(0);//正常使用
            }
            else if (action==3)//手动选择
            {
                mode.setBox_state(1);//选中状态
            }
        } else
        {
            mode.setBox_state(3);//箱门用完
        }
    }

    //全部遍历更新选择状态
    private void refreshChoose(BoxInfoType infoType, int action) {

        if (mDatas != null)
        {
            for (FasterSelectMode model : mDatas)
            {
                List<BoxInfoType> infos = model.getBox_infos();
                if (infos.size()>0)
                {
                    boolean isExist =false;
                    for (BoxInfoType info :infos)
                    {
                        if (info.getBoxCode().equals(infoType.getBoxCode())) {
                            if (action==0)
                            {
                                infos.remove(info);
                            }
                            isExist = true;
                            break;
                        }
                    }
                    //外层循环不能跳出
                    if (isExist)
                    {
                        updateState(model,action);
                    }else {
                        model.setBox_state(0);
                    }
                }
            }
            notifyDataSetChanged();
        }
    }

    //刷新选择状态
    public void refreshChoose(BoxInfoType infoType, int pos , int action)
    {
        if (mDatas != null) {
            if (pos==-1)
            {
                refreshChoose(infoType,action);
                return;
            }

            FasterSelectMode mode = mDatas.get(pos);
            if (action==0)//存件完成
            {
                List<BoxInfoType> infos = mode.getBox_infos();
                for (BoxInfoType info : infos)
                {
                    if (info.getBoxCode().equals(infoType.getBoxCode())) {
                        infos.remove(info);
                        break;
                    }
                }
            }
            updateState(mode,action);

            notifyDataSetChanged();
        }else
        {
            //重新获取数据
            setData(BoxInfoHelper.getFaster());
        }
    }




    public void setData(List<FasterSelectMode> data) {
        if (mDatas != null)
        {
            mDatas.clear();
        }
        mDatas = data;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        mContext=viewGroup.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.ex_item_faster_select, null);
        MGViewUtil.scaleContentView((ViewGroup)view);
        return new viewHolder(view ,mL);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {

        FasterSelectMode mode = mDatas.get(i);

        if (mode==null) {
            return;
        }
        viewHolder holder = (FasterSelectRecyclerAdapter.viewHolder) viewHolder;
        //图片资源
        holder.iv_icon.setImageResource(mode.getImage());
        //数量
        holder.tv_num.setTextColor(mContext.getResources().getColor(mode.getTextColor()));
        holder.tv_num.setText(mode.getBox_count() + "");
        //类型描述
        holder.tv_type.setTextColor(mContext.getResources().getColor(mode.getTextColor()));
        holder.tv_type.setText(mode.getBox_type());
        //背景色
        holder.mRoot.setBackgroundResource(mode.getBoxBg());
        //money
        holder.tv_momey.setTextColor(mContext.getResources().getColor(mode.getMoneyColor()));
        holder.tv_momey.setText(mode.getMoney());

    }


    @Override
    public int getItemCount() {

        if (mDatas==null)
        {
            return 0;
        }
        return mDatas.size();
    }

    public interface OnFasterSelectListener {

        void onBoxItemClick(BoxInfoType infoType, int pos);
    }


    class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tv_num;

        private TextView tv_type;

        private ImageView iv_icon;

        private TextView tv_momey;

        private RelativeLayout mRoot;

        private OnFasterSelectListener mL;

        private Random mRandom;

        public viewHolder(View root, OnFasterSelectListener listener) {
            super(root);
            this.mL = listener;
            mRoot = (RelativeLayout) root.findViewById(R.id.rl_item_box);
            mRoot.setOnClickListener(this);
            tv_momey = (TextView) root.findViewById(R.id.tv_money);
            if (!OperatorHelper.isAccount())
            {
                tv_momey.setVisibility(View.GONE);
            }
            iv_icon = (ImageView) root.findViewById(R.id.iv_icon);
            tv_num = (TextView) root.findViewById(R.id.tv_num);
            tv_type = (TextView) root.findViewById(R.id.tv_type);
            mRandom = new Random();
           // MGViewUtil.scaleContentView((ViewGroup) root);
        }

        @Override
        public void onClick(View v) {

            int layoutPosition = getLayoutPosition();
            FasterSelectMode mode = mDatas.get(layoutPosition);
            List<BoxInfoType> box_info = mode.getBox_infos();
            if (mode.getBox_count() > 0) {
                if (mL != null)
                {
                    mL.onBoxItemClick(getRandomInfo(box_info),layoutPosition);
                    notifySelectState(layoutPosition);
                }
            }

        }

        private void notifySelectState(int pos) {

            for (int i = 0; i < mDatas.size(); i++)
            {
                FasterSelectMode mode = mDatas.get(i);
                if (mode.getBox_count()>0)
                {
                    if (i == pos)
                    {
                        mode.setBox_state(1);
                    }else
                    {
                        mode.setBox_state(0);
                    }
                }
            }
            notifyDataSetChanged();

        }

        private int getRandomPos(int size){
            return  mRandom.nextInt(size);
        }

        private BoxInfoType getRandomInfo(List<BoxInfoType> infos) {

            int size = infos.size();

            while(true)
            {
                int info =getRandomPos(size);

                if (info >= 0 && info < size)
                {
                    return infos.get(info);
                }
            }
        }
    }
}
