package com.xhl.world.ui.main.home.recyclerHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.xhl.world.R;
import com.xhl.world.ui.main.home.HomeItemType;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * Created by Sum on 16/1/7.
 */
public class GuessLikeTipDataHolder extends RecyclerDataHolder {
    public GuessLikeTipDataHolder(Object data) {
        super(data);
    }

    @Override
    public int getType() {
        return HomeItemType.Type_Guess_you_like_tip;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_guess_like_tip, null);
        int marginTop = (int) context.getResources().getDimension(R.dimen.px_dimen_20);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -2);
        params.topMargin = marginTop;
        view.setLayoutParams(params);
        AutoUtils.auto(view);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, Object data) {
        /*View itemView = vHolder.itemView;
        TextView tip = (TextView) itemView.findViewById(R.id.tv_guess_tip);
        if (tip != null) {
        }*/
    }
}
