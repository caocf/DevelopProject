package com.xhl.xhl_library.TaskLoader;

/**
 * Created by Wendell on 14-8-20.
 */
public class LoaderResult<D> {
    
    D mData = null;
    Exception mException = null;
    Object mProgress = null;
    
    boolean mIsNew = true;
    boolean mIsRefresh = false;
    
    int mCurPageCount = 1;
    int mCurPageSize = -1;
    
    public LoaderResult(D data) {
        mData = data;
    }
    
    public D getData() {
        if(mException != null || mProgress != null) return null;
        return mData;
    }
    
    public Exception getException() {
        return mException;
    }

}
