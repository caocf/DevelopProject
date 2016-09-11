package com.moge.gege.ui.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.moge.gege.R;
import com.moge.gege.model.AlbumItemModel;
import com.moge.gege.network.util.RequestManager;
import com.moge.gege.util.MediaStoreUtil;
import com.moge.gege.util.ViewUtil;

public class AlbumListAdapter extends BaseAdapter implements
        OnItemClickListener
{
    private Context mContext;
    private List<AlbumItemModel> mDataList;
    private AlbumListListener mListener;
    private AlbumType mAlbumType;
    private GridView mParentView;

    public enum AlbumType
    {
        ALL, SELECT
    };

    public AlbumListAdapter(Context context)
    {
        mContext = context;

    }

    public void setParentView(GridView parentView)
    {
        mParentView = parentView;
    }

    public void setDataSource(AlbumType albumType, List<AlbumItemModel> list)
    {
        mDataList = list;
        mAlbumType = albumType;
    }

    public void setListener(AlbumListListener listener)
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

    public void refreshViewByIndex(int index)
    {
        if (mParentView == null || mDataList == null)
        {
            return;
        }

        int visiblePos = mParentView.getFirstVisiblePosition();
        View view = mParentView.getChildAt(index - visiblePos);
        ViewHolder holder = (ViewHolder) view.getTag();

        if (mDataList.get(index).isSelected())
        {
            holder.selectImage.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.selectImage.setVisibility(View.GONE);
        }
    }

    public void refreshViewById(int id)
    {
        if (mParentView == null || mDataList == null)
        {
            return;
        }

        int firstVisiblePos = mParentView.getFirstVisiblePosition();
        int lastVisbilePos = mParentView.getLastVisiblePosition();
        int count = lastVisbilePos - firstVisiblePos + 1;

        for (int i = 0; i < count; i++)
        {
            View view = mParentView.getChildAt(i);
            ViewHolder holder = (ViewHolder) view.getTag();

            AlbumItemModel model = mDataList.get(firstVisiblePos + i);
            if (model.getId() != id)
            {
                continue;
            }

            if (model.isSelected())
            {
                holder.selectImage.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.selectImage.setVisibility(View.GONE);
            }
            break;
        }

    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2)
    {
        ViewHolder holder;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_album, null);

            holder = new ViewHolder();
            holder.albumImage = (ImageView) convertView
                    .findViewById(R.id.albumImage);
            holder.selectImage = (ImageView) convertView
                    .findViewById(R.id.selectImage);
            convertView.setTag(holder);

            final int width = ViewUtil.getWidth() / 4;
            LayoutParams imageParams = holder.albumImage.getLayoutParams();
            imageParams.width = width;
            imageParams.height = width;
            holder.albumImage.setLayoutParams(imageParams);

            LayoutParams selectImageParams = holder.selectImage
                    .getLayoutParams();
            selectImageParams.width = width + 5;
            selectImageParams.height = width + 5;
            holder.selectImage.setLayoutParams(selectImageParams);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        AlbumItemModel model = mDataList.get(position);

        RequestManager.loadImageByUrl(
                holder.albumImage,
                MediaStoreUtil.getThumbPath(model.getId(),
                        "file://" + model.getPath()), R.drawable.icon_default);

        if (mAlbumType == AlbumType.ALL)
        {
            if (model.isSelected())
            {
                holder.selectImage.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.selectImage.setVisibility(View.GONE);
            }
        }
        return convertView;
    }

    class ViewHolder
    {
        ImageView albumImage;
        ImageView selectImage;
    }

    public interface AlbumListListener
    {
        public void onAlbumItemClick(int position, AlbumType albumType,
                AlbumItemModel model);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id)
    {
        if (mListener != null)
        {
            mListener.onAlbumItemClick(position, mAlbumType,
                    mDataList.get(position));
        }
    }

}
