package com.ebox.ex.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ebox.R;
import com.ebox.ex.network.model.base.type.SmsInfo;
import com.ebox.pub.service.global.GlobalField;
import com.ebox.pub.ui.adapter.BaseListAdapter;
import com.ebox.pub.ui.customview.Title;
import com.ebox.pub.ui.customview.Title.TitleData;
import com.ebox.pub.utils.FunctionUtil;
import com.ebox.pub.utils.MGViewUtil;
import com.ebox.pub.utils.Tip;

import java.util.ArrayList;

public class ServiceActivity extends CommonActivity {
    private final String unionpayPkg = "com.ebox.unionpay";
    private Tip tip;

    private GridView mGridView;
    private ServiceAdatpter mAdatpter;
    private ArrayList<SmsInfo> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ums_activity_service);
        MGViewUtil.scaleContentView(this, R.id.rootView);
        initView1();
    }

    private void initView1() {
        mGridView = (GridView) findViewById(R.id.gv_service);
        initData();
        initTitle();
    }

    private Title title;
    private TitleData data;

    private void initTitle() {
        title = (Title) findViewById(R.id.title);
        data = title.new TitleData();
        data.backVisibility = 1;
        data.tvContent = getResources().getString(R.string.pub_service);
        data.tvVisibility = true;
        title.setData(data, this);
    }

    private void initData() {
        mData = new ArrayList<SmsInfo>();

        createAll();
        mAdatpter = new ServiceAdatpter(this);
        mAdatpter.addAll(mData);
        mGridView.setAdapter(mAdatpter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                SmsInfo info = mData.get(position);
                starApp(info);
            }
        });
    }

    private void createAll() {
        if (GlobalField.config.getNjUms() == 0)
        {
            mData.add(getSms(R.string.ums_water_charge, R.drawable.ums_water_charge, 0, "com.ebox.unionpay.ui.WaterBillActivity"));
            mData.add(getSms(R.string.ums_elc_charge, R.drawable.ums_elc_charge, 0, "com.ebox.unionpay.ui.ElecActivity"));
            mData.add(getSms(R.string.ums_gass_charge, R.drawable.ums_gass_charge, 0, "com.ebox.unionpay.ui.FuelActivity"));
            //  mData.add(getSms(R.string.ums_JnWater, R.drawable.ums_water_charge, 0, "com.ebox.unionpay.ui.JnWaterActivity"));
            //  mData.add(getSms(R.string.ums_JnFuel, R.drawable.ums_gass_charge, 0, "com.ebox.unionpay.ui.JnFuelActivity"));

        }
        else if (GlobalField.config.getNjUms() == 1)
        {
            mData.add(getSms(R.string.ums_water_charge, R.drawable.ums_water_charge, 1, "com.ebox.unionpay.ui.WaterBillActivity"));
            mData.add(getSms(R.string.ums_elc_charge, R.drawable.ums_elc_charge, 1, "com.ebox.unionpay.ui.ElecActivity"));
            mData.add(getSms(R.string.ums_gass_charge, R.drawable.ums_gass_charge, 1, "com.ebox.unionpay.ui.FuelActivity"));
        }
        else
        {
              mData.add(getSms(R.string.ums_JnWater, R.drawable.ums_water_charge, 1, "com.ebox.unionpay.ui.JnWaterActivity"));
            mData.add(getSms(R.string.ums_elc_charge, R.drawable.ums_elc_charge, 1, "com.ebox.unionpay.ui.ElecActivity"));
              mData.add(getSms(R.string.ums_JnFuel, R.drawable.ums_gass_charge, 1, "com.ebox.unionpay.ui.JnFuelActivity"));

        }


        mData.add(getSms(R.string.ums_phone_charge, R.drawable.ums_phone_charge, 1, "com.ebox.unionpay.ui.mPhoneActivity"));

        //信用卡还款
        mData.add(getSms(R.string.ums_creditcard_charge, R.drawable.ums_creditcard_charge, 1, "com.ebox.unionpay.ui.CreditCardActivity"));

        //银行卡转账
        mData.add(getSms(R.string.ums_CardTransfer, R.drawable.ums_card_trancfer, 1, "com.ebox.unionpay.ui.CardTransferActivity"));
        //余额查询
        mData.add(getSms(R.string.ums_balance_query, R.drawable.ums_balance_query, 1, "com.ebox.unionpay.ui.BlanceQueryActivity"));
        //交通罚款
        mData.add(getSms(R.string.ums_TrafficFine, R.drawable.ums_traffic_fine, 0, "com.ebox.unionpay.ui.TrafficFineActivity"));

        //物业费缴纳
        mData.add(getSms(R.string.ums_TrafficFine, R.drawable.ums_wxf, 0, "com.ebox.unionpay.ui."));
        //社区贷款
        mData.add(getSms(R.string.ums_TrafficFine, R.drawable.ums_community_loan, 0, "com.ebox.unionpay.ui."));

    }

    private SmsInfo getSms(int id_name, int id_pic, int state, String path) {
        SmsInfo info = new SmsInfo();
        info.setName(getResources().getString(id_name));
        info.setPic_id(id_pic);
        info.setStartPath(path);
        info.setState(state);
        return info;
    }

    protected void starApp(SmsInfo info) {
        if (!checkUnionpayPkg()) {
            return;
        }
        if (info.getState() ==0)
        {
            tip = new Tip(ServiceActivity.this,
                    getResources().getString(R.string.ums_service_prompt),
                    null);
            tip.show(0);
            return;
        }
        FunctionUtil.startApp(this, unionpayPkg, info.getStartPath());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tip != null) {
            tip.closeTip();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        title.showTimer();
    }

    @Override
    protected void onStop() {
        super.onStop();
        title.stopTimer();
    }


    public boolean checkUnionpayPkg() {
        if (!FunctionUtil.checkApp(this, unionpayPkg)) {
            tip = new Tip(ServiceActivity.this,
                    getResources().getString(R.string.ums_service_prompt),
                    null);
            tip.show(0);
            return false;
        }

        return true;
    }

    class ServiceAdatpter extends BaseListAdapter<SmsInfo> {

        private Context context;

        public ServiceAdatpter(Context context) {
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(context, R.layout.ums_item_service, null);
                holder.iv_pic = (ImageView) convertView.findViewById(R.id.iv_sc_pic);
                holder.iv_pic_big = (ImageView)convertView.findViewById(R.id.iv_sc_pic_big);
                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_sc_name);
                Log.i("main","position:"+position);

                if (position > 4 && position < 10) {
                    holder.iv_pic_big.setVisibility(View.VISIBLE);
                    holder.iv_pic.setVisibility(View.GONE);
                }else {
                    holder.iv_pic_big.setVisibility(View.GONE);
                    holder.iv_pic.setVisibility(View.VISIBLE);
                }

                convertView.setTag(holder);
                MGViewUtil.scaleContentView((ViewGroup) convertView);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            SmsInfo info = list.get(position);
            holder.iv_pic.setImageResource(info.getPic_id());
            holder.iv_pic_big.setImageResource(info.getPic_id());
            holder.tv_name.setText(info.getName());

            return convertView;
        }
    }

    ;

    class ViewHolder {
        ImageView iv_pic;
        ImageView iv_pic_big;
        TextView tv_name;
    }
}
