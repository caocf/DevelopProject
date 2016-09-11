package com.ebox.ex.ui;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.ebox.R;
import com.ebox.ex.database.operator.OperatorInfo;
import com.ebox.ex.database.operator.OperatorOp;
import com.ebox.ex.network.model.base.type.BoxInfoType;
import com.ebox.ex.network.model.base.type.ExpressCharge;
import com.ebox.ex.network.model.enums.AcountType;
import com.ebox.ex.network.model.enums.BoxSizeType;
import com.ebox.ex.network.model.enums.BoxStateType;
import com.ebox.ex.network.model.enums.DoorStatusType;
import com.ebox.ex.network.model.enums.OperatorReserveStatus;
import com.ebox.ex.network.model.enums.RackType;
import com.ebox.ex.ui.fragment.BoxRackTypeFragment;
import com.ebox.ex.utils.BoxInfoHelper;
import com.ebox.ex.utils.OperatorHelper;
import com.ebox.pub.boxctl.BoxUtils;
import com.ebox.pub.service.global.GlobalField;
import com.ebox.pub.ui.customview.Title;
import com.ebox.pub.ui.customview.Title.TitleData;
import com.ebox.pub.utils.FunctionUtil;
import com.ebox.pub.utils.MGViewUtil;
import com.ebox.pub.utils.Tip;

import java.util.ArrayList;
import java.util.List;

public class SelectBoxActivity extends CommonActivity implements OnClickListener,BoxRackTypeFragment.OnFragmentRackListener {
	private Button big_box_btn;
	private Button middle_box_btn;
	private Button little_box_btn;
	private Button tiny_box_btn;
	private TextView big_fee;
	private TextView middle_fee;
	private TextView little_fee;
	private TextView tiny_fee;
	private Long bigFeeVal = 0L;
	private Long middleFeeVal = 0L;
	private Long littleFeeVal = 0L;
	private Long tinyFeeVal=0L;
	private BoxInfoType big_box = null;
	private BoxInfoType middle_box = null;
	private BoxInfoType little_box = null;
	private BoxInfoType tiny_box = null;
	private TextView big_free;
	private TextView middle_free;
	private TextView little_free;
	private TextView tiny_free;
	private int bigFreeCnt;
	private int middleFreeCnt;
	private int littleFreeCnt;
	private int tinyFreeCnt;
	private Button bt_ok;
	public final static int REQUEST_CODE_2 = 2;
	private Tip tip;
    private boolean isAccount = true;	// true：快递柜使用，需要显示计费信息 false：政务使用，无需显示计费信息
    private String operatorId =null;

	private ArrayList<BoxInfoType> mBoxs ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ex_activity_select_box);
        MGViewUtil.scaleContentView(this, R.id.rootView);
        isAccount = getIntent().getExtras().getBoolean("isAccount", true);
        operatorId = getIntent().getStringExtra("operatorId");
		readBoxState();
		initView();
		initData();
	}
	
	private void initView()
	{
		big_box_btn = (Button) findViewById(R.id.big_box_btn);
		middle_box_btn = (Button) findViewById(R.id.middle_box_btn);
		little_box_btn = (Button) findViewById(R.id.little_box_btn);
		tiny_box_btn= (Button) findViewById(R.id.tiny_box_btn);
		big_fee = (TextView) findViewById(R.id.big_fee);
		big_fee.setVisibility(View.GONE);
		middle_fee = (TextView) findViewById(R.id.middle_fee);
		middle_fee.setVisibility(View.GONE);
		little_fee = (TextView) findViewById(R.id.little_fee);
		little_fee.setVisibility(View.GONE);
		tiny_fee = (TextView) findViewById(R.id.tiny_fee);
		tiny_fee.setVisibility(View.GONE);

		big_free = (TextView) findViewById(R.id.big_free);
		middle_free = (TextView) findViewById(R.id.middle_free);
		little_free = (TextView) findViewById(R.id.little_free);
		tiny_free = (TextView) findViewById(R.id.tiny_free);
		bt_ok = (Button) findViewById(R.id.bt_ok);


		big_box_btn.setOnClickListener(this);
		middle_box_btn.setOnClickListener(this);
		tiny_box_btn.setOnClickListener(this);
		little_box_btn.setOnClickListener(this);
		bt_ok.setOnClickListener(this);

        if(isAccount)
        {
            ArrayList<ExpressCharge> charges = OperatorHelper.getExpressChargeByServer();
            for (ExpressCharge mExCharge : charges)
            {
                if(mExCharge.getBoxSize().equals(BoxSizeType.large))
                {
                    bigFeeVal = mExCharge.getFee();
                    big_fee.setText(FunctionUtil.getMoneyStr(bigFeeVal));
                    big_fee.setVisibility(View.VISIBLE);
                }
                else if(mExCharge.getBoxSize().equals(BoxSizeType.medium))
                {
                    middleFeeVal = mExCharge.getFee();
                    middle_fee.setText(FunctionUtil.getMoneyStr(middleFeeVal));
                    middle_fee.setVisibility(View.VISIBLE);
                }
                else if(mExCharge.getBoxSize().equals(BoxSizeType.small))
                {
                    littleFeeVal = mExCharge.getFee();
                    little_fee.setText(FunctionUtil.getMoneyStr(littleFeeVal));
                    little_fee.setVisibility(View.VISIBLE);
                }
				else if(mExCharge.getBoxSize().equals(BoxSizeType.tiny))
				{
					tinyFeeVal = mExCharge.getFee() == null ? 30L : mExCharge.getFee();
					tiny_fee.setText(FunctionUtil.getMoneyStr(tinyFeeVal));
					tiny_fee.setVisibility(View.VISIBLE);
				}
			}
        }
		initTitle();
	}
	
	private Title title;
	private TitleData data;
	private void initTitle() {
		title=(Title) findViewById(R.id.title);
		data=title.new TitleData();
		data.backVisibility=1;
		data.tvContent=getResources().getString(R.string.ex_select_box);
		data.tvVisibility=true;
		title.setData(data, this);
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
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(tip != null)
		{
			tip.closeTip();
		}
		//清理缓存数据
		BoxInfoHelper.clearBoxData();
	}
	
	private void initData()
	{
		int zeroCount=0;
		//存在2中以上的柜子显示选择类型
		int box_sx = BoxInfoHelper.getCountByRackType(RackType.box_sx);
		if (box_sx>0) {
			zeroCount++;
		}
		int box_bw = BoxInfoHelper.getCountByRackType(RackType.box_bw);
		if (box_bw>0) {
			zeroCount++;
		}
		int box_kd = BoxInfoHelper.getCountByRackType(RackType.box_kd);
		if (box_kd>0) {
			zeroCount++;
		}
		if (zeroCount > 1)
		{
			startChooseRackTypeFragment();
		} else {
			updateBoxCount(mBoxs);
		}
	}

	private void updateBoxCount(List<BoxInfoType> boxs) {
		BoxInfoType boxInfo;
		boolean foundLarge = false;
		boolean foundMiddle = false;
		boolean foundLittle = false;
		boolean foundTiny = false;
		int big_box_index = -1;
		int middle_box_index = -1;
		int little_box_index = -1;
		int tiny_box_index = -1;
		bigFreeCnt=0;
		middleFreeCnt=0;
		littleFreeCnt=0;
		tinyFreeCnt=0;

		for(int i = 0; i < boxs.size(); i ++)
		{
			boxInfo = boxs.get(i);
			if(boxInfo.getBoxStatus()== BoxStateType.empty
					&& BoxUtils.getBoxLocalState(boxInfo) != DoorStatusType.unknow)
			{
				if(boxInfo.getBoxSize().equals(BoxSizeType.large))
				{
					bigFreeCnt++;
					if(!foundLarge)
					{
						// 找到一个可以用的，先预选
						if(big_box == null)
						{
							big_box = boxInfo;
							big_box_index = i;
						}

						// 选择下一个箱门
						if(i > GlobalField.big_box_index)
						{
							big_box = boxInfo;
							big_box_index = i;
							foundLarge = true;
						}
					}
				}
				else if(boxInfo.getBoxSize().equals(BoxSizeType.medium))
				{
					middleFreeCnt++;
					if(!foundMiddle)
					{
						if(middle_box == null)
						{
							middle_box = boxInfo;
							middle_box_index=i;
						}

						// 选择下一个箱门
						if(i > GlobalField.middle_box_index)
						{
							middle_box = boxInfo;
							middle_box_index=i;
							foundMiddle = true;
						}
					}
				}
				else if(boxInfo.getBoxSize().equals(BoxSizeType.tiny))
				{
					tinyFreeCnt++;
					if(!foundTiny)
					{
						if(tiny_box == null)
						{
							tiny_box = boxInfo;
							tiny_box_index =i;
						}

						// 选择下一个箱门
						if(i > GlobalField.tiny_box_index)
						{
							tiny_box = boxInfo;
							tiny_box_index =i;
							foundTiny = true;
						}

					}
				}
				else if(boxInfo.getBoxSize().equals(BoxSizeType.small))
				{
					littleFreeCnt++;
					if(!foundLittle)
					{
						if(little_box == null)
						{
							little_box = boxInfo;
							little_box_index =i;
						}

						// 选择下一个箱门
						if(i > GlobalField.little_box_index)
						{
							little_box = boxInfo;
							little_box_index =i;
							foundLittle = true;
						}

					}
				}
			}
		}

		if(big_box == null)
		{
			big_fee.setTextColor(getResources().getColor(R.color.pub_grey));
			big_free.setTextColor(getResources().getColor(R.color.pub_grey));
			big_box_btn.setBackgroundResource(R.drawable.ex_big_box_full);
			big_box_btn.setClickable(false);
		}
		else
		{
			GlobalField.big_box_index = big_box_index;
		}

		if(middle_box == null)
		{
			middle_fee.setTextColor(getResources().getColor(R.color.pub_grey));
			middle_free.setTextColor(getResources().getColor(R.color.pub_grey));
			middle_box_btn.setBackgroundResource(R.drawable.ex_middle_box_full);
			middle_box_btn.setClickable(false);
		}
		else
		{
			GlobalField.middle_box_index = middle_box_index;
		}

		if(little_box == null)
		{
			little_fee.setTextColor(getResources().getColor(R.color.pub_grey));
			little_free.setTextColor(getResources().getColor(R.color.pub_grey));
			little_box_btn.setBackgroundResource(R.drawable.ex_little_box_full);
			little_box_btn.setClickable(false);
		}
		else
		{
			GlobalField.little_box_index = little_box_index;
		}

		if(tiny_box == null)
		{
			tiny_fee.setTextColor(getResources().getColor(R.color.pub_grey));
			tiny_free.setTextColor(getResources().getColor(R.color.pub_grey));
			tiny_box_btn.setBackgroundResource(R.drawable.ex_tiny_box_full);
			tiny_box_btn.setClickable(false);
		}
		else
		{
			GlobalField.tiny_box_index = tiny_box_index;
		}

		big_free.setText(getResources().getString(R.string.ex_box_free, bigFreeCnt));
		middle_free.setText(getResources().getString(R.string.ex_box_free, middleFreeCnt));
		little_free.setText(getResources().getString(R.string.ex_box_free, littleFreeCnt));
		tiny_free.setText(getResources().getString(R.string.ex_box_free, tinyFreeCnt));

	}

	private void readBoxState() {
		boolean isCanUserReserve=false;
		if (operatorId != null)
        {
            OperatorInfo mOper = OperatorOp.getOperatorById(operatorId);
            //从数据库中读取箱门状态
            if (mOper.getReserveStatus()== OperatorReserveStatus.reserveBox)
            {
				isCanUserReserve=true;
            }
        }

		mBoxs =  BoxInfoHelper.getAllBoxInfo(isCanUserReserve);

		//缓存读取的箱门状态
		BoxInfoHelper.setBoxData(mBoxs, isCanUserReserve);
	}

	private void balanceNotEnough()
	{
		tip = new Tip(SelectBoxActivity.this, 
				getResources().getString(R.string.ex_balance_not_enough),
				null);
		tip.show(0);
	}
	
	@Override
	public void onClick(View v) {
		int isAccountlocal = GlobalField.serverConfig.getIsAccount();
		switch(v.getId())
		{
		
		case R.id.big_box_btn:


            if(isAccount)
            {
                if (isAccountlocal==AcountType.is_acount)
                {
                    if(OperatorHelper.getOperatorBalance(operatorId) < bigFeeVal)
                    {
                        balanceNotEnough();
                        return;
                    }
                }

            }
			Intent data = new Intent();
			data.putExtra("box", big_box);
			data.putExtra("fee", bigFeeVal);
			setResult(RESULT_OK, data);
			this.finish();
			break;
		case R.id.middle_box_btn:
            if(isAccount)
            {
                if (isAccountlocal == AcountType.is_acount)
                {
                    if(OperatorHelper.getOperatorBalance(operatorId) < middleFeeVal)
                    {
                        balanceNotEnough();
                        return;
                    }
                }
            }

			Intent data2 = new Intent();
			data2.putExtra("box", middle_box);
			data2.putExtra("fee", middleFeeVal);
			setResult(RESULT_OK, data2);
			this.finish();
			break;
			
		case R.id.little_box_btn:
            if(isAccount)
            {
                if (isAccountlocal == AcountType.is_acount)
                {
                    if (OperatorHelper.getOperatorBalance(operatorId) < littleFeeVal)
                    {
                        balanceNotEnough();
                        return;
                    }
                }
            }

			Intent data3 = new Intent();
			data3.putExtra("box", little_box);
			data3.putExtra("fee", littleFeeVal);
			setResult(RESULT_OK, data3);
			this.finish();
			break;
			case R.id.tiny_box_btn:
				if(isAccount)
				{
					if (isAccountlocal == AcountType.is_acount)
					{
						if (OperatorHelper.getOperatorBalance(operatorId) < tinyFeeVal)
						{
							balanceNotEnough();
							return;
						}
					}
				}

				Intent data4 = new Intent();
				data4.putExtra("box", tiny_box);
				data4.putExtra("fee", tinyFeeVal);
				setResult(RESULT_OK, data4);
				this.finish();
				break;
		case R.id.bt_ok:
			Intent intent = new Intent(SelectBoxActivity.this, SelectBoxManualActivity.class);
            intent.putExtra("isAccount",isAccount);
            intent.putExtra("operatorId",operatorId);
	        startActivityForResult(intent, REQUEST_CODE_2);
			break;
		}
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE_2) {
			if (resultCode == Activity.RESULT_CANCELED) {
			} else {
				if (data != null) {
					BoxInfoType checkBox = (BoxInfoType)data.getSerializableExtra("box");
					Long fee = data.getLongExtra("fee", 0L);
					
					Intent data3 = new Intent();
					data3.putExtra("box", checkBox);
					data3.putExtra("fee", fee);
					setResult(RESULT_OK, data3);
					this.finish();
				}
			}
		}
	}

	private void startChooseRackTypeFragment(){

		FragmentManager manager = getFragmentManager();

		FragmentTransaction transaction = manager.beginTransaction();

		transaction.add(R.id.fl_choose_type,BoxRackTypeFragment.newInstance()).addToBackStack(null).commit();

	}

	@Override
	public void onRackChoosed(int rackType) {
		ArrayList<BoxInfoType> rack = BoxInfoHelper.getBoxDataByRackType(rackType);
		if (rack!=null)
		{
			updateBoxCount(rack);
		}
	}

	@Override
	public void onManualChooseClick() {
		Intent intent = new Intent(SelectBoxActivity.this, SelectBoxManualActivity.class);
		intent.putExtra("isAccount",isAccount);
		intent.putExtra("operatorId",operatorId);
		startActivityForResult(intent, REQUEST_CODE_2);
	}
}
