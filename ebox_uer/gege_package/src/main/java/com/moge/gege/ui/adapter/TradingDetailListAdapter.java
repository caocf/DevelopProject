package com.moge.gege.ui.adapter;

import android.content.Context;
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
import com.moge.gege.util.FunctionUtils;

public class TradingDetailListAdapter extends
        BaseCachedListAdapter<TradingBaseModel> implements OnItemClickListener
{
    public enum ShoppingCartMode
    {
        Normal, Eidt;
    };

    private enum OperType
    {
        Add, Reduce;
    };

    private TradingDetailListListener mListener;
    private ShoppingCartMode mMode;
    private boolean mUpdateLocalData = true; // shoppingcart can update
    private boolean mShowCheckBox = true;

    public TradingDetailListAdapter(Context context)
    {
        super(context);
        mMode = ShoppingCartMode.Normal;
    }

    public ShoppingCartMode getMode()
    {
        return mMode;
    }

    public void setMode(ShoppingCartMode mode)
    {
        this.mMode = mode;

        if (mMode == ShoppingCartMode.Normal)
        {
            if (mUpdateLocalData)
            {
                ShopcartDAO.instance().updateGoods(list,
                        AppApplication.getLoginId());
            }
        }
    }

    public void setListener(TradingDetailListListener listener)
    {
        mListener = listener;
    }

    public void setUpdateLocalData(boolean isUpdate)
    {
        mUpdateLocalData = isUpdate;
    }

    public void setShowCheckBox(boolean isShow)
    {
        mShowCheckBox = isShow;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2)
    {
        TradingBaseModel model = list.get(position);

        ViewHolder holder;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_tradingdetail, null);

            holder = new ViewHolder();

            holder.tradingImage = (ImageView) convertView
                    .findViewById(R.id.tradingImage);
            holder.titleText = (TextView) convertView
                    .findViewById(R.id.titleText);
            holder.agreeCheckBox = (CheckBox) convertView
                    .findViewById(R.id.agreeCheckBox);
            holder.moneyText = (TextView) convertView
                    .findViewById(R.id.moneyText);
            holder.buyNumText = (TextView) convertView
                    .findViewById(R.id.buyNumText);
            convertView.setTag(holder);

            holder.editNumLayout = (LinearLayout) convertView
                    .findViewById(R.id.editNumLayout);
            holder.reduceNumBtn = (Button) convertView
                    .findViewById(R.id.reduceNumBtn);
            holder.plusNumBtn = (Button) convertView
                    .findViewById(R.id.plusNumBtn);
            holder.editNumText = (TextView) convertView
                    .findViewById(R.id.editNumText);

            holder.reduceNumBtn.setFocusable(false);
            holder.reduceNumBtn.setFocusableInTouchMode(false);
            holder.plusNumBtn.setFocusable(false);
            holder.plusNumBtn.setFocusableInTouchMode(false);

            if (mShowCheckBox)
            {
                holder.agreeCheckBox.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.agreeCheckBox.setVisibility(View.GONE);
            }

        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        AttachmentModel attachModel = model.getAttachments();
        if (attachModel != null && attachModel.getImages().size() > 0)
        {
            this.setImage(holder.tradingImage, getImageUrl(attachModel
                    .getImages().get(0)) + GlobalConfig.IMAGE_STYLE90_90,
                    R.drawable.icon_default);
        }
        else
        {
            holder.tradingImage.setImageResource(R.drawable.icon_default);
        }

        holder.titleText.setText(model.getTitle());
        holder.moneyText
                .setText(getString(R.string.money, FunctionUtils
                        .getDouble(model.getDiscount_price() * 1.0 / 100)));
        holder.agreeCheckBox.setOnCheckedChangeListener(null);
        holder.agreeCheckBox.setChecked(model.isSelected());
        holder.agreeCheckBox
                .setOnCheckedChangeListener(new MyOnCheckedChangeListener(
                        model, holder));
        holder.buyNumText.setText(getString(R.string.good_num,
                model.getBuyNum()));

        if (mMode == ShoppingCartMode.Normal)
        {
            holder.buyNumText.setVisibility(View.VISIBLE);
            holder.editNumLayout.setVisibility(View.GONE);
        }
        else
        {
            holder.buyNumText.setVisibility(View.GONE);
            holder.editNumLayout.setVisibility(View.VISIBLE);
            holder.editNumText.setText(model.getBuyNum() + "");

            holder.reduceNumBtn.setEnabled(model.getBuyNum() > 1);
            holder.plusNumBtn.setEnabled(model.getBuyNum() < model.getNum());

            holder.reduceNumBtn.setOnClickListener(new MyClickListener(model,
                    holder));
            holder.plusNumBtn.setOnClickListener(new MyClickListener(model,
                    holder));
        }

        return convertView;
    }

    public class MyClickListener implements OnClickListener
    {
        TradingBaseModel model;
        ViewHolder holder;

        MyClickListener(TradingBaseModel model, ViewHolder holder)
        {
            this.model = model;
            this.holder = holder;
        }

        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
                case R.id.reduceNumBtn:
                    changeBuyNum(OperType.Reduce, model, holder);
                    break;
                case R.id.plusNumBtn:
                    changeBuyNum(OperType.Add, model, holder);
                    break;
                default:
                    break;
            }
        }
    }

    private void changeBuyNum(OperType type, TradingBaseModel model,
            ViewHolder holder)
    {
        if (type == OperType.Add)
        {
            model.setBuyNum(model.getBuyNum() + 1);
        }
        else
        {
            model.setBuyNum(model.getBuyNum() - 1);
        }

        holder.editNumText.setText(model.getBuyNum() + "");
        holder.reduceNumBtn.setEnabled(model.getBuyNum() > 1);
        holder.plusNumBtn.setEnabled(model.getBuyNum() < model.getNum());

        if (mListener != null)
        {
            mListener.onTradingNumChange();
        }
    }

    public class MyOnCheckedChangeListener implements OnCheckedChangeListener
    {
        TradingBaseModel model;
        ViewHolder holder;

        MyOnCheckedChangeListener(TradingBaseModel model, ViewHolder holder)
        {
            this.model = model;
            this.holder = holder;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView,
                boolean isChecked)
        {
            // holder.agreeCheckBox.setBackgroundResource(R.drawable.common_radio);
            model.setSelected(isChecked);

            if (mUpdateLocalData)
            {
                ShopcartDAO.instance().selectGoods(model,
                        AppApplication.getLoginId());
            }

            if (mListener != null)
            {
                mListener.onTradingCheckedClick();
            }
        }
    }

    class ViewHolder
    {
        CheckBox agreeCheckBox;
        ImageView tradingImage;
        TextView titleText;
        TextView moneyText;
        TextView buyNumText;

        LinearLayout editNumLayout;
        Button reduceNumBtn;
        Button plusNumBtn;
        TextView editNumText;
    }

    public static abstract interface TradingDetailListListener
    {
        public abstract void onTradingCheckedClick();

        public abstract void onTradingNumChange();

        public abstract void onTradingDetailItemClick(TradingBaseModel model);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id)
    {
        if (mListener != null && mMode == ShoppingCartMode.Normal)
        {
            mListener.onTradingDetailItemClick(list.get(position));
        }
    }

}
