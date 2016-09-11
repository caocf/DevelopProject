package com.moge.gege.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.moge.gege.R;
import com.moge.gege.model.MyBoardModel;
import com.moge.gege.util.widget.horizontalListview.widget.AdapterView.OnItemClickListener;

public class UserBoardListAdapter extends BaseCachedListAdapter<MyBoardModel>
        implements OnItemClickListener
{
    private UserBoardListListener mListener;

    public UserBoardListAdapter(Context context)
    {
        super(context);
    }

    public void setListener(UserBoardListListener listener)
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
                    R.layout.item_userboard, null);

            holder = new ViewHolder();
            holder.logoImage = (ImageView) convertView
                    .findViewById(R.id.logoImage);
            holder.nameText = (TextView) convertView
                    .findViewById(R.id.nameText);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        MyBoardModel model = list.get(position);
        this.setImage(holder.logoImage, getImageUrl(model.getLogo()),
                R.drawable.icon_default);
        holder.nameText.setText(model.getName());
        return convertView;
    }

    class ViewHolder
    {
        ImageView logoImage;
        TextView nameText;
    }

    public interface UserBoardListListener
    {
        public void onBoardItemClick(MyBoardModel model);
    }

    @Override
    public void onItemClick(
            com.moge.gege.util.widget.horizontalListview.widget.AdapterView<?> parent,
            View view, int position, long id)
    {
        if (mListener != null)
        {
            mListener.onBoardItemClick(list.get(position));
        }
    }

}
