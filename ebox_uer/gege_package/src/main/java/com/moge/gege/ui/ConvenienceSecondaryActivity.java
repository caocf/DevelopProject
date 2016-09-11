package com.moge.gege.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;

import com.moge.gege.R;
import com.moge.gege.model.LocalServiceModel;
import com.moge.gege.ui.adapter.LocalServiceAdapter;
import com.moge.gege.ui.adapter.LocalServiceAdapter.LocalServiceListener;

public class ConvenienceSecondaryActivity extends BaseActivity implements
        LocalServiceListener
{
    private Context mContext;
    private GridView mSecondaryGridView;
    private LocalServiceAdapter mLocalServiceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conveniencesecondary);

        mContext = ConvenienceSecondaryActivity.this;
        initView();
        initData();
    }

    @Override
    protected void initView()
    {
        super.initView();

        this.setHeaderLeftImage(R.drawable.icon_back);
        this.setHeaderLeftTitle(R.string.more);

        mSecondaryGridView = (GridView) this
                .findViewById(R.id.secondaryGridView);
        mLocalServiceAdapter = new LocalServiceAdapter(mContext);
        mLocalServiceAdapter.setListener(this);
        mSecondaryGridView.setAdapter(mLocalServiceAdapter);
        mSecondaryGridView.setOnItemClickListener(mLocalServiceAdapter);
    }

    private void initData()
    {
        final String nameList[] = this.getResources().getStringArray(
                R.array.convenience_list);
        final String resNameList[] = this.getResources().getStringArray(
                R.array.convenience_resname_list);

        List<LocalServiceModel> serviceList = new ArrayList<LocalServiceModel>();

        for (int i = 0; i < nameList.length; i++)
        {
            LocalServiceModel model = new LocalServiceModel();
            model.setName(nameList[i]);
            model.setResId(getResources().getIdentifier(resNameList[i],
                    "drawable", getPackageName()));
            model.setValue(i + 1); // convenience type
            serviceList.add(model);
        }

        mLocalServiceAdapter.clear();
        mLocalServiceAdapter.addAll(serviceList);
        mLocalServiceAdapter.notifyDataSetChanged();
    }

    private void gotoConvenienceActivity(int type)
    {
        Intent intent = new Intent(mContext, ConvenienceListActivity.class);
        intent.putExtra("type", type);
        this.startActivity(intent);
    }

    @Override
    public void onServiceClick(int position, LocalServiceModel model)
    {
        gotoConvenienceActivity(model.getValue());
    }

}
