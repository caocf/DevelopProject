package com.moge.gege.ui.widget;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.moge.gege.R;
import com.moge.gege.util.TimeUtil;
import com.moge.gege.util.widget.wheel.time.DateTimeCtrl;

public class DateTimePopupWindow
{
    private Context mContext;
    private OnDateTimeListener mCallBack;
    private DateTimeCtrl mDatetimeCtrl;
    private PopupWindow mPopup;

    /**
     * 
     * @param context
     * @param callback
     * @param id
     * @param view
     */
    public DateTimePopupWindow(Context context, OnDateTimeListener callback)
    {
        mContext = context;
        mCallBack = callback;
        initPopupWindow();
    }

    private void initPopupWindow()
    {
        View layout = LayoutInflater.from(mContext).inflate(
                R.layout.pop_datetime, null);
        mPopup = new PopupWindow(layout, LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT, true);
        mPopup.setBackgroundDrawable(new BitmapDrawable());
        mPopup.setFocusable(true);
        mPopup.setOutsideTouchable(true);
        mPopup.setAnimationStyle(R.style.popwin_anim_style);

        LinearLayout datetimePopLayout = (LinearLayout) layout
                .findViewById(R.id.datetimePopLayout);
        datetimePopLayout.setClickable(true);
        datetimePopLayout.setOnClickListener(mClickListener);

        TextView cancelText = (TextView) layout.findViewById(R.id.cancelText);
        cancelText.setClickable(true);
        cancelText.setOnClickListener(mClickListener);

        TextView commitText = (TextView) layout.findViewById(R.id.commitText);
        commitText.setClickable(true);
        commitText.setOnClickListener(mClickListener);

        mDatetimeCtrl = (DateTimeCtrl) layout.findViewById(R.id.datetimeCtrl);
    }

    public void showPopupWindow(View v)
    {
        if (mPopup != null)
        {
            mPopup.showAtLocation(v, Gravity.CENTER, 0, 0);
        }
    }

    private OnClickListener mClickListener = new OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
                case R.id.datetimePopLayout:
                case R.id.cancelText:
                    mPopup.dismiss();
                    break;
                case R.id.commitText:
                    if (mCallBack != null)
                    {
                        mCallBack.onDateTimeResult(mDatetimeCtrl.getDateTime(),
                                TimeUtil.getDateTimeStr(mDatetimeCtrl
                                        .getDateTime()));
                    }
                    mPopup.dismiss();
                    break;
                default:
                    break;
            }
        }
    };

    public static abstract interface OnDateTimeListener
    {
        public abstract void onDateTimeResult(long time, String timeStr);
    }

}
