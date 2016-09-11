package com.moge.gege.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;

import com.moge.gege.R;
import com.moge.gege.config.GlobalConfig;

public class TopicImageAdapter extends BaseCachedListAdapter<String> implements
        OnItemClickListener
{
    private TopicImageListener mListener;

    private String mSuffixUrl = "";

    private int mImageWidth = 300;
    private int mImageHeight = 300;

    public TopicImageAdapter(Context context)
    {
        super(context);
    }

    public void setListener(TopicImageListener listener)
    {
        mListener = listener;
    }

    public void setImageDisplaySize(int width, int height)
    {
        mImageWidth = width;
        mImageHeight = height;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2)
    {
        final ViewHolder holder;
        // if (convertView == null)
        // {
        convertView = LayoutInflater.from(mContext).inflate(
                R.layout.item_topicimage, null);

        holder = new ViewHolder();
        holder.topicImage = (ImageView) convertView
                .findViewById(R.id.topicImage);
        convertView.setTag(holder);
        // }
        // else
        // {
        // holder = (ViewHolder) convertView.getTag();
        // }

        LayoutParams params = (LayoutParams) holder.topicImage
                .getLayoutParams();
        params.width = mImageWidth;
        params.height = mImageHeight;
        holder.topicImage.setLayoutParams(params);

        if (getCount() >= 2)
        {
            mSuffixUrl = GlobalConfig.IMAGE_STYLE300_300;
        }
        else
        {
            mSuffixUrl = GlobalConfig.IMAGE_STYLE480;
        }

        String url = getImageUrl(list.get(position)) + mSuffixUrl;
        this.setImage(holder.topicImage, url, R.drawable.icon_default);
        return convertView;
    }

    class ViewHolder
    {
        ImageView topicImage;
    }

    public interface TopicImageListener
    {
        public void onImageItemClick(int position, String url);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id)
    {
        if (mListener != null)
        {
            mListener.onImageItemClick(position, list.get(position));
        }
    }

}
