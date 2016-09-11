package com.ebox.ex.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.ebox.AppApplication;
import com.ebox.R;
import com.ebox.ex.network.model.base.type.BoxInfoType;
import com.ebox.ex.ui.BaseDeliveryActivity;
import com.ebox.ex.ui.bar.EboxKeyboard;
import com.ebox.ex.ui.bar.SelectBoxBar;
import com.ebox.ex.ui.base.BaseOpFragment;
import com.ebox.pub.service.global.Constants;
import com.ebox.pub.utils.EditTextUtil;
import com.ebox.pub.utils.FunctionUtil;
import com.ebox.pub.utils.LogUtil;
import com.ebox.pub.utils.RingUtil;
import com.ebox.pub.utils.Tip;

/**
 * Created by Android on 2015/10/22.
 */
public class OpDeliveryFragment extends BaseOpFragment implements SelectBoxBar.FastSelectBoxListener {

    public final static int REQUEST_CODE_MANUAL_SELECT = 1;
    public final static int REQUEST_CODE_OPEN_DOOR = 2;
    private EboxKeyboard mKeyboard;
    private EditText ed_item_id,ed_customer_again,ed_customer;
    private SelectBoxBar select_box_bar;
    private Tip tip;
    //选择的箱门数据
    private BoxInfoType mBoxInfoType;
    private int selectBoxType;

    public static BaseOpFragment newInstance() {
        BaseOpFragment fragment = new OpDeliveryFragment();
        if (Constants.DEBUG)
        {
            Log.i(TAG, String.format("newInstance %s","OpDeliveryFragment"));
        }
        return fragment;
    }

    @Override
    public int getViewId() {
        return R.layout.ex_fragment_op_deliver;
    }

    View.OnTouchListener touchListener=new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            if (v instanceof EditText) {
                mKeyboard.setEditText(((EditText)v));
                mKeyboard.setNumberAudio(true);
            }
            return false;
        }
    };

    @Override
    protected void initView(View view) {
        mKeyboard= (EboxKeyboard) view.findViewById(R.id.keyboard);
        select_box_bar = (SelectBoxBar) view.findViewById(R.id.select_box_bar);

        ed_item_id= (EditText) view.findViewById(R.id.ed_item_id);
        ed_item_id.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mKeyboard.setEditText(ed_item_id);
                mKeyboard.setNumberAudio(false);
                return false;
            }
        });

        ed_customer = (EditText) view.findViewById(R.id.ed_customer);
        ed_customer.setOnTouchListener(touchListener);
        ed_customer.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View arg0, boolean arg1) {
                if (ed_customer.hasFocus())
                {
                    ed_customer
                            .setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    ed_customer.setInputType(InputType.TYPE_CLASS_TEXT
                            | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
        ed_customer.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);


        ed_customer_again = (EditText) view.findViewById(R.id.ed_customer_again);
        ed_customer_again.setOnTouchListener(touchListener);
        // 电话号码最多11位
        EditTextUtil.limitCount(ed_customer, 11,null);
        EditTextUtil.limitCount(ed_customer_again, 11, null);
        ed_customer.requestFocus();
        //快速选择箱门
        select_box_bar.setListener(this);

      view.findViewById(R.id.fl_open).setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              openDoor();
          }
      });
    }

    private boolean checkPara() {
        if (ed_item_id.getText() == null
                || ed_item_id.getText().toString().length() == 0) {
            tip = new Tip(context, getResources().getString(
                    R.string.ex_input_barcode), null);
            tip.show(0);
            return false;
        }

        if (ed_customer.getText() == null
                || !FunctionUtil.validMobilePhone(ed_customer.getText().toString())) {
            tip = new Tip(context, getResources().getString(
                    R.string.pub_telephone_error), null);
            tip.show(0);
            return false;
        }

        if (ed_customer_again.getText() == null
                || !ed_customer_again.getText().toString()
                .equals(ed_customer_again.getText().toString())) {
            tip = new Tip(context, getResources().getString(
                    R.string.pub_input_not_same), null);
            tip.show(0);
            return false;
        }

        if (mBoxInfoType == null) {
            tip = new Tip(context, getResources().getString(
                    R.string.ex_select_box), null);
            tip.show(0);
            return false;
        }

        return true;
    }

    private void openDoor() {

        if (!checkPara()) {
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString("item_id", ed_item_id.getText().toString());
        bundle.putString("customer_phone", ed_customer.getText().toString());
        bundle.putString("operator_phone", operatorPhone());
        bundle.putString("box_code", mBoxInfoType.getBoxCode());
        bundle.putLong("box_fee", 0);
      //  bundle.putSerializable("box_info",mBoxInfoType);

        Intent intent = new Intent(context, BaseDeliveryActivity.class);
        intent.putExtras(bundle);
        intent.putExtra("tag", BaseDeliveryActivity.title_store_order);
        context.startActivityForResult(intent, REQUEST_CODE_OPEN_DOOR);
    }

    private void clearAll()
    {
        ed_item_id.getText().clear();
        ed_item_id.requestFocus();

        ed_customer_again.getText().clear();
        ed_customer.getText().clear();
    }

    private void clear(int action)
    {
        select_box_bar.refreshChoose(mBoxInfoType, action);
        mBoxInfoType = null;
        select_box_bar.setBox(null);
    }

    @Override
    public void onResume() {
        super.onResume();
        ed_item_id.requestFocus();
        mKeyboard.setEditText(ed_item_id);
        mKeyboard.setNumberAudio(false);
        // 播放音效
        RingUtil.playRingtone(RingUtil.scan_id);
    }

    @Override
    public void onDestroy() {
        mBoxInfoType=null;
        clearAll();
        if (tip != null) {
            tip.closeTip();
        }

        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode!=-1)
        {
            LogUtil.d("return error req:" + requestCode);

            if (requestCode==REQUEST_CODE_OPEN_DOOR) //开门失败点击 返回
            {
                clear(1);
            }
            return;
        }

        if (requestCode == REQUEST_CODE_MANUAL_SELECT)
        {
            Bundle extras = data.getExtras();
            if (extras != null)
            {
                //手动选择清理缓存快速点击位置
                select_box_bar.clearCachePos();
                //手动选择
                selectBoxType = 1;

                mBoxInfoType = (BoxInfoType) extras.getSerializable("box");
                //更新选中状态
                select_box_bar.refreshChoose(mBoxInfoType,3);
                updateBox();
                log();
            }
        }
        else if(requestCode == REQUEST_CODE_OPEN_DOOR)
        {
            Bundle extras = data.getExtras();
            if (extras != null)
            {
                int action = extras.getInt("action", 0);
                Log.d(TAG, "store operator action:" + action);
                if (action == 0)
                {
                    //手动清理缓存快速点击位置
                    if (selectBoxType==1)
                    {
                        select_box_bar.clearCachePos();
                    }
                    clearAll();

                    clear(0);
                }
                else if (action == 1)
                {
                   clear(1);
                }
                else if (action == 2)
                {
                    AppApplication.getInstance().home();
                }
            }
        }

    }

    @Override
    public void fasterSelect(BoxInfoType infoType) {
        if (infoType!=null)
        {
            mBoxInfoType = infoType;
            selectBoxType=0;
            updateBox();
            log();
        }
    }

    private void updateBox(){
        if(mBoxInfoType!=null){
            select_box_bar.setBox(mBoxInfoType.getBoxCode());
        }
    }

    private void log() {
        if (mBoxInfoType==null) {
            Log.d(TAG, "box info is null " );
            return;
        }
        Log.d(TAG, "select "+mBoxInfoType.getBoxCode() + " " + mBoxInfoType.getBoxStatus());
    }
}
