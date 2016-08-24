package com.xhl.world.ui.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xhl.world.R;
import com.xhl.world.api.ApiControl;
import com.xhl.world.data.PreferenceData;
import com.xhl.world.data.SearchData;
import com.xhl.world.model.Base.ResponseModel;
import com.xhl.world.model.HotSearchModel;
import com.xhl.world.ui.event.SearchItemEvent;
import com.xhl.world.ui.fragment.SearchResFragment;
import com.xhl.world.ui.utils.SnackMaker;
import com.xhl.world.ui.utils.ViewUtils;
import com.xhl.world.ui.view.TagLayout;
import com.xhl.xhl_library.ui.view.RippleView;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sum on 15/12/29.
 */
@ContentView(R.layout.activity_search)
public class SearchActivity extends BaseAppActivity {

    @ViewInject(R.id.ed_input_search)
    private EditText editText;

    @ViewInject(R.id.rl_input_clear)
    private RelativeLayout rl_input_clear;

    @ViewInject(R.id.ll_search_hint)
    private LinearLayout ll_search_hint;

    @ViewInject(R.id.tag_layout)
    private TagLayout tag_layout;

    @ViewInject(R.id.tag_history_layout)
    private TagLayout tag_history_layout;

    @ViewInject(R.id.tv_hot_search_hint)
    private TextView tv_hot_search_hint;

    @Event(value = R.id.ripple_back, type = RippleView.OnRippleCompleteListener.class)
    private void onBackClick(View view) {
        ViewUtils.hideKeyBoard(view);
        finish();
    }

    @Event(R.id.rl_input_clear)
    private void onClearClick(View view) {
        if (editText != null) {
            editText.getEditableText().clear();
        }
    }

    @Event(R.id.btn_clear)
    private void onHistoryClearClick(View view) {
        clearHistory();
    }

    @Event(R.id.btn_search)
    private void onSearchClick(View view) {
        search();
    }

    private List<HotSearchModel> hotModels;

    private void search() {
        String text = editText.getText().toString().trim();
        search(text);
    }

    private void search(String text) {

        if (TextUtils.isEmpty(text)) {
            return;
        }
        ViewUtils.hideKeyBoard(editText);
        //添加到历史记录
        addToHistory(text);

        Intent intent = new Intent(this, SearchDetailsActivity.class);
        intent.putExtra(SearchDetailsActivity.SEARCH_PARAMS, text);
        intent.putExtra(SearchDetailsActivity.SEARCH_TYPE, SearchDetailsActivity.SEARCH_TYPE_INPUT);
        startActivity(intent);

    }

    private void clearHistory() {
        mSearchData = PreferenceData.getInstance().getSearchData();
        mSearchData.setSearch_history(new ArrayList<String>());

        PreferenceData.getInstance().setSearchData(mSearchData);

        getHistory();
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

    private void addHotSearch(List<HotSearchModel> models) {
        hotModels = models;
        tv_hot_search_hint.setVisibility(View.VISIBLE);
        List<String> data = new ArrayList<>();
        for (HotSearchModel hot : models) {
            data.add(hot.getName());
        }
        tag_layout.setArrayAdapter(data);
    }

    private void getDefaultHotSearch() {

        ApiControl.getApi().hotSearch(new Callback.CommonCallback<ResponseModel<List<HotSearchModel>>>() {
            @Override
            public void onSuccess(ResponseModel<List<HotSearchModel>> result) {
                if (result.isSuccess()) {
                    List<HotSearchModel> resultObj = result.getResultObj();
                    if (resultObj == null || resultObj.size() <= 0) {
                        tv_hot_search_hint.setVisibility(View.GONE);
                    } else {
                        addHotSearch(resultObj);
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                SnackMaker.shortShow(editText, ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
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

    private SearchData mSearchData;

    private SearchResFragment mResFragment;
    private boolean mHasAdd = false;

    @Override
    protected void initParams() {

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
//                String text = s.toString();
                changeVisible();
//                serverQuery(text);
            }
        });

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search();
                    handled = true;
                }
                return handled;
            }
        });

        getDefaultHotSearch();

        changeVisible();
    }

    private void changeVisible() {
        String text = editText.getText().toString().trim();
        if (TextUtils.isEmpty(text)) {
            ViewUtils.changeViewVisible(rl_input_clear, false);
        } else {
            ViewUtils.changeViewVisible(rl_input_clear, true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        forceShowKeyboard();
        getHistory();
//        addSearchFragment();
    }

    private void serverQuery(String input) {
        if (TextUtils.isEmpty(input)) {
            mResFragment.hideSelf();
            return;
        }
//        mResFragment.setShowData(test);
    }

    private void addSearchFragment() {
        if (!mHasAdd) {
            mHasAdd = true;
            mResFragment = new SearchResFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.rl_content, mResFragment).commit();
        }
    }

    public void onEvent(SearchItemEvent event) {

        editText.setText(event.search_content);

        if (event.search_type == SearchItemEvent.Type_Fast_Search) {
            search(event.search_content);
        } else if (event.search_type == SearchItemEvent.Type_Search) {
//            search(event.search_content);
        }
    }
}
