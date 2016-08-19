package com.xhl.bqlh.view.ui.recyclerHolder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xhl.bqlh.R;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * Created by Sum on 16/4/11.
 */
public class MyFunItem extends RecyclerDataHolder<MyFunItem.SheetItem> {

    public interface SheetItemClickCallback {
        void onClick(int tag);
    }

    public static class SheetItem {
        public int tag;//点击项的标示
        public int id;
        public String name;
        public Intent intent;
        public SheetItemClickCallback callback;
    }

    public MyFunItem(SheetItem data) {
        super(data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, ViewGroup parent, int position) {
        View view = View.inflate(context, R.layout.item_my_fun, null);

        view.setLayoutParams(new ViewGroup.LayoutParams(-1, 160));

        AutoUtils.auto(view);

        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final Context context, final int position, RecyclerView.ViewHolder vHolder, final SheetItem data) {

        View contentView = vHolder.itemView;

        ImageView imageView = (ImageView) contentView.findViewById(R.id.sheet_icon);
        imageView.setImageResource(data.id);

        TextView textView = (TextView) contentView.findViewById(R.id.sheet_text);
        textView.setText(data.name);

        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.intent != null) {
                    context.startActivity(data.intent);
                }
                if (data.callback != null) {
                    data.callback.onClick(data.tag);
                }
            }
        });

    }
}
