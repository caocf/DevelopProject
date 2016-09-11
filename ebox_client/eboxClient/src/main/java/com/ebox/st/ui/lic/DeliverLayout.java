package com.ebox.st.ui.lic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.ebox.AppApplication;
import com.ebox.R;
import com.ebox.ex.ui.bar.OperMainLayout;
import com.ebox.pub.model.IdcardModel;
import com.ebox.pub.service.global.GlobalField;
import com.ebox.pub.ui.keyboard.KeyboardUtil;
import com.ebox.pub.utils.DialogUtil;
import com.ebox.pub.utils.EditTextUtil;
import com.ebox.pub.utils.MGViewUtil;
import com.ebox.pub.utils.RingUtil;
import com.ebox.pub.utils.Tip;
import com.ebox.st.model.IdentiOrderModel;
import com.ebox.st.model.QueryWorkInfoReq;
import com.ebox.st.model.QueryWorkInfoRsp;
import com.ebox.st.network.http.ApiClient;
import com.ebox.st.network.http.ApiClient.ClientCallback;


public class DeliverLayout extends OperMainLayout implements OnClickListener{
	private Button bt_ok;
	private EditText et_barcode;
	private DialogUtil dialogUtil;
    private KeyboardView keyboardView;
	private Tip tip;
	private Context context;
	
	private void initViews(final Context context) {
		this.context = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.st_deliver_layout, this,
                true);
		bt_ok = (Button)findViewById(R.id.bt_ok);
		et_barcode = (EditText)findViewById(R.id.et_barcode);
		KeyboardUtil.hideInput((Activity) context, et_barcode);
		et_barcode.setOnTouchListener(new OnTouchListener() {  
            @Override  
            public boolean onTouch(View v, MotionEvent event) {  
                    new KeyboardUtil(DeliverLayout.this, context, 
                    		et_barcode).showKeyboard();
                MGViewUtil.scaleContentView(context,R.id.keyboard_view);
                    return false;
            }  
		});
		new KeyboardUtil(DeliverLayout.this, context, 
				et_barcode).showKeyboard();  
		et_barcode.requestFocus();
		bt_ok.setOnClickListener(this);
		// 单号最多30位
		EditTextUtil.limitCount(et_barcode, 30, null);
		
		// 遇到回车自动访问服务端
		et_barcode.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (KeyEvent.KEYCODE_ENTER == keyCode && event.getAction() == KeyEvent.ACTION_DOWN) {
					RingUtil.playRingtone(RingUtil.scan_success_id);
					CheckItem();
					return true;
				}
				return false;
			}
		});
		
		dialogUtil = new DialogUtil();
		dialogUtil.createProgressDialog((Activity)context);
	}
	
	public DeliverLayout(Context context, AttributeSet attribute) {
		super(context, attribute);
		initViews(context);
	}

	public DeliverLayout(Context context) {
		super(context);
		initViews(context);
	}

	public void saveParams() {
		dialogUtil.closeProgressDialog();
		if(tip != null)
		{
			tip.closeTip();
		}
	}
	
	public void resetMyself()
	{
		et_barcode.setText("");
		et_barcode.requestFocus();
	}

	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.bt_ok:
			CheckItem();
			break;
		}
	}
	
	private boolean checkPara()
	{
		if(et_barcode.getText() == null ||
				et_barcode.getText().toString().length() == 0)
		{
			tip = new Tip((Activity)context, 
					getResources().getString(R.string.ex_input_barcode),
				null);
			tip.show(0);
			return false;
		}
		
		return true;
	}
	
	private void CheckItem()
	{
		if(checkPara())
		{
			Log.i(GlobalField.tag, "ScanBarcode:"+et_barcode.getText().toString());
			dialogUtil.showProgressDialog();
			getIdentiOrder(et_barcode.getText().toString());
		}
	}
	
	private void getIdentiOrder(String barcode) {
        QueryWorkInfoReq req = new QueryWorkInfoReq();
        req.setTerminal_code(AppApplication.getInstance().getTerminal_code());
        req.setBarcode(barcode);
        ApiClient.queryWorkInfor(context,req,new ClientCallback() {
            @Override
            public void onSuccess(Object data) {
                dialogUtil.closeProgressDialog();
                QueryWorkInfoRsp rsp = (QueryWorkInfoRsp)data;
                if (rsp.getSuccess())
                {
                    showData(rsp);
                }
                else
                {
                    showPrompt(R.string.st_contact_admin);
                }
            }

            @Override
            public void onFailed(Object data) {
                dialogUtil.closeProgressDialog();
            }
        });
	}
	
	private void showPrompt(int resId) {
		tip = new Tip(context, getResources().getString(
				resId), null);
		tip.show(0);
	}
	
	private void showData(QueryWorkInfoRsp data){
		if(data.getResult().size()>0){
            /*"result":[{"creat_at":"2015-04-08 11:59:14",
				 * "idcard":"330621198304104237",
				 * "order_id":1,"telephone":"13584588888"}]*/

            //mIdentiOrder.setBarcode(object.getString("telephone"));
            int state = data.getResult().get(0).getState();
            if(state>=20)
            {
                showPrompt(R.string.st_lic_already_putin);
                return;
            }
            IdentiOrderModel mIdentiOrder = new IdentiOrderModel();
            mIdentiOrder.setCreat_at(data.getResult().get(0).getCreat_at());
            mIdentiOrder.setTelephone(data.getResult().get(0).getTelephone());
            mIdentiOrder.setSt_identi_order_id(data.getResult().get(0).getOrder_id());
            IdcardModel idcard = new IdcardModel();
            idcard.setIdcard(data.getResult().get(0).getIdcard());
            mIdentiOrder.setIdcard(idcard);

            startDeliver(mIdentiOrder);
		}else {
			showPrompt(R.string.st_barcode_not_exist);
		}
	}
	
	public void startDeliver(IdentiOrderModel mIdentiOrder)
	{
        Intent intent = new Intent(context,DeliverFileActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putSerializable("IdentiOrderModel", mIdentiOrder);
        intent.putExtras(mBundle);
        intent.putExtra("barcode",et_barcode.getText().toString());
        context.startActivity(intent);
	}
}

