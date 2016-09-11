package com.moge.gege.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moge.gege.R;
import com.moge.gege.config.GlobalConfig;
import com.moge.gege.model.AttachmentModel;
import com.moge.gege.model.TopicPostModel;
import com.moge.gege.model.UserModel;
import com.moge.gege.model.enums.GenderType;
import com.moge.gege.util.TimeUtil;

public class TopicPostListAdapter extends BaseCachedListAdapter<TopicPostModel>
        implements OnItemClickListener, OnItemLongClickListener
{
    private TopicPostListener mListener;

    public TopicPostListAdapter(Context context)
    {
        super(context);
    }

    public void setListener(TopicPostListener listener)
    {
        mListener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2)
    {
        final ViewHolder holder;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_topicpost, null);

            holder = new ViewHolder();
            holder.avatarImage = (ImageView) convertView
                    .findViewById(R.id.avatarImage);
            holder.nicknameText = (TextView) convertView
                    .findViewById(R.id.nicknameText);
            holder.genderImage = (ImageView) convertView
                    .findViewById(R.id.genderImage);
            holder.levelText = (TextView) convertView
                    .findViewById(R.id.levelText);
            holder.timeText = (TextView) convertView
                    .findViewById(R.id.timeText);
            holder.floorText = (TextView) convertView
                    .findViewById(R.id.floorText);
            holder.contentText = (TextView) convertView
                    .findViewById(R.id.contentText);
            holder.refDescText = (TextView) convertView
                    .findViewById(R.id.refDescText);
            holder.refLayout = (LinearLayout) convertView
                    .findViewById(R.id.refLayout);
            holder.replyText = (TextView) convertView
                    .findViewById(R.id.replyText);
            holder.replyImage = (ImageView) convertView
                    .findViewById(R.id.replyImage);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        TopicPostModel model = list.get(position);
        UserModel userModel = model.getAuthor();

        this.setImage(holder.avatarImage, getImageUrl(userModel.getAvatar()) + GlobalConfig.IMAGE_STYLE90_90,
                R.drawable.icon_default_avatar);
        holder.avatarImage.setOnClickListener(new MyClickListener(model));
        holder.nicknameText.setText(userModel.getNickname());
        holder.timeText.setText(TimeUtil.timeToNow(model.getCrts()));
        holder.floorText.setText(model.getNumber()
                + mContext.getResources().getString(R.string.floor));

        AttachmentModel attachmentModel = model.getAttachments();
        if (attachmentModel != null && attachmentModel.getImages().size() > 0)
        {
            this.setImage(holder.replyImage, getImageUrl(attachmentModel
                    .getImages().get(0)) + GlobalConfig.IMAGE_STYLE300,
                    R.drawable.icon_default);
            holder.replyImage.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.replyImage.setVisibility(View.GONE);
        }
        holder.replyImage.setOnClickListener(new MyClickListener(model));

        holder.contentText.setText(model.getContent());
        if (model.getRef_number() == 0)
        {
            holder.refLayout.setVisibility(View.GONE);
        }
        else
        {
            holder.refLayout.setVisibility(View.VISIBLE);
            if (TextUtils.isEmpty(model.getRef_descript()))
            {
                holder.refDescText.setText(model.getRef_number()
                        + mContext.getResources().getString(R.string.floor)
                        + " : " + getString(R.string.image_text));
            }
            else
            {
                holder.refDescText.setText(model.getRef_number()
                        + mContext.getResources().getString(R.string.floor)
                        + " : " + model.getRef_descript());
            }
        }

        if (userModel.getGender() == GenderType.WOMAN)
        {
            holder.genderImage.setImageResource(R.drawable.icon_woman);
        }
        else
        {
            holder.genderImage.setImageResource(R.drawable.icon_man);
        }

        if (model.getMember() != null)
        {
            holder.levelText.setVisibility(View.VISIBLE);
            holder.levelText.setText(model.getMember().getIntegration_level()
                    + "");
        }
        else
        {
            holder.levelText.setVisibility(View.GONE);
        }

        // if (model.getAuthor_uid()
        // .equals(PersistentData.instance().getLoginId()))
        // {
        // holder.replyText.setText(getString(R.string.delete));
        // }
        // else
        {
            holder.replyText.setText(getString(R.string.reply));
        }
        holder.replyText.setOnClickListener(new MyClickListener(model));

        return convertView;
    }

    public class MyClickListener implements OnClickListener
    {
        TopicPostModel mModel;

        MyClickListener(TopicPostModel model)
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
                case R.id.avatarImage:
                    mListener.onTopicPostAvatarClick(mModel);
                    break;
                case R.id.replyText:
                    mListener.onTopicPostClick(mModel);
                    break;
                case R.id.replyImage:
                    mListener.onTopicPostImageClick(mModel);
                    break;
                default:
                    break;

            }
        }
    }

    class ViewHolder
    {
        ImageView avatarImage;
        TextView nicknameText;
        TextView timeText;
        ImageView genderImage;
        TextView levelText;
        TextView floorText;
        TextView contentText;
        TextView refDescText;
        LinearLayout refLayout;
        TextView replyText;
        ImageView replyImage;
    }

    public interface TopicPostListener
    {
        public void onTopicPostAvatarClick(TopicPostModel model);

        public void onTopicPostImageClick(TopicPostModel model);

        public void onTopicPostClick(TopicPostModel model);

        public void onTopicPostItemClick(TopicPostModel model);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id)
    {
        if (mListener != null)
        {
            // mListener.onTopicPostClick(list.get(position));
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view,
            int position, long id)
    {
        if (mListener != null)
        {
            mListener.onTopicPostItemClick(list.get(position));
        }
        return false;
    }

}
