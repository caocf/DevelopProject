package com.xhl.bqlh.view.ui.fragment;

import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.PopupWindowCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xhl.bqlh.AppConfig.GlobalParams;
import com.xhl.bqlh.R;
import com.xhl.bqlh.data.PreferenceData;
import com.xhl.bqlh.data.SearchData;
import com.xhl.bqlh.model.type.SearchTyp;
import com.xhl.bqlh.utils.ToastUtil;
import com.xhl.bqlh.view.base.BaseAppFragment;
import com.xhl.bqlh.view.custom.SelectSearchTypeView;
import com.xhl.bqlh.view.custom.TagLayout;
import com.xhl.bqlh.view.helper.ViewHelper;
import com.xhl.bqlh.view.ui.activity.SearchProductResActivity;
import com.xhl.bqlh.view.ui.activity.SearchShopResActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sum on 16/7/5.
 */
@ContentView(R.layout.fragment_search)
public class SearchFragment extends BaseAppFragment implements TagLayout.onFasterClickListener {

    @ViewInject(R.id.line)
    private View line;

    @ViewInject(R.id.ed_input_search)
    private EditText ed_input_search;

    @ViewInject(R.id.tv_search_type)
    private TextView tv_search_type;

    @ViewInject(R.id.tag_history_layout)
    private TagLayout tag_history_layout;

    @ViewInject(R.id.ll_search_hint)
    private LinearLayout ll_search_hint;

    @Event(R.id.fl_search_type)//搜索类型
    private void onSearchTypeClick(View view) {
        PopupWindowCompat.showAsDropDown(mPopup, line, 0, 1, GravityCompat.START);
    }

    @Event(R.id.search)
    private void onSearchClick(View view) {
        searchEvent();
    }

    @Event(R.id.fl_back)
    private void onBackClick(View view) {
        getSumContext().popTopFragment(null);
    }


    @Event(R.id.btn_clear)
    private void onHistoryClearClick(View view) {
        clearHistory();
    }

    private void clearHistory() {
        mSearchData = PreferenceData.getInstance().getSearchData();
        mSearchData.setSearch_history(new ArrayList<String>());

        PreferenceData.getInstance().setSearchData(mSearchData);

        getHistory();
    }

    //获取搜索历史数据
    private void getHistory() {
        mSearchData = PreferenceData.getInstance().getSearchData();
        if (mSearchData != null) {
            List<String> history = mSearchData.getSearch_history();
            if (history != null && history.size() > 0) {
                tag_history_layout.setArrayAdapter(history);
                ll_search_hint.setVisibility(View.VISIBLE);
            } else {
                ll_search_hint.setVisibility(View.GONE);
            }
        } else {
            ll_search_hint.setVisibility(View.GONE);
        }
    }

    private void addToHistory(String search) {
        if (TextUtils.isEmpty(search)) {
            return;
        }
        if (mSearchData == null) {
            mSearchData = new SearchData();
            mSearchData.setSearch_history(new ArrayList<String>());
        }
        //添加数据
        List<String> history = mSearchData.getSearch_history();
        if (history == null) {
            history = new ArrayList<>();
            mSearchData.setSearch_history(history);
        }
        boolean isExits = false;
        for (String st : history) {
            if (st.equals(search)) {
                isExits = true;
                break;
            }
        }
        if (!isExits) {
            mSearchData.getSearch_history().add(search);
        }

        PreferenceData.getInstance().setSearchData(mSearchData);
    }

    private SearchData mSearchData;
    private int mSearchType = SearchTyp.search_product;

    private SelectSearchTypeView mPopup;

    @Override
    protected void initParams() {
        tag_history_layout.setListener(this);

        mPopup = new SelectSearchTypeView(getContext());
        mPopup.setListener(new SelectSearchTypeView.TypeSelectListener() {
            @Override
            public void select(String content, int type) {
                tv_search_type.setText(content);
                mSearchType = type;
                updateHint();
            }
        });

        ed_input_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                fastSearch(text);
            }
        });

        ed_input_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchEvent();
                    handled = true;
                }
                return handled;
            }
        });
        updateHint();
        addSearchFragment();
    }

    private void updateHint() {
        if (mSearchType == SearchTyp.search_shop) {
            ed_input_search.setHint("搜索店铺");
        } else if (mSearchType == SearchTyp.search_product) {
            ed_input_search.setHint("搜索商品");
        }
        ed_input_search.getText().clear();
    }

    private SearchFastFragment mSearch;

    private void fastSearch(String text) {

        if (TextUtils.isEmpty(text)) {
            mSearch.hideSelf();
        } else {
            mSearch.search(text, mSearchType);
        }
    }

    private void addSearchFragment() {
        mSearch = new SearchFastFragment();
        getChildFragmentManager().beginTransaction().add(R.id.fl_content_view,mSearch).commit();

    }


    @Override
    public void onResume() {
        super.onResume();
        getHistory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPopup = null;
    }

    private void searchEvent() {
        String text = ed_input_search.getEditableText().toString().trim();
        if (!TextUtils.isEmpty(text)) {
            searchText(text);
        } else {
            ToastUtil.showToastShort("搜索内容不能为空");
        }
    }

    private void searchText(String text) {

        if (TextUtils.isEmpty(text)) {
            return;
        }
        ViewHelper.KeyBoardHide(ed_input_search);
        if (mSearchType == SearchTyp.search_product) {
            //添加到历史记录
            addToHistory(text);
        }
        startSearch(text);
    }

    private void startSearch(String text) {
        if (mSearchType == SearchTyp.search_product) {
            Intent intent = new Intent(getContext(), SearchProductResActivity.class);
            intent.putExtra(SearchProductResActivity.SEARCH_TYPE, SearchProductResActivity.SEARCH_TYPE_INPUT);
            intent.putExtra(SearchProductResActivity.SEARCH_PARAMS, text);
            getContext().startActivity(intent);
        } else if (mSearchType == SearchTyp.search_shop) {
            Intent intent = new Intent(getContext(), SearchShopResActivity.class);
            intent.putExtra(GlobalParams.Intent_serach_text, text);
            getContext().startActivity(intent);
        }
    }

    @Override
    public void onSearchClick(String text) {
        Intent intent = new Intent(getContext(), SearchProductResActivity.class);
        intent.putExtra(SearchProductResActivity.SEARCH_TYPE, SearchProductResActivity.SEARCH_TYPE_INPUT);
        intent.putExtra(SearchProductResActivity.SEARCH_PARAMS, text);
        getContext().startActivity(intent);
    }
}
