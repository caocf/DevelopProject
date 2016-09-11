package com.moge.gege.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.moge.gege.AppApplication;
import com.moge.gege.R;
import com.moge.gege.config.GlobalConfig;
import com.moge.gege.data.PersistentData;
import com.moge.gege.model.ProfileModel;
import com.moge.gege.model.RespProfileModel;
import com.moge.gege.model.RespUploadTokenModel;
import com.moge.gege.model.RespUserModel;
import com.moge.gege.model.UserModel;
import com.moge.gege.model.enums.GenderType;
import com.moge.gege.network.ErrorCode;
import com.moge.gege.network.request.GetUploadTokenRequest;
import com.moge.gege.network.request.RegisterRequest;
import com.moge.gege.network.request.UserInfoEditRequest;
import com.moge.gege.network.request.UserProfileRequest;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.RequestManager;
import com.moge.gege.network.util.ResponseEventHandler;
import com.moge.gege.network.util.qiniu.auth.JSONObjectRet;
import com.moge.gege.network.util.qiniu.io.IO;
import com.moge.gege.network.util.qiniu.io.PutExtra;
import com.moge.gege.network.util.qiniu.utils.QiniuException;
import com.moge.gege.util.EnviromentUtil;
import com.moge.gege.util.FileUtil;
import com.moge.gege.util.LogUtil;
import com.moge.gege.util.ToastUtil;
import com.moge.gege.util.UmengUtil;
import com.moge.gege.util.widget.RoundedImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;

public class RegisterActivity extends BaseActivity implements OnTouchListener,
        OnCheckedChangeListener
{
    private Context mContext;
    private TextView mAvatarText;
    private RoundedImageView mAvatarImageView;
    private TextView mCommunityText;
    private EditText mNicknameEdit;
    private RadioGroup mGenderGroup;
    private RadioButton mManRadioBtn;
    private RadioButton mWomanRadioBtn;
    private EditText mJobEdit;
    private EditText mHobbyEdit;

    private String mUserName;
    private String mCommunityId;
    private String mNickName;
    private String mAvatar = "";
    private int mGender;
    private String mJob;
    private String mHobby;
    private boolean mHaveSelectImage = false;

    private Uri mCropOutImgUri;
    private String mCropFileName;

    private String mUploadToken;

    private boolean mIsEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mUserName = this.getIntent().getStringExtra("username");

        if (TextUtils.isEmpty(mUserName))
        {
            mIsEditMode = true;
        }

        mContext = RegisterActivity.this;
        initView();
        initData();
    }

    @Override
    protected void initView()
    {
        super.initView();

        mAvatarText = (TextView) this.findViewById(R.id.avatarText);
        if (mIsEditMode)
        {
            this.setHeaderLeftImage(R.drawable.icon_back);
            this.setHeaderLeftTitle(R.string.user_info);
            this.setHeaderRightTitle(R.string.finish);
            mAvatarText.setText(R.string.edit_avatar);
        }
        else
        {
            this.setHeaderTitle(R.string.register_header);
            this.setHeaderRightTitle(R.string.next);
            mAvatarText.setText(R.string.avatar);
        }

        mAvatarImageView = (RoundedImageView) this
                .findViewById(R.id.avatarImage);
        mAvatarImageView.setOnClickListener(mClickListener);
        mAvatarImageView.requestFocus();
        if (mIsEditMode)
        {
            mAvatarImageView.setCornerRadius(100f);
            mAvatarImageView.setBorderWidth(1f);
        }

        mCommunityText = (TextView) this.findViewById(R.id.communityText);
        mCommunityText.setOnTouchListener(this);
        mNicknameEdit = (EditText) this.findViewById(R.id.nicknameEdit);
        mJobEdit = (EditText) this.findViewById(R.id.jobEdit);
        mHobbyEdit = (EditText) this.findViewById(R.id.hobbyEdit);
        mGenderGroup = (RadioGroup) this.findViewById(R.id.genderGroup);
        mGenderGroup.setOnCheckedChangeListener(this);
        mManRadioBtn = (RadioButton) this.findViewById(R.id.manRadioBtn);
        mWomanRadioBtn = (RadioButton) this.findViewById(R.id.womanRadioBtn);
        mJobEdit = (EditText) this.findViewById(R.id.jobEdit);
        mHobbyEdit = (EditText) this.findViewById(R.id.hobbyEdit);

        TextView jobTitleText = (TextView) this.findViewById(R.id.jobTitleText);
        jobTitleText.setText(Html.fromHtml(getString(R.string.job)));

        TextView hobbyTitleText = (TextView) this
                .findViewById(R.id.hobbyTitleText);
        hobbyTitleText.setText(Html.fromHtml(getString(R.string.hobby)));
    }

    private void initData()
    {
        this.mGender = GenderType.MAN;

        mCropFileName = EnviromentUtil.getImageDirectory(this) + File.separator
                + String.valueOf(System.currentTimeMillis()) + ".jpg";
        mCropOutImgUri = Uri.fromFile(new File(mCropFileName));

        if (mIsEditMode)
        {
            doUserProfileRequest();
        }
    }

    private OnClickListener mClickListener = new OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
                case R.id.avatarImage:
                    showPicturePicker(mContext);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onHeaderRightClick()
    {
        if (mIsEditMode)
        {
            onEditUserAction();
        }
        else
        {
            onRegisterAction();
        }
    }

    private boolean checkInputValid()
    {
        // no must
        // if (!mHaveSelectImage)
        // {
        // ToastUtil.showToastShort(R.string.please_select_avatar);
        // return false;
        // }

        if (TextUtils.isEmpty(mCommunityId))
        {
            ToastUtil.showToastShort(R.string.please_select_community);
            return false;
        }

        mNickName = mNicknameEdit.getText().toString().trim();
        if (TextUtils.isEmpty(mNickName))
        {
            ToastUtil.showToastShort(R.string.please_input_nickname);
            return false;
        }

        mJob = mJobEdit.getText().toString().trim();
        mHobby = mHobbyEdit.getText().toString().trim();

        return true;
    }

    private boolean checkEditValid()
    {
        mNickName = mNicknameEdit.getText().toString().trim();
        if (TextUtils.isEmpty(mNickName))
        {
            ToastUtil.showToastShort(R.string.please_input_nickname);
            return false;
        }

        mJob = mJobEdit.getText().toString().trim();
        mHobby = mHobbyEdit.getText().toString().trim();

        return true;
    }

    private void onRegisterAction()
    {
        if (!checkInputValid())
        {
            return;
        }

        if (mHaveSelectImage)
        {
            doGetUploadToken();
        }
        else
        {
            doRegister();
        }
    }

    private void onEditUserAction()
    {
        if (!checkEditValid())
        {
            return;
        }

        if (mHaveSelectImage)
        {
            doGetUploadToken();
        }
        else
        {
            doEditUserInfoRequest();
        }
    }

    private void doUserProfileRequest()
    {
        UserProfileRequest request = new UserProfileRequest("",
                new ResponseEventHandler<RespProfileModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespProfileModel> request,
                            RespProfileModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            ProfileModel model = result.getData();

                            mAvatar = model.getAvatar();
                            mNickName = model.getNickname();
                            mGender = model.getGender();
                            mJob = model.getProfession();
                            mHobby = model.getInterest();
                            mCommunityId = model.getCommunity_id();

                            setImage(mAvatarImageView, getImageUrl(mAvatar) + GlobalConfig.IMAGE_STYLE90_90,
                                    R.drawable.icon_default_avatar);
                            mNicknameEdit.setText(mNickName);

                            mCommunityText.setText(PersistentData.instance()
                                        .getCommunityName());

                            mJobEdit.setText(mJob);
                            mHobbyEdit.setText(mHobby);
                            if (GenderType.MAN == model.getGender())
                            {
                                mManRadioBtn.setChecked(true);
                            }
                            else
                            {
                                mWomanRadioBtn.setChecked(true);
                            }
                        }
                        else
                        {
                            ToastUtil.showToastShort(result.getMsg());
                        }
                    }

                    @Override
                    public void onResponseError(VolleyError error)
                    {
                        ToastUtil.showToastShort(error.getMessage());
                    }

                });
        executeRequest(request);
    }

    private void doGetUploadToken()
    {
        GetUploadTokenRequest request = new GetUploadTokenRequest(
                new ResponseEventHandler<RespUploadTokenModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespUploadTokenModel> request,
                            RespUploadTokenModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            mUploadToken = result.getData().getToken();
                            doUploadAvatar(mCropOutImgUri, mUploadToken);
                        }
                        else
                        {
                            ToastUtil.showToastShort(result.getMsg());
                        }
                    }

                    @Override
                    public void onResponseError(VolleyError error)
                    {
                        ToastUtil.showToastShort(error.getMessage());
                    }

                });
        executeRequest(request);
    }

    private void doRegister()
    {
        RegisterRequest request = new RegisterRequest(mUserName, mNickName,
                mAvatar, mGender, mCommunityId, GlobalConfig.getLongitude(),
                GlobalConfig.getLatitude(), mJob, mHobby,
                new ResponseEventHandler<RespUserModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespUserModel> request,
                            RespUserModel result)
                    {
                        if (result != null
                                && result.getStatus() == ErrorCode.SUCCESS)
                        {
                            // save user information
                            AppApplication.login(result.getData());

                            // umeng event
                            UmengUtil.statRegisterEvent(mContext, result
                                    .getData().getUsername());

                            // direct exit activity
                            setResult(RESULT_OK, null);
                            finish();

//                            Intent intent = new Intent(mContext,
//                                    RecommendBoardActivity.class);
//                            startActivityForResult(intent,
//                                    GlobalConfig.INTENT_RECOMMEND_BOARD);
                        }
                        else
                        {
                            ToastUtil.showToastShort(result.getMsg());
                        }
                    }

                    @Override
                    public void onResponseError(VolleyError error)
                    {
                        ToastUtil.showToastShort(error.getMessage());
                    }
                });
        RequestManager.addRequest(request, RequestManager.CookieTag);
    }

    private void doEditUserInfoRequest()
    {
        UserInfoEditRequest request = new UserInfoEditRequest(mCommunityId,
                mNickName, mAvatar, mGender, "", mJob, mHobby,
                new ResponseEventHandler<RespUserModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespUserModel> request,
                            RespUserModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            ToastUtil.showToastShort(R.string.edit_success);

                            // update key info
                            UserModel oldUserModel = PersistentData.instance().getUserInfo();
                            UserModel newUserModel = result.getData();
                            newUserModel.setSecret_key(oldUserModel.getSecret_key());
                            newUserModel.setSecure_key(oldUserModel.getSecure_key());
                            newUserModel.setSign_key(oldUserModel.getSign_key());

                            // update user info
                            PersistentData.instance().setUserInfo(
                                    newUserModel);

                            Intent intent = new Intent();
                            intent.putExtra("userinfo", newUserModel);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                        else
                        {
                            ToastUtil.showToastShort(result.getMsg());
                        }
                    }

                    @Override
                    public void onResponseError(VolleyError error)
                    {
                        ToastUtil.showToastShort(error.getMessage());
                    }
                });
        executeRequest(request);
    }

    public void showPicturePicker(Context context)
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(getString(R.string.select_avatar));
        dialog.setNegativeButton(getString(R.string.cancel), null);
        dialog.setItems(new String[] { getString(R.string.take_picture),
                        getString(R.string.album) },
                new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        switch (which)
                        {
                            case 0:
                                openCamera();
                                break;
                            case 1:
                                openAlbum();
                                break;
                        }
                    }
                });
        dialog.create().show();
    }

    public void onTakeCameraResult(Uri imageUri)
    {
        cropImageUri(imageUri, mCropOutImgUri, 114, 114,
                GlobalConfig.INTENT_CROP_IMAGE);
    }

    public void onTakeAlbumResult(Uri imageUri)
    {
        cropImageUri(imageUri, mCropOutImgUri, 114, 114,
                GlobalConfig.INTENT_CROP_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK)
        {
            return;
        }

        switch (requestCode)
        {
            case GlobalConfig.INTENT_CROP_IMAGE:
                if (mCropOutImgUri != null)
                {
                    Bitmap bitmap = decodeUriAsBitmap(mCropOutImgUri);
                    if (bitmap != null)
                    {
                        mAvatarImageView.setImageBitmap(bitmap);
                        mAvatarImageView.setCornerRadius(100f);
                        mAvatarImageView.setBorderWidth(1f);

                        mHaveSelectImage = true;
                    }
                }
                break;
            case GlobalConfig.INTENT_SELECT_COMMUNITY:
                mCommunityId = data.getStringExtra("community_id");
                mCommunityText.setText(data.getStringExtra("community_name"));
                break;
            case GlobalConfig.INTENT_RECOMMEND_BOARD:
                setResult(RESULT_OK, null);
                finish();
                break;
            default:
                break;
        }
    }

    private void cropImageUri(Uri uri, Uri outUri, int outputX, int outputY,
            int requestCode)
    {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outUri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, requestCode);
    }

    private Bitmap decodeUriAsBitmap(Uri uri)
    {
        Bitmap bitmap = null;
        try
        {
            bitmap = BitmapFactory.decodeStream(getContentResolver()
                    .openInputStream(uri));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    /**
     * upload avatar
     */
    private void doUploadAvatar(Uri uri, String upToken)
    {
        String key = IO.UNDEFINED_KEY;
        PutExtra extra = new PutExtra();
        extra.params = new HashMap<String, String>();
        extra.params.put("x:source", "headImage");
        extra.mimeType = "image/jpeg";

        IO.putFile(this, upToken, key, uri, extra, new JSONObjectRet()
        {
            @Override
            public void onProcess(long current, long total)
            {
            }

            @Override
            public void onSuccess(JSONObject resp)
            {
                try
                {
                    if (resp.has("success"))
                    {
                        if (resp.getBoolean("success"))
                        {
                            mAvatar = resp.getString("name");
                            if (mIsEditMode)
                            {
                                doEditUserInfoRequest();
                            }
                            else
                            {
                                doRegister();
                            }

                            return;
                        }
                    }
                }
                catch (JSONException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                LogUtil.e("upload avatar failed");
            }

            @Override
            public void onFailure(QiniuException ex)
            {
                LogUtil.e("upload avatar failed");
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        switch (v.getId())
        {
            case R.id.communityText:
                Intent intent = new Intent(mContext, CommunityActivity.class);
                startActivityForResult(intent,
                        GlobalConfig.INTENT_SELECT_COMMUNITY);
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId)
    {
        if (checkedId == R.id.manRadioBtn)
        {
            this.mGender = GenderType.MAN;
        }
        else
        {
            this.mGender = GenderType.WOMAN;
        }

    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        if (mCropOutImgUri != null)
        {
            FileUtil.deleteFileWithPath(mCropFileName);
        }
    }
}
