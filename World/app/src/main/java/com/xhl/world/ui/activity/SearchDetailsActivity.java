package com.xhl.world.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xhl.world.R;
import com.xhl.world.model.Entities.QueryConditionEntity;
import com.xhl.world.ui.fragment.SearchDetailsFragment;
import com.xhl.world.ui.utils.MDDialogHelper;
import com.xhl.world.ui.utils.MDTintHelper;
import com.xhl.xhl_library.Base.Sum.SumFragmentActivity;
import com.xhl.xhl_library.ui.view.RippleView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;


@ContentView(R.layout.activity_search_details)
public class SearchDetailsActivity extends SumFragmentActivity {

    public static final String SEARCH_TYPE = "search_type";

    public static final String SEARCH_TYPE_SCAN = "1";//扫码
    public static final String SEARCH_TYPE_INPUT = "2";//输入
    public static final String SEARCH_TYPE_CATEGORY = "3";//分类

    public static final String SEARCH_PARAMS = "search_params";


    private static final String order_by_default = "";
    //销量
    private static final String order_by_sale = "volume";
    //价格
    private static final String order_by_price = "productPrice";

    //搜索关键字
    private String mSearchType;
    private String mSearchParams;
    private QueryConditionEntity mCondition;
    private boolean mIsFristLoad = false;

    private boolean isPriceAsc = false;

    private SearchDetailsFragment mFragment;

    @ViewInject(R.id.tv_search_all)
    private TextView tv_search_all;

    @ViewInject(R.id.tv_search_sale)
    private TextView tv_search_sale;

    @ViewInject(R.id.tv_search_price)
    private TextView tv_search_price;

    @ViewInject(R.id.tv_search_filter)
    private TextView tv_search_filter;

    @ViewInject(R.id.iv_search_up_price)
    private ImageView iv_search_up_price;

    @ViewInject(R.id.iv_search_down_price)
    private ImageView iv_search_down_price;

    @ViewInject(R.id.tv_home_top_search_hint)
    private TextView tv_home_top_search_hint;

    @Event(value = R.id.ripple_bar_back, type = RippleView.OnRippleCompleteListener.class)
    private void onBackClick(View view) {
        finish();
    }

    @Event(value = R.id.ripple_bar_home_search)
    private void onSearchClick(View view) {
        startActivity(new Intent(this, SearchActivity.class));
    }

    @ViewInject(R.id.rl_search_all)
    private View rl_search_all;

    @Event(value = {R.id.rl_search_all, R.id.rl_search_sale, R.id.rl_search_price, R.id.rl_search_filter})
    private void onChangeConditionClick(View view) {
        switch (view.getId()) {
            case R.id.rl_search_all:
                searchAll();
                break;
            case R.id.rl_search_sale:
                searchSale();
                break;
            case R.id.rl_search_price:
                searchPrice();
                break;
            case R.id.rl_search_filter:
                searchFilter();
                break;
        }
    }

    //综合排序
    private void searchAll() {
        tv_search_all.setSelected(true);
        mCondition.setOrderBy(order_by_default);

        tv_search_sale.setSelected(false);
        tv_search_price.setSelected(false);
        tv_search_filter.setSelected(false);
        changePriceState(false);
        query();
    }

    //销量排序
    private void searchSale() {
        tv_search_all.setSelected(false);
        tv_search_sale.setSelected(true);
        mCondition.setOrderBy(order_by_sale);

        tv_search_price.setSelected(false);
        tv_search_filter.setSelected(false);
        changePriceState(false);
        query();
    }

    //价格排序
    private void searchPrice() {
        tv_search_all.setSelected(false);
        tv_search_sale.setSelected(false);
        tv_search_price.setSelected(true);
        tv_search_filter.setSelected(false);
        changePriceState(true);
        isPriceAsc = !isPriceAsc;
        query();
    }

    private void changePriceState(boolean selected) {

        if (selected) {
            if (isPriceAsc) {//价格升序
                mCondition.setOrderBy(order_by_price);
                mCondition.setSortBy(QueryConditionEntity.ASC);
                iv_search_up_price.setEnabled(true);
                iv_search_down_price.setEnabled(false);
            } else {//价格降序
                mCondition.setOrderBy(order_by_price);
                mCondition.setSortBy(QueryConditionEntity.DESC);
                iv_search_up_price.setEnabled(false);
                iv_search_down_price.setEnabled(true);
            }
        } else {
            iv_search_up_price.setEnabled(false);
            iv_search_down_price.setEnabled(false);
        }
    }


    private void searchFilter() {
    }

    private void query() {
        if (mFragment != null) {
            mFragment.setQueryCondition(mCondition);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSearchType = getIntent().getStringExtra(SEARCH_TYPE);
        mSearchParams = getIntent().getStringExtra(SEARCH_PARAMS);

        //创建筛选条件
        mCondition = new QueryConditionEntity();
        mCondition.setType(mSearchType);
        mCondition.setQueryParmas(mSearchParams);
    }

    @Override
    protected void initParams() {
        //主题色
        int color = MDDialogHelper.resolveColor(this, R.attr.main_theme_accent_color);
        MDTintHelper.setTint(iv_search_up_price, color, R.drawable.icon_query_up_normal);
        MDTintHelper.setTint(iv_search_down_price, color, R.drawable.icon_query_down_normal);

        //默认初始化状态
        iv_search_up_price.setEnabled(false);
        iv_search_down_price.setEnabled(false);
        tv_search_all.setSelected(true);

        mFragment = new SearchDetailsFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.add(R.id.fl_content, mFragment).commit();

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mSearchType.equals(SEARCH_TYPE_INPUT)) {
            tv_home_top_search_hint.setTextColor(ContextCompat.getColor(this, R.color.base_dark_text_color));
            tv_home_top_search_hint.setText(mSearchParams);
        }
        //首次加载根据默认条件查询参数
        if (!mIsFristLoad) {
//            searchAll();
            query();
            mIsFristLoad = false;
        }
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.fl_child_content;
    }

}
