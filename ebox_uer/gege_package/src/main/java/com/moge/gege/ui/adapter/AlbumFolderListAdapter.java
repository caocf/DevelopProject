package com.moge.gege.ui.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.moge.gege.R;
import com.moge.gege.model.AlbumFolderModel;
import com.moge.gege.network.util.RequestManager;
import com.moge.gege.util.MediaStoreUtil;

public class AlbumFolderListAdapter extends BaseAdapter implements
        OnItemClickListener
{
    private Context mContext;
    private AlbumFolderListListener mListener;
    private List<AlbumFolderModel> mDataList;

    public AlbumFolderListAdapter(Context context)
    {
        mContext = context;
    }

    public void setListener(AlbumFolderListListener listener)
    {
        mListener = listener;
    }

    public void setDataSource(List<AlbumFolderModel> list)
    {
        mDataList = list;
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

    @Override
    public View getView(int position, View convertView, ViewGroup arg2)
    {
        ViewHolder holder;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_albumfolder, null);

            holder = new ViewHolder();
            holder.albumImage = (ImageView) convertView
                    .findViewById(R.id.albumImage);
            holder.nameText = (TextView) convertView
                    .findViewById(R.id.nameText);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        AlbumFolderModel model = mDataList.get(position);

        // Bitmap bitmap = MediaStore.Images.Thumbnails.getThumbnail(
        // mContext.getContentResolver(), model.getFirstResId(),
        // Thumbnails.MICRO_KIND, null);
        // holder.albumImage.setImageBitmap(bitmap);

        RequestManager.loadImageByUrl(
                holder.albumImage,
                MediaStoreUtil.getThumbPath(model.getFirstResId(), "file://"
                        + model.getPath()), R.drawable.icon_default);

        holder.nameText.setText(model.getName() + "(" + model.getCount() + ")");
        return convertView;
    }

    class ViewHolder
    {
        ImageView albumImage;
        TextView nameText;
    }

    public interface AlbumFolderListListener
    {
        public void onAlbumFolderItemClick(AlbumFolderModel model);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id)
    {
        if (mListener != null)
        {
            mListener.onAlbumFolderItemClick(mDataList.get(position));
        }
    }

}
