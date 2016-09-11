package com.moge.gege.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.moge.gege.R;
import com.moge.gege.config.GlobalConfig;
import com.moge.gege.model.NewBoardModel;

public class LatestBoardListAdapter extends
        BaseCachedListAdapter<NewBoardModel> implements OnItemClickListener
{
    private LatestBoardListListener mListener;

    public LatestBoardListAdapter(Context context)
    {
        super(context);
    }

    public void setListener(LatestBoardListListener listener)
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
                    R.layout.item_newboard, null);

            holder = new ViewHolder();
            holder.avatarImage = (ImageView) convertView
                    .findViewById(R.id.avatarImage);
            holder.nameText = (TextView) convertView
                    .findViewById(R.id.nameText);
            holder.descText = (TextView) convertView
                    .findViewById(R.id.descText);
            holder.supportCountText = (TextView) convertView
                    .findViewById(R.id.supportCountText);
            holder.distanceText = (TextView) convertView
                    .findViewById(R.id.distanceText);
            holder.supportBtn = (Button) convertView
                    .findViewById(R.id.supportBtn);
            holder.line = (View) convertView.findViewById(R.id.line);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        NewBoardModel model = list.get(position);

        this.setImage(holder.avatarImage, getImageUrl(model.getLogo()) + GlobalConfig.IMAGE_STYLE90_90,
                R.drawable.icon_default);
        holder.nameText.setText(model.getName());
        holder.descText.setText(model.getDescript());
        holder.supportCountText.setText(getString(R.string.support_count,
                model.getUp_count()));
        holder.distanceText.setText("");

        holder.supportBtn
                .setOnClickListener(new MyClickListener(model.get_id()));

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

    public class MyClickListener implements OnClickListener
    {
        String mBoardId;

        MyClickListener(String boardId)
        {
            mBoardId = boardId;
        }

        @Override
        public void onClick(View v)
        {
            if (mListener != null)
            {
                mListener.onSupportBoardClick(mBoardId);
            }
        }
    }

    class ViewHolder
    {
        ImageView avatarImage;
        TextView nameText;
        TextView descText;
        TextView supportCountText;
        TextView distanceText;
        Button supportBtn;
        View line;
    }

    public interface LatestBoardListListener
    {
        public void onLatestBoardItemClick(NewBoardModel model);

        public void onSupportBoardClick(String boardId);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id)
    {
        if (mListener != null)
        {
            mListener.onLatestBoardItemClick(list.get(position - 1));
        }
    }

}
