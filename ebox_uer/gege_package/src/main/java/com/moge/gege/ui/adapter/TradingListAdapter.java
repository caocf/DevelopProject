package com.moge.gege.ui.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moge.gege.R;
import com.moge.gege.config.GlobalConfig;
import com.moge.gege.model.AttachmentModel;
import com.moge.gege.model.IMMessageModel;
import com.moge.gege.model.TradingModel;
import com.moge.gege.util.FunctionUtil;
import com.moge.gege.util.FunctionUtils;
import com.moge.gege.util.ViewUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class TradingListAdapter extends BaseCachedListAdapter<TradingModel>
        implements OnItemClickListener
{
    private TradingListListener mListener;
    private int mWidth;

    public TradingListAdapter(Context context)
    {
        super(context);

        mWidth = (ViewUtil.getWidth() - FunctionUtil.dip2px(mContext, 20)) / 2;
    }

    public void setListener(TradingListListener listener)
    {
        mListener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2)
    {
        ViewHolder holder;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_trading, null);

            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        TradingModel model = list.get(position);
        String imagename = "";

        AttachmentModel attachModel = model.getAttachments();
        if (attachModel != null && attachModel.getImages().size() > 0)
        {
            imagename = attachModel.getImages().get(0);
            this.setImage(holder.tradingImage, getImageUrl(imagename)
                    + GlobalConfig.IMAGE_STYLE300, R.drawable.icon_default);
        }
        else
        {
            holder.tradingImage.setImageResource(R.drawable.icon_default);
        }

        LinearLayout.LayoutParams p = (LinearLayout.LayoutParams) holder.tradingImage
                .getLayoutParams();

        // to do list!!!
        p.height = mWidth;
        // if (TextUtils.isEmpty(imagename))
        // {
        // p.height = mWidth;
        // }
        // else
        // {
        // p.height = getImageDestHeight(imagename, mWidth);
        // }
//        holder.tradingImage.setLayoutParams(p);

        holder.titleText.setText(model.getTitle());
        holder.oldPriceText.setText(getString(R.string.money,
                FunctionUtils.getDouble(model.getPrice() * 1.0 / 100)));
        holder.oldPriceText.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        String newprice = getString(R.string.money,
                FunctionUtils.getDouble(model.getDiscount_price() * 1.0 / 100));
        Spannable span = new SpannableString(newprice);
        span.setSpan(new AbsoluteSizeSpan(FunctionUtil.dip2px(mContext, 11)),
                0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.newPriceText.setText(span);

        holder.sellNumText.setText(getString(R.string.sell_count,
                model.getSale_num()));
        holder.addShoppingCartImage.setOnClickListener(new MyClickListener(model));

        return convertView;
    }

    // FpOW5AfBglQ8Fudf3A1zh8UWr_EJ_800x533_147479.jpeg
    private int getImageDestHeight(String imagename, int dstWidth)
    {
        int defaultHeight = 140;

        if (TextUtils.isEmpty(imagename) || imagename.length() <= 29)
        {
            return defaultHeight;
        }

        String leftStr = imagename.substring(29, imagename.indexOf("_", 29));

        String[] pxArray = leftStr.split("x");
        if (pxArray != null && pxArray.length >= 2)
        {
            int imageWidth;
            int imageHeight;

            imageWidth = Integer.parseInt(pxArray[0]);
            imageHeight = Integer.parseInt(pxArray[1]);
            defaultHeight = (int) (dstWidth * 1.0 / imageWidth * imageHeight);
        }

        return defaultHeight;
    }

    class ViewHolder
    {
        @InjectView(R.id.tradingImage) ImageView tradingImage;
        @InjectView(R.id.titleText) TextView titleText;
        @InjectView(R.id.sellNumText) TextView sellNumText;
        @InjectView(R.id.oldPriceText) TextView oldPriceText;
        @InjectView(R.id.newPriceText) TextView newPriceText;
        @InjectView(R.id.addShoppingCartImage) ImageView addShoppingCartImage;

        public ViewHolder(View view)
        {
            ButterKnife.inject(this, view);
        }
    }


    public class MyClickListener implements View.OnClickListener
    {
        TradingModel mModel;

        MyClickListener(TradingModel model)
        {
            mModel = model;
        }

        @Override
        public void onClick(View v)
        {
            if (mListener == null)
            {
                return;
            }

            switch (v.getId())
            {
                case R.id.addShoppingCartImage:
                    mListener.onShoppingCartClick(mModel);
                    break;
                default:
                    break;
            }
        }
    }


    public static abstract interface TradingListListener
    {
        public abstract void onShoppingCartClick(TradingModel model);
        public abstract void onTradingItemClick(TradingModel model);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id)
    {
        if (mListener != null)
        {
            mListener.onTradingItemClick(list.get(position));
        }
    }

}
