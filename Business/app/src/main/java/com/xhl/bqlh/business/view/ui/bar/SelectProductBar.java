package com.xhl.bqlh.business.view.ui.bar;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xhl.bqlh.business.Model.App.ShopCarModel;
import com.xhl.bqlh.business.Model.Type.ProductType;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.utils.ToastUtil;
import com.xhl.bqlh.business.view.base.BaseBar;
import com.xhl.bqlh.business.view.custom.ComplexText;
import com.xhl.bqlh.business.view.custom.LifeCycleImageView;
import com.xhl.bqlh.business.view.helper.DialogMaker;
import com.xhl.bqlh.business.view.helper.ViewHelper;
import com.xhl.bqlh.business.view.helper.pub.Callback.RecycleViewCallBack;
import com.xhl.bqlh.business.view.helper.pub.Callback.RecycleViewCallBackWithData;
import com.xhl.bqlh.business.view.ui.activity.ConfirmProductActivity;
import com.xhl.xhl_library.utils.NumberUtil;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by Sum on 16/6/14.
 */
public class SelectProductBar extends BaseBar implements View.OnClickListener {

    @ViewInject(R.id.tv_num_1)
    private TextView tv_num_1;

    @ViewInject(R.id.tv_num_hint_1)
    private TextView tv_num_hint_1;

    @ViewInject(R.id.tv_num_2)
    private TextView tv_num_2;

    @ViewInject(R.id.tv_num_hint_2)
    private TextView tv_num_hint_2;

    @ViewInject(R.id.tv_account)
    private TextView tv_account;

    @ViewInject(R.id.tv_product_name)
    private TextView tv_product_name;

    @ViewInject(R.id.tv_product_remark)
    private TextView tv_product_remark;

    @ViewInject(R.id.tv_product_numb)
    private TextView tv_product_numb;

    @ViewInject(R.id.iv_fix_price)
    private ImageView iv_fix_price;//修改单价按钮

    @ViewInject(R.id.iv_fix_price2)
    private ImageView iv_fix_price2;//修改单价按钮

    @ViewInject(R.id.tv_product_price)
    private TextView tv_product_price;//商品原价

    @ViewInject(R.id.tv_product_fix_price)
    private TextView tv_product_fix_price;//修改后的单价

    @ViewInject(R.id.btn_reduce)
    private View btn_reduce;

    @ViewInject(R.id.btn_add)
    private View btn_add;

    @ViewInject(R.id.ll_unit_content)
    private View ll_unit_content;//单位数据

    @ViewInject(R.id.iv_product_pic)
    private LifeCycleImageView iv_product_pic;

    @Event(value = {R.id.iv_fix_price, R.id.iv_fix_price2, R.id.tv_product_price})
    private void onFixEvent(View view) {
        if (mItemData.showFixPrice && mItemData.productType != ProductType.PRODUCT_GIFT) {
            onFixPrice();
        }
    }

    public SelectProductBar(Context context) {
        super(context);
    }

    public SelectProductBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private ShopCarModel mItemData;
    private RecycleViewCallBack callBack;

    @Override
    protected void initParams() {
        btn_reduce.setOnClickListener(this);
        btn_add.setOnClickListener(this);
        tv_product_numb.setOnClickListener(this);
        tv_num_1.setOnClickListener(this);
        tv_num_2.setOnClickListener(this);
        findViewById(R.id.ll_content).setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onDeleteData();
                return true;
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.bar_item_select_product;
    }

    public void onBindData(ShopCarModel shop) {
        if (shop == null) {
            return;
        }
        mItemData = shop;
        //商品名称
        if (shop.productType == ProductType.PRODUCT_GIFT) {
            ll_unit_content.setVisibility(GONE);
            tv_product_name.setText(ProductType.GetGiftTitle(shop.productName));
        } else {
            ll_unit_content.setVisibility(VISIBLE);
            tv_product_name.setText(shop.productName);
        }
        if (shop.productType == ProductType.PRODUCT_GIFT) {
            tv_product_price.setVisibility(INVISIBLE);
        } else {
            tv_product_price.setVisibility(VISIBLE);
            //批发价
            tv_product_price.setText(mContext.getResources().getString(shop.priceHint, shop.productPrice, shop.unitMin));
        }
        //图片
        iv_product_pic.bindImageUrl(shop.productPic);

        tv_product_remark.setText(shop.productRemark);

        showPrice();

        update();
    }

    //价格显示
    private void showPrice() {
        if (mItemData.productType == ProductType.PRODUCT_GIFT) {
            iv_fix_price.setVisibility(INVISIBLE);
            iv_fix_price2.setVisibility(INVISIBLE);
            return;
        }

        float productFixPrice = mItemData.productFixPrice;
        String origin = mContext.getResources().getString(mItemData.priceHint, mItemData.productPrice, mItemData.unitMin);

        if (productFixPrice == 0) {
            tv_product_fix_price.setText("");

            SpannableString text = new SpannableString(origin);
            int color = ContextCompat.getColor(mContext, R.color.app_price_color);
            //字体颜色
            text.setSpan(new ForegroundColorSpan(color), 0, text.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            tv_product_price.setText(text);
            if (mItemData.showFixPrice) {
                iv_fix_price.setVisibility(VISIBLE);
                iv_fix_price2.setVisibility(INVISIBLE);
            }
        } else {
            //退货价
            if (mItemData.operatorType == ConfirmProductActivity.TYPE_ORDER_RETURN || mItemData.operatorType == ConfirmProductActivity.TYPE_ORDER_RETURN_1) {
                tv_product_fix_price.setText(mContext.getString(R.string.product_price_3, productFixPrice, mItemData.unitMin));
            } else {
                tv_product_fix_price.setText(mContext.getString(R.string.product_price_2, productFixPrice, mItemData.unitMin));
            }
            ComplexText.TextBuilder builder = new ComplexText.TextBuilder();
            builder.setText(origin);
            builder.setStrikeLine(true);
            int color = ContextCompat.getColor(mContext, R.color.app_black_1);
            builder.setTextColor(color);
            tv_product_price.setText(builder.Build());

            iv_fix_price.setVisibility(INVISIBLE);
            iv_fix_price2.setVisibility(VISIBLE);
        }
    }

    //小计
    private void account() {

        float money = mItemData.curNum * mItemData.getProductPrice();

        String res = NumberUtil.getDouble(money);

        tv_account.setText(mContext.getString(R.string.product_price2, res));

        if (callBack != null) {
            callBack.onItemClick(0);
        }
    }

    public void setCallBack(RecycleViewCallBack callBack) {
        this.callBack = callBack;
    }


    @Override
    public void onClick(View v) {
        if (mItemData == null) {
            return;
        }
        if (v.getId() == R.id.tv_product_numb) {
            inputNum();
            return;
        }

        if (v.getId() == R.id.tv_num_1) {
            inputMax(tv_num_1.getText().toString(), 0);
        } else if (v.getId() == R.id.tv_num_2) {
            inputMax(tv_num_2.getText().toString(), 1);
        } else if (v.getId() == R.id.btn_add) {
            //当前数量小于库存
            if (mItemData.curNum < mItemData.maxNum) {
                mItemData.curNum++;
                update();
            } else {
                ToastUtil.showToastShort("已是商品最大数量");
            }
        } else if (v.getId() == R.id.btn_reduce) {
            if (mItemData.curNum > 0) {
                mItemData.curNum--;
                update();
            }
            if (mItemData.curNum == 0) {
                onZeroDelete();
            }
        }
    }

    private void update() {

        int curNum = mItemData.curNum;
        int conversionRate = mItemData.conversionRate;
        boolean show = false;
        //转换比为0或1的时候隐藏最大单位的显示
        if (conversionRate == 0 || conversionRate == 1) {
            ViewHelper.setViewGone(tv_num_1, true);
            ViewHelper.setViewGone(tv_num_hint_1, true);
        } else {
            show = true;
            ViewHelper.setViewGone(tv_num_1, false);
            ViewHelper.setViewGone(tv_num_hint_1, false);
        }
        //单位显示
        if (!TextUtils.isEmpty(mItemData.unitMax)) {
            tv_num_hint_1.setText(mItemData.unitMax);
        }
        if (!TextUtils.isEmpty(mItemData.unitMin)) {
            tv_num_hint_2.setText(mItemData.unitMin);
        }
        //数字显示
        if (show) {
            int i = curNum / conversionRate;
            int m = curNum % conversionRate;
            tv_num_1.setText(String.valueOf(i));
            tv_num_2.setText(String.valueOf(m));
        } else {
            tv_num_2.setText(String.valueOf(curNum));
        }
        tv_product_numb.setText(String.valueOf(curNum));

        account();
    }

    private void inputMax(String num, final int tag) {
        AlertDialog.Builder dialog = DialogMaker.getDialog(mContext);
        dialog.setTitle("修改数量");
        View content = View.inflate(mContext, R.layout.dialog_input_num2, null);

        //输入的值
        final EditText input = (EditText) content.findViewById(R.id.ed_num);
        input.setText(num);
        input.setSelection(num.length());
        //换算比例
//        if (tag == 0) {
//            String hint = String.format("1", mItemData.unitMax, mItemData.conversionRate, mItemData.unitMin);
//            input.setHint(hint);
//        }

        dialog.setView(content);
        dialog.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String num = input.getText().toString();
                if (!TextUtils.isEmpty(num)) {
                    int curNum = 0;
                    Integer integer = Integer.valueOf(num);
                    if (tag == 0) {
                        curNum = integer * mItemData.conversionRate;
                    } else {
                        curNum = integer;
                    }
                    if (curNum > mItemData.maxNum) {
                        ToastUtil.showToastLong("超出库存数量");
                    } else {
                        if (tag == 0) {
                            //计算余数
                            int left = mItemData.curNum % mItemData.conversionRate;
                            mItemData.curNum = curNum + left;
                        } else {
                            //存在换算比例
                            if (mItemData.conversionRate > 1) {
                                int left = mItemData.curNum / mItemData.conversionRate;
                                int numa = mItemData.conversionRate * left;
                                mItemData.curNum = numa + curNum;
                            } else {
                                mItemData.curNum = curNum;
                            }

                        }
                        update();
                    }
                }
            }
        });
        dialog.setNegativeButton(R.string.dialog_cancel, null);
        dialog.setCancelable(false);
        dialog.show();
    }

    private void inputNum() {
        AlertDialog.Builder dialog = DialogMaker.getDialog(mContext);
        dialog.setTitle("修改数量");
        View content = View.inflate(mContext, R.layout.dialog_input_num, null);

        final EditText input = (EditText) content.findViewById(R.id.ed_num);
        final TextView tv_num = (TextView) content.findViewById(R.id.tv_product_stock);

        tv_num.setText(String.valueOf(mItemData.maxNum));
        String num = String.valueOf(mItemData.curNum);
        input.setText(num);
        input.setSelection(num.length());

        dialog.setView(content);
        dialog.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String num = input.getText().toString();
                if (!TextUtils.isEmpty(num)) {
                    Integer valueOf = Integer.valueOf(num);
                    if (valueOf > mItemData.maxNum) {
                        ToastUtil.showToastLong("超出库存数量");
                    } else {
                        mItemData.curNum = valueOf;
                        update();
                    }
                }
            }
        });
        dialog.setNegativeButton(R.string.dialog_cancel, null);
        dialog.setCancelable(false);
        dialog.show();
    }

    //长按删除
    private void onDeleteData() {
        if (callBack instanceof RecycleViewCallBackWithData) {

            final RecycleViewCallBackWithData back = (RecycleViewCallBackWithData) callBack;
            AlertDialog.Builder dialog = DialogMaker.getDialog(mContext);
            dialog.setTitle(R.string.dialog_title);
            dialog.setMessage("您确定删除该商品?");
            dialog.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    back.onClickData(mItemData);
                }
            });
            dialog.setNegativeButton(R.string.dialog_cancel, null);
            dialog.show();
        }
    }

    //商品数量为0的时候提示删除
    private void onZeroDelete() {
        if (callBack instanceof RecycleViewCallBackWithData) {
            final RecycleViewCallBackWithData back = (RecycleViewCallBackWithData) callBack;
            AlertDialog.Builder dialog = DialogMaker.getDialog(mContext);
            dialog.setTitle(R.string.dialog_title);
            dialog.setMessage("当前商品数量为0,是否删除该商品?");
            dialog.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    back.onClickData(mItemData);
                }
            });
            dialog.setNegativeButton(R.string.dialog_cancel, null);
            dialog.show();
        }
    }

    private void onFixPrice() {
        AlertDialog.Builder dialog = DialogMaker.getDialog(mContext);
        dialog.setTitle("修改单品价格");
        View content = View.inflate(mContext, R.layout.dialog_fix_price, null);
        TextView money = (TextView) content.findViewById(R.id.tv_origin_price);
        money.setText(mContext.getString(R.string.price2, mItemData.productPrice, mItemData.unitMin));
        //输入的值
        final EditText input = (EditText) content.findViewById(R.id.ed_price);

        dialog.setView(content);
        dialog.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String num = input.getText().toString();
                if (TextUtils.isEmpty(num)) {
                    num = "0";
                }
                mItemData.productFixPrice = Float.valueOf(num);

                showPrice();

                account();
            }
        });
        dialog.setNegativeButton(R.string.dialog_cancel, null);
        dialog.setCancelable(false);
        dialog.show();
    }
}
