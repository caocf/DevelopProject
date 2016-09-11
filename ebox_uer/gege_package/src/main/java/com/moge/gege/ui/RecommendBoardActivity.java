package com.moge.gege.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;

import com.moge.gege.R;
import com.moge.gege.ui.fragment.BoardFragment;
import com.moge.gege.ui.fragment.BoardFragment.BoardType;

public class RecommendBoardActivity extends BaseActivity
{
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        mContext = RecommendBoardActivity.this;
        initView();
        initData();
    }

    @Override
    protected void initView()
    {
        super.initView();

        // this.setHeaderLeftImage(R.drawable.icon_back);
        // this.setHeaderLeftTitle(R.string.board_recomment);
        this.setHeaderTitle(R.string.board_recomment);
        this.setHeaderRightTitle(R.string.finish);

        // init fragment
        BoardFragment boardFragment = new BoardFragment();
        boardFragment.setFragmentType(BoardType.RECOMMEND);
        FragmentTransaction fragmentTransaction = this
                .getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.boardLayout, boardFragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onHeaderRightClick()
    {
        setResult(RESULT_OK, null);
        finish();
    }

    private void initData()
    {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            onHeaderRightClick();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
