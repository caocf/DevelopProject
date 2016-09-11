package com.moge.gege.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.moge.gege.R;
import com.moge.gege.model.BaseOptionModel;

public class TradingCategoryAdapter extends
        BaseCachedListAdapter<BaseOptionModel> implements OnItemClickListener
{
    private TradingCategoryListener mListener;

    public TradingCategoryAdapter(Context context)
    {
        super(context);
    }

    public void setListener(TradingCategoryListener listener)
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
                    R.layout.item_trading_category, null);

            holder = new ViewHolder();
            holder.nameText = (TextView) convertView
                    .findViewById(R.id.nameText);
            holder.iconImage = (ImageView) convertView
                    .findViewById(R.id.iconImage);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        BaseOptionModel model = list.get(position);
        if (model.getResId() > 0)
        {
            holder.iconImage.setImageResource(model.getResId());
        }
        holder.nameText.setText(model.getName());

        return convertView;
    }

    class ViewHolder
    {
        ImageView iconImage;
        TextView nameText;
    }

    public interface TradingCategoryListener
    {
        public void onCategoryItemClick(BaseOptionModel model);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id)
    {
        if (mListener != null)
        {
            mListener.onCategoryItemClick(list.get(position));
        }
    }

}
