package com.moge.gege.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.moge.gege.R;
import com.moge.gege.model.BaseOptionModel;
import com.moge.gege.ui.customview.OptionPopupWindow.OnOptionListener;
import com.moge.gege.ui.fragment.BaseFragment;
import com.moge.gege.ui.fragment.BoardFragment;
import com.moge.gege.ui.fragment.BoardFragment.BoardType;
import com.moge.gege.ui.fragment.CategoryBoardFragment;
import com.moge.gege.util.ToastUtil;
import com.moge.gege.util.ViewUtil;
import com.moge.gege.util.widget.MyPageAdapter;
import com.moge.gege.util.widget.viewpagerindicator.LinePageIndicator;

public class MoreBoardActivity extends BaseActivity implements
        OnOptionListener, TextWatcher
{
    private Context mContext;

    private FrameLayout mSearchResultLayout;
    private LinearLayout mOptionBoardLayout;
    private RadioGroup mTabBoardGroup;
    private MyPageAdapter mAdapter;
    private ViewPager mViewPager;
    private LinePageIndicator mIndicator;
    private RadioButton mCategoryBtn;
    private RadioButton mHotBtn;
    private RadioButton mLatestBtn;
    private List<BaseFragment> mFragmentList;

    private TextView mHeaderLeftText;
    private EditText mKeywordEdit;
    private ImageView mDeleteImage;
    private BoardFragment mBoardFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moreboard);

        mContext = MoreBoardActivity.this;
        initView();
    }

    @Override
    protected void initView()
    {
        super.initView();
        this.setHeaderLeftImage(R.drawable.icon_back);

        mSearchResultLayout = (FrameLayout) this
                .findViewById(R.id.searchResultLayout);
        mOptionBoardLayout = (LinearLayout) this
                .findViewById(R.id.optionBoardLayout);

        mHeaderLeftText = (TextView) this.findViewById(R.id.headerLeftText);
        mDeleteImage = (ImageView) this.findViewById(R.id.deleteImage);
        mDeleteImage.setOnClickListener(mClickListener);
        mKeywordEdit = (EditText) this.findViewById(R.id.keywordEdit);
        mKeywordEdit.setOnEditorActionListener(mEditorActionListener);
        mKeywordEdit.addTextChangedListener(this);

        // init fragment
        mBoardFragment = new BoardFragment();
        mBoardFragment.setFragmentType(BoardType.SEARCH);
        FragmentTransaction fragmentTransaction = this
                .getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.searchResultLayout, mBoardFragment);
        fragmentTransaction.commit();

        initViewPager();
    }

    private List<BaseFragment> getViewPagerSource()
    {
        mFragmentList = new ArrayList<BaseFragment>();

        CategoryBoardFragment categoryFragment = new CategoryBoardFragment();
        mFragmentList.add(categoryFragment);

        BoardFragment hotFragment = new BoardFragment();
        hotFragment.setFragmentType(BoardType.HOT);
        mFragmentList.add(hotFragment);

        BoardFragment latestFragment = new BoardFragment();
        latestFragment.setFragmentType(BoardType.LATEST);
        mFragmentList.add(latestFragment);

        return mFragmentList;
    }

    private void initViewPager()
    {
        mAdapter = new MyPageAdapter(getSupportFragmentManager(),
                getViewPagerSource());
        mViewPager = (ViewPager) findViewById(R.id.boardViewPager);
        mViewPager.setAdapter(mAdapter);

        mIndicator = (LinePageIndicator)findViewById(R.id.indicator);
        mIndicator.setViewPager(mViewPager);
        mIndicator.setLineWidth(ViewUtil.getWidth()/3);

        mTabBoardGroup = (RadioGroup) this.findViewById(R.id.tabBoardGroup);
        mCategoryBtn = (RadioButton) this.findViewById(R.id.categoryBtn);
        mCategoryBtn.setOnClickListener(mClickListener);
        mHotBtn = (RadioButton) this.findViewById(R.id.hotBtn);
        mLatestBtn = (RadioButton) this.findViewById(R.id.latestBtn);
        mTabBoardGroup.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                switch (checkedId)
                {
                    case R.id.categoryBtn:
                        mIndicator.setCurrentItem(0);
                        break;
                    case R.id.hotBtn:
                        mIndicator.setCurrentItem(1);
                        break;
                    case R.id.latestBtn:
                        mIndicator.setCurrentItem(2);
                        break;
                }

            }

        });

        mIndicator.setOnPageChangeListener(new OnPageChangeListener()
        {
            @Override
            public void onPageSelected(int position)
            {
                // if (mTabBoardGroup != null
                // && mTabBoardGroup.getChildCount() > position)
                // {
                // ((RadioButton) mTabBoardGroup.getChildAt(position))
                // .setChecked(true);
                // }

                switch (position)
                {
                    case 0:
                        mCategoryBtn.setChecked(true);
                        break;
                    case 1:
                        mHotBtn.setChecked(true);
                        break;
                    case 2:
                        mLatestBtn.setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2)
            {

            }

            @Override
            public void onPageScrollStateChanged(int arg0)
            {

            }
        });

        // init
        ((RadioButton) mTabBoardGroup.getChildAt(0)).setChecked(true);
    }

    @Override
    protected void onHeaderRightClick()
    {

    }

    private OnClickListener mClickListener = new OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
                case R.id.deleteImage:
                    mKeywordEdit.setText("");
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onOptionItemClick(BaseOptionModel model)
    {
        BoardFragment fragment = (BoardFragment) mAdapter.getItem(mViewPager
                .getCurrentItem());
        fragment.setCategoryId(model.get_id());
        fragment.reloadData();
    }

    OnEditorActionListener mEditorActionListener = new OnEditorActionListener()
    {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
        {
            if (actionId == EditorInfo.IME_ACTION_SEARCH)
            {
                onSearchBoard();

                InputMethodManager imm = (InputMethodManager) v.getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm.isActive())
                {
                    imm.hideSoftInputFromWindow(v.getApplicationWindowToken(),
                            0);
                }

                return true;
            }
            return false;
        }
    };

    private void onSearchBoard()
    {
        String keyword = mKeywordEdit.getText().toString().trim();
        if (TextUtils.isEmpty(keyword))
        {
            ToastUtil.showToastShort(R.string.keyword_not_empty);
            return;
        }

        mSearchResultLayout.setVisibility(View.VISIBLE);
        mOptionBoardLayout.setVisibility(View.GONE);

        mBoardFragment.setSearchKeyword(keyword);
        mBoardFragment.reloadData();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
            int after)
    {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
        if (TextUtils.isEmpty(s))
        {
            mDeleteImage.setVisibility(View.GONE);
            mOptionBoardLayout.setVisibility(View.VISIBLE);
            mSearchResultLayout.setVisibility(View.GONE);
        }
        else
        {
            mDeleteImage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void afterTextChanged(Editable s)
    {

    }

}
