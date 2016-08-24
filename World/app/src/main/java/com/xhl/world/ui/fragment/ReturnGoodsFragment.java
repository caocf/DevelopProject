package com.xhl.world.ui.fragment;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.xhl.world.R;
import com.xhl.world.api.ApiControl;
import com.xhl.world.model.ApplyReturnGoodsModel;
import com.xhl.world.model.Base.ResponseModel;
import com.xhl.world.model.serviceOrder.Order;
import com.xhl.world.model.serviceOrder.OrderDetail;
import com.xhl.world.ui.utils.SnackMaker;
import com.xhl.xhl_library.ui.view.RippleView;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sum on 16/1/18.
 */
@ContentView(R.layout.fragment_goods_return)
public class ReturnGoodsFragment extends BaseAppFragment {


    @ViewInject(R.id.title_name)
    private TextView title_name;

    @Event(value = R.id.title_back, type = RippleView.OnRippleCompleteListener.class)
    private void onBackClick(View view) {
        getSumContext().popTopFragment(null);
    }

    @ViewInject(R.id.tv_goods_name)
    private TextView tv_goods_name;

    @ViewInject(R.id.rg_content)
    private RadioGroup rg_content;

    @ViewInject(R.id.ed_return_content)
    private EditText ed_return_content;

    @ViewInject(R.id.spinner)
    private Spinner mSp;

    @Event(value = R.id.ripple_commit, type = RippleView.OnRippleCompleteListener.class)
    private void onCommitClick(View view) {
        commit();
    }

    private Order mOrder;

    private ArrayAdapter<String> mAdapter;

    private List<String> mNumData;

    private ApplyReturnGoodsModel mApply;

    @Override
    protected void initParams() {

        title_name.setText("退货申请");
        tv_goods_name.setText(mOrder.getProductName());
        mApply = new ApplyReturnGoodsModel();
        mApply.setProductNum("1");//默认退1个
        mApply.setReturnType("1");//默认退货类型
        mApply.setPhone(mOrder.getPhone());//收货人手机号
        mApply.setProductId(mOrder.getProductId());//商品id
        mApply.setCompanyId(mOrder.getCompanyId());//商品卖家id
        mApply.setStoreOrderCode(mOrder.getStoreOrderCode());//子订单

        rg_content.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_choose_1:
                        mApply.setReturnType("1");
                        break;
                    case R.id.rb_choose_2:
                        mApply.setReturnType("2");
                        break;
                    case R.id.rb_choose_3:
                        mApply.setReturnType("3");
                        break;
                    case R.id.rb_choose_4:
                        mApply.setReturnType("4");
                        break;
                }
            }
        });
        if (mOrder != null) {

            mNumData = new ArrayList<>();

            try {
                OrderDetail detail = mOrder.getOrderDetailList().get(0);
                String num = detail.getNum();
                int number = Integer.parseInt(num);
                for (int i = 0; i < number; i++) {
                    mNumData.add(String.valueOf(i + 1));
                }
                mAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, mNumData);
                mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mSp.setAdapter(mAdapter);
                mSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String num = mNumData.get(position);
                        mApply.setProductNum(num);//设置申请的数量
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onEnter(Object data) {
        if (data != null && data instanceof Order) {
            mOrder = (Order) data;
        }
    }

    private boolean mIsApplying = false;

    private void commit() {

        String content = ed_return_content.getText().toString().trim();
        if (content.length() < 8) {
            SnackMaker.shortShow(title_name,"亲，请多输入些吧");
            return;
        }
        if (mIsApplying) {
            return;
        }
        mIsApplying = true;
        mApply.setReturnContent(content);//理由说明

        showLoadingDialog();
        ApiControl.getApi().orderReturn(mApply, new Callback.CommonCallback<ResponseModel<Integer>>() {
            @Override
            public void onSuccess(ResponseModel<Integer> result) {
                if (result.isSuccess()) {
                    if (result.getResultObj() == 1) {
                        SnackMaker.shortShow(title_name,"申请成功");
                        getSumContext().popTopFragment(null);
                    } else {
                        ed_return_content.setText("");
                        SnackMaker.shortShow(title_name,"申请失败");
                    }
                } else {
                    SnackMaker.shortShow(title_name,result.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                SnackMaker.shortShow(title_name,ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                mIsApplying = false;
                hideLoadingDialog();
            }
        });
    }
}
