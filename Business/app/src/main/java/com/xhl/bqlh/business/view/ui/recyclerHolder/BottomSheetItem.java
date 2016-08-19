package com.xhl.bqlh.business.view.ui.recyclerHolder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.view.helper.ViewHelper;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;

/**
 * Created by Sum on 16/4/11.
 */
public class BottomSheetItem extends RecyclerDataHolder<BottomSheetItem.SheetItem> {

    public interface SheetItemClickCallback {
        void onClick(int tag);
    }

    public static class SheetItem {
        public int tag;//点击项的标示
        public int id;
        public String name;
        public Intent intent;
        public int count = 0;
        public SheetItemClickCallback callback;
    }

    public BottomSheetItem(SheetItem data) {
        super(data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, ViewGroup parent, int position) {
        return new RecyclerViewHolder(View.inflate(context, R.layout.item_bottom_sheet, null));
    }

    @Override
    public void onBindViewHolder(final Context context, final int position, RecyclerView.ViewHolder vHolder, final SheetItem data) {

        View contentView = vHolder.itemView;

        View sheet_tint = contentView.findViewById(R.id.sheet_tint);
        if (data.count == 0) {
            ViewHelper.setViewGone(sheet_tint, true);
        } else {
            ViewHelper.setViewGone(sheet_tint, false);
        }

        ImageView imageView = (ImageView) contentView.findViewById(R.id.sheet_icon);
        imageView.setImageResource(data.id);

        TextView textView = (TextView) contentView.findViewById(R.id.sheet_text);
        textView.setText(data.name);

        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.callback != null) {
                    data.callback.onClick(data.tag);
                } else if (data.intent != null) {
                    context.startActivity(data.intent);
                }
            }
        });

    }


}
