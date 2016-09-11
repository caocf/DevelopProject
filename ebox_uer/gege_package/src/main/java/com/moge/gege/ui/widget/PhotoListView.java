package com.moge.gege.ui.widget;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.moge.gege.R;
import com.moge.gege.config.GlobalConfig;
import com.moge.gege.model.AlbumItemModel;
import com.moge.gege.ui.AlbumMainActivity;
import com.moge.gege.ui.adapter.PhotoListAdapter;
import com.moge.gege.ui.adapter.PhotoListAdapter.PhotoListListener;
import com.moge.gege.util.DialogUtil;
import com.moge.gege.util.widget.horizontalListview.widget.HListView;

public class PhotoListView extends LinearLayout implements OnClickListener,
        PhotoListListener
{
    private Context mContext;
    private HListView mListView;
    private PhotoListAdapter mAdapter;
    private int mMaxPhotoNum = 10;
    private List<String> mImageList = new ArrayList<String>();
    private Dialog mMenuDialog;

    public PhotoListView(Context context)
    {
        super(context);
        mContext = context;
    }

    public PhotoListView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mContext = context;
    }

    public void setMaxPhotoNum(int maxPhotoNum)
    {
        this.mMaxPhotoNum = maxPhotoNum;
    }

    public void setDataSource(List<AlbumItemModel> data)
    {
        if (data == null || data.size() == 0)
        {
            return;
        }

        mAdapter.clear();
        mAdapter.addAll(data);
        if (data.size() < mMaxPhotoNum)
        {
            mAdapter.addItem(new AlbumItemModel(R.drawable.icon_add_photo, null));
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();
        initViews(mContext);
    }

    private void initViews(Context context)
    {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.custom_photolistview, this, true);

        mListView = (HListView) this.findViewById(R.id.photoHListView);
        mAdapter = new PhotoListAdapter(mContext);
        mAdapter.setListener(this);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(mAdapter);

        mAdapter.addItem(new AlbumItemModel(R.drawable.icon_add_photo, null));
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            default:
                break;
        }
    }

    public boolean onKeyBack()
    {
        return false;
    }

    @Override
    public void onPhotoItemClick(AlbumItemModel model)
    {
        if (!TextUtils.isEmpty(model.getPath()))
        {
            return;
        }

        if (mMenuDialog == null)
        {
            mMenuDialog = DialogUtil.createMenuDialog(mContext, "",
                    R.array.image_select_menu, new OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            mMenuDialog.dismiss();
                            switch (((Integer) v.getTag()).intValue())
                            {
                                case 0:
                                    break;
                                case 1:
                                    ((Activity) mContext)
                                            .startActivityForResult(
                                                    new Intent(
                                                            mContext,
                                                            AlbumMainActivity.class),
                                                    GlobalConfig.INTENT_SELECT_ALBUM);
                                    break;
                                case 2:
                                    break;
                                default:
                                    break;
                            }
                        }

                    });
        }

        mMenuDialog.show();
    }

    public void showCamera()
    {
        // Intent openCameraIntent = new
        // Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // File file = new File(GlobalConfig.getImageStorePath(),
        // String.valueOf(System.currentTimeMillis())
        // + ".jpg");
        // path = file.getPath();
        // Uri imageUri = Uri.fromFile(file);
        // openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        // startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }

    @Override
    public void onPhotoDeleteClick(int position)
    {
        mAdapter.remove(position);

        if (mAdapter.getCount() == (mMaxPhotoNum - 1))
        {
            mAdapter.addItem(new AlbumItemModel(R.drawable.icon_add_photo, null));
        }

        mAdapter.notifyDataSetChanged();
    }

}
