package com.moge.gege.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Application;
import com.moge.gege.AppApplication;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.moge.gege.R;
import com.moge.gege.config.GlobalConfig;
import com.moge.gege.model.AlbumItemModel;
import com.moge.gege.model.BaseModel;
import com.moge.gege.model.LocalServiceModel;
import com.moge.gege.model.RespUploadTokenModel;
import com.moge.gege.model.TopicPublishModel;
import com.moge.gege.model.enums.ServiceType;
import com.moge.gege.model.enums.TopicType;
import com.moge.gege.network.ErrorCode;
import com.moge.gege.network.request.GetUploadTokenRequest;
import com.moge.gege.network.request.TopicPublishRequest;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;
import com.moge.gege.network.util.qiniu.auth.JSONObjectRet;
import com.moge.gege.network.util.qiniu.io.IO;
import com.moge.gege.network.util.qiniu.io.PutExtra;
import com.moge.gege.network.util.qiniu.utils.InputStreamAt;
import com.moge.gege.network.util.qiniu.utils.QiniuException;
import com.moge.gege.service.AppService;
import com.moge.gege.ui.adapter.LocalServiceAdapter;
import com.moge.gege.ui.adapter.LocalServiceAdapter.LocalServiceListener;
import com.moge.gege.ui.fragment.PhotoListFragment;
import com.moge.gege.ui.fragment.PhotoListFragment.PhotoListChangeListener;
import com.moge.gege.ui.widget.PhotoListView;
import com.moge.gege.util.BitmapUtil;
import com.moge.gege.util.DeviceInfoUtil;
import com.moge.gege.util.FunctionUtils;
import com.moge.gege.util.LogUtil;
import com.moge.gege.util.ToastUtil;
import com.moge.gege.util.widget.chat.EmoticonView;
import com.moge.gege.util.widget.chat.EmoticonView.OnEmoticonListener;

public class TopicPublishActivity extends BaseActivity implements
        PhotoListChangeListener, OnEmoticonListener, LocalServiceListener
{
    private Activity mContext;
    private LinearLayout mRequireLayout;
    private TextView mHeaderLeftText;
    private TextView mHeaderRightText;
    private RadioGroup mRequireRadioGroup;
    private ScrollView mTopicScrollView;
    private GridView mServiceGridView;
    private LocalServiceAdapter mLocalServiceAdapter;

    private EditText mTitleEdit;
    private EditText mContentEdit;

    private RadioButton mTopicBtn;
    private RadioButton mServiceBtn;

    private String[] mServiceNameStrings = new String[8];

    // external params
    private String mBoardId;
    private int mTopicType;

    private PhotoListView mPhotoListView;
    private List<AlbumItemModel> mSelectAlbumList;
    private List<String> mUploadPhotoList = new ArrayList<String>();
    private int mTotalUploadNum = 0;
    private int mTotalUploadSuccessNum = 0;
    private int mTotalUploadFailedNum = 0;

    private ImageView mEmoticonImage;
    private ImageView mPhotoImage;
    private TextView mPhotoCountText;
    private EmoticonView mEmoticonView;
    private PhotoListFragment mPhotoListFragment;
    private Dialog mProgressDialog;
    private InputMethodManager mInputManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topicpublish);

        mBoardId = getIntent().getStringExtra("board_id");
        mTopicType = TopicType.GENERAL_TOPIC;

        mContext = TopicPublishActivity.this;
        initView();
        initData();
    }

    @Override
    protected void initView()
    {
        super.initView();

        mEmoticonImage = (ImageView) this.findViewById(R.id.emoticonImage);
        mEmoticonImage.setOnClickListener(mClickListener);
        mPhotoImage = (ImageView) this.findViewById(R.id.photoImage);
        mPhotoImage.setOnClickListener(mClickListener);
        mPhotoCountText = (TextView) this.findViewById(R.id.photoCountText);
        mEmoticonView = (EmoticonView) this.findViewById(R.id.emoticonView);
        mEmoticonView.setListener(this);
        mPhotoListFragment = (PhotoListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.photoListFragment);
        mPhotoListFragment.setPhotoNumChangeListener(this);
        showPhotoFragment(false);

        mInputManager = ((InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE));

        // init layout
        mRequireLayout = (LinearLayout) this.findViewById(R.id.requireLayout);
        mRequireLayout.setVisibility(View.VISIBLE);
        mHeaderLeftText = (TextView) this.findViewById(R.id.headerLeftText);
        mHeaderLeftText.setOnClickListener(mClickListener);
        mHeaderRightText = (TextView) this.findViewById(R.id.headerRightText);
        mHeaderRightText.setOnClickListener(mClickListener);
        mTopicScrollView = (ScrollView) this.findViewById(R.id.topicScrollView);
        mTitleEdit = (EditText) this.findViewById(R.id.titleEdit);
        mContentEdit = (EditText) this.findViewById(R.id.contentEdit);

        mServiceGridView = (GridView) this.findViewById(R.id.serviceGridView);
        mLocalServiceAdapter = new LocalServiceAdapter(mContext);
        mLocalServiceAdapter.setListener(this);
        mServiceGridView.setAdapter(mLocalServiceAdapter);
        mServiceGridView.setOnItemClickListener(mLocalServiceAdapter);

        mRequireRadioGroup = (RadioGroup) this
                .findViewById(R.id.requireRadioGroup);
        mRequireRadioGroup
                .setOnCheckedChangeListener(new OnCheckedChangeListener()
                {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId)
                    {
                        switch (checkedId)
                        {
                            case R.id.topicBtn:
                                mTopicScrollView.setVisibility(View.VISIBLE);
                                mServiceGridView.setVisibility(View.GONE);
                                mHeaderRightText.setVisibility(View.VISIBLE);
                                break;
                            case R.id.serviceBtn:
                                mTopicScrollView.setVisibility(View.GONE);
                                mServiceGridView.setVisibility(View.VISIBLE);
                                mHeaderRightText.setVisibility(View.GONE);
                                break;
                            default:
                                break;
                        }

                    }

                });
    }

    private void initData()
    {
        initServiceData();
    }

    private void initServiceData()
    {
        final String nameList[] = this.getResources().getStringArray(
                R.array.service_des);
        final String resNameList[] = this.getResources().getStringArray(
                R.array.service_des_resname);
        final String valueList[] = this.getResources().getStringArray(
                R.array.service_des_value);

        List<LocalServiceModel> serviceList = new ArrayList<LocalServiceModel>();

        for (int i = 2; i < nameList.length - 2; i++)
        {
            LocalServiceModel model = new LocalServiceModel();
            model.setName(nameList[i]);
            model.setResId(getResources().getIdentifier(resNameList[i],
                    "drawable", mContext.getPackageName()));
            model.setValue(Integer.parseInt(valueList[i]));

            serviceList.add(model);
        }

        LocalServiceModel model = new LocalServiceModel();
        model.setName(getString(R.string.activity));
        model.setResId(R.drawable.icon_activity);
        model.setValue(ServiceType.ACTIVITY_SERVICE);
        serviceList.add(model);

        mLocalServiceAdapter.clear();
        mLocalServiceAdapter.addAll(serviceList);
        mLocalServiceAdapter.notifyDataSetChanged();
    }

    private OnClickListener mClickListener = new OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            // hide keyboard
            FunctionUtils.hideInputMethod(mContext);

            switch (v.getId())
            {
                case R.id.emoticonImage:
                    onEmoticonImageClick();
                    break;
                case R.id.photoImage:
                    onPhotoImageClick();
                    break;
                case R.id.headerLeftText:
                    finish();
                    break;
                case R.id.headerRightText:
                    onPublishClick();
                    break;
                default:
                    break;
            }
        }
    };

    private void onPublishClick()
    {
        FunctionUtils.hideInputMethod(mContext);

        if (!DeviceInfoUtil.isHaveInternet(mContext))
        {
            ToastUtil.showToastShort(R.string.network_exception);
            return;
        }

        List<NameValuePair> paramList = buildPostParamList();
        if (paramList == null)
        {
            return;
        }

        if(!AppApplication.checkLoginState(mContext))
        {
            return;
        }

        publishTopicByBack();
    }

    private void publishTopicByBack()
    {
        TopicPublishModel requestModel = new TopicPublishModel();
        requestModel.setTopicType(mTopicType);
        requestModel.setImageList(mPhotoListFragment.getSelectPhotoList());
        requestModel.setParamList(buildPostParamList());

        if (AppService.instance() != null)
        {
            AppService.instance().pushRequestToService(requestModel);
        }
        finish();
    }

    private void onEmoticonImageClick()
    {
        mContentEdit.requestFocus();

        showEmotIconView(true);
        showPhotoFragment(false);
    }

    private void onPhotoImageClick()
    {
        showEmotIconView(false);
        showPhotoFragment(true);
    }

    private void showEmotIconView(boolean show)
    {
        if (show)
        {
            mInputManager.hideSoftInputFromWindow(
                    mContentEdit.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);

            mEmoticonView.setVisibility(View.VISIBLE);
        }
        else
        {
            mEmoticonView.setVisibility(View.GONE);
        }

    }

    private void showPhotoFragment(boolean show)
    {
        if (show && mPhotoListFragment.isVisible())
        {
            mInputManager.hideSoftInputFromWindow(
                    mContentEdit.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
            return;
        }

        if (!show && mPhotoListFragment.isHidden())
        {
            return;
        }

        FragmentTransaction ft = this.getSupportFragmentManager()
                .beginTransaction();
        // ft.setCustomAnimations(android.R.animator.fade_in,
        // android.R.animator.fade_out);

        if (show)
        {
            mInputManager.hideSoftInputFromWindow(
                    mContentEdit.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);

            ft.show(mPhotoListFragment);
        }
        else
        {
            ft.hide(mPhotoListFragment);
        }
        ft.commit();
    }

    private String buildPostParam()
    {
        List<NameValuePair> list = new ArrayList<NameValuePair>();

        if (!buildGeneralTopicParam(list))
        {
            return null;
        }

        list.add(new BasicNameValuePair("bid", mBoardId));

        for (int i = 0; i < mUploadPhotoList.size(); i++)
        {
            list.add(new BasicNameValuePair("images", mUploadPhotoList.get(i)));
        }

        // list.add(new BasicNameValuePair("descript", ""));
        list.add(new BasicNameValuePair("longitude", String
                .valueOf(GlobalConfig.getLongitude())));
        list.add(new BasicNameValuePair("latitude", String.valueOf(GlobalConfig
                .getLatitude())));
        return URLEncodedUtils.format(list, HTTP.UTF_8);
    }

    private List<NameValuePair> buildPostParamList()
    {
        List<NameValuePair> list = new ArrayList<NameValuePair>();

        if (!buildGeneralTopicParam(list))
        {
            return null;
        }

        list.add(new BasicNameValuePair("bid", mBoardId));

        for (int i = 0; i < mUploadPhotoList.size(); i++)
        {
            list.add(new BasicNameValuePair("images", mUploadPhotoList.get(i)));
        }

        // list.add(new BasicNameValuePair("descript", ""));
        list.add(new BasicNameValuePair("longitude", String
                .valueOf(GlobalConfig.getLongitude())));
        list.add(new BasicNameValuePair("latitude", String.valueOf(GlobalConfig
                .getLatitude())));
        return list;
    }

    private boolean buildGeneralTopicParam(List<NameValuePair> listParam)
    {
        String title = mTitleEdit.getText().toString().trim();
        if (TextUtils.isEmpty(title))
        {
            ToastUtil.showToastShort(R.string.please_input_title);
            return false;
        }

        String content = mContentEdit.getText().toString().trim();
        if (TextUtils.isEmpty(content))
        {
            ToastUtil.showToastShort(R.string.please_input_content);
            return false;
        }

        listParam.add(new BasicNameValuePair("title", title));
        listParam.add(new BasicNameValuePair("content", content));
        return true;
    }

    private void doUploadPhotoList(String token)
    {
        for (int i = 0; i < mSelectAlbumList.size(); i++)
        {
            AlbumItemModel itemModel = mSelectAlbumList.get(i);
            doUploadPhoto(itemModel.getPath(), token);
        }

        mUploadPhotoList.clear();
        mTotalUploadNum = mSelectAlbumList.size();
        mTotalUploadSuccessNum = 0;
        mTotalUploadFailedNum = 0;

        doCheckUploadFinish();
    }

    private void doCheckUploadFinish()
    {
        if (mTotalUploadSuccessNum + mTotalUploadFailedNum >= mTotalUploadNum)
        {
            this.doTopicPublish();
        }
        else
        {
            new Handler().postDelayed(new Runnable()
            {
                public void run()
                {
                    doCheckUploadFinish();
                }
            }, 500);
        }
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
                            String uploadToken = result.getData().getToken();
                            doUploadPhotoList(uploadToken);
                        }
                        else
                        {
                            ToastUtil.showToastShort(result.getMsg());
                            mProgressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onResponseError(VolleyError error)
                    {
                        ToastUtil.showToastShort(error.getMessage());
                        mProgressDialog.dismiss();
                    }

                });
        executeRequest(request);
    }

    private void doUploadPhoto(String filename, String upToken)
    {
        String key = IO.UNDEFINED_KEY;
        PutExtra extra = new PutExtra();
        extra.params = new HashMap<String, String>();
        extra.params.put("x:source", "headImage");
        extra.mimeType = "image/jpeg";

        InputStreamAt imageStream = new InputStreamAt(
                BitmapUtil.compressImage(filename));

        IO.put(upToken, key, imageStream, extra, new JSONObjectRet()
        // Uri uri = Uri.fromFile(new File(filename));
        //
        // IO.putFile(this, upToken, key, uri, extra, new JSONObjectRet()
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
                                    mUploadPhotoList.add(resp.getString("name"));
                                    mTotalUploadSuccessNum++;
                                    return;
                                }
                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                        mTotalUploadFailedNum++;
                    }

                    @Override
                    public void onFailure(QiniuException ex)
                    {
                        LogUtil.e("upload avatar failed");
                    }
                });
    }

    private void doTopicPublish()
    {
        String params = buildPostParam();
        if (TextUtils.isEmpty(params))
        {
            return;
        }

        doTopicPublishRequest(mTopicType, params);
    }

    private void doTopicPublishRequest(int topicType, String param)
    {
        TopicPublishRequest request = new TopicPublishRequest(topicType, param,
                new ResponseEventHandler<BaseModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<BaseModel> request, BaseModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            ToastUtil.showToastShort("发布成功~");
                            setResult(RESULT_OK);
                            finish();
                        }
                        else
                        {
                            ToastUtil.showToastShort(result.getMsg());
                        }

                        mProgressDialog.dismiss();
                    }

                    @Override
                    public void onResponseError(VolleyError error)
                    {
                        ToastUtil.showToastShort(error.getMessage());
                        mProgressDialog.dismiss();
                    }

                });
        executeRequest(request);
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
            case GlobalConfig.INTENT_LOGIN:
                break;
            case GlobalConfig.INTENT_PUBLISH_TOPIC:
                setResult(RESULT_OK);
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onPhotoNumChanged(int selectNum)
    {
        if (selectNum > 0)
        {
            mPhotoCountText.setVisibility(View.VISIBLE);
            mPhotoCountText.setText(String.valueOf(selectNum));
        }
        else
        {
            mPhotoCountText.setVisibility(View.GONE);
        }
    }

    private int getEditTextCursorIndex(EditText editText)
    {
        return editText.getSelectionStart();
    }

    private void insertText(EditText editText, CharSequence text)
    {
        editText.getText().insert(getEditTextCursorIndex(editText), text);
    }

    @Override
    public void onEmoticonClick(CharSequence cs)
    {
        mContentEdit.requestFocus();
        insertText(mContentEdit, cs);
    }

    @Override
    public void onEmoticonDelete()
    {
        int action = KeyEvent.ACTION_DOWN;
        int code = KeyEvent.KEYCODE_DEL;
        KeyEvent event = new KeyEvent(action, code);
        mContentEdit.onKeyDown(KeyEvent.KEYCODE_DEL, event);
    }

    @Override
    public void onServiceClick(int position, LocalServiceModel model)
    {
        Intent intent = new Intent(mContext, ServicePublishActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("board_id", mBoardId);
        if (model.getValue() == ServiceType.ACTIVITY_SERVICE)
        {
            bundle.putInt("topic_type", TopicType.ACTIVITY_TOPIC);
        }
        else
        {
            bundle.putInt("topic_type", TopicType.CATEGORY_TOPIC);
        }
        bundle.putInt("service_type", model.getValue());
        intent.putExtras(bundle);
        this.startActivityForResult(intent, GlobalConfig.INTENT_PUBLISH_TOPIC);
    }

    @Override
    protected void onLoginResult(int from, int result)
    {
        if (result != ErrorCode.SUCCESS)
        {
            return;
        }

        publishTopicByBack();
    }
}
