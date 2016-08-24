package com.xhl.world.ui.main.home.recyclerHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.xhl.world.model.ProductModel;
import com.xhl.world.ui.main.home.HomeItemType;
import com.xhl.world.ui.main.home.bar.HomeGuessItemBar;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;

import java.util.List;

/**
 * Created by Sum on 15/12/2.
 */
public class GuessYouLikeDataHolder extends RecyclerDataHolder {


    public GuessYouLikeDataHolder(Object data) {
        super(data);
    }

    @Override
    public int getType() {
        return HomeItemType.Type_Guess_you_like;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, int position) {
        // int margin = (int) context.getResources().getDimension(R.dimen.px_dimen_20);
        HomeGuessItemBar bar2 = new HomeGuessItemBar(context);

     /*   LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
        // layoutParams.topMargin = margin;

        bar2.setLayoutParams(layoutParams);*/

        return new RecyclerViewHolder(bar2);
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, Object data) {
        if (vHolder.getItemViewType() == getType()) {

            HomeGuessItemBar bar2 = (HomeGuessItemBar) vHolder.itemView;

            bar2.onBindGuessLike((List<ProductModel>) data);
        }

    }
}
