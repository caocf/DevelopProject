package com.moge.gege.ui.helper;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;

import com.android.volley.VolleyError;
import com.moge.gege.R;
import com.moge.gege.model.RespUploadTokenModel;
import com.moge.gege.network.ErrorCode;
import com.moge.gege.network.request.GetUploadTokenRequest;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.RequestManager;
import com.moge.gege.network.util.ResponseEventHandler;
import com.moge.gege.network.util.qiniu.auth.JSONObjectRet;
import com.moge.gege.network.util.qiniu.io.IO;
import com.moge.gege.network.util.qiniu.io.PutExtra;
import com.moge.gege.network.util.qiniu.utils.InputStreamAt;
import com.moge.gege.network.util.qiniu.utils.QiniuException;
import com.moge.gege.util.BitmapUtil;
import com.moge.gege.util.DialogUtil;
import com.moge.gege.util.LogUtil;

public class UploadImageHelper
{
    private Context mContext;
    private String mFilePath;
    private Dialog mProgressDialog;
    private UploadImageListener mListener;

    public static int UPLOAD_SUCCESS = 0;
    public static int GET_TOKEN_FAILED = 1;
    public static int UPLOAD_FAILED = 2;

    public interface UploadImageListener
    {
        public void onUploadResult(int result, String msg, String imagename);
    }

    public void uploadImage(Context context, String filepath,
            UploadImageListener listener, boolean showProgressBar)
    {
        if (showProgressBar)
        {
            mProgressDialog = DialogUtil.createProgressDialog(context, context
                    .getResources().getString(R.string.uploading_photo));
            mProgressDialog.show();
        }

        mContext = context;
        mFilePath = filepath;
        mListener = listener;

        doGetUploadToken();
    }

    private void dispatchResult(int result, String msg, String imagename)
    {
        if (mProgressDialog != null)
        {
            mProgressDialog.dismiss();
        }

        if (mListener != null)
        {
            mListener.onUploadResult(result, msg, imagename);
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
                            doUploadPhoto(mFilePath, uploadToken);
                        }
                        else
                        {
                            dispatchResult(GET_TOKEN_FAILED, result.getMsg(),
                                    null);
                        }
                    }

                    @Override
                    public void onResponseError(VolleyError error)
                    {
                        dispatchResult(GET_TOKEN_FAILED, error.getMessage(),
                                null);
                    }

                });
        RequestManager.addRequest(request, mContext);
    }

    private void doUploadPhoto(String filepath, String upToken)
    {
        String key = IO.UNDEFINED_KEY;
        PutExtra extra = new PutExtra();
        extra.params = new HashMap<String, String>();
        extra.params.put("x:source", "headImage");
        extra.mimeType = "image/jpeg";

        InputStreamAt imageStream = new InputStreamAt(
                BitmapUtil.compressImage(filepath));

        IO.put(upToken, key, imageStream, extra, new JSONObjectRet()
        // IO.putFile(this, upToken, key, imageUri, extra, new JSONObjectRet()
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
                                    dispatchResult(UPLOAD_SUCCESS, null,
                                            resp.getString("name"));
                                    return;
                                }
                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                            LogUtil.e(e.getMessage());
                        }

                        dispatchResult(UPLOAD_FAILED, mContext.getResources()
                                .getString(R.string.upload_failed), null);
                    }

                    @Override
                    public void onFailure(QiniuException ex)
                    {
                        LogUtil.e("upload avatar failed");
                        dispatchResult(UPLOAD_FAILED, ex.getMessage(), null);
                    }
                });
    }

}
