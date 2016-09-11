package com.moge.gege.ui.widget.update;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;

import com.android.volley.VolleyError;
import com.moge.gege.AppApplication;
import com.moge.gege.R;
import com.moge.gege.config.NetworkConfig;
import com.moge.gege.data.PersistentData;
import com.moge.gege.model.AppInitModel;
import com.moge.gege.model.RespAppInitModel;
import com.moge.gege.model.enums.UpdateResult;
import com.moge.gege.network.ErrorCode;
import com.moge.gege.network.request.AppInitRequest;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.RequestManager;
import com.moge.gege.network.util.ResponseEventHandler;
import com.moge.gege.ui.BaseActivity;
import com.moge.gege.util.FunctionUtils;
import com.moge.gege.util.LogUtil;
import com.moge.gege.util.widget.CustomDialog;

public class VersionUpdate
{
    private static final String APK_NAME = "gege.apk";
    private Activity mActivity;
    private AppApplication mApplication;
    private String mDownloadUrl;
    private boolean isMustUpdate;
    private UpdateResultListener mListener;

    public interface UpdateResultListener
    {
        public void onNotUpdate(int result);
    }

    public VersionUpdate(BaseActivity activity, UpdateResultListener listener)
    {
        this.mActivity = activity;
        mApplication = AppApplication.instance();
        mListener = listener;

        AppInitRequest checkUpdateRequest = new AppInitRequest(
                new ResponseEventHandler<RespAppInitModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespAppInitModel> request,
                            RespAppInitModel result)
                    {
                        if (result != null
                                && result.getStatus() == ErrorCode.SUCCESS)
                        {
                            AppInitModel model = result.getData();
                            if (null != model)
                            {
                                // update server and websocket address
                                if (model.getConfig() != null)
                                {
                                    NetworkConfig.generalAddress = deleteSuffix(
                                            model.getConfig()
                                                    .getAPI_URL_PREFIX(), "/");
                                    PersistentData
                                            .instance()
                                            .setGeneralApiAddress(
                                                    NetworkConfig.generalAddress);

                                    NetworkConfig.chatAddress = deleteSuffix(
                                            model.getConfig()
                                                    .getMSG_URL_PREFIX(), "/");
                                    PersistentData.instance().setChatAddress(
                                            NetworkConfig.chatAddress);

                                    NetworkConfig.payAddress = deleteSuffix(
                                            model.getConfig()
                                                    .getPAY_URL_PREFIX(), "/");
                                    PersistentData.instance().setPayAddress(
                                            NetworkConfig.payAddress);

                                    NetworkConfig.imageAddress = addSuffix(
                                            model.getConfig()
                                                    .getIMG_URL_PREFIX(), "/");
                                    PersistentData.instance().setImageAddress(
                                            NetworkConfig.imageAddress);

                                    NetworkConfig.chatWebsocketAddress = deleteSuffix(
                                            model.getConfig()
                                                    .getPOLL_URL_PREFIX(), "/");
                                    PersistentData
                                            .instance()
                                            .setChatWebsocketAddress(
                                                    NetworkConfig.chatWebsocketAddress);
                                }

                                int update = model.getUpdate();
                                if (update > 0)
                                {
                                    mDownloadUrl = model.getUrl();

                                    // must update
                                    if (update == 2)
                                    {
                                        isMustUpdate = true;
                                        new DownLoadTask().execute();
                                        return;
                                    }

                                    String updescText = model.getMsg();
                                    String versionName = model
                                            .getNewest_version();
                                    displayDialog(updescText, versionName);
                                    return;
                                }
                            }
                        }

                        // notify other listener
                        if (mListener != null)
                        {
                            mListener
                                    .onNotUpdate(UpdateResult.NO_UPDATE_VERSION);
                        }
                    }

                    @Override
                    public void onResponseError(VolleyError error)
                    {
                        // notify other listener
                        if (mListener != null)
                        {
                            mListener.onNotUpdate(UpdateResult.REQUEST_FAILED);
                        }
                    }

                });
        RequestManager.addRequest(checkUpdateRequest, activity);
    }

    private String addSuffix(String src, String suffix)
    {
        if (TextUtils.isEmpty(src))
        {
            return null;
        }
        return src.endsWith(suffix) ? src : src + suffix;
    }

    private String deleteSuffix(String src, String suffix)
    {
        if (TextUtils.isEmpty(src))
        {
            return null;
        }
        return src.endsWith(suffix) ? src.substring(0, src.length() - 1) : src;
    }

    public void displayDialog(String updescText, String versionName)
    {
        // AlertDialog.Builder builer = new Builder(mActivity);
        // builer.setTitle("版本升级" + versionName);
        // builer.setMessage(updescText);
        // builer.setPositiveButton("确定", new DialogInterface.OnClickListener()
        // {
        // public void onClick(DialogInterface dialog, int which)
        // {
        // new DownLoadTask().execute();
        // }
        // });
        // builer.setNegativeButton("下次再说", new
        // DialogInterface.OnClickListener()
        // {
        // public void onClick(DialogInterface dialog, int which)
        // {
        // // notify other listener
        // if (mListener != null)
        // {
        // mListener.onNotUpdate(UpdateResult.USER_NOT_SELECT);
        // }
        // }
        // });
        // AlertDialog dialog = builer.create();
        // dialog.setCancelable(false);
        // dialog.show();

        CustomDialog dialog = new CustomDialog.Builder(mActivity)
                .setTitle(
                        mActivity.getResources().getString(
                                R.string.version_update)
                                + versionName)
                .setMessage(updescText)
                .setPositiveButton(R.string.general_confirm,
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog,
                                    int which)
                            {
                                new DownLoadTask().execute();
                                dialog.dismiss();
                            }

                        })
                .setNegativeButton(R.string.donot_update,
                        new DialogInterface.OnClickListener()
                        {

                            @Override
                            public void onClick(DialogInterface dialog,
                                    int which)
                            {
                                if (mListener != null)
                                {
                                    mListener
                                            .onNotUpdate(UpdateResult.USER_NOT_SELECT);
                                }
                                dialog.dismiss();
                            }

                        }).create();

        dialog.setCancelable(false);
        dialog.show();
    }

    private class DownLoadTask extends AsyncTask<Void, Integer, Boolean>
    {
        private ProgressDialog mProgressDialog;

        private int preCurLength;

        private boolean isPause;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(mActivity);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.setMessage("正在下载新版本");
            mProgressDialog.setCanceledOnTouchOutside(false);
            if (isMustUpdate)
            {
                mProgressDialog.setCancelable(false);
            }
            else
            {
                mProgressDialog.setCancelable(true);
            }

            mProgressDialog.setOnCancelListener(new OnCancelListener()
            {
                @Override
                public void onCancel(DialogInterface dialog)
                {
                    isPause = true;

                    if (isMustUpdate)
                    {
                        mApplication.exit();
                    }
                }
            });
            mProgressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params)
        {
            return downLoad(mDownloadUrl);
        }

        @Override
        protected void onProgressUpdate(Integer... values)
        {
            super.onProgressUpdate(values);
            mProgressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Boolean result)
        {
            super.onPostExecute(result);
            mProgressDialog.dismiss();
            if (result && !isPause)
            {
                install(mActivity);
            }
            else
            {
                if (isMustUpdate)
                {
                    mApplication.exit();
                }
                FunctionUtils.showToastShort(mActivity.getResources()
                        .getString(R.string.download_failed));

                if (mListener != null)
                {
                    mListener.onNotUpdate(UpdateResult.DOWNLOAD_FAILED);
                }
            }
        }

        private boolean downLoad(String downLoadUrl)
        {
            FileOutputStream fileOutputStream = null;
            BufferedInputStream bis = null;
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(downLoadUrl);
            HttpResponse response;
            try
            {
                response = httpClient.execute(httpGet);
            }
            catch (Exception e)
            {
                LogUtil.logException(e);
                return false;
            }
            if (null != response
                    && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
            {
                HttpEntity entity = response.getEntity();
                if (null != entity)
                {
                    long totalLength = entity.getContentLength();
                    if (totalLength > 0)
                    {
                        mProgressDialog.setMax(100);
                        try
                        {
                            InputStream in = entity.getContent();
                            if (null != in)
                            {
                                File file = getFile();
                                if (file.exists())
                                {
                                    file.delete();
                                }
                                fileOutputStream = new FileOutputStream(
                                        getFile());
                                bis = new BufferedInputStream(in);
                                byte[] buf = new byte[1024];
                                int ch = -1;
                                long total = 0;
                                while ((ch = bis.read(buf)) != -1)
                                {
                                    if (isPause)
                                    {
                                        break;
                                    }

                                    fileOutputStream.write(buf, 0, ch);
                                    total += ch;
                                    int curLength = (int) ((total * 1.0 / totalLength) * 100 % 100);
                                    if (total == totalLength)
                                    {
                                        curLength = 100;
                                    }
                                    if (preCurLength != curLength)
                                    {
                                        publishProgress(curLength);
                                        preCurLength = curLength;
                                    }
                                }
                            }
                            else
                            {
                                return false;
                            }
                        }
                        catch (Exception e)
                        {
                            LogUtil.logException(e);
                            return false;
                        }
                        finally
                        {
                            try
                            {
                                if (fileOutputStream != null)
                                {
                                    fileOutputStream.flush();
                                    fileOutputStream.close();
                                    fileOutputStream = null;
                                }

                                if (bis != null)
                                {
                                    bis.close();
                                    bis = null;
                                }
                            }
                            catch (Exception e)
                            {
                                LogUtil.logException(e);
                            }
                        }
                    }
                    else
                    {
                        return false;
                    }
                }
                else
                {
                    return false;
                }
            }
            else
            {
                return false;
            }

            return true;
        }
    }

    private File getFile()
    {
        File file = null;
        boolean isHaveSDCard = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
        if (isHaveSDCard)
        {
            file = new File(Environment.getExternalStorageDirectory() + "/"
                    + APK_NAME);
        }
        else
        {
            file = new File(mApplication.getFilesDir() + "/" + APK_NAME);
        }
        return file;
    }

    private void install(Context context)
    {
        String[] args = { "chmod", "604", getFile().getAbsolutePath() };
        exec(args);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(getFile()),
                "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        if (isMustUpdate)
        {
            mApplication.exit();
        }
    }

    private static String exec(String[] args)
    {
        String result = "";
        ProcessBuilder processBuilder = new ProcessBuilder(args);
        Process process = null;
        InputStream errIs = null;
        InputStream inIs = null;
        try
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int read = -1;
            process = processBuilder.start();
            errIs = process.getErrorStream();
            while ((read = errIs.read()) != -1)
            {
                baos.write(read);
            }
            baos.write('\n');
            inIs = process.getInputStream();
            while ((read = inIs.read()) != -1)
            {
                baos.write(read);
            }
            byte[] data = baos.toByteArray();
            result = new String(data);
        }
        catch (IOException e)
        {
            LogUtil.logException(e);
        }
        catch (Exception e)
        {
            LogUtil.logException(e);
        }
        finally
        {
            try
            {
                if (errIs != null)
                {
                    errIs.close();
                }
                if (inIs != null)
                {
                    inIs.close();
                }
            }
            catch (IOException e)
            {
                LogUtil.logException(e);
            }
            if (process != null)
            {
                process.destroy();
            }
        }
        LogUtil.e(result);
        return result;
    }

}
