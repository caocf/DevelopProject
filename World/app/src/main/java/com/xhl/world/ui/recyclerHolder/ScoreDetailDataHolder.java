package com.xhl.world.ui.recyclerHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.xhl.world.R;
import com.xhl.world.model.ScoreModel;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by Sum on 16/1/8.
 */
public class ScoreDetailDataHolder extends RecyclerDataHolder {
    public ScoreDetailDataHolder(Object data) {
        super(data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, int position) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_score_detail, null);

        return new ScoreDetailView(view);
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, Object data) {
        ScoreDetailView view = (ScoreDetailView) vHolder;
        view.onBindData((ScoreModel) data);
    }

    static class ScoreDetailView extends RecyclerViewHolder {
        @ViewInject(R.id.tv_score_res)
        private TextView tv_score_res;

        @ViewInject(R.id.tv_score_num)
        private TextView tv_score_num;

        @ViewInject(R.id.tv_score_lef)
        private TextView tv_score_lef;

        public ScoreDetailView(View view) {

            super(view);
            x.view().inject(this, view);
        }

        public void onBindData(ScoreModel scoreModel) {
            if (scoreModel == null) {
                return;
            }
            tv_score_lef.setText(scoreModel.getScoreLef());
            tv_score_num.setText(scoreModel.getScoreNum());
            tv_score_res.setText(scoreModel.getScoreRes());
        }
    }
}
