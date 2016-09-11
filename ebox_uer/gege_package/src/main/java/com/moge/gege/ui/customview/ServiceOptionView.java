package com.moge.gege.ui.customview;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.moge.gege.R;
import com.moge.gege.model.BaseOptionModel;
import com.moge.gege.ui.customview.OptionPopupWindow.OnOptionListener;
import com.moge.gege.util.FunctionUtil;
import com.moge.gege.util.ViewUtil;

public class ServiceOptionView extends LinearLayout implements OnOptionListener
{
    private Context mContext;
    private LinearLayout mMainLayout;
    private ServiceOptionListener mListener;
    private List<? extends BaseOptionModel> mHeadOptionList;
    private List<List<? extends BaseOptionModel>> mContentOptionList;
    private OptionPopupWindow mOptionPopWin;
    private int mCurHeaderIndex = 0;
    DisplayMetrics mDm;

    public ServiceOptionView(Context context)
    {
        super(context);
        initViews(context);
    }

    public ServiceOptionView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initViews(context);
    }

    @SuppressLint("ResourceAsColor")
    private void initViews(Context context)
    {
        mContext = context;
        mMainLayout = new LinearLayout(context);
        mMainLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        mMainLayout.setOrientation(LinearLayout.HORIZONTAL);
        mMainLayout.setBackgroundColor(mContext.getResources().getColor(
                R.color.service_option_bg_color));
        mMainLayout.setGravity(Gravity.CENTER_VERTICAL);
        addView(mMainLayout);

        mOptionPopWin = new OptionPopupWindow(context, this);

        mDm = getResources().getDisplayMetrics();
    }

    // public void setTextSize(TextView text, int textSize)
    // {
    // Context c = getContext();
    // Resources r;
    //
    // if (c == null)
    // r = Resources.getSystem();
    // else
    // r = c.getResources();
    // int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
    // textSize, r.getDisplayMetrics());
    // text.setTextSize(size);
    // }

    public void setDataSource(List<? extends BaseOptionModel> headerlist,
            List<List<? extends BaseOptionModel>> contentList)
    {
        mHeadOptionList = headerlist;
        mContentOptionList = contentList;

        if (mHeadOptionList == null)
        {
            return;
        }

        mMainLayout.removeAllViews();
        for (int i = 0; i < mHeadOptionList.size(); i++)
        {
            Button btn = new Button(mContext);
            btn.setLayoutParams(new LayoutParams(ViewUtil.getWidth()
                    / mHeadOptionList.size(), LayoutParams.MATCH_PARENT));
            btn.setGravity(Gravity.CENTER);
            btn.setBackgroundDrawable(mContext.getResources().getDrawable(
                    android.R.color.transparent));
            btn.setId(i);
            btn.setText(mHeadOptionList.get(i).getName());
            // to do list!!!
            // btn.setTextSize((int) TypedValue.applyDimension(
            // TypedValue.COMPLEX_UNIT_SP, 14, mDm));
            btn.setTextSize(14);
            btn.setTextColor(mContext.getResources().getColor(
                    R.color.topic_desc_color));

            // Drawable iconDrawable = mContext.getResources().getDrawable(
            // mHeadOptionList.get(i).getResId());
            // iconDrawable.setBounds(0, 0, iconDrawable.getMinimumWidth(),
            // iconDrawable.getMinimumHeight());
            // btn.setCompoundDrawables(iconDrawable, null, null, null);

            if (mHeadOptionList.get(i).getResId() > 0)
            {
                SpannableString spanText = new SpannableString("1 "
                        + mHeadOptionList.get(i).getName());
                Drawable d = mContext.getResources().getDrawable(
                        mHeadOptionList.get(i).getResId());
                int a = (int) btn.getTextSize();
                d.setBounds(0, 0, a, a);// 設定圖片為文字的大小
                // d.setBounds(0,0,d.getIntrinsicWidth(),d.getIntrinsicHeight());//原始大小
                ImageSpan imageSpan = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
                spanText.setSpan(imageSpan, 0, 1,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                btn.setText(spanText);
            }

            btn.setOnClickListener(mClickListener);
            mMainLayout.addView(btn);

            if (i < mHeadOptionList.size() - 1)
            {
                View line = new View(mContext);
                line.setLayoutParams(new LayoutParams(1, FunctionUtil.dip2px(
                        mContext, 30)));
                line.setBackgroundColor(mContext.getResources().getColor(
                        R.color.option_line_color));
                mMainLayout.addView(line);
            }
        }
    }

    private OnClickListener mClickListener = new OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            mCurHeaderIndex = v.getId();
            mOptionPopWin.showPopupWindow(v, 5, 0,
                    mContentOptionList.get(v.getId()));
        }
    };

    public void setListner(ServiceOptionListener listener)
    {
        this.mListener = listener;
    }

    public interface ServiceOptionListener
    {
        void onItemSelect(int headerIndex, BaseOptionModel model);
    }

    @Override
    public void onOptionItemClick(BaseOptionModel model)
    {
        if (mListener != null)
        {
            mListener.onItemSelect(mCurHeaderIndex, model);
        }
    }

}
