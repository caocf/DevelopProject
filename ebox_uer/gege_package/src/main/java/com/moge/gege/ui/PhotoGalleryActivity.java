package com.moge.gege.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.moge.gege.R;
import com.moge.gege.config.NetworkConfig;
import com.moge.gege.network.util.RequestManager;
import com.moge.gege.util.EnviromentUtil;
import com.moge.gege.util.ImageLoaderUtil;
import com.moge.gege.util.ToastUtil;
import com.moge.gege.util.widget.photoview.HackyViewPager;
import com.moge.gege.util.widget.photoview.PhotoView;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

public class PhotoGalleryActivity extends BaseActivity implements
        OnClickListener, OnPageChangeListener
{
    private Context mContext;
    private HackyViewPager mPhotoViewPager;
    private TextView mBackText;
    private TextView mIndicatorText;
    private TextView mSaveText;

    private ImagePagerAdapter mAdapter;
    private Map<Integer, PhotoView> mViewMap = new HashMap<Integer, PhotoView>();
    private ArrayList<String> mPhotoUrlList;
    private int mCurrentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photogallery);

        mPhotoUrlList = getIntent().getStringArrayListExtra("photolist");
        mCurrentIndex = getIntent().getIntExtra("index", 0);

        mContext = PhotoGalleryActivity.this;
        initView();
    }

    @Override
    protected void initView()
    {
        super.initView();

        mPhotoViewPager = (HackyViewPager) this
                .findViewById(R.id.photoViewPager);
        mBackText = (TextView) this.findViewById(R.id.backText);
        mBackText.setOnClickListener(this);
        mIndicatorText = (TextView) this.findViewById(R.id.indicatorText);
        mSaveText = (TextView) this.findViewById(R.id.saveText);
        mSaveText.setOnClickListener(this);

        mAdapter = new ImagePagerAdapter();
        mPhotoViewPager.setAdapter(mAdapter);
        mPhotoViewPager.setOnPageChangeListener(this);
        mPhotoViewPager.setCurrentItem(mCurrentIndex);
        mIndicatorText.setText(getString(R.string.photo_indicator,
                mCurrentIndex + 1, mPhotoUrlList.size()));

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
            default:
                break;
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.backText:
                finish();
                break;
            case R.id.saveText:
                onSaveCurrentPhoto();
                break;
            default:
                break;
        }
    }

    private void onSaveCurrentPhoto()
    {
        try
        {
            Bitmap localBitmap = ((BitmapDrawable) ((PhotoView) mViewMap
                    .get(Integer.valueOf(mCurrentIndex))).getDrawable())
                    .getBitmap();
            String str1 = EnviromentUtil.getExternalPhotoSavePath("gege");
            String str2 = getFileName();
            File localFile = new File(str1 + File.separator + str2);
            FileOutputStream localFileOutputStream = new FileOutputStream(
                    localFile);
            localBitmap.compress(Bitmap.CompressFormat.JPEG, 100,
                    localFileOutputStream);
            sendBroadcast(new Intent(
                    "android.intent.action.MEDIA_SCANNER_SCAN_FILE",
                    Uri.fromFile(localFile)));
            saveSuccess(localFile.getAbsolutePath());
            return;
        }
        catch (FileNotFoundException localFileNotFoundException)
        {
            saveFail();
        }
    }

    private String getFileName()
    {
        return System.currentTimeMillis() + ".jpg";
    }

    void saveSuccess(String filename)
    {
        ToastUtil.showToastShort(getString(R.string.save_success, filename));
    }

    private void saveFail()
    {
        ToastUtil.showToastShort(R.string.save_fail);
    }

    private class ImagePagerAdapter extends PagerAdapter
    {
        private ImagePagerAdapter()
        {
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object)
        {
            container.removeView((View) object);
            mViewMap.remove(Integer.valueOf(position));
        }

        @Override
        public int getCount()
        {
            return mPhotoUrlList.size();
        }

        @Override
        public int getItemPosition(Object object)
        {
            View localView = (View) object;
            int i = mPhotoUrlList.indexOf(localView.getTag());
            if (i == -1)
                i = -2;
            return i;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position)
        {
            View localView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_photogallery, container, false);
            PhotoView localPhotoView = (PhotoView) localView
                    .findViewById(R.id.image);
            localPhotoView.setTag((ProgressBar) localView
                    .findViewById(R.id.loading));

            // support local and network image
            String url = mPhotoUrlList.get(position);
            if (!url.startsWith("file://"))
            {
                url = NetworkConfig.imageAddress
                        + RequestManager.getImageUrl(url);
            }

            ImageLoaderUtil.instance().displayImage(url, localPhotoView,
                    R.drawable.icon_default, new ImageLoadingListener()
                    {
                        public void onLoadingStarted(String imageUri, View view)
                        {
                            ((ProgressBar) view.getTag())
                                    .setVisibility(View.VISIBLE);
                            view.setVisibility(View.GONE);
                        }

                        public void onLoadingCancelled(String imageUri,
                                View view)
                        {
                            ((ProgressBar) view.getTag())
                                    .setVisibility(View.GONE);
                            view.setVisibility(View.VISIBLE);
                        }

                        public void onLoadingComplete(String imageUri,
                                View view, Bitmap loadedImage)
                        {
                            PhotoView localPhotoView = (PhotoView) view;
                            ((ProgressBar) localPhotoView.getTag())
                                    .setVisibility(View.GONE);
                            view.setVisibility(View.VISIBLE);
                            localPhotoView.setImageBitmap(loadedImage);
                            mViewMap.put(Integer.valueOf(position),
                                    localPhotoView);
                        }

                        public void onLoadingFailed(String imageUri, View view,
                                FailReason failReason)
                        {
                            ToastUtil
                                    .showToastShort(getString(R.string.load_photo_fail));
                            ((ProgressBar) ((PhotoView) view).getTag())
                                    .setVisibility(View.GONE);
                            view.setVisibility(View.VISIBLE);
                        }

                    }, new ImageLoadingProgressListener()
                    {
                        public void onProgressUpdate(String imageUri,
                                View view, int current, int total)
                        {
                        }
                    });
            container.addView(localView, 0);
            localView.setTag(mPhotoUrlList.get(position));
            return localView;
        }

        @Override
        public boolean isViewFromObject(View view, Object object)
        {
            return view == object;
        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0)
    {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2)
    {

    }

    @Override
    public void onPageSelected(int arg0)
    {
        this.mCurrentIndex = arg0;
        mIndicatorText.setText(getString(R.string.photo_indicator,
                mCurrentIndex + 1, mPhotoUrlList.size()));

    }
}
