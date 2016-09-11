package com.ebox.ex.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ebox.R;

import java.util.ArrayList;

/**
 * Created by admin on 2015/8/4.
 */
public class OrgnizationAdapter extends BaseAdapter {

    private Context mContext;
    ArrayList<String> mAreasList;
    private boolean clicked = false;
    private int positon;

    public OrgnizationAdapter(Context context, ArrayList<String> areasList) {
        mContext = context;
        mAreasList = areasList;
    }

    @Override
    public int getCount() {
        return mAreasList.size();
    }

    @Override
    public Object getItem(int i) {
        return mAreasList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
       ViewHolder viewHolder=null;
        if (convertView == null) {
            viewHolder=new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.ex_gv_origitem, null);

            viewHolder.tv_Lable= (TextView) convertView.findViewById(R.id.ex_tv_label);
            viewHolder.iv_Bg= (ImageView) convertView.findViewById(R.id.ex_iv_bg);

            convertView.setTag(viewHolder);
        }else{
           viewHolder= (ViewHolder) convertView.getTag();

        }

        viewHolder.tv_Lable.setText(mAreasList.get(position));



        if (position == bgPosition) {
//            view.setBackgroundColor(mContext.getResources().getColor(R.color.ex_box_big_blue));
            viewHolder.iv_Bg.setVisibility(View.VISIBLE);
        }else{
            viewHolder.iv_Bg.setVisibility(View.INVISIBLE);
        }


        return convertView;
    }

    class ViewHolder{
        TextView tv_Lable;
        ImageView iv_Bg;
    }

    private int bgPosition = -1;

    public void setPositon(int positon) {
        bgPosition = positon;
    }

}
