package com.xhl.bqlh.business.view.ui.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.xhl.bqlh.business.Api.ApiControl;
import com.xhl.bqlh.business.AppConfig.GlobalParams;
import com.xhl.bqlh.business.AppConfig.NetWorkConfig;
import com.xhl.bqlh.business.Model.App.RegisterModel;
import com.xhl.bqlh.business.Model.Base.ResponseModel;
import com.xhl.bqlh.business.Model.ShopApplyModel;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.doman.CustomerHelper;
import com.xhl.bqlh.business.utils.SnackUtil;
import com.xhl.bqlh.business.view.base.BaseAppActivity;
import com.xhl.bqlh.business.view.custom.LifeCycleImageView;
import com.xhl.bqlh.business.view.helper.DialogMaker;
import com.xhl.xhl_library.utils.BitmapUtil;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by Sum on 16/5/22.
 */
@ContentView(R.layout.activity_customer_update)
public class CustomerUpdateActivity extends BaseAppActivity {


    public static final String TAG_UPDATE_TYPE = "update_type";
    public static final int TYPE_EXIT_INFO = 0;//传递数据
    public static final int TYPE_GET_INFO = 1;//服务的获取数据


    //门店照
    private static final int Req_camera_shop = 10;
    //营业执照
    private static final int Req_camera_license = 11;
    //定位
    private static final int Req_Location = 12;
    //店铺类型
    private static final int Req_shop_type = 13;

    @ViewInject(R.id.iv_pic_shop)
    private LifeCycleImageView iv_pic_shop;

    @ViewInject(R.id.iv_pic_license)
    private LifeCycleImageView iv_pic_license;

    @ViewInject(R.id.ll_content)
    private View ll_content;

    @ViewInject(R.id.tv_input_shop_name)
    private TextView tv_input_shop_name;

    @ViewInject(R.id.tv_input_user)
    private TextView tv_input_user;

    @ViewInject(R.id.tv_input_phone)
    private TextView tv_input_phone;

    @ViewInject(R.id.tv_input_license_id)
    private TextView tv_input_license_id;

    @ViewInject(R.id.tv_input_maker_location)
    private TextView tv_input_maker_location;

    @ViewInject(R.id.tv_input_address)
    private TextView tv_input_address;

    @ViewInject(R.id.tv_input_details_address)
    private TextView tv_input_details_address;

    @ViewInject(R.id.tv_input_shop_type)
    private TextView tv_input_shop_type;

    @ViewInject(R.id.tv_input_login_name)
    private TextView tv_input_login_name;

    @ViewInject(R.id.btn_confirm)
    private View btn_confirm;

    @Event(R.id.iv_pic_shop)
    private void onShopClick(View view) {
        checkCamera(Req_camera_shop);
    }

    @Event(R.id.iv_pic_license)
    private void onLicenseClick(View view) {
        checkCamera(Req_camera_license);
    }

    @Event(R.id.tv_input_shop_name)
    private void onShopNameClick(View view) {
        inputDialogShow(tv_input_shop_name, R.string.customer_shop_name);
    }

    @Event(R.id.tv_input_user)
    private void onShopUserClick(View view) {
        inputDialogShow(tv_input_user, R.string.customer_shop_user);
    }

    @Event(R.id.tv_input_phone)
    private void onShopPhoneClick(View view) {
        inputDialogShow(tv_input_phone, R.string.customer_shop_phone);
    }

    @Event(R.id.tv_input_license_id)
    private void onShopLicenseIdClick(View view) {
        inputDialogShow(tv_input_license_id, R.string.customer_shop_license_id);
    }

    @Event(R.id.tv_input_address)
    private void onShopInputLocationClick(View view) {
        inputDialogShow(tv_input_address, R.string.customer_shop_location_text);
    }

    @Event(R.id.tv_input_login_name)
    private void onShopInputNameClick(View view) {
        if (!mRegister.fixInfo) {
            inputDialogShow(tv_input_login_name, R.string.customer_shop_login_name);
        }
    }

    @Event(R.id.tv_input_details_address)
    private void onShopInputAddressClick(View view) {
        inputDialogShow(tv_input_details_address, R.string.customer_shop_address);
    }

    @Event(R.id.tv_input_maker_location)
    private void onShopMakerClick(View view) {
        if (mEditEnable) {
            startActivityForResult(new Intent(this, SelectShopLocationActivity.class), Req_Location);
        }
    }

    @Event(R.id.tv_input_shop_type)
    private void onShopTypeClick(View view) {
        if (mEditEnable) {
            startActivityForResult(new Intent(this, SelectShopTypeActivity.class), Req_shop_type);
        }
    }

    @Event(R.id.btn_confirm)
    private void onConfirmClick(View view) {
        if (!mEditEnable)
            return;
        AlertDialog.Builder dialog = DialogMaker.getDialog(this);
        dialog.setTitle(R.string.dialog_title);
        dialog.setMessage("您确定修改零售商信息?");
        dialog.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateInfo();
            }
        });
        dialog.setNegativeButton(R.string.dialog_cancel, null);
        dialog.show();
    }

    private void updateInfo() {
        //获取输入值
        mRegister.companyName = getTextView(tv_input_shop_name);
        mRegister.liableName = getTextView(tv_input_user);
        mRegister.liablePhone = getTextView(tv_input_phone);
        mRegister.businessLicenceId = getTextView(tv_input_license_id);
        mRegister.loginUserName = getTextView(tv_input_login_name);
        mRegister.location = getTextView(tv_input_address);
        mRegister.address = getTextView(tv_input_details_address);

        mHelper.confirmShop();
    }

    @Override
    public Object getValue(int type) {
        return mRegister;
    }

    @Override
    public void showValue(int type, Object obj) {
        super.showValue(type, obj);
        //清理注册信息
        if (type == CustomerHelper.TYPE_REGISTER_SUCCESS) {
            SnackUtil.shortShow(mToolbar, "修改成功");
            mToolbar.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent data = new Intent();
                    setResult(RESULT_OK, data);
                    finish();
                }
            }, 800);
        } else if (type == CustomerHelper.TYPE_REGISTER_FAILED) {
            AlertDialog.Builder dialog = DialogMaker.getDialog(CustomerUpdateActivity.this);
            dialog.setTitle(R.string.dialog_title);
            dialog.setMessage((String) obj);
            dialog.setPositiveButton(R.string.dialog_ok, null);
            dialog.show();
        } else if (type == CustomerHelper.TYPE_FIX_INFO_SUCCESS) {
            SnackUtil.shortShow(mToolbar, "提交审核中");
            mToolbar.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 1000);
        }
    }

    private String getTextView(TextView itemView) {
        String hint = getString(R.string.customer_input_hint);
        String hint1 = getString(R.string.customer_input_hint1);
        String text = itemView.getText().toString().trim();
        if (!text.equals(hint) && !text.equals(hint1)) {
            return text;
        }
        return null;
    }

    private void inputDialogShow(final TextView itemView, int titleId) {
        if (!mEditEnable) {
            return;
        }
        AlertDialog.Builder builder = DialogMaker.getDialog(this);
        builder.setTitle(titleId);
        View content = View.inflate(this, R.layout.dialog_input_message, null);
        final EditText editText = (EditText) content.findViewById(R.id.ed_input);
        String text = itemView.getText().toString();
        String hint = getString(R.string.customer_input_hint);
        String hint1 = getString(R.string.customer_input_hint1);
        if (!text.equals(hint) && !text.equals(hint1)) {
            editText.setText(text);
            editText.setSelection(text.length());
        }
        builder.setView(content);
        builder.setNegativeButton(R.string.dialog_cancel, null);
        builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String trim = editText.getEditableText().toString().trim();
                if (!TextUtils.isEmpty(trim)) {
                    itemView.setText(trim);
                    itemView.setTextColor(ContextCompat.getColor(CustomerUpdateActivity.this, R.color.colorPrimary));
                }
            }
        });
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private RegisterModel mRegister;
    private ReverseGeoCodeResult.AddressComponent mAddress;
    private CustomerHelper mHelper;

    private ShopApplyModel mShopInfo;
    private boolean mEditEnable = false;
    private boolean mFixShopInfo = false;

    @Override
    protected void initParams() {
        super.initToolbar();
        int intExtra = getIntent().getIntExtra(TAG_UPDATE_TYPE, 0);
        if (intExtra == TYPE_EXIT_INFO) {
            mShopInfo = (ShopApplyModel) getIntent().getSerializableExtra("data");
            setTitle(mShopInfo.getCompanyName());
            loadInfo();
        } else if (intExtra == TYPE_GET_INFO) {
            setTitle(R.string.shop_detail_update_info);
            String shopId = getIntent().getStringExtra(GlobalParams.Intent_shop_id);
            getShopInfo(shopId);
        }
    }

    //加载数据
    private void loadInfo() {
        mHelper = new CustomerHelper(this);
        mRegister = new RegisterModel();
        mRegister.fixInfo = mFixShopInfo;
        //update的非显示信息
        mRegister.uid = mShopInfo.getUid();
        mRegister.areaId = mShopInfo.getAreaId();
        mRegister.retailerImg = mShopInfo.getRetailerImg();
        mRegister.businessLicence = mShopInfo.getBusinessLicence();
        mRegister.companyTypeId = mShopInfo.getCompanyTypeId();
        mRegister.coordinateY = mShopInfo.getCoordinateY();
        mRegister.coordinateX = mShopInfo.getCoordinateX();

        int shstate = mShopInfo.getState();
        //待审核状态可以修改
        if (shstate == 0) {
            mEditEnable = true;
            btn_confirm.setVisibility(View.VISIBLE);
        } else {
            mEditEnable = false;
        }

        showData();
    }

    private void showData() {
        //店铺名称
        tv_input_shop_name.setText(mShopInfo.getCompanyName());
        //负责人
        tv_input_user.setText(mShopInfo.getLiableName());
        //负责人手机号
        tv_input_phone.setText(mShopInfo.getLiablePhone());
        //营业执照号
        tv_input_license_id.setText(mShopInfo.getBusinessLicenceId());
        //收货地址
        tv_input_address.setText(mShopInfo.getArea());
        //收货详情地址
        tv_input_details_address.setText(mShopInfo.getAddress());
        //登陆用户名
        tv_input_login_name.setText(mShopInfo.getLoginUserName());
        if (mRegister.coordinateY == 0 || mRegister.coordinateX == 0) {
            tv_input_maker_location.setText(R.string.customer_shop_location_mark);
        } else {
            tv_input_maker_location.setText("已标记");
        }
        //门店类型
        tv_input_shop_type.setText(mShopInfo.getCompanyTypeName());

        //图片
        iv_pic_license.bindImageUrl(NetWorkConfig.imageHost + mShopInfo.getBusinessLicence());
        iv_pic_shop.bindImageUrl(NetWorkConfig.imageHost + mShopInfo.getRetailerImg());
    }

    private void checkCamera(int reqType) {
        if (!mEditEnable) {
            return;
        }
        mPhotoHelper.capture(reqType);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case Req_camera_shop:
                String path = mPhotoHelper.getPhoto().getPath();
                iv_pic_shop.setImageBitmap(BitmapUtil.readBitmapForFixMaxSize(path));
                mRegister.retailerImgPath = path;
                mRegister.retailerImg = null;
                break;
            case Req_camera_license:
                String pathLic = mPhotoHelper.getPhoto().getPath();
                iv_pic_license.setImageBitmap(BitmapUtil.readBitmapForFixMaxSize(pathLic));
                mRegister.businessLicencePath = pathLic;
                mRegister.businessLicence = null;
                break;
            case Req_shop_type:
                String id = data.getStringExtra("id");
                String name = data.getStringExtra("name");
                tv_input_shop_type.setText(name);
                tv_input_shop_type.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                mRegister.companyTypeId = id;
                break;
            case Req_Location:
                mAddress = data.getParcelableExtra(SelectShopLocationActivity.ADDRESS);
                CharSequence text = TextUtils.concat(mAddress.province, mAddress.city, mAddress.district, mAddress.street);
                mRegister.companyAddress = (String) text;
                //收货地址
                tv_input_address.setText(text);
                tv_input_address.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                mRegister.location = (String) text;      //定位地址
                //标记状态
                tv_input_maker_location.setText("已标记");
                tv_input_maker_location.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));

                //位置信息
                mRegister.province = mAddress.province;
                mRegister.city = mAddress.city;
                mRegister.district = mAddress.district;
                mRegister.street = mAddress.street;
                LatLng latLng = data.getParcelableExtra(SelectShopLocationActivity.LOCATION);
                mRegister.coordinateX = latLng.longitude;
                mRegister.coordinateY = latLng.latitude;

                mHelper.loadArea();
                break;
        }
    }

    private void getShopInfo(String shopId) {

        ll_content.setVisibility(View.INVISIBLE);

        showLoadingDialog();

        ApiControl.getApi().registerQueryRetailerById(shopId, new Callback.CommonCallback<ResponseModel<ShopApplyModel>>() {
            @Override
            public void onSuccess(ResponseModel<ShopApplyModel> result) {
                if (result.isSuccess()) {
                    mShopInfo = result.getObj();
                    mShopInfo.setState(0);
                    mFixShopInfo = true;
                    loadInfo();
                    ll_content.setVisibility(View.VISIBLE);
                } else {
                    SnackUtil.shortShow(mToolbar, result.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                SnackUtil.shortShow(mToolbar, ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                hideLoadingDialog();
            }
        });
    }

}
