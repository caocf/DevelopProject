package com.xhl.bqlh.business.view.ui.recyclerHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xhl.bqlh.business.R;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;

/**
 * Created by Sum on 16/4/30.
 */
public class ProductTypeDataHolder extends RecyclerDataHolder<String> {

    public ProductTypeDataHolder(String data) {
        super(data);
    }

    @Override
    public int getType() {
        return 2;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, ViewGroup parent, int position) {

        View view = View.inflate(context, R.layout.pub_text_hint, null);
        view.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));

        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, String data) {
        TextView textView = (TextView) vHolder.itemView;
        textView.setText(data);
    }
}
