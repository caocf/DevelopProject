package com.moge.ebox.phone.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.moge.ebox.phone.R;
import com.moge.ebox.phone.bettle.base.EboxApplication;
import com.moge.ebox.phone.bettle.network.http.ApiClient;
import com.moge.ebox.phone.bettle.network.http.ApiClient.ClientCallback;
import com.moge.ebox.phone.bettle.tools.Logger;
import com.moge.ebox.phone.bettle.utils.ViewUtils;
import com.moge.ebox.phone.model.AcitveModel;
import com.moge.ebox.phone.model.PayResultModel;
import com.moge.ebox.phone.ui.adapter.ActiveAdapter;
import com.moge.ebox.phone.ui.customview.Head;
import com.moge.ebox.phone.ui.customview.Head.HeadData;
import com.moge.ebox.phone.utils.DeviceInfoUtil;
import com.moge.ebox.phone.utils.PackageUtil;
import com.moge.ebox.phone.utils.ToastUtil;
import com.moge.ebox.phone.utils.pay.PayHelper;
import com.moge.ebox.phone.utils.pay.PayHelper.PayHelperResultLinstener;
import com.moge.ebox.phone.utils.pay.PayResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FastRechargeChooseActivity extends BaseActivity {

    private Button mbtnRecharge;
    private Head mHead;
    private EditText ed_zfb;
    private int curChoose = 0;
    private static PayResult mAliResult;
    private ImageView iv_loading;
    private GridView mGridView;
    private ActiveAdapter mAdapter;
    private ArrayList<AcitveModel> mData;
    private Context mContext;
    private String money;


    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1: {
                    Logger.i("mAliResult:结果" + msg.obj);
                    mAliResult = new PayResult((String) msg.obj);
                    Logger.i("mAliResult:jieguo:  " + msg.obj);
                    int result = PayResult.PAY_SUCCESS;
                    String error_msg = "";

                    if (mAliResult.getResultStatus().equals(PayResult.SUCCESS)) {
                        result = PayResult.PAY_SUCCESS;
                        error_msg = "";
//                        ToastUtil.showToastLong("付款成功,请您注意短信提醒 !");
                        Toast.makeText(FastRechargeChooseActivity.this,"付款成功,请您注意短信提醒 !",Toast.LENGTH_LONG).show();
                    } else if (mAliResult.getResultStatus().equals(
                            PayResult.CANCEL)) {
                        result = PayResult.PAY_CANCEL;
                        error_msg = mAliResult.getErrorMsg();
                        Toast.makeText(FastRechargeChooseActivity.this,mAliResult.getErrorMsg(),Toast.LENGTH_SHORT).show();
                    } else if (mAliResult.getResultStatus().equals(
                            PayResult.DEALING)) {
                        result = PayResult.PAY_FAILED;
                        error_msg = mAliResult.getErrorMsg();
                        Toast.makeText(FastRechargeChooseActivity.this,mAliResult.getErrorMsg(),Toast.LENGTH_SHORT).show();
                    } else {
                        result = PayResult.PAY_FAILED;
                        error_msg = mAliResult.getErrorMsg();
                        Toast.makeText(FastRechargeChooseActivity.this,mAliResult.getErrorMsg(),Toast.LENGTH_SHORT).show();
                    }
//                    //获得pay_info
                    HashMap payInfoMap = new HashMap();
                    payInfoMap.put("resultStatus",
                            Integer.parseInt(mAliResult.getResultStatus()));
                    payInfoMap.put("memo", mAliResult.getMemo());
                    payInfoMap.put("result", mAliResult.getResult());

                    Logger.i("FastAli:"+result+"  "+error_msg+"   ");
                    //上报订单信息
                    doGetPayOrderOver(result, error_msg, payInfoMap);


                }
                break;
                default:
                    break;
            }
        }
    };

    private void doGetPayOrderOver(int result, String error_msg, HashMap pay_info) {
        HashMap params = new HashMap();
        params.put("pay_id", pay_id);
        params.put("pay_type", pay_type);
        params.put("result", result);    //支付结果
        params.put("error_msg", error_msg);    //错误信息
        params.put("pay_info", pay_info);
//        Logger.i();
        ApiClient.getPayOrderOver(EboxApplication.getInstance(), params, new ClientCallback() {
            @Override
            public void onSuccess(JSONArray data, int code) {
                Logger.i("FastRecharge支付上报完成"+data.toString());
            }

            @Override
            public void onFailed(byte[] data, int code) {
                Logger.i("FastRecharge支付上报失败");

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_active);
        mContext = FastRechargeChooseActivity.this;
        initView();
        //获得活动
        getAcitve();
    }

    private void initView() {
        mbtnRecharge = findviewById_(R.id.btn_recharge);
        mbtnRecharge.setOnClickListener(mClickListener);
        ed_zfb = findviewById_(R.id.ed_zfb_money);
        mGridView = findviewById_(R.id.GridView);
        iv_loading = findviewById_(R.id.loading);
        initHead();
    }

    private void initHead() {
        mHead = findviewById_(R.id.title);
        HeadData data = mHead.new HeadData();
        data.backVisibility = 1;
        data.tvVisibility = true;
        data.tvContent = getResources().getString(R.string.fast_recharege);
        mHead.setData(data, this);
    }

    private void startLoad() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.loading_rotate);
        iv_loading.startAnimation(animation);
    }

    private void stopLoad() {
        iv_loading.clearAnimation();
    }

    private void getAcitve() {
        startLoad();
        Map params = new HashMap();
//		String dot_id = EboxApplication.getInstance().getLoginUserInfo().dot_id;
//		params.put("dot_id", dot_id);
        /**
         * 活动
         */
        ApiClient.getActive(EboxApplication.getInstance(), params,
                new ClientCallback() {

                    @Override
                    public void onSuccess(JSONArray data, int code) {
                        stopLoad();
                        showData(data);
                    }

                    @Override
                    public void onFailed(byte[] data, int code) {
                        stopLoad();
                        mGridView.setVisibility(View.GONE);
                    }
                });
    }

    protected void showData(JSONArray data) {
        mAdapter = new ActiveAdapter(this);
        try {
            AcitveModel acitveModel = new AcitveModel();
            ArrayList<AcitveModel> list = acitveModel.parseList(data.getJSONObject(0).getJSONArray("activities").toString());
            mData = list;
            mAdapter.addAll(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mData == null || mData.size() == 0) {
            mGridView.setVisibility(View.GONE);
        } else {
            setListener();
            mGridView.setAdapter(mAdapter);
        }
    }

    private void setListener() {
        mGridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                AcitveModel acitveModel = mAdapter.getItem(position);
                money = acitveModel.getAct_value()/100 + "";
                Log.i("main", "快捷点击支付：" + money);
                //商品信息
                int num = 1;
                payOrder(money, num);//支付订单
            }
        });
    }

    private OnClickListener mClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.btn_recharge:
                    payMoney();
                    break;
            }
        }
    };

    //设置显示钱数
    public void setPayMoney(String money) {
        if (curChoose == 0) {
            ed_zfb.setText(money);
        }
    }

    public void payMoney() {

        String money = null;
        String phone = EboxApplication.getInstance().getLoginPhone();

        Logger.i("Fast:-"+phone);

        if (curChoose == 0) {
            money = checkMoney(ed_zfb);
        }

        if (TextUtils.isEmpty(money)) {
            ToastUtil.showToastShort("请输入充值金额");
            return;
        }
        Float valueOf = Float.valueOf(money);
        if (valueOf == 0) {
            ToastUtil.showToastShort("充值金额不能为0");
            return;
        }

        if (TextUtils.isEmpty(phone)) {
            ToastUtil.showToastShort(R.string.pay_order_failed);
            return;
        }

        Logger.i("Fast:+++"+money);
        payOrder(money, 1);
    }

    /**
     * 支付宝支付订单
     *
     * @param
     */
    private void payOrder(final String moneyStr, final int num) {
        Logger.i("FastRecharge:payOrder-"+moneyStr+"   "+num);
        String phone = EboxApplication.getInstance().getLoginPhone();
        Logger.i("FastRecharge:payOrder-"+phone);
        PayHelper helper = new PayHelper(this, new PayHelperResultLinstener() {

            @Override
            public void onPayResult(PayResultModel result) {
                //获取了订单详情
                //获得订单详情后需要通过order_id获得支付单信息
                //为了获得支付服务器的登录状态
//                int money = Integer.valueOf(moneyStr).intValue();
                float money=Float.valueOf(moneyStr).floatValue()*100;
                Logger.i("FastRecharge:----money"+money);
                getPayOrderInfo(money, num, result.order_id);
            }
        });
        //支付新订单
        Logger.i("FastRecharge:支付新订单"+moneyStr);
        helper.payForNewOrder(phone, moneyStr);
    }


    /**
     * 获得支付单信息
     */
    private String pay_id;
    private int pay_type;

    public void getPayOrderInfo(float total_fee, int total_num, int order_id) {
        HashMap params = new HashMap();
        params.put("total_fee", total_fee);
        params.put("total_num", total_num);
        params.put("service", PayHelper.delivery_courier_recharge_order_service);
        params.put("pay_type", PayHelper.pay_type_alipay);
//		JSONArray orderIdArray = new JSONArray();
//		orderIdArray.put(order_id);
        ArrayList orderIDArray = new ArrayList();
        orderIDArray.add(order_id);
        params.put("order_ids", orderIDArray);
        params.put("device_info", getDeviceInfoObject(mContext));
        Logger.i("FastRechargeChooseActivity:获得支付单信息" + params.get("total_fee"));
        ApiClient.getPayOrder(EboxApplication.getInstance(), params, new ClientCallback() {
            @Override
            public void onSuccess(JSONArray data, int code) {
                //支付单获得成功，便可以向pay sdk发送数据

                try {
                    JSONObject order_data = data.getJSONObject(0);
                    JSONObject order_info = order_data.getJSONObject("order_info");
                    pay_id = order_data.getString("pay_id");
                    pay_type = order_data.getInt("pay_type");
                    Logger.i("FastRecharge:" + order_info);
                    toPayOrder(FastRechargeChooseActivity.this, order_info.getString("params"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailed(byte[] data, int code) {

            }
        });
    }

    /**
     * 获得设备信息
     *
     * @param context
     * @return
     */
    public static Map getDeviceInfoObject(Context context) {
        Map deviceObject = new HashMap();
        deviceObject.put("udid", DeviceInfoUtil.getDeviceId(context));
        deviceObject.put("appver", PackageUtil.getVersionName(context));
        deviceObject.put("os", "android");
        deviceObject.put("phonemodel",
                DeviceInfoUtil.getDeviceName(context));
        deviceObject.put("network", DeviceInfoUtil.getNetType(context));
        deviceObject.put("app_name", "gege");
        deviceObject.put("osver", DeviceInfoUtil.getSysVersion(context));
        return deviceObject;
    }

    private String checkMoney(EditText editText) {
        String money =editText.getText().toString().trim();
//        if (money.contains(".")) {
//            int indexOf = money.indexOf(".");
//            String mo = money.substring(0, indexOf);
//            String res = Integer.valueOf(mo).toString();
//            editText.setText(res);
//            return res;
//        }

        return money;
    }

    public void toPayOrder(final Activity context, final String orderParams) {
        if (TextUtils.isEmpty(orderParams)) {
            ToastUtil.showToastShort("生成订单失败");
            return;
        }
        Logger.i("Ali:"+orderParams);
        new Thread() {
            public void run() {
                //向alipay传递
                PayTask alipay = new PayTask(context);
                String result = alipay.pay(orderParams);
                Message msg = mHandler.obtainMessage();
                msg.what = 1;
                msg.obj = result;
                Logger.i("Ali:"+msg);
                mHandler.sendMessage(msg);
            }
        }.start();
    }

    public void checkalipay() {
        Runnable checkRunnable = new Runnable() {
            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask payTask = new PayTask(FastRechargeChooseActivity.this);
                // 调用查询接口，获取查询结果
                boolean isExist = payTask.checkAccountIfExist();
                Message msg = new Message();
                msg.what = 2;
                msg.obj = isExist;
                mHandler.sendMessage(msg);
            }
        };
        Thread checkThread = new Thread(checkRunnable);
        checkThread.start();
        getSDKVersion();
    }

    public void getSDKVersion() {
        PayTask payTask = new PayTask(this);
        String version = payTask.getVersion();
        Toast.makeText(this, version, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        ViewUtils.closeInput(this, ed_zfb);
        return super.dispatchTouchEvent(ev);
    }

}
