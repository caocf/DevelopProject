package com.moge.gege.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.widget.ListView;

import com.moge.gege.R;
import com.moge.gege.config.GlobalConfig;
import com.moge.gege.model.AlbumFolderModel;
import com.moge.gege.model.AlbumItemModel;
import com.moge.gege.ui.adapter.AlbumFolderListAdapter;
import com.moge.gege.ui.adapter.AlbumFolderListAdapter.AlbumFolderListListener;
import com.moge.gege.util.MediaStoreUtil;

public class AlbumMainActivity extends BaseActivity implements
        AlbumFolderListListener
{
    private Context mContext;
    private ListView mListView;
    private AlbumFolderListAdapter mAdapter;
    private List<AlbumFolderModel> mAlbumFolderList = new ArrayList<AlbumFolderModel>();
    private ArrayList<AlbumItemModel> mSelectAlbumList = new ArrayList<AlbumItemModel>();
    private int mMaxImageNum;
    private AlbumFolderModel mCurrentAlbumFolderModel;

    private static final String[] STORE_IMAGES = {
            MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.LONGITUDE, MediaStore.Images.Media._ID,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME

    };

    private Handler mHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case 0:
                    mAlbumFolderList.clear();
                    mAlbumFolderList.addAll((List<AlbumFolderModel>) msg.obj);
                    mAdapter.setDataSource(mAlbumFolderList);
                    mAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_albummain);

        mSelectAlbumList = (ArrayList<AlbumItemModel>) getIntent()
                .getSerializableExtra("select_album");
        mMaxImageNum = getIntent().getIntExtra("max_image_num", 10);

        mContext = AlbumMainActivity.this;
        initView();
        initData();
    }

    @Override
    protected void initView()
    {
        super.initView();

        this.setHeaderLeftImage(R.drawable.icon_back);
        this.setHeaderLeftTitle(R.string.select_album);

        mListView = (ListView) this.findViewById(R.id.albumFolderList);
        mAdapter = new AlbumFolderListAdapter(this);
        mAdapter.setListener(this);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(mAdapter);

    }

    @Override
    protected void onHeaderRightClick()
    {

    }

    private void initData()
    {
        loadAlbumImage();
    }

    private void loadAlbumImage()
    {
        new Thread(new Runnable()
        {
            public void run()
            {
                // 获取缩略图
                MediaStoreUtil.initThumbImage(mContext);

                Cursor cursor = MediaStore.Images.Media.query(
                        getContentResolver(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        STORE_IMAGES, null,
                        MediaStore.Images.Media.DATE_MODIFIED + " desc");
                Map<String, AlbumFolderModel> countMap = new HashMap<String, AlbumFolderModel>();
                AlbumFolderModel albumFolderModel = null;
                while (cursor.moveToNext())
                {
                    String path = cursor.getString(1);
                    String id = cursor.getString(3);
                    String dirId = cursor.getString(4);
                    String dir = cursor.getString(5);

                    if (!countMap.containsKey(dirId))
                    {
                        albumFolderModel = new AlbumFolderModel();
                        albumFolderModel.setPath(path);
                        albumFolderModel.setName(dir);
                        albumFolderModel.setCount(1);
                        albumFolderModel.setFirstResId(Integer.parseInt(id));
                        albumFolderModel.getAlbumList().add(
                                new AlbumItemModel(Integer.valueOf(id), path));
                        countMap.put(dirId, albumFolderModel);
                    }
                    else
                    {
                        albumFolderModel = countMap.get(dirId);
                        albumFolderModel.setCount(albumFolderModel.getCount() + 1);
                        albumFolderModel.getAlbumList().add(
                                new AlbumItemModel(Integer.valueOf(id), path));
                    }
                }
                cursor.close();

                List<AlbumFolderModel> albumFolderList = new ArrayList<AlbumFolderModel>();
                Iterable<String> it = countMap.keySet();
                for (String key : it)
                {
                    albumFolderList.add(countMap.get(key));
                }

                // 发送结果
                Message msg = mHandler.obtainMessage();
                msg.what = 0;
                msg.obj = albumFolderList;
                mHandler.sendMessage(msg);
            }
        }).start();
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
            case GlobalConfig.INTENT_ALBUM_SECONDARY:
                if (data != null)
                {
                    mSelectAlbumList = (ArrayList<AlbumItemModel>) data
                            .getSerializableExtra("select_album");
                    if (data.getBooleanExtra("commit_select", false))
                    {
                        Intent intent = new Intent();
                        intent.putExtra("select_album", mSelectAlbumList);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }

                break;
            default:
                break;
        }
    }

    @Override
    public void onAlbumFolderItemClick(AlbumFolderModel model)
    {
        mCurrentAlbumFolderModel = model;

        Intent intent = new Intent(mContext, AlbumSecondaryActivity.class);
        intent.putExtra("secondary_album", model);
        intent.putExtra("select_album", mSelectAlbumList);
        intent.putExtra("max_image_num", mMaxImageNum);
        startActivityForResult(intent, GlobalConfig.INTENT_ALBUM_SECONDARY);
    }
}
