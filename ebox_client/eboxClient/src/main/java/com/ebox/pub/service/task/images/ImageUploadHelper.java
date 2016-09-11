package com.ebox.pub.service.task.images;

import com.android.volley.VolleyError;
import com.ebox.Anetwork.RequestManager;
import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.ex.network.model.RspUploadPicToken;
import com.ebox.ex.network.model.base.BaseRsp;
import com.ebox.ex.network.request.UploadPicTagRequest;
import com.ebox.ex.network.request.UploadPicTokenRequest;
import com.ebox.pub.utils.LogUtil;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCancellationSignal;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

/**
 * Created by Android on 2015/9/10.
 */
public class ImageUploadHelper implements UpCompletionHandler {

    private static String Token;

    private static ImageUploadHelper imageUploadHelper;

    private UploadManager manager;
    private  uploadFinishListener finishListener;


    private File uploadDirectory;//上传的文件夹

    private File mUploadingFile;//上传中的文件
    private String mTag;//文件Tag

    private boolean isFile =false;
    private boolean isNeedStop=false;

    private ImageUploadHelper() {

        Configuration.Builder builder = new Configuration.Builder();
        Configuration configuration = builder.connectTimeout(5*60).retryMax(1).build();

        manager = new UploadManager(configuration);

    }

    public static ImageUploadHelper instance()
    {
        if (imageUploadHelper == null) {
            imageUploadHelper = new ImageUploadHelper();
        }
        return imageUploadHelper;
    }


    public ImageUploadHelper files(File files) {

        if (files!=null)
        {
            uploadDirectory = files;
        }

        if (files.isFile())
        {
            isFile = true;
        }
        else
        {
            isFile=false;
        }

        isNeedStop=false;

        return imageUploadHelper;
    }

    public ImageUploadHelper listener(uploadFinishListener listener)
    {
        this.finishListener = listener;
        return imageUploadHelper;
    }

    public void stop(){
        isNeedStop=true;
    }

    public void start()
    {
        LogUtil.i("upload images start");
        if (uploadDirectory != null&&!isNeedStop)
        {
            if (!isFile)
            {
                File[] files = uploadDirectory.listFiles();

                if (files != null && files.length > 0)
                {
                    uploadImage( files[0]);
                } else {
                    if (finishListener != null)
                    {
                        finishListener.uploadFinish();//全部上传完成
                    }
                }
            }
        }
        else
        {
            if (finishListener != null)
            {
                finishListener.uploadFinish();//全部上传完成
            }
        }
    }


    private  void uploadImage(File imageFile) {

        mUploadingFile = imageFile;

        if (Token == null || Token.equals(""))
        {
            getToken();
        } else {
            uploadFile(imageFile);
        }
    }

    private void getToken() {

        UploadPicTokenRequest request = new UploadPicTokenRequest(new ResponseEventHandler<RspUploadPicToken>() {
            @Override
            public void onResponseSuccess(RspUploadPicToken result) {
                if (result.isSuccess())
                {
                    Token = result.getData().getToken();
                    if (mUploadingFile!=null)
                    {
                        uploadFile(mUploadingFile);
                    }
                }
            }

            @Override
            public void onResponseError(VolleyError error) {
            }
        });

        RequestManager.addRequest(request, null);

    }

    private boolean isCancelled;

    private void uploadFile(File imageFile) {
        isCancelled=false;
        if (!imageFile.isFile())
        {
            uploadFinishDo();
            return;
        }
        String name = imageFile.getName();
        String orderId = OrderPictureHelper.getOrderId(name);
        //订单长度错误直接跳过
        if (orderId.length() < 24)
        {
            uploadFinishDo();
            return;
        }

        try {
            HashMap<String, String> option = new HashMap<String, String>();
            option.put("x:source","order");
            UploadOptions options = new UploadOptions(option,null,false,null, new UpCancellationSignal(){
                public boolean isCancelled(){
                    return isCancelled;
                }
            });
            manager.put(imageFile, null, Token, this, options);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void complete(String key, ResponseInfo info, JSONObject response) {


        try {
            if (response==null) {
              /*  if (mUploadingFile != null)
                {
                    LogUtil.w("restart upload image "+mUploadingFile.getName());
                    uploadFile(mUploadingFile);
                }else
                    uploadFinishDo();*/
                if (finishListener != null)
                {
                    isCancelled=true;
                    finishListener.nullStop();
                }
                return;
            }
            boolean success = response.getBoolean("success");
            if (success)
            {
                //{"success":true,"name":"FtNxwMqqjB86YLZngOFqmoLFD2oj_640x480_115319.jpeg"}
                mTag = response.getString("name");
                UploadPicTag(mTag);
            }else
            {
                uploadFinishDo();
            }
            if (info.statusCode == 401) //授权失效
            {
                getToken();
            }
            if (info.statusCode == 614)//资源已经存在
            {
                uploadFinishDo();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }


    }

    private void uploadFinishDo() {
        if (mUploadingFile!=null)
        {
            mUploadingFile.delete();
            LogUtil.d("delete image file:" + mUploadingFile.getName());
        }
        if (!isFile) {
            start();
        } else {
            if (finishListener != null) {
                finishListener.uploadFinish();//全部上传完成
            }
        }
    }


    public void UploadPicTag(String tag) {
        if (mUploadingFile == null)
        {
            uploadFinishDo();
            return;
        }
        String name = mUploadingFile.getName();

        HashMap<String, Object> data = new HashMap<String, Object>();

        data.put("image", tag);
        data.put("type", OrderPictureHelper.getOrderType(name));

        UploadPicTagRequest request = new UploadPicTagRequest(OrderPictureHelper.getOrderId(name), data, handler);

        RequestManager.addRequest(request, null);
    }

    private ResponseEventHandler<BaseRsp> handler=new ResponseEventHandler<BaseRsp>() {
        @Override
        public void onResponseSuccess(BaseRsp result) {

            uploadFinishDo();
        }

        @Override
        public void onResponseError(VolleyError error)
        {
            UploadPicTag(mTag);
        }
    };


    public interface uploadFinishListener{

        void uploadFinish();
        void nullStop();
    }



}
