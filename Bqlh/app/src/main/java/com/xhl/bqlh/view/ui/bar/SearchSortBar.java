package com.xhl.bqlh.view.ui.bar;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xhl.bqlh.R;
import com.xhl.bqlh.model.app.SearchParams;
import com.xhl.bqlh.view.base.BaseBar;
import com.xhl.bqlh.view.helper.MDTintHelper;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by Sum on 16/7/6.
 */
public class SearchSortBar extends BaseBar {

    public SearchSortBar(Context context) {
        super(context);
    }

    public SearchSortBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private static final String order_by_default = "";
    //销量
    private static final String order_by_sale = "sortForSalesVolume";
    //价格
    private static final String order_by_price = "original_price";

    private static final String parma_1 = "sortForPrice";
    private static final String parma_2 = "sortForSalesVolume";

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
        mCondition.myParam = order_by_default;

        tv_search_sale.setSelected(false);
        tv_search_price.setSelected(false);
        changePriceState(false);
        query();
    }

    //销量排序
    private void searchSale() {
        tv_search_all.setSelected(false);
        tv_search_sale.setSelected(true);
        mCondition.setOrderBy(order_by_sale);
        mCondition.myParam = parma_2;

        tv_search_price.setSelected(false);
        changePriceState(false);
        query();
    }

    //价格排序
    private void searchPrice() {
        tv_search_all.setSelected(false);
        tv_search_sale.setSelected(false);
        tv_search_price.setSelected(true);
        changePriceState(true);
        isPriceAsc = !isPriceAsc;
        query();
    }

    private void searchFilter() {
        if (mListener != null) {
            mListener.filter();
        }
    }

    private void changePriceState(boolean selected) {
        if (selected) {
            mCondition.myParam = parma_1;
            if (isPriceAsc) {//价格升序
                mCondition.setOrderBy(order_by_price);
                mCondition.setSortBy(SearchParams.ASC);
                iv_search_up_price.setEnabled(true);
                iv_search_down_price.setEnabled(false);
            } else {//价格降序
                mCondition.setOrderBy(order_by_price);
                mCondition.setSortBy(SearchParams.DESC);
                iv_search_up_price.setEnabled(false);
                iv_search_down_price.setEnabled(true);
            }
        } else {
            iv_search_up_price.setEnabled(false);
            iv_search_down_price.setEnabled(false);
        }
    }

    private void query() {
        if (mListener != null) {
            mListener.search(mCondition);
        }
    }

    private boolean isPriceAsc = false;

    private SearchParams mCondition;

    private SearchSortListener mListener;

    @Override
    protected void initParams() {
        //主题色
        MDTintHelper.setTint(iv_search_up_price, ContextCompat.getColor(getContext(), R.color.main_check_color_select), R.drawable.icon_query_up_normal);
        MDTintHelper.setTint(iv_search_down_price, ContextCompat.getColor(getContext(), R.color.main_check_color_select), R.drawable.icon_query_down_normal);

    }

    public void setSearchParams(SearchParams searchParams) {
        mCondition = searchParams;
        searchAll();
        tv_search_filter.setText(R.string.search_details_filter);
        tv_search_filter.setSelected(false);
    }

    public void setListener(SearchSortListener listener) {
        mListener = listener;
    }

    public void updateBrand(String brand) {
        if (brand.equals("全部")) {
            tv_search_filter.setText(R.string.search_details_filter);
            tv_search_filter.setSelected(false);
        } else {
            tv_search_filter.setSelected(true);
            tv_search_filter.setText(brand);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.bar_search_sort;
    }

    public interface SearchSortListener {

        void filter();

        void search(SearchParams searchParams);
    }
}
