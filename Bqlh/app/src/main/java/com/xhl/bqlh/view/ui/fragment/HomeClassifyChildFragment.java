package com.xhl.bqlh.view.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.xhl.bqlh.R;
import com.xhl.bqlh.model.ClassifyModel;
import com.xhl.bqlh.view.base.BaseAppFragment;
import com.xhl.bqlh.view.ui.recyclerHolder.ClassifyChildDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerAdapter;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sum on 16/7/4.
 */
@ContentView(R.layout.fragment_home_classify_child)
public class HomeClassifyChildFragment extends BaseAppFragment {

    @ViewInject(R.id.recycler_view)
    private RecyclerView recycler_view;

    @Override
    protected void initParams() {

        ArrayList<ClassifyModel> data = (ArrayList<ClassifyModel>) getArguments().getSerializable("data");

        List<RecyclerDataHolder> holders = new ArrayList<>();
        int index = 0;
        for (ClassifyModel classify : data) {
            int size = classify.getChildren().size();
            if (size > 0) {
                index++;
                holders.add(new ClassifyChildDataHolder(classify, index));
                recycler_view.getRecycledViewPool().setMaxRecycledViews(index, 20);
            }
        }

        recycler_view.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler_view.setAdapter(new RecyclerAdapter(getContext(), holders));
        recycler_view.setHasFixedSize(true);

    }
}
