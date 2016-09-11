package com.moge.gege.ui.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.moge.gege.R;
import com.moge.gege.config.GlobalConfig;
import com.moge.gege.model.IMMessageModel;
import com.moge.gege.model.enums.IMListType;
import com.moge.gege.model.enums.IMMessageStatusType;
import com.moge.gege.model.enums.MessageContentType;
import com.moge.gege.network.util.RequestManager;
import com.moge.gege.util.ImageLoaderUtil;
import com.moge.gege.util.TimeUtil;
import com.moge.gege.util.ViewUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class ChatListAdapter extends BaseAdapter
{
    private Context mContext;
    private List<IMMessageModel> mDataList;
    private ChatListListener mListener;
    private ImageSize mImageSize;
    private DisplayImageOptions mImageOptions;

    public ChatListAdapter(Context context)
    {
        mContext = context;
        if(ViewUtil.getWidth() <= 480)
        {
            mImageSize = new ImageSize(150, 1);
        }
        else
        {
            mImageSize = new ImageSize(300, 1);
        }

        mImageOptions = getDisplayImageOptions(R.drawable.icon_default);
    }

    public void setDataSource(List<IMMessageModel> list)
    {
        mDataList = list;
    }

    public void setListener(ChatListListener listener)
    {
        mListener = listener;
    }

    public int getCount()
    {
        return mDataList == null ? 0 : mDataList.size();
    }

    public Object getItem(int position)
    {
        return mDataList.get(position);
    }

    public long getItemId(int position)
    {
        return position;
    }

    public int getItemViewType(int position)
    {
        IMMessageModel model = mDataList.get(position);
        return model.getListType();
    }

    public int getViewTypeCount()
    {
        return 2;
    }

    private DisplayImageOptions getDisplayImageOptions(int defaultResId)
    {
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
        builder.cacheInMemory(true).cacheOnDisk(true);
        builder.bitmapConfig(Bitmap.Config.RGB_565);
        builder.showImageForEmptyUri(defaultResId);
        builder.showImageOnFail(defaultResId);
        builder.showImageOnLoading(defaultResId);
        builder.imageScaleType(ImageScaleType.EXACTLY);
        return builder.build();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2)
    {
        final ViewHolder holder;

        IMMessageModel model = mDataList.get(position);

        if (convertView == null)
        {
            if (model.getListType() == IMListType.IM_FROM)
            {
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.im_from, null);
            }
            else
            {
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.im_to, null);
            }

            holder = new ViewHolder();
            holder.avatarImage = (ImageView) convertView
                    .findViewById(R.id.avatarImage);
            holder.timeText = (TextView) convertView
                    .findViewById(R.id.timeText);
            holder.contentText = (TextView) convertView
                    .findViewById(R.id.contentText);
            holder.contentImage = (ImageView) convertView
                    .findViewById(R.id.contentImage);
            holder.statusLayout = (LinearLayout) convertView
                    .findViewById(R.id.statusLayout);
            holder.imageProgressBar = (ProgressBar) convertView
                    .findViewById(R.id.imageProgressBar);
            holder.resendImage = (ImageView) convertView
                    .findViewById(R.id.resendImage);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        RequestManager.loadImage(holder.avatarImage,
                RequestManager.getImageUrl(model.getAvatar()) + GlobalConfig.IMAGE_STYLE90_90,
                R.drawable.icon_default_avatar);
        holder.timeText.setText(TimeUtil.getDateTimeStr((long) (model.getMsg()
                .getCrts() * 1000)));
        if (model.getMsg().getMsg_type() == MessageContentType.MSG_TEXT)
        {
            holder.contentImage.setVisibility(View.GONE);
            holder.contentText.setVisibility(View.VISIBLE);
            holder.contentText.setText(model.getMsg().getContent());
        }
        else
        {
            holder.contentImage.setVisibility(View.VISIBLE);
            holder.contentText.setVisibility(View.GONE);

            if (model.getMsg().getContent().startsWith("file://"))
            {
                ImageLoaderUtil.instance().loadImage(
                        model.getMsg().getContent(), mImageSize, mImageOptions,
                        new SimpleImageLoadingListener()
                        {
                            public void onLoadingComplete(String imageUri,
                                    View view, Bitmap loadedImage)
                            {
                                holder.contentImage.setImageBitmap(loadedImage);
                            }
                        });
            }
            else
            {
                if(ViewUtil.getWidth() <= 480)
                {
                    RequestManager.loadImage(holder.contentImage,
                            RequestManager.getImageUrl(model.getMsg().getContent())
                                    + GlobalConfig.IMAGE_STYLE150,
                            R.drawable.icon_default);
                }
                else
                {
                    RequestManager.loadImage(holder.contentImage,
                            RequestManager.getImageUrl(model.getMsg().getContent())
                                    + GlobalConfig.IMAGE_STYLE300,
                            R.drawable.icon_default);
                }

            }

        }

        if (holder.statusLayout != null)
        {
            if (model.getStatus() == IMMessageStatusType.MSG_SEND_SUCCESS)
            {
                holder.statusLayout.setVisibility(View.GONE);
            }
            else if (model.getStatus() == IMMessageStatusType.MSG_SENDING)
            {
                holder.statusLayout.setVisibility(View.VISIBLE);
                holder.imageProgressBar.setVisibility(View.VISIBLE);
                holder.resendImage.setVisibility(View.GONE);
            }
            else
            {
                holder.statusLayout.setVisibility(View.VISIBLE);
                holder.imageProgressBar.setVisibility(View.GONE);
                holder.resendImage.setVisibility(View.VISIBLE);
                holder.resendImage
                        .setOnClickListener(new MyClickListener(model));
            }
        }

        holder.contentImage.setOnClickListener(new MyClickListener(model));
        holder.avatarImage.setOnClickListener(new MyClickListener(model));
        return convertView;
    }

    public class MyClickListener implements OnClickListener
    {
        IMMessageModel mModel;

        MyClickListener(IMMessageModel model)
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
                    mListener.onChatAvatarClick(mModel);
                    break;
                case R.id.contentImage:
                    mListener.onChatImageClick(mModel);
                    break;
                case R.id.resendImage:
                    mListener.onResendClick(mModel);
                    break;
                default:
                    break;
            }
        }
    }

    class ViewHolder
    {
        ImageView avatarImage;
        TextView timeText;
        TextView contentText;
        ImageView contentImage;
        LinearLayout statusLayout;
        ProgressBar imageProgressBar;
        ImageView resendImage;
    }

    public interface ChatListListener
    {
        public void onChatAvatarClick(IMMessageModel model);

        public void onChatImageClick(IMMessageModel model);

        public void onResendClick(IMMessageModel model);
    }

}
