package com.ebox.ex.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ebox.AppApplication;
import com.ebox.R;
import com.ebox.ex.database.LocalOrder.OrderLocalInfo;
import com.ebox.ex.database.LocalOrder.OrderLocalInfoOp;
import com.ebox.ex.network.model.enums.OrderImageType;
import com.ebox.ex.network.model.enums.PickupType;
import com.ebox.ex.ui.bar.OpenBoxBar;
import com.ebox.ex.ui.base.BaseFragment;
import com.ebox.ex.ui.base.BaseOpenDoorFragment;
import com.ebox.pub.boxctl.BoxInfo;
import com.ebox.pub.boxctl.BoxUtils;
import com.ebox.pub.camera.CameraData;
import com.ebox.pub.service.global.Constants;
import com.ebox.pub.service.global.GlobalField;
import com.ebox.pub.utils.RingUtil;

/**
 * Created by Android on 2015/10/29.
 */
public class ResultPickupFragment extends BaseOpenDoorFragment {

    public static final String PICK_TYPE="pick_type";
    public static final String TAG="ResultPickupFragment";
    private boolean isFirstOpened;
    private CameraData cameraData;

    private OpenBoxBar open_box_bar;
    private TextView tv_number;
    private LinearLayout rl_header;

    private OrderLocalInfo order;
    private String box_code;
    private String pick_type;

    @Override
    protected String getBoxCode() {
        return box_code;
    }

    @Override
    public void OpenDoorSuccess() {

        if (pick_type.equals(PickupType.operator))//用户播放
        {
            // 播放音效
            BoxInfo info = BoxUtils.getBoxByCode(box_code);
            RingUtil.playPickUpDoorOpened(info.getBoardNum(), info.getBoxNum());
        }else
        {
            //快递员操作超期件
            if (order.getTime_out()==1)
            {
                RingUtil.playRingtone(RingUtil.withdraw_id);
            }
        }

        dealPickupItem();

        if (!isFirstOpened)
        {
            isFirstOpened = true;
            Log.i(GlobalField.tag, "pickup from local order BoxCode:" + order.getBox_code()
                    + " ItemId:" + order.getItem_id()
                    + " Password:" + order.getPassword()
                    + " OrderId:" + order.getOrder_id()
                    + " TimeOut:" + order.getTime_out()
                    + " UserType::" + pick_type);

            order.setUser_type(pick_type);

            OrderLocalInfoOp.updateLocalOrderState(order);
            //释放箱门
            //BoxDynSyncOp.boxRelease(box_code);
            // AutoReportManager.instance().reportPickupOrder();
        }

    }

    @Override
    public void CloseDoorSuccess() {

    }

    @Override
    public void OpenDoorFailed() {
        // 播放音效
        RingUtil.playRingtone(RingUtil.calladmin_id);
        Log.i(GlobalField.tag, "pickup from local failed ItemId:" + order.getItem_id());

        //快递员操作超期件
        if (pick_type.equals(PickupType.operator) && order.getTime_out() == 1) {
            //设置成未超期状态
            OrderLocalInfoOp.updateLocalOrderToNotTimeOut(order.getOrder_id());
        }

    }

    private void dealPickupItem() {
        // 拍照
        cameraData = new CameraData();
        cameraData.setOrderId(order.getOrder_id());
        cameraData.setItemId(order.getItem_id());
        cameraData.setImageType(OrderImageType.PICK_UP);
        AppApplication.getInstance().getCc().takePicture(cameraData, true);
    }


    public static BaseFragment newInstance(Bundle bundle) {
        BaseFragment fragment = new ResultPickupFragment();
        fragment.setArguments(bundle);

        if (Constants.DEBUG) {
            Log.i(TAG, String.format("newInstance %s", "ResultPickupFragment"));
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();

        order = (OrderLocalInfo) arguments.getSerializable("order");

        if (order == null)
        {
            throw new NullPointerException("order is null");
        }
        //箱门
        box_code = order.getBox_code();
        //取件类型
        pick_type = arguments.getString(PICK_TYPE);

        if (pick_type == null)
        {
            throw new NullPointerException("pick type is null");
        }
    }

//    //无需自动开门
//    @Override
//    public boolean isNeedOpen() {
//        return false;
//    }

    @Override
    protected int getViewId() {
        return R.layout.ex_fragment_result_pickup;
    }

    @Override
    protected void initView(View view) {
        rl_header = (LinearLayout) view.findViewById(R.id.rl_header);
        open_box_bar = (OpenBoxBar) view.findViewById(R.id.open_box_bar);

        view.findViewById(R.id.btn_reopen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reOpen();
            }
        });
        Button button= (Button) view.findViewById(R.id.btn_exit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit();
            }
        });

        tv_number = (TextView) view.findViewById(R.id.tv_number);
        String boxDesc = BoxUtils.getBoxDesc(box_code);
        tv_number.setText(boxDesc);

        if (pick_type.equals(PickupType.customer))
        {
            open_box_bar.setHintText("运单号", "送达时间", "快递员电话");
            open_box_bar.setImageIcon(R.drawable.ex_input_item_id, R.drawable.ex_icon_store_time, R.drawable.ex_icon_phone1);
            //设置取件显示数据
            open_box_bar.setShowData(order.getItem_id(), order.getDelivery_at(), order.getOperator_telephone());
            rl_header.setVisibility(View.INVISIBLE);
        } else
        {
            button.setText("回收完成");
            rl_header.setVisibility(View.VISIBLE);
            open_box_bar.setShowData(boxDesc, order.getItem_id(), order.getCustomer_telephone());
        }

    }

    private void reOpen()
    {
        super.openDoor();
    }
    private void exit() {
        Log.i(TAG, "pick up exit: " + pick_type);
        getActivity().finish();
    }


    @Override
    public void onResume()
    {
        super.onResume();
        AppApplication.getInstance().getCc().start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
