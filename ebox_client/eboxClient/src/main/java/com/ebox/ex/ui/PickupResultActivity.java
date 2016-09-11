package com.ebox.ex.ui;

import android.os.Bundle;

import com.ebox.R;
import com.ebox.ex.network.model.enums.PickupType;
import com.ebox.ex.ui.base.BaseFragment;
import com.ebox.ex.ui.fragment.ResultPickupFragment;
import com.ebox.pub.service.AppService;
import com.ebox.pub.ui.customview.Title;
import com.ebox.pub.ui.customview.Title.TitleData;
import com.ebox.pub.utils.MGViewUtil;

public class PickupResultActivity extends CommonActivity {

    private Title title;
    private TitleData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ex_activity_pickup_result);
        MGViewUtil.scaleContentView(this, R.id.rootView);

        Bundle extras = getIntent().getExtras();

        String pick_type = extras.getString(ResultPickupFragment.PICK_TYPE);

        BaseFragment baseFragment = ResultPickupFragment.newInstance(getIntent().getExtras());

        getFragmentManager().beginTransaction().replace(R.id.fl_content, baseFragment).commit();

        initTitle(pick_type);
    }

    private void initTitle(String pick_type)
    {
        title = (Title) findViewById(R.id.title);
        data = title.new TitleData();
        data.backVisibility = 1;
        String string;
        if (pick_type.equals(PickupType.customer))//用户取件
        {
            string=getResources().getString(R.string.ex_i_pickup);
        }
        else if(pick_type.equals(PickupType.operator))//确认回收
        {
            string=getResources().getString(R.string.ex_op_withdraw_confirm);
        }else
        {
            string="未知取件类型";
        }

        data.tvContent = string;
        data.tvVisibility = true;
        title.setData(data, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        title.showTimer();
        AppService.getIntance().setTimeoutLeft(30);
    }

    @Override
    protected void onPause() {
        super.onPause();
        title.stopTimer();
    }


}
