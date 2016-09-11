package com.moge.gege.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;

import com.moge.gege.R;
import com.moge.gege.model.AlbumFolderModel;
import com.moge.gege.model.AlbumItemModel;
import com.moge.gege.ui.adapter.AlbumListAdapter;
import com.moge.gege.ui.adapter.AlbumListAdapter.AlbumListListener;
import com.moge.gege.ui.adapter.AlbumListAdapter.AlbumType;
import com.moge.gege.util.FunctionUtil;
import com.moge.gege.util.ImageLoaderUtil;
import com.moge.gege.util.ToastUtil;

public class AlbumSecondaryActivity extends BaseActivity implements
        AlbumListListener, OnClickListener
{
    private Context mContext;
    private GridView mGridView;
    private AlbumListAdapter mAdapter;
    private List<AlbumItemModel> mAlbumList;

    private GridView mSelectAlbumGridView;
    private AlbumListAdapter mSelectAdapter;
    private ArrayList<AlbumItemModel> mSelectAlbumList;

    private AlbumFolderModel mAlbumFolderModel;
    private Button mCommitBtn;
    private HorizontalScrollView mSelectSrollView;
    private int mMaxImageNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_albumsecondary);

        mAlbumFolderModel = getIntent().getParcelableExtra("secondary_album");
        mSelectAlbumList = (ArrayList<AlbumItemModel>) getIntent()
                .getSerializableExtra("select_album");
        mMaxImageNum = getIntent().getIntExtra("max_image_num", 10);

        mContext = AlbumSecondaryActivity.this;
        initView();
        initData();
    }

    @Override
    protected void initView()
    {
        super.initView();

        this.setHeaderLeftImage(R.drawable.icon_back);
        this.setHeaderLeftTitle(mAlbumFolderModel.getName());

        mGridView = (GridView) this.findViewById(R.id.albumGridView);
        mAdapter = new AlbumListAdapter(this);
        mAdapter.setDataSource(AlbumType.ALL, mAlbumFolderModel.getAlbumList());
        mAdapter.setListener(this);
        mAdapter.setParentView(mGridView);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(mAdapter);

        mGridView.setOnScrollListener(new OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState)
            {
                if (scrollState == OnScrollListener.SCROLL_STATE_IDLE)
                {
                    ImageLoaderUtil.instance().resume();
                }
                else
                {
                    ImageLoaderUtil.instance().pause();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                    int visibleItemCount, int totalItemCount)
            {
            }
        });

        mSelectAlbumGridView = (GridView) this
                .findViewById(R.id.selectAlbumGridView);
        mSelectAdapter = new AlbumListAdapter(this);
        mSelectAdapter.setDataSource(AlbumType.SELECT, mSelectAlbumList);
        mSelectAdapter.setListener(this);
        mSelectAlbumGridView.setAdapter(mSelectAdapter);
        mSelectAlbumGridView.setOnItemClickListener(mSelectAdapter);
        setSelectGridViewSingleLine();

        mSelectSrollView = (HorizontalScrollView) this
                .findViewById(R.id.selectSrollView);
        mCommitBtn = (Button) this.findViewById(R.id.commitBtn);
        mCommitBtn.setEnabled(mSelectAlbumList.size() > 0);
        mCommitBtn.setText(getString(R.string.select_finish)
                + String.format("(%d/%d)", mSelectAlbumList.size(),
                        mMaxImageNum));
        mCommitBtn.setOnClickListener(this);
    }

    @Override
    protected void onHeaderLeftClick()
    {
        gotoAlbumMainActivity(false);
    }

    @Override
    protected void onHeaderRightClick()
    {

    }

    private void initData()
    {
        updateAlbumFolderSelectData();
    }

    private void updateAlbumFolderSelectData()
    {
        for (int i = 0; i < mAlbumFolderModel.getAlbumList().size(); i++)
        {
            AlbumItemModel albumItemModel = mAlbumFolderModel.getAlbumList()
                    .get(i);

            for (int j = 0; j < mSelectAlbumList.size(); j++)
            {
                if (albumItemModel.getId() == mSelectAlbumList.get(j).getId())
                {
                    albumItemModel.setSelected(true);
                    break;
                }
            }
        }
    }

    @Override
    public void onAlbumItemClick(int position, AlbumType albumType,
            AlbumItemModel model)
    {
        if (albumType == AlbumType.ALL)
        {
            if (model.isSelected())
            {
                mSelectAlbumList.remove(model);
            }
            else
            {
                if (mSelectAlbumList.size() >= mMaxImageNum)
                {
                    ToastUtil.showToastShort(getString(
                            R.string.max_select_photo, mMaxImageNum));
                    return;
                }
                mSelectAlbumList.add(model);
            }
            model.setSelected(!model.isSelected());
            mAdapter.refreshViewByIndex(position);
        }
        else
        {
            model.setSelected(false);
            mSelectAlbumList.remove(model);
            mAdapter.refreshViewById(model.getId());
        }

        setSelectGridViewSingleLine();
        mSelectAdapter.notifyDataSetChanged();

        // 划到最后一个
        mSelectSrollView.post(new Runnable()
        {
            public void run()
            {
                mSelectSrollView.fullScroll(ScrollView.FOCUS_RIGHT);
            }
        });

        mCommitBtn.setEnabled(mSelectAlbumList.size() > 0);
        mCommitBtn.setText(getString(R.string.select_finish)
                + String.format("(%d/%d)", mSelectAlbumList.size(),
                        mMaxImageNum));
    }

    private void setSelectGridViewSingleLine()
    {
        ViewGroup.LayoutParams params = mSelectAlbumGridView.getLayoutParams();
        params.width = FunctionUtil.dip2px(mContext, 70)
                * mSelectAlbumList.size();
        mSelectAlbumGridView.setLayoutParams(params);
        mSelectAlbumGridView.setNumColumns(mSelectAlbumList.size());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
        {
            gotoAlbumMainActivity(false);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.commitBtn:
                gotoAlbumMainActivity(true);
                break;
            default:
                break;
        }
    }

    private void gotoAlbumMainActivity(boolean commit)
    {
        Intent intent = new Intent();
        intent.putExtra("secondary_album", mAlbumFolderModel);
        intent.putExtra("select_album", mSelectAlbumList);
        intent.putExtra("commit_select", commit);
        setResult(RESULT_OK, intent);
        finish();
    }
}
