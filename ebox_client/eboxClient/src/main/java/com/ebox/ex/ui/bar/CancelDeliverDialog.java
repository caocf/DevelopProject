package com.ebox.ex.ui.bar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.ebox.R;
import com.ebox.pub.ui.customview.Title;
import com.ebox.pub.utils.MGViewUtil;

public class CancelDeliverDialog{
	private AlertDialog mDialog;
    private TextView description;
    private Object value;
    private Button cancel;
    private Button ok;
    private TextView tv_pickphone_tip;
    private TextView tv_phone;
    private TextView tv_itemId;

    private TextView tv_box_barcode;
	
	private Context mContext;
    private Title title;
    private Title.TitleData data;

    public CancelDeliverDialog(Context context, String text, final onClickListener listener, Object value) {
    	this.value = value;
    	this.mContext=context;
    	mDialog = new AlertDialog.Builder(context).create();
        mDialog.show();
        mDialog.setCancelable(false);
        mDialog.setOnDismissListener(new OnDismissListener() {
            public void onDismiss(DialogInterface arg0) {
                title.stopTimer();
             }
            });

       View v= View.inflate(context,R.layout.ex_cancel_deliver,null);
        Window window = mDialog.getWindow();
		window.setContentView(v);

		window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        description = (TextView)window.findViewById(R.id.tv_door);
        description.setText(text);

		tv_phone=(TextView) window.findViewById(R.id.tv_pickPhone);
        tv_pickphone_tip =(TextView) window.findViewById(R.id.tv_pickphone_tip);
		tv_itemId=(TextView) window.findViewById(R.id.tv_itemId);

        tv_box_barcode = (TextView) window.findViewById(R.id.tv_box_barcode);


        cancel = (Button) window.findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		if(listener != null)
				{
					listener.onCancel(CancelDeliverDialog.this.value);
				}
        		mDialog.cancel();
        	}
        });
        ok = (Button) window.findViewById(R.id.bt_ok);
        ok.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		if(listener != null)
				{
					listener.onOk(CancelDeliverDialog.this.value);
				}
        		mDialog.cancel();
        	}
        });

        //标题
        title=(Title) window.findViewById(R.id.title);
        data=title.new TitleData();
        data.backVisibility=0;
        data.tvContent=context.getResources().getString(R.string.ex_deliver_choose);
        data.tvVisibility=true;
        title.setData(data,(Activity)context);

        title.showTimer();
        //缩放
        MGViewUtil.scaleContentView((ViewGroup)v);
    }

    public void setShowData(String phone, String itemId) {
		tv_phone.setText(phone);
		tv_itemId.setText(itemId);
	}

    public AlertDialog getDialog()
    {
    	return mDialog;
    }
    
    public void cantOp()
    {
    	cancel.setClickable(false);
    	ok.setClickable(false);
    	ok.setEnabled(false);
//    	ok.setBackgroundResource(R.drawable.deliver_done_dis);
//    	cancel.setBackgroundResource(R.drawable.deliver_no_dis);
    }

	public void notShowItem()
	{
		tv_box_barcode.setVisibility(View.GONE);
        tv_itemId.setVisibility(View.GONE);
        tv_pickphone_tip.setText("存件人:");
	}
    
    public void canOp()
    {
    	ok.setEnabled(true);
    	cancel.setClickable(true);
    	ok.setClickable(true);
//    	ok.setBackgroundResource(R.drawable.btn_delivery);
//    	cancel.setBackgroundResource(R.drawable.btn_delivery_cancle);
    }
    
    public void closeDialog()
    {
    	if(mDialog != null && mDialog.isShowing())
		{
			mDialog.dismiss();
		}
    }
    
   
    public interface onClickListener
	{
		void onCancel(Object value);
		void onOk(Object value);
	}
}
