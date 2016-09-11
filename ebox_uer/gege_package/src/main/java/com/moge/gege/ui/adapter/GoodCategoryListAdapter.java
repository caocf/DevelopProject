package com.moge.gege.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.moge.gege.R;
import com.moge.gege.model.BaseOptionModel;
import com.moge.gege.network.util.RequestManager;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class GoodCategoryListAdapter extends BaseCachedListAdapter<BaseOptionModel>
{

    public GoodCategoryListAdapter(Context context)
    {
        super(context);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup arg2)
    {
        ViewHolder holder;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_good_category, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        BaseOptionModel model = list.get(position);
        RequestManager.loadImage(holder.categoryImage,
                RequestManager.getImageUrl(model.getLogo()),
                R.drawable.icon_default);
        holder.categoryName.setText(model.getName());

        return convertView;
    }

    class ViewHolder
    {
        @InjectView(R.id.categoryImage) ImageView categoryImage;
        @InjectView(R.id.categoryName) TextView categoryName;

        public ViewHolder(View view)
        {
            ButterKnife.inject(this, view);
        }
    }

}
