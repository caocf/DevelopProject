package com.moge.gege.ui.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moge.gege.R;
import com.moge.gege.config.GlobalConfig;
import com.moge.gege.model.FeedModel;
import com.moge.gege.network.util.RequestManager;
import com.moge.gege.util.FunctionUtil;

public class UserFeedListAdapter extends BaseCachedListAdapter<FeedModel>
        implements OnItemClickListener
{
    private UserFeedListener mListener;
    private HashSet mDateHash = new HashSet();

    public UserFeedListAdapter(Context context)
    {
        super(context);
    }

    public void setListener(UserFeedListener listener)
    {
        mListener = listener;
    }

    @Override
    public void notifyDataSetChanged()
    {
        mDateHash.clear();

        super.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2)
    {
        final ViewHolder holder;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_userfeed, null);

            holder = new ViewHolder();

            holder.dateText = (TextView) convertView
                    .findViewById(R.id.dateText);
            holder.titleText = (TextView) convertView
                    .findViewById(R.id.titleText);
            holder.descText = (TextView) convertView
                    .findViewById(R.id.descText);
            holder.imagesLayout = (LinearLayout) convertView
                    .findViewById(R.id.imagesLayout);
            holder.attachment1Image = (ImageView) convertView
                    .findViewById(R.id.attachment1Image);
            holder.attachment2Image = (ImageView) convertView
                    .findViewById(R.id.attachment2Image);
            holder.attachment3Image = (ImageView) convertView
                    .findViewById(R.id.attachment3Image);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        FeedModel model = list.get(position);

        showPublishTime(holder.dateText, model.getCrts() * 1000);
        holder.titleText.setText(model.getTitle());
        holder.descText.setText(model.getDescript());

        List<String> imageList = model.getImages();
        if (imageList == null || imageList.size() == 0)
        {
            holder.imagesLayout.setVisibility(View.GONE);
        }
        else
        {
            holder.imagesLayout.setVisibility(View.VISIBLE);
            showImages(holder, imageList);
        }

        return convertView;
    }

    private void showPublishTime(TextView textView, long time)
    {
        String day = new SimpleDateFormat("yyyy/MM/dd", Locale.CHINA).format(
                new Date(time)).toString();
        String today = new SimpleDateFormat("yyyy/MM/dd", Locale.CHINA).format(
                new Date()).toString();

        if (mDateHash.contains(day))
        {
            textView.setVisibility(View.GONE);
            return;
        }

        String strDate = "";
        if (day.equals(today))
        {
            strDate = getString(R.string.today_str);
            textView.setText(strDate);
        }
        else
        {
            strDate = day.substring(5);

            Spannable span = new SpannableString(strDate);
            span.setSpan(
                    new AbsoluteSizeSpan(FunctionUtil.dip2px(mContext, 11)), 2,
                    5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.setText(span);
        }

        textView.setVisibility(View.VISIBLE);
        mDateHash.add(day);
    }

    private void showImages(ViewHolder holder, List<String> images)
    {
        ImageView imageViewArray[] = { holder.attachment1Image,
                holder.attachment2Image, holder.attachment3Image };

        for (int i = 0; i < 3; i++)
        {
            if (i < images.size())
            {
                imageViewArray[i].setVisibility(View.VISIBLE);
                this.setImage(imageViewArray[i],
                        RequestManager.getImageUrl(images.get(i))
                                + GlobalConfig.IMAGE_STYLE90_90,
                        R.drawable.icon_default);
            }
            else
            {
                imageViewArray[i].setVisibility(View.GONE);
            }
        }
    }

    class ViewHolder
    {
        TextView dateText;
        TextView titleText;
        TextView descText;
        LinearLayout imagesLayout;
        ImageView attachment1Image;
        ImageView attachment2Image;
        ImageView attachment3Image;
    }

    public interface UserFeedListener
    {
        public void onUserFeedItemClick(FeedModel model);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id)
    {
        if (mListener != null)
        {
            mListener.onUserFeedItemClick(list.get(position));
        }
    }

}
