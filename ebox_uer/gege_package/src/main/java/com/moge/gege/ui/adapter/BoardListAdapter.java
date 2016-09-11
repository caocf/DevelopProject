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
import com.moge.gege.model.BoardModel;
import com.moge.gege.util.FunctionUtils;

public class BoardListAdapter extends BaseCachedListAdapter<BoardModel>
        implements OnItemClickListener
{
    private BoardListListener mListener;
    private boolean mShowJoinBtn = false;

    public BoardListAdapter(Context context)
    {
        super(context);
    }

    public void setListener(BoardListListener listener)
    {
        mListener = listener;
    }

    public void setShowJoinBtn(boolean isShow)
    {
        mShowJoinBtn = isShow;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2)
    {
        ViewHolder holder;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_board, null);

            holder = new ViewHolder();
            holder.avatarImage = (ImageView) convertView
                    .findViewById(R.id.avatarImage);
            holder.nameText = (TextView) convertView
                    .findViewById(R.id.nameText);
            holder.descText = (TextView) convertView
                    .findViewById(R.id.descText);
            holder.memberCountText = (TextView) convertView
                    .findViewById(R.id.memberCountText);
            holder.topicCountText = (TextView) convertView
                    .findViewById(R.id.topicCountText);
            holder.distanceText = (TextView) convertView
                    .findViewById(R.id.distanceText);
            holder.joinBtn = (Button) convertView.findViewById(R.id.joinBtn);
            holder.line = (View) convertView.findViewById(R.id.line);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        BoardModel model = list.get(position);

        this.setImage(holder.avatarImage, getImageUrl(model.getLogo()) + GlobalConfig.IMAGE_STYLE90_90,
                R.drawable.icon_default);
        holder.nameText.setText(model.getName());
        if (TextUtils.isEmpty(model.getDescript()))
        {
            holder.descText.setVisibility(View.INVISIBLE);
        }
        else
        {
            holder.descText.setVisibility(View.VISIBLE);
            holder.descText.setText(model.getDescript());
        }

        holder.memberCountText.setText(String.valueOf(model.getMember_count()));
        holder.topicCountText.setText(String.valueOf(model.getTopic_count()));
        holder.distanceText.setText(getString(R.string.km,
                FunctionUtils.getDouble(model.getDistance())));

        if (model.isBoardMember())
        {
            holder.joinBtn.setText(getString(R.string.have_join_board));
            holder.joinBtn.setEnabled(false);
        }
        else
        {
            holder.joinBtn.setText(getString(R.string.join_board));
            holder.joinBtn.setEnabled(true);
        }

        if (mShowJoinBtn)
        {
            holder.joinBtn.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.joinBtn.setVisibility(View.GONE);
        }
        holder.joinBtn.setOnClickListener(new MyClickListener(model));

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
        BoardModel mBoardModel;

        MyClickListener(BoardModel model)
        {
            mBoardModel = model;
        }

        @Override
        public void onClick(View v)
        {
            if (mListener != null)
            {
                mListener.onJoinBoardClick(mBoardModel);
            }
        }
    }

    class ViewHolder
    {
        ImageView avatarImage;
        TextView nameText;
        TextView descText;
        TextView memberCountText;
        TextView topicCountText;
        TextView distanceText;
        Button joinBtn;
        View line;
    }

    public interface BoardListListener
    {
        public void onBoardItemClick(BoardModel model);

        public void onJoinBoardClick(BoardModel model);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id)
    {
        if (mListener != null)
        {
            mListener.onBoardItemClick(list.get(position - 1));
        }
    }

}
