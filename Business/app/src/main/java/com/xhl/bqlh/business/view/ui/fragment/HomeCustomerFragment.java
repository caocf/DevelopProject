package com.xhl.bqlh.business.view.ui.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVAnalytics;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.xhl.bqlh.business.Model.App.RegisterModel;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.doman.CustomerHelper;
import com.xhl.bqlh.business.utils.SnackUtil;
import com.xhl.bqlh.business.view.base.BaseAppFragment;
import com.xhl.bqlh.business.view.helper.DialogMaker;
import com.xhl.bqlh.business.view.ui.activity.CustomerQueryActivity;
import com.xhl.bqlh.business.view.ui.activity.CustomerQueryFriendsActivity;
import com.xhl.bqlh.business.view.ui.activity.SelectShopLocationActivity;
import com.xhl.bqlh.business.view.ui.activity.SelectShopTypeActivity;
import com.xhl.xhl_library.utils.BitmapUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by Sum on 16/5/15.
 */
@ContentView(R.layout.fragment_home_customer)
public class HomeCustomerFragment extends BaseAppFragment implements Toolbar.OnMenuItemClickListener {

    //门店照
    private static final int Req_camera_shop = 10;
    //营业执照
    private static final int Req_camera_license = 11;
    //定位
    private static final int Req_Location = 12;
    //店铺类型
    private static final int Req_shop_type = 13;

    @ViewInject(R.id.iv_pic_shop)
    private ImageView iv_pic_shop;

    @ViewInject(R.id.iv_pic_license)
    private ImageView iv_pic_license;

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
        inputDialogShow(tv_input_login_name, R.string.customer_shop_login_name);
    }

    @Event(R.id.tv_input_details_address)
    private void onShopInputAddressClick(View view) {
        inputDialogShow(tv_input_details_address, R.string.customer_shop_address);
    }

    @Event(R.id.tv_input_maker_location)
    private void onShopMakerClick(View view) {
        startActivityForResult(new Intent(getContext(), SelectShopLocationActivity.class), Req_Location);
    }

    @Event(R.id.tv_input_shop_type)
    private void onShopTypeClick(View view) {
        startActivityForResult(new Intent(getContext(), SelectShopTypeActivity.class), Req_shop_type);
    }

    @Event(R.id.btn_confirm)
    private void onConfirmClick(View view) {
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
            //店铺名称
            tv_input_shop_name.setText(R.string.customer_input_hint);
            setTextColor(tv_input_shop_name);
            //负责人
            tv_input_user.setText(R.string.customer_input_hint);
            setTextColor(tv_input_user);
            //负责人手机号
            tv_input_phone.setText(R.string.customer_input_hint);
            setTextColor(tv_input_phone);
            //营业执照号
            tv_input_license_id.setText(R.string.customer_input_hint);
            setTextColor(tv_input_license_id);
            //收货地址
            tv_input_address.setText(R.string.customer_input_hint);
            setTextColor(tv_input_address);
            //收货详情地址
            tv_input_details_address.setText(R.string.customer_input_hint);
            setTextColor(tv_input_details_address);
            //登陆用户名
            tv_input_login_name.setText(R.string.customer_input_hint);
            setTextColor(tv_input_login_name);
            //标记
            tv_input_maker_location.setText(R.string.customer_shop_location_mark);
            setTextColor(tv_input_maker_location);
            tv_input_shop_type.setText("请选择");
            setTextColor(tv_input_shop_type);
            //pic
            iv_pic_shop.setImageResource(R.drawable.ic_new_cus_add);
            iv_pic_license.setImageResource(R.drawable.ic_new_cus_add);

            //重新创建数据
            mRegister = new RegisterModel();

            SnackUtil.shortShow(mToolbar, "提交审核中");

            AVAnalytics.onEvent(getContext(), "add new customer");

        } else if (type == CustomerHelper.TYPE_REGISTER_FAILED) {
            AlertDialog.Builder dialog = DialogMaker.getDialog(getContext());
            dialog.setTitle(R.string.dialog_title);
            dialog.setMessage((String) obj);
            dialog.setPositiveButton(R.string.dialog_ok, null);
            dialog.show();
        }
    }

    private void setTextColor(TextView textView) {
        textView.setTextColor(ContextCompat.getColor(getContext(), R.color.base_disable_text_color));
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
        AlertDialog.Builder builder = DialogMaker.getDialog(getContext());
        builder.setTitle(titleId);
        View content = View.inflate(getContext(), R.layout.dialog_input_message, null);
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
                    itemView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
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

    @Override
    protected void initParams() {
        super.initHomeToolbar();
        //菜单初始化
        mToolbar.inflateMenu(R.menu.user_menu_customer);
        mToolbar.setOnMenuItemClickListener(this);
        mToolbar.setTitle(R.string.user_nav_main_add_new_customer);
        mHelper = new CustomerHelper(this);
        mRegister = new RegisterModel();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_apply_friends) {
            startActivity(new Intent(getContext(), CustomerQueryFriendsActivity.class));
        } else {
            startActivity(new Intent(getContext(), CustomerQueryActivity.class));
        }
        return true;
    }

    private void checkCamera(int reqType) {
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
                tv_input_shop_type.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                mRegister.companyTypeId = id;
                break;
            case Req_Location:
                mAddress = data.getParcelableExtra(SelectShopLocationActivity.ADDRESS);
                CharSequence text = TextUtils.concat(mAddress.province, mAddress.city, mAddress.district, mAddress.street);
                mRegister.companyAddress = (String) text;
                //收货地址
                tv_input_address.setText(text);
                tv_input_address.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                mRegister.location = (String) text;      //定位地址
                //标记状态
                tv_input_maker_location.setText("已标记");
                tv_input_maker_location.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPhotoHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
