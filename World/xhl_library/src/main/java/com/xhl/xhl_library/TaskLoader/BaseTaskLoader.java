package com.xhl.xhl_library.TaskLoader;

import android.content.Context;
import android.os.Handler;
import android.support.v4.content.AsyncTaskLoader;

import com.xhl.xhl_library.utils.execption.ReflectHiddenFuncException;

import java.lang.reflect.Field;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by Wendell on 14-8-20.
 */
public abstract class BaseTaskLoader<D> extends AsyncTaskLoader<LoaderResult<D>> {

    private ForceLoadContentObserver mObserver = null;
    private boolean mIsRefresh = false;
    private boolean mIsRefreshing = false;
    private boolean mIsLoading = false;
    private LoaderResult<D> mLoadedResult = null;
    LoaderResult<D> mResult = null;
    int mResumeType = 0;
    public boolean isUserVisibleHint = true;
    private boolean mCanCancelOnStop = true;
    private boolean mIsCancelled = false;
    private Handler mHandler = new Handler();
    
    public static ThreadPoolExecutor getThreadPoolExecutor() throws ReflectHiddenFuncException {
        try {
            Field field = Class.forName("android.support.v4.content.ModernAsyncTask").getDeclaredField("THREAD_POOL_EXECUTOR");
            field.setAccessible(true);
            Object obj = field.get(null);
            if(obj instanceof ThreadPoolExecutor) return (ThreadPoolExecutor)obj;
            else throw new ReflectHiddenFuncException("the field is not ThreadPoolExecutor.");
        }catch(ClassNotFoundException e) {
            throw new ReflectHiddenFuncException(e);
        }catch(NoSuchFieldException e) {
            throw new ReflectHiddenFuncException(e);
        }catch(IllegalAccessException e) {
            throw new ReflectHiddenFuncException(e);
        }
    }

    public BaseTaskLoader(Context context) {
        this(context,true);
    }
    
    public BaseTaskLoader(Context context,boolean canCancelOnStop) {
        super(context);
        mObserver = new ForceLoadContentObserver();
        mCanCancelOnStop = canCancelOnStop;
    }

    @Override
    public void forceLoad() {
        mIsRefresh = false;
        super.forceLoad();
        mIsRefreshing = false;
        mIsLoading = true;
    }

    public void forceRefresh() {
        mIsRefresh = true;
        super.forceLoad();
        mIsRefreshing = true;
        mIsLoading = true;
    }

    @Override
    public boolean cancelLoad() {
        boolean returnVal = super.cancelLoad();
        if(mIsLoading) {
            mIsCancelled = true;
        }
        mIsRefreshing = false;
        mIsLoading = false;
        return returnVal;
    }

    public boolean isRefreshing() {
        return mIsRefreshing;
    }

    public boolean isLoading() {
        return mIsLoading;
    }
    
    public boolean canCancelOnStop() {
        return mCanCancelOnStop;
    }
    
    public boolean isCancelled() {
        return mIsCancelled;
    }
    
    public void publishProgress(final Object progress) {
        if(progress == null) throw new NullPointerException();
        mHandler.post(new Runnable()
        {
            @Override
            public void run()
            {
                if(!mIsLoading) return;
                if(mIsCancelled) return;
                LoaderResult<D> result = new LoaderResult<D>(null);
                result.mProgress = progress;
                result.mIsRefresh = mIsRefresh;
                if(mResult != null) {
                    result.mData = mResult.mData;
                    result.mException = mResult.mException;
                }
                deliverResult(result);
            }
        });
    }

    @Override
    public final LoaderResult<D> loadInBackground() {
        D data = null;
        boolean isRefresh = mIsRefresh;
        try {
            data = loadInBackgroundImpl(isRefresh);
        }catch (Exception e) {
            mLoadedResult = new LoaderResult<D>(null);
            mLoadedResult.mException = e;
            mLoadedResult.mIsRefresh = isRefresh;
            return mLoadedResult;
        }
        mLoadedResult = new LoaderResult<D>(data);
        mLoadedResult.mIsRefresh = isRefresh;
        return mLoadedResult;
    }

    @Override
    public final void deliverResult(LoaderResult<D> data) {
        if(mLoadedResult != null && mLoadedResult == data) {
            mIsRefreshing = false;
            mIsLoading = false;
            mLoadedResult = null;
            if(data.mException == null) deliverLoadedResult(data);
            else deliverLoadedError(data);
            return;
        }
        D curData = data==null?null:data.mData;
        if(isReset()) {
            if(curData != null) {
                onReleaseData(curData);
            }
            return;
        }
        LoaderResult<D> oldResult = mResult;
        mResult = data;
        if(oldResult == null || oldResult.mData != curData) {
            if(curData != null) {
                try {
                    registerContentObserver(curData, mObserver);
                }catch (RuntimeException e) {
                    onReleaseData(curData);
                    throw e;
                }
            }
        }
        if(isStarted()) {
            super.deliverResult(data);
        }
        if(oldResult != null) {
            D oldData = oldResult.mData;
            if(oldData != curData) {
                if(oldData != null) {
                    onReleaseData(oldData);
                }
            }
        }
    }

    protected void deliverLoadedResult(LoaderResult<D> data) {
        deliverResult(data);
    }
    
    protected void deliverLoadedError(LoaderResult<D> data) {
        data.mData = mResult==null?null:mResult.mData;
        deliverResult(data);
    }

    @Override
    protected void onStartLoading() {
        if(!isUserVisibleHint) return;
        if(mResult != null) {
            deliverResult(mResult);
        }
        int resumeType = mResumeType;
        mResumeType = 0;
        boolean takeContentChanged = takeContentChanged();
        if(resumeType == 1) {
            forceRefresh();
        }else if(resumeType == 2 || takeContentChanged) {
            forceLoad();
        }else if(mResult == null) {
            if(!mIsLoading) forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        if(mCanCancelOnStop) {
            doNotCallOutside();
        }
    }
    
    public void doNotCallOutside() {
        if(mIsLoading) {
            mResumeType = mIsRefresh?1:2;
        }
        cancelLoad();
    }
    
    @Override
    public void onCanceled(LoaderResult<D> data) {
        if(data != null) {
            D curData = data.mData;
            if(curData != null) {
                onReleaseData(curData);
            }
        }
        mIsCancelled = false;
    }

    @Override
    protected void onReset() {
        super.onReset();
        cancelLoad();
        if(mResult != null) {
            D curData = mResult.mData;
            if(curData != null) {
                onReleaseData(curData);
            }
        }
        mResult = null;
    }
    
    protected void registerContentObserver(D data,ForceLoadContentObserver observer) {
    }

    protected abstract D loadInBackgroundImpl(boolean isRefresh) throws Exception;
    protected abstract void onReleaseData(D data);

}
