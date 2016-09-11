package com.moge.gege.util;

/**
 * Created by sam on 2014/12/8.
 */

import android.os.Handler;
import android.os.Message;

public abstract class MyCountDownTimer
{
    private final long mCountdownInterval;
    private long mRemainTime;
    private long mRemainEndTime;

    public MyCountDownTimer(long millisStartTime, long millisEndTime,
            long countDownInterval)
    {
        mCountdownInterval = countDownInterval;
        mRemainTime = millisStartTime;
        mRemainEndTime = millisEndTime;
    }

    public final void setValue(long millisStartTime, long millisEndTime)
    {
        mRemainTime = millisStartTime;
        mRemainEndTime = millisEndTime;
    }

    public final void cancel()
    {
        mHandler.removeMessages(MSG_RUN);
        mHandler.removeMessages(MSG_PAUSE);
    }

    public final void resume()
    {
        mHandler.removeMessages(MSG_PAUSE);
        mHandler.sendMessageAtFrontOfQueue(mHandler.obtainMessage(MSG_RUN));
    }

    public final void pause()
    {
        mHandler.removeMessages(MSG_RUN);
        mHandler.sendMessageAtFrontOfQueue(mHandler.obtainMessage(MSG_PAUSE));
    }

    public synchronized final MyCountDownTimer start()
    {
        if (mRemainTime <= 0 && mRemainEndTime <= 0)
        {
            onFinish();
            return this;
        }

        // first time
        if (mRemainTime > 0)
        {
            onTick(true, mRemainTime);
        }
        else if (mRemainEndTime > 0)
        {
            onTick(false, mRemainEndTime);
        }

        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_RUN),
                mCountdownInterval);
        return this;
    }

    public abstract void onTick(boolean unStart, long millisUntilFinished);

    public abstract void onFinish();

    private static final int MSG_RUN = 1;
    private static final int MSG_PAUSE = 2;
    private Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            synchronized (MyCountDownTimer.this)
            {
                if (msg.what == MSG_RUN)
                {
                    mRemainTime = mRemainTime - mCountdownInterval;
                    mRemainEndTime = mRemainEndTime - mCountdownInterval;

                    if (mRemainTime <= 0 && mRemainEndTime <= 0)
                    {
                        onFinish();
                        return;
                    }

                    if (mRemainTime >= mCountdownInterval)
                    {
                        onTick(true, mRemainTime);
                        sendMessageDelayed(obtainMessage(MSG_RUN),
                                mCountdownInterval);
                    }
                    else if ((mRemainTime > 0 && mRemainTime < mCountdownInterval))
                    {
                        sendMessageDelayed(obtainMessage(MSG_RUN), mRemainTime);
                    }
                    else if (mRemainEndTime >= mCountdownInterval)
                    {
                        onTick(false, mRemainEndTime);

                        sendMessageDelayed(obtainMessage(MSG_RUN),
                                mCountdownInterval);
                    }
                    else if ((mRemainEndTime > 0 && mRemainEndTime < mCountdownInterval))
                    {
                        sendMessageDelayed(obtainMessage(MSG_RUN),
                                mRemainEndTime);
                    }

                }
                else if (msg.what == MSG_PAUSE)
                {
                }
            }
        }
    };
}
