package com.moge.gege.ui.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moge.gege.AppApplication;
import com.moge.gege.R;
import com.moge.gege.config.GlobalConfig;
import com.moge.gege.data.dao.ShopcartDAO;
import com.moge.gege.model.AttachmentModel;
import com.moge.gege.model.TradingBaseModel;
import com.moge.gege.ui.widget.NumberEditView;
import com.moge.gege.util.FunctionUtils;
import com.moge.gege.util.ToastUtil;
import com.moge.gege.util.widget.CustomDialog;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ShoppingCartListAdapter extends
        BaseCachedListAdapter<TradingBaseModel> implements OnItemClickListener {
    public enum ShoppingCartMode {
        Normal, Eidt;
    }

    ;

    private ShoppingCartListListener mListener;
    private ShoppingCartMode mMode;
    private boolean mUpdateLocalData = true; // shoppingcart can update


    public ShoppingCartListAdapter(Context context) {
        super(context);
        mMode = ShoppingCartMode.Normal;
    }

    public ShoppingCartMode getMode() {
        return mMode;
    }

    public void setMode(ShoppingCartMode mode) {
        this.mMode = mode;

        if (mMode == ShoppingCartMode.Normal) {
            if (mUpdateLocalData) {
                ShopcartDAO.instance().updateGoods(list,
                        AppApplication.getLoginId());
            }
        }
    }

    public void setListener(ShoppingCartListListener listener) {
        mListener = listener;
    }

    public void setUpdateLocalData(boolean isUpdate) {
        mUpdateLocalData = isUpdate;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        final TradingBaseModel model = list.get(position);

        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_shoppingcart, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        AttachmentModel attachModel = model.getAttachments();
        if (attachModel != null && attachModel.getImages().size() > 0) {
            this.setImage(holder.tradingImage, getImageUrl(attachModel
                            .getImages().get(0)) + GlobalConfig.IMAGE_STYLE90_90,
                    R.drawable.icon_default);
        } else {
            holder.tradingImage.setImageResource(R.drawable.icon_default);
        }

        holder.titleText.setText(model.getTitle());
        holder.moneyText
                .setText(getString(R.string.money, FunctionUtils
                        .getDouble(model.getDiscount_price() * 1.0 / 100)));
        holder.goodNumberEdit.setNumberValue(model.getBuyNum()).setMaxValue(model.getNum()).setMinValue(0);
        holder.goodNumberEdit.setOnNumberViewChangeListener(new NumberEditView.OnNumberViewChangeListener() {
            @Override
            public void onNumberChange(int value) {

                if (value == 0) {
                    CustomDialog dialog = new CustomDialog.Builder(mContext)
                            .setTitle(R.string.general_tips)
                            .setMessage(getString(R.string.delete_goods_tips, 1))
                            .setPositiveButton(R.string.general_confirm,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {

                                            list.remove(model);
                                            ShoppingCartListAdapter.this.notifyDataSetChanged();
                                            ShopcartDAO.instance().deleteGoods(model, AppApplication.getLoginId());
                                            ToastUtil.showToastShort(R.string.delete_success);
                                            if (mListener != null) {
                                                mListener.onTradingNumChange();
                                            }

                                            dialog.dismiss();
                                        }
                                    })
                            .setNegativeButton(R.string.general_cancel,
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            holder.goodNumberEdit.setNumberValue(1);
                                            dialog.dismiss();
                                        }
                                    }).create();
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setCancelable(false);
                    dialog.show();
                } else {
                    model.setBuyNum(value);
                    ShopcartDAO.instance().updateGoods(model, AppApplication.getLoginId());
                    if (mListener != null) {
                        mListener.onTradingNumChange();
                    }
                }
            }
        });

        return convertView;
    }


    class ViewHolder {
        @InjectView(R.id.tradingImage)
        ImageView tradingImage;
        @InjectView(R.id.titleText)
        TextView titleText;
        @InjectView(R.id.moneyText)
        TextView moneyText;
        @InjectView(R.id.goodNumberEdit)
        NumberEditView goodNumberEdit;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

    public static abstract interface ShoppingCartListListener {
        public abstract void onTradingNumChange();

        public abstract void onTradingDetailItemClick(TradingBaseModel model);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        if (mListener != null && mMode == ShoppingCartMode.Normal) {
            mListener.onTradingDetailItemClick(list.get(position));
        }
    }
}
