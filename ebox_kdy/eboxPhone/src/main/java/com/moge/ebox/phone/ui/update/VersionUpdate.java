package com.moge.ebox.phone.ui.update;

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
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;

import com.moge.ebox.phone.bettle.base.EboxApplication;
import com.moge.ebox.phone.ui.BaseActivity;
import com.moge.ebox.phone.utils.FunctionUtils;
import com.moge.ebox.phone.utils.LogUtil;
import com.moge.ebox.phone.utils.PackageUtil;

public class VersionUpdate
{
    private static final String APK_NAME = "eboxphone.apk";
    private Activity activity;
    private EboxApplication mApplication;
    private String mDownloadUrl="http://mogefiles.qiniudn.com/dev_gege_android_1.1.0.apk";
    private boolean isMustUpdate;
    private UpdateResultListener mListener;

    public interface UpdateResultListener
    {
        public void onNotUpdate();
    }

    public VersionUpdate(BaseActivity activity, UpdateResultListener listener)
    {
        this.activity = activity;
        mApplication = EboxApplication.getInstance();
        mListener = listener;
        checkNewVersion();
    }
    
    public VersionUpdate(BaseActivity activity)
    {
        this.activity = activity;
        mApplication = EboxApplication.getInstance();
        checkNewVersion();
    }

    private void checkNewVersion() {
    	String versionName = PackageUtil.getVersionName(mApplication);
    	  new DownLoadTask().execute();
	}

	public void displayDialog(String updescText, String versionName)
    {
        AlertDialog.Builder builer = new Builder(activity);
        builer.setTitle("发现新版本:" + versionName);
        builer.setMessage(updescText);
        builer.setPositiveButton("确定", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                new DownLoadTask().execute();
            }
        });
        builer.setNegativeButton("下次再说", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                // notify other listener
                if (mListener != null)
                {
                    mListener.onNotUpdate();
                }
            }
        });
        AlertDialog dialog = builer.create();
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
            mProgressDialog = new ProgressDialog(activity);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.setMessage("正在下载新版本");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setCancelable(false);
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
                install(activity);
            }
            else
            {
                if (isMustUpdate)
                {
                    mApplication.exit();
                }
                FunctionUtils.showToastShort("下载新版本失败，请重试");
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
