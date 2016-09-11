package com.moge.gege.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.moge.gege.R;
import com.moge.gege.model.AlbumItemModel;
import com.moge.gege.util.BitmapUtil;
import com.moge.gege.util.widget.horizontalListview.widget.AdapterView.OnItemClickListener;

public class PhotoListAdapter extends BaseCachedListAdapter<AlbumItemModel>
        implements OnItemClickListener
{
    private PhotoListListener mListener;

    public PhotoListAdapter(Context context)
    {
        super(context);
    }

    public void setListener(PhotoListListener listener)
    {
        mListener = listener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup arg2)
    {
        ViewHolder holder;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_photo, null);

            holder = new ViewHolder();
            holder.photoImage = (ImageView) convertView
                    .findViewById(R.id.photoImage);
            holder.deleteImage = (ImageView) convertView
                    .findViewById(R.id.deleteImage);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        AlbumItemModel itemModel = list.get(position);

        if (!TextUtils.isEmpty(itemModel.getPath()))
        {
            holder.photoImage.setImageBitmap(BitmapUtil
                    .readBitmapForFixMaxSize(itemModel.getPath(), 125, 205));
            holder.deleteImage.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.photoImage.setImageBitmap(BitmapUtil.readBitMap(mContext,
                    itemModel.getId()));
            holder.deleteImage.setVisibility(View.GONE);
        }

        holder.deleteImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (mListener != null)
                {
                    mListener.onPhotoDeleteClick(position);
                }
            }

        });

        return convertView;
    }

    class ViewHolder
    {
        ImageView photoImage;
        ImageView deleteImage;
    }

    public interface PhotoListListener
    {
        public void onPhotoDeleteClick(int position);

        public void onPhotoItemClick(AlbumItemModel model);
    }

    @Override
    public void onItemClick(
            com.moge.gege.util.widget.horizontalListview.widget.AdapterView<?> parent,
            View view, int position, long id)
    {
        if (mListener != null)
        {
            mListener.onPhotoItemClick(list.get(position));

        }
    }

}
