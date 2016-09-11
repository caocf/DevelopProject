package com.moge.gege.ui.customview;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.moge.gege.R;
import com.moge.gege.model.BaseOptionModel;
import com.moge.gege.ui.adapter.OptionAdapter;
import com.moge.gege.ui.adapter.OptionAdapter.OptionListener;

public class OptionPopupWindow implements OptionListener
{
    private Context mContext;
    private OnOptionListener mListener;

    private ListView mListView;
    private OptionAdapter mAdapter;

    private ListView mSecondListView;
    private OptionAdapter mSecondAdapter;

    private PopupWindow mPopup;

    /**
     * 
     * @param context
     * @param callback
     * @param id
     * @param view
     */
    public OptionPopupWindow(Context context, OnOptionListener listener)
    {
        mContext = context;
        mListener = listener;
        initPopupWindow();
    }

    private void initPopupWindow()
    {
        View layout = LayoutInflater.from(mContext).inflate(
                R.layout.pop_option, null);
        mPopup = new PopupWindow(layout, LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT, true);
        mPopup.setBackgroundDrawable(new BitmapDrawable());
        mPopup.setFocusable(true);
        mPopup.setOutsideTouchable(true);
        // mPopup.setAnimationStyle(R.style.popwin_anim_style);

        LinearLayout datetimePopLayout = (LinearLayout) layout
                .findViewById(R.id.publishCategoryPopLayout);
        datetimePopLayout.setClickable(true);
        datetimePopLayout.setOnClickListener(mClickListener);

        mListView = (ListView) layout.findViewById(R.id.optionList);
        mAdapter = new OptionAdapter(mContext);
        mAdapter.setListener(this);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(mAdapter);

        mSecondListView = (ListView) layout.findViewById(R.id.secondOptionList);
        mSecondAdapter = new OptionAdapter(mContext);
        mSecondAdapter.setListener(this);
        mSecondListView.setAdapter(mSecondAdapter);
        mSecondListView.setOnItemClickListener(mSecondAdapter);
    }

    public void showPopupWindow(View v, int x, int y,
            List<? extends BaseOptionModel> datasource)
    {
        if (mPopup != null)
        {
            mAdapter.clear();
            mAdapter.addAll(datasource);
            mAdapter.notifyDataSetChanged();

            mPopup.showAsDropDown(v, x, y);
        }
    }

    private OnClickListener mClickListener = new OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
                case R.id.publishCategoryPopLayout:
                    mPopup.dismiss();
                    break;
                default:
                    break;
            }
        }
    };

    public static abstract interface OnOptionListener
    {
        public abstract void onOptionItemClick(BaseOptionModel model);
    }

    @Override
    public void onOptionItemClick(BaseOptionModel model)
    {
        mPopup.dismiss();
        if (mListener != null)
        {
            mListener.onOptionItemClick(model);
        }
    }

}
