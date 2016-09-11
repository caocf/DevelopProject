package com.ebox.ex.ui;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import com.ebox.R;
import com.ebox.ex.ui.fragment.BoxManualSelectionFragment;
import com.ebox.ex.utils.OperatorHelper;
import com.ebox.ex.network.model.enums.AcountType;
import com.ebox.ex.network.model.enums.BoxSizeType;
import com.ebox.ex.network.model.base.type.BoxInfoType;
import com.ebox.ex.network.model.base.type.ExpressCharge;
import com.ebox.pub.service.global.GlobalField;
import com.ebox.pub.ui.customview.Title;
import com.ebox.pub.ui.customview.Title.TitleData;
import com.ebox.pub.utils.MGViewUtil;
import com.ebox.pub.utils.RingUtil;
import com.ebox.pub.utils.Tip;

import java.util.ArrayList;

public class SelectBoxManualActivity  extends CommonActivity implements BoxManualSelectionFragment.OnFragmentSelectionListener{
	private Long bigFeeVal = 0L;
	private Long middleFeeVal = 0L;
	private Long littleFeeVal = 0L;
	private Long tinyFeeVal = 0L;
	private Tip tip;
    private boolean isAccount = true;	// true：快递柜使用，需要显示计费信息 false：政务使用，无需显示计费信息
    private String operatorId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ex_ebox_sp);
        MGViewUtil.scaleContentView(this, R.id.rootView);
        isAccount = getIntent().getExtras().getBoolean("isAccount", true);
        operatorId = getIntent().getStringExtra("operatorId");
		initView();
		initData();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(tip != null)
		{
			tip.closeTip();
		}
	}

	private void initView()
	{
		initTitle();
	}

	private Title title;
	private TitleData data;
	private void initTitle() {
		title=(Title) findViewById(R.id.title);
		data=title.new TitleData();
		data.backVisibility=1;
		data.tvContent=getResources().getString(R.string.ex_choose_yourself);
		data.tvVisibility=true;
		title.setData(data, this);
	}

	private void startChooseRackTypeFragment(){

		FragmentManager manager = getFragmentManager();

		FragmentTransaction transaction = manager.beginTransaction();

		transaction.add(R.id.rl_box_group, BoxManualSelectionFragment.newInstance()).commit();

	}


	@Override
	protected void onResume() {
		super.onResume();
		title.showTimer();
	}

	@Override
	protected void onPause() {
		super.onPause();
		title.stopTimer();
	}

	private void initData()
	{
        if(isAccount)
        {
            ArrayList<ExpressCharge> charges = OperatorHelper.getExpressChargeByServer();
            for (ExpressCharge mExCharge : charges)
            {
                if(mExCharge.getBoxSize().equals(BoxSizeType.large))
                {
                    bigFeeVal = mExCharge.getFee();
                }
                else if(mExCharge.getBoxSize().equals(BoxSizeType.medium))
                {
                    middleFeeVal = mExCharge.getFee();
                }
                else if(mExCharge.getBoxSize().equals(BoxSizeType.small))
                {
                    littleFeeVal = mExCharge.getFee();
                }
				else if(mExCharge.getBoxSize().equals(BoxSizeType.tiny))
				{
					tinyFeeVal = mExCharge.getFee();
				}
            }
        }

		startChooseRackTypeFragment();
		RingUtil.playRingtone(RingUtil.st035);
	}


	private void balanceNotEnough()
	{
		tip = new Tip(SelectBoxManualActivity.this,
				getResources().getString(R.string.ex_balance_not_enough),
				null);
		tip.show(0);
	}


	public void onCheckClick(BoxInfoType boxType) {
		Long fee = 0L;
		int isAccountLocal = GlobalField.serverConfig.getIsAccount();

		if(boxType.getBoxSize().equals(BoxSizeType.large))
		{
            if(isAccount)
            {
                if (isAccountLocal == AcountType.is_acount)
                {

                    if(OperatorHelper.getOperatorBalance(operatorId) < bigFeeVal)
                    {
                        balanceNotEnough();
                        return;
                    }
                }
            }

			fee = bigFeeVal;
		}
		else if(boxType.getBoxSize().equals(BoxSizeType.medium))
		{
            if(isAccount)
            {
                if (isAccountLocal == AcountType.is_acount)
                {
                    if(OperatorHelper.getOperatorBalance(operatorId) < middleFeeVal)
                    {
                        balanceNotEnough();
                        return;
                    }
                }
            }

			fee = middleFeeVal;
		}
		else if(boxType.getBoxSize().equals(BoxSizeType.tiny))
		{
			if(isAccount)
			{
				if (isAccountLocal == AcountType.is_acount)
				{
					if(OperatorHelper.getOperatorBalance(operatorId) < tinyFeeVal)
					{
						balanceNotEnough();
						return;
					}
				}
			}

			fee = tinyFeeVal;
		}
		else
		{
            if(isAccount)
            {
                if (isAccountLocal == AcountType.is_acount)
                {
                    if(OperatorHelper.getOperatorBalance(operatorId) < littleFeeVal)
                    {
                        balanceNotEnough();
                        return;
                    }
                }
            }

			fee = littleFeeVal;
		}

		Intent data = new Intent();
		data.putExtra("box", boxType);
		data.putExtra("fee", fee);
		setResult(RESULT_OK, data);
		this.finish();
	}

	@Override
	public void onBoxItemSelection(BoxInfoType infoType) {
		if (infoType!=null)
		{
			onCheckClick(infoType);
		}

	}
}

