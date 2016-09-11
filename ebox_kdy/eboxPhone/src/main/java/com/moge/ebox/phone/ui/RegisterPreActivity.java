package com.moge.ebox.phone.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.moge.ebox.phone.R;
import com.moge.ebox.phone.bettle.base.EboxApplication;
import com.moge.ebox.phone.bettle.network.http.ApiClient;
import com.moge.ebox.phone.bettle.network.http.ApiClient.ClientCallback;
import com.moge.ebox.phone.bettle.network.http.ApiClient.UploadCallback;
import com.moge.ebox.phone.bettle.network.http.CommonValue;
import com.moge.ebox.phone.bettle.tools.Logger;
import com.moge.ebox.phone.bettle.tools.UIHelper;
import com.moge.ebox.phone.bettle.utils.ViewUtils;
import com.moge.ebox.phone.config.GlobalConfig;
import com.moge.ebox.phone.ui.adapter.BaseListAdapter;
import com.moge.ebox.phone.ui.customview.Head;
import com.moge.ebox.phone.ui.customview.Head.HeadData;
import com.moge.ebox.phone.ui.customview.RoundedImageView;
import com.moge.ebox.phone.utils.IDCard;
import com.moge.ebox.phone.utils.ToastUtil;
import com.moge.ebox.phone.utils.UmengUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import tools.StringUtils;

public class RegisterPreActivity extends BaseActivity {
    public static final int tag_register = 0;// 注册
    public static final int tag_perfect = 1;// 完善
    private Context mContext;
    private Head mHead;
    private int mTag;
    private RelativeLayout rl_take_face;
    //step1
    private LinearLayout step1;
    private EditText medName, medIdCard, et_site_name, et_site_loc;
    private TextView tv_area, tv_company;
    private Spinner spinner_company, spinner_area;
    private companyAdapter adapterCompany, adapterArea;
    private String company_id;
    private int region_id;
    private ArrayList<HashMap<String, String>> mCompanyData, mProvinceData, mCityData, mAreaData;
    ;
    private Button mtbNext;

    private LinearLayout step2;
    private Button mtbRegister, mbtTackPic;
    private RoundedImageView iv_head_icon;
    private ImageView iv_camera;
    private Uri mUri;
    private boolean hasPhoto;
    private String faceServicePath = "";
    //    private Integer faceServiceId = -1;
    private String faceServiceId;
    private String mVerify, mPassword, mTel;// 注册传递信息

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_pre);
        mContext = RegisterPreActivity.this;
        hasPhoto = false;
        getData();
        initView();
    }

    private void getData() {
        Intent intent = getIntent();
        mTag = intent.getIntExtra("tag", -1);
        if (mTag == tag_register) {
            mVerify = intent.getStringExtra("verify");
            mTel = intent.getStringExtra("tel");
            mPassword = intent.getStringExtra("password");
        } else if (mTag == tag_perfect) // 完善信息
        {
            mTel = intent.getStringExtra("tel");
            getOperatorInfor();
        }
    }

    private void getOperatorInfor() {
        Map params = new HashMap();
//        params.put("tel", mTel);
        /**
         * 获得快递员详细信息
         */
        ApiClient.getOperatorInfor(EboxApplication.getInstance(), params,
                new ClientCallback() {
                    @Override
                    public void onSuccess(JSONArray dataPre, int code) {
                        Logger.i("RegisterPre:getOperatorInfor+" + dataPre.toString());
                        try {
                            JSONObject data = dataPre.getJSONObject(0).getJSONObject("user");

                            if (!data.isNull("orgnization_id")) {
                                String orgnization_id = data.getString("orgnization_id");
                                company_id = orgnization_id;
                                if (!data.isNull("orgnization_name")) {
                                    String orgnization_name = data.getString("orgnization_name");
                                    tv_company.setText(orgnization_name);
                                }

                                if (!data.isNull("region_id")) {
                                    region_id = data.getInt("region_id");
                                    if (!data.isNull("region_name")) {
                                        String region_name = data.getString("region_name");
                                        tv_area.setText(region_name);
                                    }
                                }
                                if (!data.isNull("operator_name")) {
                                    String operator_name = data.getString("operator_name");
                                    medName.setText(operator_name);
                                }

                                if (!data.isNull("identity")) {
                                    String identity = data.getString("identity");
                                    medIdCard.setText(identity);
                                }
                                if (!data.isNull("site_name")) {
                                    String site_name_str = data.getString("site_name");
                                    et_site_name.setText(site_name_str);
                                }
                                if (!data.isNull("site_loc")) {
                                    String site_loc_str = data.getString("site_loc");
                                    et_site_loc.setText(site_loc_str);
                                }
                                if (!data.isNull("face_id")) {
                                    faceServiceId = data.getString("face_id");
                                    String u = CommonValue.PIC_API + faceServiceId;
                                    Logger.i("Register:face_id" + CommonValue.PIC_API + faceServiceId);
                                    //设置图片不为空状态   设置
                                    hasPhoto = true;
                                    mbtTackPic.setText(getResources().getString(R.string.confim_tack_pic_reset));
                                    ImageLoader.getInstance().displayImage(u, iv_head_icon);
                                }


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailed(byte[] data, int code) {
                    }
                });
    }


    private void initView() {
//		rl_take_face = findviewById_(R.id.rl_take_face);
//		rl_take_face.setOnClickListener(mClickListener);
        //step1
        step1 = findviewById_(R.id.step1);
        medName = findviewById_(R.id.et_name);
        medIdCard = findviewById_(R.id.et_id);
        tv_area = findviewById_(R.id.tv_area);
        tv_area.setOnClickListener(mClickListener);
        tv_company = findviewById_(R.id.tv_company);
        tv_company.setOnClickListener(mClickListener);
        spinner_company = findviewById_(R.id.spinner_company);
        spinner_area = findviewById_(R.id.spinner_area);
        et_site_name = findviewById_(R.id.et_site_name);
        et_site_loc = findviewById_(R.id.et_site_loc);
        mtbNext = findviewById_(R.id.btn_next);
        mtbNext.setOnClickListener(mClickListener);

        //step2
        step2 = findviewById_(R.id.step2);
        iv_head_icon = findviewById_(R.id.iv_head_icon);
        iv_head_icon.setOnClickListener(mClickListener);
        iv_camera = findviewById_(R.id.iv_camera);
        iv_camera.getBackground().setAlpha(180);
        mtbRegister = findviewById_(R.id.btn_register);
        mtbRegister.setOnClickListener(mClickListener);
        mbtTackPic = findviewById_(R.id.btn_tack_pic);
        mbtTackPic.setOnClickListener(mClickListener);
        initHead();
        stepSwitch(1);
    }

    //1:公司资料；2：拍照
    private void stepSwitch(int i) {
        HeadData data = mHead.new HeadData();
        switch (i) {
            case 1:
                step1.setVisibility(View.VISIBLE);
                step2.setVisibility(View.GONE);
                data.backVisibility = 1;
                data.tvVisibility = true;
                data.tvContent = getResources().getString(R.string.register_pre);
                mHead.setData(data, this);
                break;
            case 2:
                step1.setVisibility(View.GONE);
                step2.setVisibility(View.VISIBLE);
                data.backVisibility = 2;
                data.tvVisibility = true;
                data.tvContent = getResources().getString(R.string.register_pre);
                mHead.setZCTitleListner(new Head.ZCTitleListener() {
                    @Override
                    public void onOperateBtnClick(int index) {
                        if (index == 1) {
                            stepSwitch(1);
                        }
                    }
                });
                mHead.setData(data, this);

                break;
        }
    }

    private void initHead() {
        mHead = findviewById_(R.id.title);
        HeadData data = mHead.new HeadData();
        data.backVisibility = 1;
        data.tvVisibility = true;
        data.tvContent = getResources().getString(R.string.register_pre);
        mHead.setData(data, this);
    }

    private OnClickListener mClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_register:
                    onConfimAction();
                    break;
                case R.id.btn_next:
                    if (checkInput()) {
                        stepSwitch(2);
                    }
                    break;
                case R.id.btn_tack_pic:
                    showCamer(null);
                    break;
                case R.id.iv_head_icon:
                    ShowPickDialog();
                    break;
                case R.id.tv_area:
                    //显示选择城市
                    showArea();
                    break;
                case R.id.tv_company:
                    showCommpany();
                    break;
            }
        }
    };

    private ProgressDialog dialog;
    private boolean isFirstUploadFace = true;

    private void onConfimAction() {
        Logger.i("");
        // umeng event
        UmengUtil.statRegisterEvent(mContext, mTel);
        if (checkImage()) {
            dialog = UIHelper.showProgress(mContext, "", "提交信息中,请稍等...", false);
            Logger.i("pic--------");
            if (isFirstUploadFace) {
                Logger.i("pic--------isFirstUploadFace");
                /**
                 * 上传图片接口
                 *
                 * 图片上传成功，便进行注册接口或者完善资料接口的调用
                 */
                ApiClient.uploadFace(EboxApplication.getInstance(),
                        getMyCachePicFile(mTel), new UploadCallback() {
                            @Override
                            public void onSuccess(String fileHash) {
                                Logger.i("pic--------OK");
//                                faceServicePath = filePath;
                                faceServiceId = fileHash;

                                if (faceServiceId != null) {
                                    isFirstUploadFace = false;
                                    Logger.i("pic--------OK2");
                                    confim();
                                } else {
                                    Logger.i("pic--------EEEE");
                                    ToastUtil.showToastShort("图片上传异常，请重新设置头像");
                                    isFirstUploadFace = true;
                                }
                            }

                            @Override
                            public void onFailed(byte[] data, int code) {
                                Logger.i("pic--------error" + code);
                                isFirstUploadFace = true;
                                UIHelper.dismissProgress(dialog);
                            }
                        });
            } else {
                if (faceServiceId != null) {
                    confim();
                } else {
                    ToastUtil.showToastShort("图片上传异常，请重新设置头像");
                }
            }

        }
    }


    //    private ArrayList<HashMap<String, String>> mCompanyData, mProvinceData,mCityData,mAreaData;
    protected void showArea() {
        Intent selectAreaIntent = new Intent(RegisterPreActivity.this, SelectAreaActivity.class);
        startActivityForResult(selectAreaIntent, GlobalConfig.LOGIC_SELECT_ORIGINATION);
    }

    protected void showCommpany() {
        Intent selectAreaIntent = new Intent(RegisterPreActivity.this, SelectCompanyActivity.class);
        startActivityForResult(selectAreaIntent, GlobalConfig.LOGIC_SELECT_COMPANY);
    }


    @SuppressWarnings("unchecked")
    /**
     * 传递注册的用户信息
     */
    private void confim() {
        Map params = new HashMap();
        params.put("opname", getText(medName));
        params.put("id_card", getText(medIdCard));
        params.put("region_id", region_id);
        params.put("org_id", company_id);
        params.put("face_id", faceServiceId);
        params.put("site_name", getText(et_site_name));
        params.put("site_loc", getText(et_site_loc));

        if (mTag == tag_register) // 注册
        {
            params.put("username", mTel);
            params.put("verify", mVerify);
//            params.put("password", MD5Util.getMD5String(mPassword));
            params.put("password", mPassword);
            register(params);
        } else if (mTag == tag_perfect)// 完善
        {
            prefercet(params);
        }
    }

    private void prefercet(Map params) {
        /**
         * 完善资料
         */
        ApiClient.replenishOperator(EboxApplication.getInstance(), params,
                new ClientCallback() {

                    @Override
                    public void onSuccess(JSONArray data, int code) {
                        UIHelper.dismissProgress(dialog);
                        String msg = "提交成功,我们会在3个工作日审核您的申请，请耐心等待！";
                        success(msg);
                    }

                    @Override
                    public void onFailed(byte[] data, int code) {
                        UIHelper.dismissProgress(dialog);
                        mtbRegister.setEnabled(false);
                        exit();
                    }
                });
    }

    /**
     * 注册
     *
     * @param params
     */
    private void register(Map params) {

        Logger.i("----:>" + params.toString());
        /**
         * 注册接口
         */
        ApiClient.register(EboxApplication.getInstance(), params,
                new ClientCallback() {
                    @Override
                    public void onSuccess(JSONArray data, int code) {
                        UIHelper.dismissProgress(dialog);
                        Logger.i("RegistrePreActivity:" + data.toString());
                        String msg = "注册成功,我们会在3个工作日审核您的申请,请耐心等待！";
                        success(msg);
                    }

                    @Override
                    public void onFailed(byte[] data, int code) {
                        UIHelper.dismissProgress(dialog);
                    }
                });
    }

    private void success(String msg) {
        ToastUtil.showToastShort(msg);
        mtbRegister.setEnabled(false);
        exit();
    }

    private void exit() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(1500);

                RegisterPreActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        RegisterPreActivity.this.finish();
                    }
                });
            }
        }).start();
    }

    private void ShowPickDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("设置头像");
        CharSequence[] item = {"相册", "拍照"};
        builder.setItems(item, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        showDICM(dialog);
                        break;
                    case 1:
                        showCamer(dialog);
                        break;
                }
            }
        });
        builder.create().show();
    }

    private void showCamer(DialogInterface dialog) {
        if (null != dialog) {
            dialog.dismiss();
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(getMyCachePicFile()));
        startActivityForResult(intent, GlobalConfig.INTENT_TAKE_PIC_CAMEAR);
    }

    private void showDICM(DialogInterface dialog) {
        dialog.dismiss();
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        startActivityForResult(intent, GlobalConfig.INTENT_TAKE_PIC_DICM);
    }

    protected void onTakePic() {
        Intent takePicIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicIntent,
                GlobalConfig.INTENT_TAKE_PIC_CAMEAR);
    }

    private boolean checkImage() {
        if (!hasPhoto) {
            ToastUtil.showToastShort("请先设置头像");
            return false;
        }
        return true;
    }

    private boolean checkInput() {
        /*if (!hasPhoto) {
            ToastUtil.showToastShort("请先设置头像");
			return false;
		}*/
        String name = medName.getText().toString().trim();
        if (TextUtils.isEmpty(name) || !StringUtils.isChineseName(name)) {
            ToastUtil.showToastShort("请输入正确的姓名");
            medName.requestFocus();
            return false;
        }
        String idCard = medIdCard.getText().toString().trim();
        if (TextUtils.isEmpty(idCard)) {
            ToastUtil.showToastShort("请输入身份证号码");
            return false;
        } else {
            IDCard cc = new IDCard();
            try {
                String mesage = cc.IDCardValidate(idCard);
                if (!"".equals(mesage)) {
                    ToastUtil.showToastShort(mesage);
                    medIdCard.requestFocus();
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

//        if (TextUtils.isEmpty(region_id)) {
//            ToastUtil.showToastShort("请选择所在区域");
//            return false;
//        }

        if (TextUtils.isEmpty(company_id)) {
            ToastUtil.showToastShort("请选择快递公司");
            return false;
        }
        String mSiteName = et_site_name.getText().toString().trim();
        if (TextUtils.isEmpty(mSiteName)) {
            ToastUtil.showToastShort("请填写站点名称");
            return false;
        }
        String mSiteLoc = et_site_loc.getText().toString().trim();
        if (TextUtils.isEmpty(mSiteLoc)) {
            ToastUtil.showToastShort("请填写站点地址");
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.i("RegisterPreActivitty:" + requestCode + "   " + resultCode);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case GlobalConfig.INTENT_TAKE_PIC_CAMEAR:
                File temp = new File(getMyCachePicFile().toString());
                mUri = Uri.fromFile(temp);
                startPhotoZoom(mUri);
                //setPicToView(mUri);
                break;

            case GlobalConfig.INTENT_TAKE_PIC_DICM:
                if (data != null) {
                    mUri = data.getData();
                    startPhotoZoom(mUri);
                    //setPicToView(mUri);
                }
                break;
            case GlobalConfig.INTENT_TAKE_PIC_SCALE:
                if (data != null) {
                    setPicToView(data);
                }
                break;

            //获得快递公司name,id
            case GlobalConfig.LOGIC_SELECT_COMPANY:
                if (data != null) {
                    String c_id = data.getExtras().getString("orgnization_id");
                    String c_name = data.getExtras().getString("name");
                    company_id = c_id;
                    tv_company.setText(c_name);
                }
                break;

            //获得选择的区域name，id
            case GlobalConfig.LOGIC_SELECT_ORIGINATION:
                if (data != null) {
                    region_id = data.getExtras().getInt("id");
                    String region_name = data.getExtras().getString("name");
                    Logger.i("RegisterPreActivity:" + region_name);
                    tv_area.setText(region_name);
                }

                break;
        }
    }

    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 400);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, GlobalConfig.INTENT_TAKE_PIC_SCALE);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param picdata
     */
    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            mbtTackPic.setText(getResources().getString(R.string.confim_tack_pic_reset));
            Bitmap photo = extras.getParcelable("data");
            iv_head_icon.setImageBitmap(photo);
            savePic(photo);
        } else {
            mbtTackPic.setText(getResources().getString(R.string.confim_tack_pic));
            iv_head_icon.setImageResource(R.drawable.main_face);
        }
    }

    private void setPicToView(Uri mUri) {

        Bitmap photo = getBitmapFromUri(mUri);
        if (photo != null) {
            iv_head_icon.setImageBitmap(photo);
            savePic(photo);
        } else {
            iv_head_icon.setImageResource(R.drawable.main_face);
        }
    }

    private Bitmap getBitmapFromUri(Uri uri) {
        try {
            // 读取uri所在的图片
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            return bitmap;
        } catch (Exception e) {
            Log.e("[Android]", e.getMessage());
            Log.e("[Android]", "目录为：" + uri);
            e.printStackTrace();
            return null;
        }
    }

    private void savePic(Bitmap face) {
        File file = getMyCachePicFile(mTel);
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
            OutputStream outStream = new FileOutputStream(file);
            face.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();
            hasPhoto = true;
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    进入到照片界面，先检查之前填写的字段
     */
    private void toPhoto() {
        if (checkInput()) {
            stepSwitch(2);
        }
    }

    private String getText(TextView textView) {
        return textView.getText().toString().trim();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        ViewUtils.closeInput(this, medName);
        ViewUtils.closeInput(this, medIdCard);
        return super.dispatchTouchEvent(ev);
    }


    private class companyAdapter extends
            BaseListAdapter<HashMap<String, String>> {

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_company,
                        null);
            }
            TextView tv = ViewHolder.get(convertView, R.id.tv_company);
            HashMap<String, String> item = getItem(position);

            tv.setText(item.get("name"));
            return convertView;
        }
    }
}
