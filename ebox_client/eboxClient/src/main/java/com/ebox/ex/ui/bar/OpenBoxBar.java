package com.ebox.ex.ui.bar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ebox.R;
import com.ebox.pub.utils.MGViewUtil;

/**
 * Created by Android on 2015/10/29.
 */
public class OpenBoxBar extends FrameLayout {

    private TextView tv_door, tv_item_id, tv_customer;
    private ImageView iv_door, iv_item_id, iv_customer;
    private TextView tv_door_num, tv_item_id_num, tv_customer_num;


    public OpenBoxBar(Context context) {
        super(context);
        initViews(context);
    }

    public OpenBoxBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public OpenBoxBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews(context);
    }

    private void initViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.ex_bar_open_box, this, true);

        tv_door = (TextView) findViewById(R.id.tv_door);
        tv_item_id = (TextView) findViewById(R.id.tv_item_id);
        tv_customer = (TextView) findViewById(R.id.tv_customer);

        tv_door_num = (TextView) findViewById(R.id.tv_door_num);
        tv_item_id_num = (TextView) findViewById(R.id.tv_item_id_num);
        tv_customer_num = (TextView) findViewById(R.id.tv_customer_num);

        iv_door = (ImageView) findViewById(R.id.iv_door);
        iv_item_id = (ImageView) findViewById(R.id.iv_item_id);
        iv_customer = (ImageView) findViewById(R.id.iv_customer);

        MGViewUtil.scaleContentView(this);
    }


    public void setShowData(String door, String item_id, String customer) {
        tv_door_num.setText(door);
        tv_item_id_num.setText(item_id);
        tv_customer_num.setText(customer);
    }

    public void setHintText(String text1, String text2, String text3) {
        tv_door.setText(text1);
        tv_item_id.setText(text2);
        tv_customer.setText(text3);
    }

    public void setImageIcon(int icon1, int icon2, int icon3) {
        iv_door.setImageResource(icon1);
        iv_item_id.setImageResource(icon2);
        iv_customer.setImageResource(icon3);
    }


}
