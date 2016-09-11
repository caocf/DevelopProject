package com.moge.gege.ui.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moge.gege.R;
import com.moge.gege.config.GlobalConfig;
import com.moge.gege.model.AlbumItemModel;
import com.moge.gege.ui.AlbumMainActivity;
import com.moge.gege.ui.adapter.PhotoListAdapter;
import com.moge.gege.ui.adapter.PhotoListAdapter.PhotoListListener;
import com.moge.gege.util.DialogUtil;
import com.moge.gege.util.EnviromentUtil;
import com.moge.gege.util.widget.horizontalListview.widget.HListView;

public class PhotoListFragment extends BaseFragment implements OnClickListener,
        PhotoListListener
{
    private Context mContext;
    private HListView mListView;
    private PhotoListAdapter mAdapter;
    private int mMaxPhotoNum = 9;
    private Dialog mMenuDialog;
    private ArrayList<AlbumItemModel> mSelectAlbumList = new ArrayList<AlbumItemModel>();
    private String mCameraImagePath;
    private TextView mSelectInfoText;
    private PhotoListChangeListener mPhotoNumChangeListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        final View layout = inflater.inflate(R.layout.custom_photolistview,
                container, false);
        initView(layout);
        initData();
        return layout;
    }

    @Override
    protected void initView(View v)
    {
        mContext = getActivity();

        mListView = (HListView) v.findViewById(R.id.photoHListView);
        mAdapter = new PhotoListAdapter(mContext);
        mAdapter.setListener(this);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(mAdapter);

        mAdapter.addItem(new AlbumItemModel(R.drawable.icon_add_photo, null));
        mAdapter.notifyDataSetChanged();

        mSelectInfoText = (TextView) v.findViewById(R.id.selectInfoText);
        mSelectInfoText.setText(getString(R.string.select_photo_info, 0,
                mMaxPhotoNum));
    }

    private void initData()
    {

    }

    public void setMaxPhotoNum(int maxPhotoNum)
    {
        this.mMaxPhotoNum = maxPhotoNum;
        mSelectInfoText.setText(getString(R.string.select_photo_info, 0,
                mMaxPhotoNum));
    }

    public void setPhotoNumChangeListener(PhotoListChangeListener listener)
    {
        mPhotoNumChangeListener = listener;
    }

    public void setDataSource(List<AlbumItemModel> data)
    {
        if (data == null)
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

        mSelectInfoText.setText(getString(R.string.select_photo_info,
                data.size(), mMaxPhotoNum - data.size()));

        if (mPhotoNumChangeListener != null)
        {
            mPhotoNumChangeListener.onPhotoNumChanged(data.size());
        }
    }

    public List<AlbumItemModel> getSelectPhotoList()
    {
        return mSelectAlbumList;
    }

    private void takeCamera()
    {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(EnviromentUtil.getImageDirectory(mContext),
                String.valueOf(System.currentTimeMillis()) + ".jpg");
        mCameraImagePath = file.getPath();
        Uri imageUri = Uri.fromFile(file);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(openCameraIntent,
                GlobalConfig.INTENT_TAKE_CAMERA);
    }

    private void takeAlbum()
    {
        Intent intent = new Intent(mContext, AlbumMainActivity.class);
        intent.putExtra("select_album", mSelectAlbumList);
        intent.putExtra("max_image_num", mMaxPhotoNum);
        startActivityForResult(intent, GlobalConfig.INTENT_SELECT_ALBUM);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK)
        {
            return;
        }

        switch (requestCode)
        {
            case GlobalConfig.INTENT_TAKE_CAMERA:
                mSelectAlbumList.add(new AlbumItemModel(0, mCameraImagePath));
                this.setDataSource(mSelectAlbumList);
                break;
            case GlobalConfig.INTENT_SELECT_ALBUM:
                if (data != null)
                {
                    mSelectAlbumList = (ArrayList<AlbumItemModel>) data
                            .getSerializableExtra("select_album");
                    this.setDataSource(mSelectAlbumList);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onPhotoDeleteClick(int position)
    {
        mSelectAlbumList.remove(position);
        this.setDataSource(mSelectAlbumList);
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
                                    takeCamera();
                                    break;
                                case 1:
                                    takeAlbum();
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

    @Override
    public void onClick(View v)
    {
        // TODO Auto-generated method stub

    }

    public interface PhotoListChangeListener
    {
        void onPhotoNumChanged(int selectNum);
    }

}
