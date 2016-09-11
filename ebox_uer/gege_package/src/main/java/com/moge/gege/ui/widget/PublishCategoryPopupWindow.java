package com.moge.gege.ui.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;

import com.moge.gege.R;
import com.moge.gege.config.GlobalConfig;
import com.moge.gege.model.LocalServiceModel;
import com.moge.gege.ui.adapter.LocalServiceAdapter;
import com.moge.gege.ui.adapter.LocalServiceAdapter.LocalServiceListener;

public class PublishCategoryPopupWindow implements LocalServiceListener
{
    private Context mContext;
    private OnCategoryCallBack mCallBack;

    private GridView mCategoryInfoGridView;
    private LocalServiceAdapter mCategoryInfoAdapter;

    private PopupWindow mPopup;

    /**
     * 
     * @param context
     * @param callback
     * @param id
     * @param view
     */
    public PublishCategoryPopupWindow(Context context,
            OnCategoryCallBack callback)
    {
        mContext = context;
        mCallBack = callback;
        initPopupWindow();
        initData();
    }

    private void initPopupWindow()
    {
        View layout = LayoutInflater.from(mContext).inflate(
                R.layout.pop_publish_category, null);
        mPopup = new PopupWindow(layout, LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT, true);
        mPopup.setBackgroundDrawable(new BitmapDrawable());
        mPopup.setFocusable(true);
        mPopup.setOutsideTouchable(true);
        mPopup.setAnimationStyle(R.style.popwin_anim_style);

        LinearLayout datetimePopLayout = (LinearLayout) layout
                .findViewById(R.id.publishCategoryPopLayout);
        datetimePopLayout.setClickable(true);
        datetimePopLayout.setOnClickListener(mClickListener);

        mCategoryInfoGridView = (GridView) layout
                .findViewById(R.id.categoryGridView);
        mCategoryInfoAdapter = new LocalServiceAdapter(mContext);
        mCategoryInfoAdapter.setListener(this);
        mCategoryInfoGridView.setAdapter(mCategoryInfoAdapter);
        mCategoryInfoGridView.setOnItemClickListener(mCategoryInfoAdapter);
    }

    private void initData()
    {
        List<LocalServiceModel> categoryList = new ArrayList<LocalServiceModel>();

        LocalServiceModel general = new LocalServiceModel();
        general.setName(mContext.getResources().getString(R.string.topic));
        general.setResId(GlobalConfig.mLocalServiceResId[0]);
        categoryList.add(general);

        LocalServiceModel activity = new LocalServiceModel();
        activity.setName(mContext.getResources().getString(
                GlobalConfig.mLocalServiceName[1]));
        activity.setResId(GlobalConfig.mLocalServiceResId[1]);
        categoryList.add(activity);

        LocalServiceModel carpool = new LocalServiceModel();
        carpool.setName(mContext.getResources().getString(
                GlobalConfig.mLocalServiceName[10]));
        carpool.setResId(GlobalConfig.mLocalServiceResId[10]);
        categoryList.add(carpool);

        LocalServiceModel marrigae = new LocalServiceModel();
        marrigae.setName(mContext.getResources().getString(
                GlobalConfig.mLocalServiceName[11]));
        marrigae.setResId(GlobalConfig.mLocalServiceResId[11]);
        categoryList.add(marrigae);

        LocalServiceModel together = new LocalServiceModel();
        together.setName(mContext.getResources().getString(
                GlobalConfig.mLocalServiceName[9]));
        together.setResId(GlobalConfig.mLocalServiceResId[9]);
        categoryList.add(together);

        LocalServiceModel pet = new LocalServiceModel();
        pet.setName(mContext.getResources().getString(
                GlobalConfig.mLocalServiceName[8]));
        pet.setResId(GlobalConfig.mLocalServiceResId[8]);
        categoryList.add(pet);

        LocalServiceModel secondhand = new LocalServiceModel();
        secondhand.setName(mContext.getResources().getString(
                GlobalConfig.mLocalServiceName[6]));
        secondhand.setResId(GlobalConfig.mLocalServiceResId[6]);
        categoryList.add(secondhand);

        LocalServiceModel renthouse = new LocalServiceModel();
        renthouse.setName(mContext.getResources().getString(
                GlobalConfig.mLocalServiceName[7]));
        renthouse.setResId(GlobalConfig.mLocalServiceResId[7]);
        categoryList.add(renthouse);

        mCategoryInfoAdapter.clear();
        mCategoryInfoAdapter.addAll(categoryList);
        mCategoryInfoAdapter.notifyDataSetChanged();
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
                case R.id.publishCategoryPopLayout:
                    mPopup.dismiss();
                    break;
                default:
                    break;
            }
        }
    };

    public static abstract interface OnCategoryCallBack
    {
        public abstract void onCategoryItemClick(int index);
    }

    @Override
    public void onServiceClick(int position, LocalServiceModel model)
    {
        mPopup.dismiss();
        if (mCallBack != null)
        {
            mCallBack.onCategoryItemClick(position);
        }
    }

}
