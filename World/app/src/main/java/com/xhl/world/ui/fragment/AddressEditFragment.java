package com.xhl.world.ui.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xhl.world.R;
import com.xhl.world.api.ApiControl;
import com.xhl.world.model.AddressModel;
import com.xhl.world.model.Base.ResponseModel;
import com.xhl.world.ui.utils.IDCard;
import com.xhl.world.ui.utils.SnackMaker;
import com.xhl.xhl_library.ui.view.CityPopupWindow;
import com.xhl.xhl_library.ui.view.RippleView;
import com.xhl.xhl_library.utils.LogonUtils;
import com.xhl.xhl_library.utils.log.Logger;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.HashMap;

/**
 * Created by Sum on 15/12/28.
 */
@ContentView(R.layout.fragment_add_address)
public class AddressEditFragment extends BaseAppFragment implements CityPopupWindow.OnWheelItemSelected, Callback.CommonCallback<ResponseModel<AddressModel>> {
    @ViewInject(R.id.title_name)
    private TextView title_name;

    @ViewInject(R.id.title_other)
    private Button title_edit;

    @ViewInject(R.id.ed_address_username)
    private EditText ed_address_username;//输入用户名

    @ViewInject(R.id.ed_address_phone)
    private EditText ed_address_phone;//输入手机号

    @ViewInject(R.id.ed_address_user_cardId)
    private EditText ed_address_user_cardId;//身份证号

    @ViewInject(R.id.ed_address_city)
    private TextView ed_address_city;//选择所在城市

    @ViewInject(R.id.ed_address_address_details)
    private EditText ed_address_address_details; //输入详细地址

    @ViewInject(R.id.ed_address_num)
    private EditText ed_address_num;//邮编号

    @Event(R.id.ed_address_city)
    private void onSelectCityClick(View view) {
        mPopup = new CityPopupWindow(getContext(), this);
        mPopup.show();
    }

    @Event(value = R.id.title_back, type = RippleView.OnRippleCompleteListener.class)
    private void onBackClick(View view) {
        getSumContext().popTopFragment(null);
    }

    @Event(R.id.title_other)//保存用户信息
    private void onSaveClick(View view) {
        saveInfo();
    }

    private AddressModel mAddress;

    private CityPopupWindow mPopup;

    private boolean mEditStyle = false;//是否是编辑模式

    @Override
    protected void initParams() {
        title_name.setText("添加地址");
        title_edit.setText("保存");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAddress != null && mEditStyle) {
            title_name.setText("编辑地址");
            ed_address_username.setText(mAddress.getConsigneeName());
            ed_address_phone.setText(mAddress.getTelephone());
            ed_address_address_details.setText(mAddress.getAddress());
            ed_address_user_cardId.setText(mAddress.getIdCard());
            ed_address_city.setText(mAddress.getArea());
            ed_address_num.setText(mAddress.getPostCode());
        }
    }


    private void saveInfo() {
        String name = getEditText(ed_address_username);
        if (TextUtils.isEmpty(name)) {
            SnackMaker.shortShow(title_name,"收货人不能为空");
            return;
        }
        String phone = getEditText(ed_address_phone);
        if (TextUtils.isEmpty(phone) || !LogonUtils.matcherLogonPhone(phone)) {
            SnackMaker.shortShow(title_name,"手机号格式错误");
            return;
        }
        String idCard = getEditText(ed_address_user_cardId);
        if (TextUtils.isEmpty(idCard)) {
            SnackMaker.shortShow(title_name,"身份证号不能为空");
            return;
        }
        IDCard cc = new IDCard();
        String mesage = null;
        try {
            mesage = cc.IDCardValidate(idCard);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!"".equals(mesage)) {
            SnackMaker.longShow(title_name,mesage);
            ed_address_user_cardId.requestFocus();
            return;
        }

        String area = ed_address_city.getText().toString();
        if (TextUtils.isEmpty(area)) {
            SnackMaker.shortShow(title_name,"所在区域不能为空");
            return;
        }
        String addressDetails = getEditText(ed_address_address_details);
        if (addressDetails.length() <= 6) {
            SnackMaker.shortShow(title_name,"详细地址不能少于6个字符哦~");
            return;
        }
        String code = getEditText(ed_address_num);

        if (mAddress == null) {
            mAddress = new AddressModel();
            //添加地址非默认
            mAddress.setDefaultAddress("1");
        }
        mAddress.setPostCode(code);
        mAddress.setIdCard(idCard);
        mAddress.setConsigneeName(name);
        mAddress.setArea(area);
        mAddress.setAddress(addressDetails);
        mAddress.setTelephone(phone);

        showLoadingDialog();

        if (mEditStyle) {
            updateToService();
        } else {
            saveToService();
        }
    }

    //更新地址
    private void updateToService() {
        ApiControl.getApi().addressUpdate(mAddress, this);
    }

    //添加地址
    private void saveToService() {
        ApiControl.getApi().addressAdd(mAddress, this);
    }


    private String getEditText(EditText editText) {
        return editText.getText().toString().trim();
    }

    @Override
    public void onEnter(Object data) {
        super.onEnter(data);
        if (mDataIn != null && mDataIn instanceof AddressModel) {
            mAddress = (AddressModel) mDataIn;
            mEditStyle = true;
        }
    }

    @Override
    public void onWheelSelect(HashMap<String, String> data) {
        Logger.v(data.toString());
        //{area=建邺区, code=210000, province=江苏省, city=南京市}
        StringBuilder builder = new StringBuilder();
        builder.append(data.get("province")).append(",").append(data.get("city")).append(",").append(data.get("area"));
        ed_address_city.setText(builder.toString());
        String code = data.get("code");
        if (!TextUtils.isEmpty(code)) {
            ed_address_num.setText(code);
        }
    }

    @Override
    public void onSuccess(ResponseModel<AddressModel> result) {
        if (result.isSuccess()) {

            String op = result.getResultObj().getResult();
            if (!TextUtils.isEmpty(op) && op.equals("200")) {
                if (mEditStyle) {
                    SnackMaker.shortShow(title_name,"修改地址完成");
                } else {
                    SnackMaker.shortShow(title_name,"地址添加成功");
                }

                getSumContext().popTopFragment(null);
            }

        } else {
            SnackMaker.shortShow(title_name,result.getMessage());
        }
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {

    }

    @Override
    public void onCancelled(CancelledException cex) {

    }

    @Override
    public void onFinished() {
        hideLoadingDialog();
    }
}
