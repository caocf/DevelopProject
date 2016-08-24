package com.xhl.world.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.xhl.world.R;
import com.zhy.autolayout.AutoRelativeLayout;

/**
 * Created by Sum on 15/12/4.
 */
public class SwitchButton extends AutoRelativeLayout implements View.OnClickListener {
    protected Context mContext;
    private boolean mSwitchOn;
    private TextView switch_on, switch_off;

    private switchCheckChangeListener listener;

    public SwitchButton(Context context) {
        this(context, null);
    }

    public SwitchButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout(context);
    }

    private void initLayout(Context context) {
        this.mContext = context;
        LayoutInflater.from(context).inflate(R.layout.pub_switch_button, this, true);
        switch_on = (TextView) findViewById(R.id.switch_on);
        switch_off = (TextView) findViewById(R.id.switch_off);
        switch_off.setOnClickListener(this);
        switch_on.setOnClickListener(this);
    }


    public void setSwitchOn(boolean switchOn) {
        mSwitchOn = switchOn;
        toggle();
    }

    private void toggle() {
        if (mSwitchOn) {
            switch_on.setSelected(true);
            switch_off.setSelected(false);
        } else {
            switch_on.setSelected(false);
            switch_off.setSelected(true);
        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.switch_on) {
            if (!mSwitchOn) {
                setSwitchOn(true);
                if (listener != null) {
                    listener.switchOn();
                }
            }
        } else if (v.getId() == R.id.switch_off) {
            if (mSwitchOn) {
                setSwitchOn(false);
                if (listener != null) {
                    listener.switchOff();
                }
            }
        }
    }

    public void setOnText(String name) {
        switch_on.setText(name);
    }

    public void setOffText(String name) {
        switch_off.setText(name);
    }

    public void setListener(switchCheckChangeListener listener) {
        this.listener = listener;
    }

    public interface switchCheckChangeListener {
        void switchOn();

        void switchOff();
    }
}
