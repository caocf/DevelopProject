package com.ebox.ex.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ebox.AppApplication;
import com.ebox.R;
import com.ebox.ex.database.boxDyn.BoxDynSyncOp;
import com.ebox.ex.database.deliver.Deliver;
import com.ebox.ex.database.deliver.DeliverOp;
import com.ebox.ex.database.deliver.DeliverTable;
import com.ebox.ex.database.operator.OperatorOp;
import com.ebox.ex.network.model.enums.OrderImageType;
import com.ebox.ex.ui.bar.OpenBoxBar;
import com.ebox.ex.ui.base.BaseFragment;
import com.ebox.ex.ui.base.BaseOpenDoorFragment;
import com.ebox.ex.ui.base.callback.OpenDoorResCallBack;
import com.ebox.ex.utils.OperatorHelper;
import com.ebox.pub.boxctl.BoxInfo;
import com.ebox.pub.boxctl.BoxUtils;
import com.ebox.pub.camera.CameraData;
import com.ebox.pub.service.global.Constants;
import com.ebox.pub.service.global.GlobalField;
import com.ebox.pub.service.task.report.AutoReportManager;
import com.ebox.pub.utils.LogUtil;
import com.ebox.pub.utils.RingUtil;
import com.ebox.pub.utils.TimeUtil;
import com.ebox.pub.utils.Tip;

/**
 * Created by Android on 2015/10/26.
 */
public class ResultDeliveryFragment extends BaseOpenDoorFragment implements View.OnClickListener {

    private String box_code;
    private String item_id;
    private String customer_phone;
    private String operator_phone;
    private Long box_fee;
    private CameraData cameraData;
    private deliveryListener listener;
    private OpenDoorResCallBack openDoorResCallBack;
    private OpenBoxBar open_box_bar;
    private Button btn_cancle, btn_confirm;
    private TextView tv_number;
    private boolean isOpened =false;
    private boolean isCancel = false;
    private boolean isSaved = false;
    private Tip tip;


    public static BaseFragment newInstance(Long box_fee, String box_code, String item_id, String customer_phone, String operator_phone) {
        BaseFragment fragment = new ResultDeliveryFragment();

        Bundle bundle = new Bundle();
        bundle.putString("box_code", box_code);
        bundle.putString("item_id", item_id);
        bundle.putString("customer_phone", customer_phone);
        bundle.putString("operator_phone", operator_phone);
        bundle.putLong("box_fee", box_fee);
        fragment.setArguments(bundle);

        if (Constants.DEBUG) {
            Log.i(TAG, String.format("newInstance %s", "ResultDeliveryFragment"));
        }
        return fragment;
    }

    public static BaseFragment newInstance(Bundle bundle) {
        BaseFragment fragment = new ResultDeliveryFragment();
        fragment.setArguments(bundle);

        if (Constants.DEBUG) {
            Log.i(TAG, String.format("newInstance %s", "ResultDeliveryFragment"));
        }
        return fragment;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (deliveryListener) activity;
            openDoorResCallBack= (OpenDoorResCallBack) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement deliveryListener");
        }
    }

    @Override
    protected String getBoxCode() {
        return box_code;
    }

    @Override
    public void OpenDoorSuccess() {
        BoxInfo info = BoxUtils.getBoxByCode(box_code);
        RingUtil.playChooseDoorHaveOpened(info.getBoardNum(), info.getBoxNum());
        //标识箱门已经打开
        isOpened=true;
        context.runOnUiThread(new Runnable() {
            @Override
            public void run()
            {
                openDoorResCallBack.OpenSuccess();
            }
        });

    }

    @Override
    public void CloseDoorSuccess() {
        //取消订单
        if (isCancel)
        {
            // 播放音效
            RingUtil.playRingtone(RingUtil.cancel_id);

            listener.cancelDelivery();
            return;
        }
        RingUtil.playRingtone(RingUtil.confirm_id);
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                btn_cancle.setEnabled(true);
                btn_confirm.setEnabled(true);
            }
        });
    }

    @Override
    public void OpenDoorFailed() {

        RingUtil.playRingtone(RingUtil.choose_id);

        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                openDoorResCallBack.OpenFailed();
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        box_code = (String) bundle.get("box_code");
        item_id = (String) bundle.get("item_id");
        customer_phone = (String) bundle.get("customer_phone");
        operator_phone = (String) bundle.get("operator_phone");
        box_fee = (Long) bundle.get("box_fee");
    }

    @Override
    protected int getViewId() {
        return R.layout.ex_fragment_result_delivery;
    }

    @Override
    protected void initView(View view) {
        open_box_bar = (OpenBoxBar) view.findViewById(R.id.open_box_bar);

        btn_cancle = (Button) view.findViewById(R.id.btn_cancel);
        btn_cancle.setOnClickListener(this);
        btn_cancle.setEnabled(false);

        btn_confirm = (Button) view.findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(this);
        btn_confirm.setEnabled(false);

        tv_number = (TextView) view.findViewById(R.id.tv_number);

        String boxDesc = BoxUtils.getBoxDesc(box_code);
        tv_number.setText(boxDesc);
        //设置显示数据
        open_box_bar.setShowData(boxDesc, item_id, customer_phone);
    }

    private void cancel()
    {
        isCancel = true;
        Log.i(GlobalField.tag, "cancel order: " + box_code);
        super.openDoor();
    }

    private void confirm()
    {
        saveOrder();
        tip = new Tip(context, getResources().getString(
                R.string.ex_delivery_success), new Tip.onDismissListener() {
            @Override
            public void onDismiss() {
                //上报订单
                AutoReportManager.instance().reportConfirmDelivery();
                listener.confirmDelivery();
            }
        });
        tip.show(0);
    }

    @Override
    public void onResume() {
        super.onResume();

        // 开启摄像头
        AppApplication.getInstance().getCc().start();

    }


    /**
     * 本地存储订单
     */
    private void saveOrder() {

        try {
            isSaved=true;

            // 本地存储订单
            Deliver deliver = new Deliver();
            String order_id = TimeUtil.orderTime() + AppApplication.getInstance().getTerminal_code();
            deliver.setOrder_id(order_id);

            deliver.setBox_code(box_code);
            deliver.setItemId(item_id);
            if (operator_phone == null || operator_phone.equals("")) {
                operator_phone = OperatorHelper.mPhone;
            }
            deliver.setOperatorId(operator_phone);
            deliver.setTelephone(customer_phone);
            deliver.setState(DeliverTable.STATE_0);
            deliver.setFee(box_fee.intValue());
            deliver.setTime(System.currentTimeMillis() + "");
            //添加到数据
            DeliverOp.CreateDeliver(deliver);

            //更新本地的对应box的状态state为已占用
            BoxDynSyncOp.boxLock(box_code);

            Log.i(GlobalField.tag, "save order orderId:" + order_id + " boxCode:" + box_code);

            //update balance
            if (OperatorHelper.isAccount()) {
                Long balance = OperatorHelper.getOperatorBalance(operator_phone) - box_fee;
                OperatorOp.updateOperatorBalance(operator_phone, balance, 0);
            }

            // 拍照
            cameraData = new CameraData();
            cameraData.setOrderId(order_id);
            cameraData.setItemId(item_id);
            cameraData.setImageType(OrderImageType.STORE);
            AppApplication.getInstance().getCc().takePicture(cameraData, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        //打开箱门，未取消，未点击保存 销毁后自动保存
        if (isOpened && !isCancel && !isSaved)
        {
            LogUtil.w("auto save order item_id:"+item_id);
            saveOrder();
        }
        // 关闭摄像头
        AppApplication.getInstance().getCc().release();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_cancel)
        {
            cancel();
        } else if (v.getId() == R.id.btn_confirm)
        {
            confirm();
        }
    }


    public interface deliveryListener {
        void confirmDelivery();

        void cancelDelivery();

        void exit();
    }
}
