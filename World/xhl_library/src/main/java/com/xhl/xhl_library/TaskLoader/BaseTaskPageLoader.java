package com.xhl.xhl_library.TaskLoader;

import android.content.Context;

/**
 * Created by Wendell on 14-9-12.
 */
public abstract class BaseTaskPageLoader<D> extends BaseTaskLoader<D> {

    private int[] mParams;
    private int mStartSign;
    private int mPageSize = -1;
    private int mPageCount = -1;

    public BaseTaskPageLoader(Context context) {
        super(context);
    }
    
    public BaseTaskPageLoader(Context context, boolean canCancelOnStop) {
        super(context, canCancelOnStop);
    }

    protected abstract int getCount(D data);
    /**
     * <p>���ط�ҳ���</>
     * @param isRefresh
     * @param start ��ʼλ�ã���СΪ0
     * @param page ��ʼҳ����СΪ1
     * @return
     * @throws Exception
     */
    protected abstract D loadPageInBackground(boolean isRefresh,int start,int page) throws Exception;
    protected abstract D merge(D old,D add);

    @Override
    public void forceLoad() {
        super.forceLoad();
        mParams = new int[]{-1, 1};
    }

    @Override
    public void forceRefresh() {
        super.forceRefresh();
        mParams = new int[]{-1, 1};
    }

    public void forcePageLoad() {
        super.forceLoad();
        D data = mResult==null?null:mResult.mData;
        mParams = new int[]{data==null?0:getCount(data), mResult==null?1:(mResult.mCurPageCount+1)};
    }

    @Override
    protected final D loadInBackgroundImpl(boolean isRefresh) throws Exception {
        int[] params = mParams;
        int start = params[0];
        int page = params[1];
        if(start == -1) {
            start = 0;
            mStartSign = start;
        }else {
            mStartSign = -1;
            if(isRefresh) { //�����ǿ�����ȡ���ʱisRefreshΪtrue�ᵼ���߼�����
                return null;
            }
        }
        return loadPageInBackground(isRefresh,start,page);
    }

    @Override
    protected void deliverLoadedResult(LoaderResult<D> data) {
        D pageData = data.mData;
        data.mCurPageSize = pageData==null?0:getCount(pageData);
        if(mStartSign == -1 && mResult != null) {
            data.mCurPageCount = mResult.mCurPageCount + 1;
            D oldData = mResult.mData;
            if(oldData != null) {
                boolean isRefresh = data.mIsRefresh;
                int curPageCount = data.mCurPageCount;
                int curPageSize = data.mCurPageSize;
                if(pageData == null) {
                    data = new LoaderResult<D>(oldData);
                }else {
                    D allData = merge(oldData,pageData);
                    if(pageData != allData) {
                        onReleaseData(pageData);
                    }
                    data = new LoaderResult<D>(allData);
                }
                data.mIsRefresh = isRefresh;
                data.mCurPageCount = curPageCount;
                data.mCurPageSize = curPageSize;
            }
        }
        super.deliverLoadedResult(data);
    }
    
    @Override
    protected void deliverLoadedError(LoaderResult<D> data)
    {
        if(mResult == null) {
            data.mCurPageCount = 0;
        }else {
            data.mCurPageCount = mResult.mCurPageCount;
            data.mCurPageSize = mResult.mCurPageSize;
        }
        super.deliverLoadedError(data);
    }

    @Override
    protected void onStartLoading() {
        int resumeType = mResumeType;
        super.onStartLoading();
        if(resumeType == 3 && !isLoading()) {
            forcePageLoad();
        }
    }
    
    @Override
    public void doNotCallOutside()
    {
        boolean isLoading = isLoading();
        super.doNotCallOutside();
        if(isLoading && mParams[0] != -1) {
            mResumeType = 3;
        }
    }
    
    public void setPageSize(int pageSize) {
        if(pageSize <= 0) throw new IllegalArgumentException("pageSize <= 0");
        this.mPageSize = pageSize;
    }
    
    public void setPageCount(int pageCount) {
        if(pageCount < 0) throw new IllegalArgumentException("pageCount < 0");
        this.mPageCount = pageCount;
    }

    public boolean isLoadedAll() {
        if(mResult != null) {
            if(mPageCount == -1) {
                int curPageSize = mResult.mCurPageSize;
                if(curPageSize != -1) {
                    if(mPageSize == -1) {
                        return curPageSize <= 0;
                    }else {
                        return curPageSize < mPageSize;
                    }
                }
            }else {
                return mResult.mCurPageCount >= mPageCount;
            }
        }
        return false;
    }
    
}
