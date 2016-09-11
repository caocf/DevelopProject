package com.moge.gege.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.android.volley.VolleyError;
import com.moge.gege.R;
import com.moge.gege.config.GlobalConfig;
import com.moge.gege.model.AlbumItemModel;
import com.moge.gege.model.BaseModel;
import com.moge.gege.model.RespUploadTokenModel;
import com.moge.gege.model.TopicPublishModel;
import com.moge.gege.network.ErrorCode;
import com.moge.gege.network.request.GetUploadTokenRequest;
import com.moge.gege.network.request.TopicPublishRequest;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.RequestManager;
import com.moge.gege.network.util.ResponseEventHandler;
import com.moge.gege.network.util.WebSocketManager;
import com.moge.gege.network.util.qiniu.auth.JSONObjectRet;
import com.moge.gege.network.util.qiniu.io.IO;
import com.moge.gege.network.util.qiniu.io.PutExtra;
import com.moge.gege.network.util.qiniu.utils.InputStreamAt;
import com.moge.gege.network.util.qiniu.utils.QiniuException;
import com.moge.gege.util.BitmapUtil;
import com.moge.gege.util.LogUtil;
import com.moge.gege.util.ToastUtil;

public class AppService extends Service
{
    private static AppService appService;
    private final IBinder mBinder = new LocalBinder();

    private Map<String, Integer> mNotifyIdMap = new HashMap<String, Integer>();
    private LinkedBlockingQueue<TopicPublishModel> mPublishQueue = new LinkedBlockingQueue<TopicPublishModel>();
    private PublishTopicThread mPublishThread;

    private List<String> mUploadPhotoList = new ArrayList<String>();
    private int mTotalUploadNum = 0;
    private int mTotalUploadSuccessNum = 0;
    private int mTotalUploadFailedNum = 0;
    private static final String PublishTopicTag = "publish_topic_tag";
    private TopicPublishModel mPublishModel;

    @Override
    public IBinder onBind(Intent intent)
    {
        return mBinder;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        appService = this;

        WebSocketManager.instance().init(this);

        mPublishThread = new PublishTopicThread(mPublishQueue);
        mPublishThread.start();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2)
        {
            Notification localNotification = new Notification();
            startForeground(0x111, localNotification);
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        appService = null;
        WebSocketManager.instance().close();
        exitService();

        if (mPublishThread != null && !mPublishThread.isInterrupted())
        {
            mPublishThread.quit();
        }

        stopForeground(true);
        Intent intent = new Intent(
                GlobalConfig.BROADCAST_ACTION_DESTROY_SERVICE);
        sendBroadcast(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        flags = START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }

    public static AppService instance()
    {
        return appService;
    }

    public class LocalBinder extends Binder
    {
        public AppService getService()
        {
            return AppService.this;
        }
    }

    public void connectSocket()
    {
        WebSocketManager.instance().init(this);
    }

    public void reconnectSocket()
    {
        WebSocketManager.instance().reconnect();
    }

    public void disconnectSocket()
    {
        WebSocketManager.instance().close();
    }

    public void showNotity(String tag, int id, String title, String content,
            Intent targetIntent)
    {
        NotificationManager notifManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                this);
        builder.setContentTitle(title)
                .setContentText(content)
                .setContentIntent(contentIntent)
                .setTicker(content)
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setOngoing(false)
                .setDefaults(
                        Notification.DEFAULT_VIBRATE
                                | Notification.DEFAULT_SOUND)
                .setSmallIcon(R.drawable.icon);

        Notification notification = builder.build();
        notification.flags = Notification.FLAG_AUTO_CANCEL;

        notifManager.notify(tag, id, notification);
    }

    public void showPublishTopicNotity(String content, int resId)
    {
        NotificationManager notifManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                this);
        builder.setContentTitle(content).setContentText(content)
                .setTicker(content).setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_DEFAULT).setAutoCancel(true)
                .setOngoing(false).setSmallIcon(resId);

        Notification notification = builder.build();
        notifManager.notify(PublishTopicTag, 0, notification);
    }

    public void removeNotify(String tag, int id)
    {
        NotificationManager mNotifManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifManager.cancel(tag, id);
    }

    public void removeAllNotify()
    {
        NotificationManager mNotifManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifManager.cancelAll();
    }

    public void exitService()
    {
        removeAllNotify();
    }

    public void pushRequestToService(TopicPublishModel model)
    {
        mPublishQueue.add(model);
    }

    private class PublishTopicThread extends Thread
    {
        private LinkedBlockingQueue<TopicPublishModel> mRequestQueue;
        private volatile boolean mQuit = false;

        public PublishTopicThread(LinkedBlockingQueue<TopicPublishModel> queue)
        {
            mRequestQueue = queue;
        }

        @Override
        public void run()
        {
            TopicPublishModel request;
            while (true)
            {
                try
                {
                    request = mRequestQueue.take();
                }
                catch (InterruptedException e)
                {
                    if (mQuit)
                    {
                        return;
                    }
                    continue;
                }

                try
                {
                    showPublishTopicNotity(getString(R.string.sending_topic),
                            R.drawable.icon_send_topic);

                    if (request.getImageList() != null
                            && request.getImageList().size() > 0)
                    {
                        doGetUploadToken(request);
                    }
                    else
                    {
                        doTopicPublishRequest(request.getTopicType(),
                                URLEncodedUtils.format(request.getParamList(),
                                        HTTP.UTF_8));
                    }
                }
                catch (Exception e)
                {
                }
            }

        }

        public void quit()
        {
            mQuit = true;
            interrupt();
        }
    }

    ;

    private void notifyTopicPublishSuccess(Context context)
    {
        Intent intent = new Intent();
        intent.setAction(GlobalConfig.BROADCAST_ACTION_PUBLISH_TOPIC);
        context.sendBroadcast(intent);
    }

    private void doUploadPhotoList(String token, TopicPublishModel model)
    {
        for (int i = 0; i < model.getImageList().size(); i++)
        {
            AlbumItemModel itemModel = model.getImageList().get(i);
            doUploadPhoto(itemModel.getPath(), token);
        }

        mUploadPhotoList.clear();
        mTotalUploadNum = model.getImageList().size();
        mTotalUploadSuccessNum = 0;
        mTotalUploadFailedNum = 0;

        mPublishModel = model;
        doCheckUploadFinish(model);
    }

    private void doCheckUploadFinish(TopicPublishModel model)
    {
        if (mTotalUploadSuccessNum + mTotalUploadFailedNum >= mTotalUploadNum)
        {
            for (int i = 0; i < mUploadPhotoList.size(); i++)
            {
                model.getParamList().add(
                        new BasicNameValuePair("images", mUploadPhotoList
                                .get(i)));
            }

            String params = URLEncodedUtils.format(model.getParamList(),
                    HTTP.UTF_8);

            doTopicPublishRequest(model.getTopicType(), params);
        }
        else
        {
            new Handler().postDelayed(new Runnable()
            {
                public void run()
                {
                    doCheckUploadFinish(mPublishModel);
                }
            }, 500);
        }

    }

    private void doGetUploadToken(TopicPublishModel model)
    {
        GetUploadTokenRequest request = new GetUploadTokenRequest(model,
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
                            doUploadPhotoList(uploadToken,
                                    ((GetUploadTokenRequest) request)
                                            .getPublishModel());
                        }
                        else
                        {
                            removeNotify(PublishTopicTag, 0);
                            ToastUtil.showToastShort(result.getMsg());
                        }
                    }

                    @Override
                    public void onResponseError(VolleyError error)
                    {
                        removeNotify(PublishTopicTag, 0);
                        ToastUtil.showToastShort(error.getMessage());
                    }
                });
        RequestManager.addRequest(request, this);
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
                            showPublishTopicNotity(
                                    getString(R.string.send_topic_success),
                                    R.drawable.icon_send_topic_success);
                            notifyTopicPublishSuccess(AppService.this);
                        }
                        else
                        {
                            showPublishTopicNotity(
                                    getString(R.string.send_topic_failed),
                                    R.drawable.icon_send_topic_failed);
                        }

                        removeNotify(PublishTopicTag, 0);
                    }

                    @Override
                    public void onResponseError(VolleyError error)
                    {
                        showPublishTopicNotity(
                                getString(R.string.send_topic_failed),
                                R.drawable.icon_send_topic_failed);
                        removeNotify(PublishTopicTag, 0);
                    }

                });
        RequestManager.addRequest(request, this);
    }
}
