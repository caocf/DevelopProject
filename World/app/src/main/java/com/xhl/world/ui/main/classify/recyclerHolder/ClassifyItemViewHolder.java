package com.xhl.world.ui.main.classify.recyclerHolder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xhl.world.R;
import com.xhl.world.ui.view.pub.callback.RecycleViewCallBack;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;

/**
 * Created by Sum on 15/11/27.
 */
public class ClassifyItemViewHolder extends RecyclerViewHolder implements View.OnClickListener {

    private RecycleViewCallBack mCallBack;

    public TextView tv_classify_item_name;

    public LinearLayout rl_content;

    public View line_hor;

    public ClassifyItemViewHolder(View view, RecycleViewCallBack callBack) {
        super(view);
        mCallBack = callBack;
        line_hor = view.findViewById(R.id.line_hor);
        rl_content = (LinearLayout) view.findViewById(R.id.rl_content);
        tv_classify_item_name = (TextView) view.findViewById(R.id.tv_classify_item_name);
        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (mCallBack != null)
        {
            mCallBack.onItemClick(getAdapterPosition());
        }

    }
}
