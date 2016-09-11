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
import com.moge.gege.config.GlobalConfig;
import com.moge.gege.model.MyBoardModel;

public class MyBoardListAdapter extends BaseCachedListAdapter<MyBoardModel>
        implements OnItemClickListener, AdapterView.OnItemLongClickListener
{
    private MyBoardListListener mListener;

    public MyBoardListAdapter(Context context)
    {
        super(context);
    }

    public void setListener(MyBoardListListener listener)
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
                    R.layout.item_myboard, null);

            holder = new ViewHolder();
            holder.avatarImage = (ImageView) convertView
                    .findViewById(R.id.avatarImage);
            holder.managerImage = (ImageView) convertView
                    .findViewById(R.id.managerImage);
            holder.vipText = (TextView) convertView.findViewById(R.id.vipText);
            holder.ownerText = (TextView) convertView
                    .findViewById(R.id.ownerText);
            holder.nameText = (TextView) convertView
                    .findViewById(R.id.nameText);
            holder.levelText = (TextView) convertView
                    .findViewById(R.id.levelText);
            holder.topicText = (TextView) convertView
                    .findViewById(R.id.topicCountText);
            holder.line = convertView.findViewById(R.id.line);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        MyBoardModel model = list.get(position);

        if (model.getIs_default() == 1)
        {
            this.setImage(holder.avatarImage, getImageUrl(model.getLogo()) + GlobalConfig.IMAGE_STYLE90_90,
                    R.drawable.icon_default_zone);
        }
        else
        {
            this.setImage(holder.avatarImage, getImageUrl(model.getLogo()) + GlobalConfig.IMAGE_STYLE90_90,
                    R.drawable.icon_default);
        }

        // to do list!!! not display manager
        // if (model.getIs_manager() == 1)
        // {
        // holder.managerImage.setImageResource(R.drawable.vip);
        // }
        // else
        // {
        // holder.managerImage.setImageResource(0);
        // }

        if (model.getIs_vip() == 1)
        {
            holder.vipText.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.vipText.setVisibility(View.GONE);
        }

        if (model.getIs_default() == 1)
        {
            holder.ownerText.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.ownerText.setVisibility(View.GONE);
        }

        holder.nameText.setText(model.getName());
        holder.levelText.setText("" + model.getIntegration());

        if (model.getNewTopicCount() > 0)
        {
            holder.topicText.setVisibility(View.VISIBLE);
            holder.topicText.setText(model.getNewTopicCount() + "");
        }
        else
        {
            holder.topicText.setVisibility(View.GONE);
        }

        if (position == getCount() - 1)
        {
            holder.line.setVisibility(View.GONE);
        }
        else
        {
            holder.line.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    class ViewHolder
    {
        ImageView avatarImage;
        ImageView managerImage;
        TextView vipText;
        TextView ownerText;
        TextView nameText;
        TextView levelText;
        TextView topicText;
        View line;
    }

    public interface MyBoardListListener
    {
        public void onMyBoardClick(MyBoardModel model);
        public void onMyBoardLongClick(MyBoardModel model);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id)
    {
        if (mListener != null)
        {
            mListener.onMyBoardClick(list.get(position - 1));
        }
    }

    @Override
    public  boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
    {
        if (mListener != null)
        {
            mListener.onMyBoardLongClick(list.get(position - 1));
            return true;
        }
        return false;
    }

}
