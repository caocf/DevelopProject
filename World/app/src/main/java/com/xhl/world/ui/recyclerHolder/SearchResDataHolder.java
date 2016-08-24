package com.xhl.world.ui.recyclerHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xhl.world.R;
import com.xhl.world.ui.event.EventBusHelper;
import com.xhl.world.ui.event.SearchItemEvent;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;

/**
 * Created by Sum on 15/12/30.
 */
public class SearchResDataHolder extends RecyclerDataHolder {

    public SearchResDataHolder(Object data) {
        super(data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_serach_res, null);
        view.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
        return new SearchResViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, Object data) {
        SearchResViewHolder holder = (SearchResViewHolder) vHolder;
        final String st = (String) data;
        holder.btn_search_res.setText(st);
        vHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBusHelper.postSearchContent(SearchItemEvent.Type_Fast_Search,st);
            }
        });
    }

    static class SearchResViewHolder extends RecyclerViewHolder {
        TextView btn_search_res;

        public SearchResViewHolder(View view) {
            super(view);
            btn_search_res = (TextView) view.findViewById(R.id.btn_search_res);
        }
    }
}
