package com.ebox.ex.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.ebox.Anetwork.RequestManager;
import com.ebox.R;

import java.util.ArrayList;

/**
 * Created by prin on 2015/11/3.
 * 常用快递公司显示
 */
public class ImageAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> imageList;
    private boolean clicked = false;
    private int positon;

    public ImageAdapter(Context context, ArrayList<String> imageList) {
        this.context = context;
        this.imageList = imageList;
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public Object getItem(int i) {
        return imageList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.ex_item_area_image, null);
            viewHolder.areaIV = (ImageView) convertView.findViewById(R.id.ex_iv_iai_area);
            viewHolder.bgIV= (ImageView) convertView.findViewById(R.id.ex_iv_iai_bg);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        RequestManager.loadImage(viewHolder.areaIV, imageList.get(position), R.drawable.default_ptr_rotate);

        if (position == bgPosition) {
            viewHolder.bgIV.setVisibility(View.VISIBLE);
        }else{
            viewHolder.bgIV.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    class ViewHolder {
        ImageView areaIV;
        ImageView bgIV;
    }

    private int bgPosition = -1;

    public void setPositon(int positon) {
        bgPosition = positon;
    }



}
