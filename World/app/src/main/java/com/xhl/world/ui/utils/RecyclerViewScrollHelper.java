package com.xhl.world.ui.utils;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.xutils.common.util.LogUtil;

/**
 * Created by Sum on 15/12/18.
 */
public class RecyclerViewScrollHelper {

    private LinearLayoutManager mLayoutManger;
    private RecyclerView mRecyclerView;
    private static final int TARGET_LEFT = 0x0;
    private static final int TARGET_CENTER = 0x1;
    private static final int TARGET_RIGHT = 0x2;
    private int mTargetLocation = -1;

    private int mCurPos = -1;
    private boolean isLeft = false;
    private boolean isRight = false;
    private int mCenterEdgeOffsetCount = 2;

    public RecyclerViewScrollHelper(LinearLayoutManager manager, RecyclerView recyclerView) {
        mLayoutManger = manager;
        mRecyclerView = recyclerView;
    }


    public void ScrollToPosition(int position) {
        if (mCurPos == position) {
            return;
        }
        if (mCurPos < position) {
            isRight = true;
            isLeft = false;
        } else {
            isRight = false;
            isLeft = true;
        }
        mCurPos = position;
        //当前View位置信息
        int firstVisibleItem = mLayoutManger.findFirstVisibleItemPosition();
        int visibleItemCount = mLayoutManger.getChildCount();
        int totalItemCount = mLayoutManger.getItemCount();
        int lastVisibleItemPosition = mLayoutManger.findLastVisibleItemPosition();

        LogUtil.e("" + firstVisibleItem + " " + visibleItemCount + " " + totalItemCount + " " + lastVisibleItemPosition);
        int targetPos = 0;
        if (isRight) {
            targetPos = position + mCenterEdgeOffsetCount;
            if (position - firstVisibleItem < mCenterEdgeOffsetCount) {
                targetPos = position - mCenterEdgeOffsetCount;
                if (targetPos < 0) {
                    targetPos = 0;
                }
            }
        } else if (isLeft) {
            targetPos = position - mCenterEdgeOffsetCount;
            if (position - lastVisibleItemPosition > mCenterEdgeOffsetCount) {
                targetPos = targetPos - lastVisibleItemPosition + visibleItemCount + mCenterEdgeOffsetCount + 1;
            } else if (position - lastVisibleItemPosition < mCenterEdgeOffsetCount) {
                targetPos = position + mCenterEdgeOffsetCount;
            }
        }
        if (targetPos >= 0) {
            mLayoutManger.smoothScrollToPosition(mRecyclerView, null, targetPos);
        }

        LogUtil.e("position:" + position + " targetPos:" + targetPos + " isLeft:" + isLeft + " isRight:" + isRight);

    }

    public void scrollToPos(int position) {
        if (mCurPos == position) {
            return;
        }
        //判断View的显示位置信息
        int visibleItemCount = mLayoutManger.getChildCount();
        int totalItemCount = mLayoutManger.getItemCount();
        if (visibleItemCount == totalItemCount) {
            return;
        }
        int firstVisibleItem = mLayoutManger.findFirstVisibleItemPosition();
        int lastVisibleItemPosition = mLayoutManger.findLastVisibleItemPosition();

        if (firstVisibleItem > position) {
            mTargetLocation = TARGET_LEFT;
        } else if (lastVisibleItemPosition < position) {
            mTargetLocation = TARGET_RIGHT;
        } else {
            mTargetLocation = TARGET_CENTER;
        }

        if (mCurPos < position) {
            isRight = true;
            isLeft = false;
        } else {
            isRight = false;
            isLeft = true;
        }
        mCurPos = position;
        // LogUtil.e("" + firstVisibleItem + " " + visibleItemCount + " " + totalItemCount + " " + lastVisibleItemPosition);
        boolean isNeedScroll = false;
        int targetLocation = 0;
        switch (mTargetLocation) {
            case TARGET_LEFT:
                targetLocation = position - mCenterEdgeOffsetCount;
                if (targetLocation < 0) {
                    targetLocation = 0;
                }
                isNeedScroll = true;
                break;
            case TARGET_CENTER:
                if (position == lastVisibleItemPosition) {
                    targetLocation = lastVisibleItemPosition + visibleItemCount - 1;
                    isNeedScroll = true;
                } else if (position == firstVisibleItem) {
                    isNeedScroll = true;
                    targetLocation = firstVisibleItem - visibleItemCount + 1;
                }

                break;
            case TARGET_RIGHT:
                targetLocation = position + mCenterEdgeOffsetCount;
                isNeedScroll = true;
                break;
        }

        if (targetLocation >= 0 && isNeedScroll) {
            mLayoutManger.smoothScrollToPosition(mRecyclerView, null, targetLocation);
        }
        //  LogUtil.e("position:" + position + " targetLocation:" + targetLocation + " isNeedScroll:" + isNeedScroll);
    }

    /**
     * 滚动RecyclerView当中间View距离偏移的位置
     */
    public void setmCenterEdgeOffsetCount(int centerEdgeOffsetCount) {
        this.mCenterEdgeOffsetCount = centerEdgeOffsetCount;
    }
}
