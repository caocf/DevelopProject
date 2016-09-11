package com.moge.gege.util.widget.wheel.time;

import java.util.Calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.moge.gege.R;
import com.moge.gege.util.TimeUtil;
import com.moge.gege.util.widget.wheel.OnWheelChangedListener;
import com.moge.gege.util.widget.wheel.OnWheelScrollListener;
import com.moge.gege.util.widget.wheel.WheelView;
import com.moge.gege.util.widget.wheel.adapters.ArrayWheelAdapter;

public class DateCtrl extends LinearLayout
{
    private static final Calendar TEMP_CAL = Calendar.getInstance();
    private int[][] mDateItems = new int[10][3]; // ÿ�μ���10�����ڼ�¼
    private int mCurrDayOfMonth;
    private int mCurrMonth;
    private int mCurrYear;
    private int mPrevYear;
    private int mTodayDayOfMonth;
    private int mTodayMonth;
    private int mTodayYear;
    private WheelView dateWheel;
    private OnDateChangedListener mDateChangedListener;
    private OnYearChangedListener mYearChangedListener;
    protected static final int[] TRANSPARENT_COLORS = { 0, 0, 0 };

    public DateCtrl(Context paramContext)
    {
        this(paramContext, System.currentTimeMillis());
    }

    public DateCtrl(Context paramContext, long time)
    {
        super(paramContext);
        init(time);
    }

    public DateCtrl(Context paramContext, AttributeSet paramAttributeSet)
    {
        super(paramContext, paramAttributeSet);
        init(System.currentTimeMillis());
    }

    private void init(long time)
    {
        View localView = LayoutInflater.from(getContext()).inflate(
                R.layout.date_ctrl, null);
        dateWheel = (WheelView) (localView.findViewById(R.id.day));
        addView(localView);
        initWheelView(time);
    }

    private void initWheelView(long time)
    {
        setWheelViewGlobal(dateWheel, false);

        Calendar localCalendar = Calendar.getInstance();
        localCalendar.add(Calendar.DATE, 1);
        mTodayYear = localCalendar.get(Calendar.YEAR);
        mTodayMonth = localCalendar.get(Calendar.MONTH);
        mTodayDayOfMonth = localCalendar.get(Calendar.DAY_OF_MONTH);

        localCalendar.clear();
        localCalendar.setTimeInMillis(time);
        mCurrYear = localCalendar.get(Calendar.YEAR);
        mCurrMonth = localCalendar.get(Calendar.MONTH);
        mCurrDayOfMonth = localCalendar.get(Calendar.DAY_OF_MONTH);
        setDateWheelAdapter(dateWheel);

        dateWheel.addScrollingListener(new OnWheelScrollListener()
        {
            public void onScrollingFinished(WheelView paramWheelView)
            {
                notifyListener();
            }

            public void onScrollingStarted(WheelView paramWheelView)
            {

            }
        });
        dateWheel.addChangingListener(new OnWheelChangedListener()
        {
            public void onChanged(WheelView wheel, int oldValue, int newValue)
            {
                int[] item = mDateItems[wheel.getCurrentItem()];
                mCurrYear = item[0];
                mCurrMonth = item[1];
                mCurrDayOfMonth = item[2];
                if (3 + wheel.getCurrentItem() >= wheel.getViewAdapter()
                        .getItemsCount())
                    setDateWheelAdapter(wheel);
                if (-3 + wheel.getCurrentItem() < 0)
                    setDateWheelAdapter(wheel);
                notifyListener();
            }
        });
    }

    private int calcuateDateItems(String[] paramArrayOfString)
    {
        Calendar.getInstance().set(mCurrYear, mCurrMonth, mCurrDayOfMonth);
        int i = fillPrevDateItems(paramArrayOfString);
        fillNextDateItems(paramArrayOfString, i);
        return i - 1;
    }

    private void fillNextDateItems(String[] paramArrayOfString, int index)
    {
        int i = mCurrYear;
        int j = mCurrMonth;
        int k = mCurrDayOfMonth;
        TEMP_CAL.clear();
        TEMP_CAL.set(i, j, k);
        int m = TEMP_CAL.getActualMaximum(Calendar.DATE);
        int w;
        int i2 = index;

        while (i2 < this.mDateItems.length)
        {
            k++;
            if (k > m)
            {
                j++;
                k = 1;
                if (j > 11)
                {
                    i++;
                    j = 0;
                }
            }
            TEMP_CAL.clear();
            TEMP_CAL.set(i, j, k, 0, 0);
            m = TEMP_CAL.getActualMaximum(Calendar.DATE);
            w = TEMP_CAL.get(Calendar.DAY_OF_WEEK);
            paramArrayOfString[i2] = getDateString(i, j, k, w);
            int[] item = new int[3];
            item[0] = i;
            item[1] = j;
            item[2] = k;
            mDateItems[i2] = item;
            i2++;
        }
    }

    private int fillPrevDateItems(String[] paramArrayOfString)
    {
        int i = mCurrYear;
        int j = mCurrMonth; // 0-11
        int k = mCurrDayOfMonth; // 1-30
        TEMP_CAL.set(i, j, k);
        int m = TEMP_CAL.get(Calendar.DAY_OF_WEEK); // 1-7

        int n = Math
                .min(4, getDuration(mCurrYear, mCurrMonth, mCurrDayOfMonth));
        int fillCount;

        for (fillCount = n; fillCount >= 0; fillCount--)
        {
            paramArrayOfString[fillCount] = getDateString(i, j, k, m);
            int[] item = new int[3];
            item[0] = i;
            item[1] = j;
            item[2] = k;
            mDateItems[fillCount] = item;
            k--;
            if (k <= 0)
            {
                j--;
                if (j < 0)
                {
                    i--;
                    j = 11;
                }
                TEMP_CAL.clear();
                TEMP_CAL.set(i, j, 1, 0, 0);
                k = TEMP_CAL.getActualMaximum(Calendar.DATE);
            }
            m--;
            if (m == 0)
            {
                m = 7;
            }
        }

        return n + 1;
    }

    private String getDateString(int year, int month, int day, int week)
    {
        String str;

        if (isToday(year, month, day))
            str = getContext().getString(R.string.today);
        else
        {
            Object[] dateString = new Object[3];
            dateString[0] = TimeUtil.getMonthOfYear(getContext(), month);
            dateString[1] = getDoubleNumber(day);
            dateString[2] = TimeUtil.getDayOfWeekStr(getContext(), week);
            str = getContext().getString(R.string.date_time_ctrl_format_str,
                    dateString);
        }

        return str;
    }

    private String getDoubleNumber(int day)
    {
        String str;
        if (day < 10)
            str = "0" + day;
        else
            str = day + "";
        return str;
    }

    private int getDuration(int year, int month, int day)
    {
        Calendar localCalendar = Calendar.getInstance();
        localCalendar.clear();
        localCalendar.set(mTodayYear, mTodayMonth, mTodayDayOfMonth, 0, 0);
        long l = localCalendar.getTimeInMillis();
        localCalendar.clear();
        localCalendar.set(year, month, day, 0, 0);
        int i = (int) ((localCalendar.getTimeInMillis() - l) / 86400000);
        if (i < 0)
            i = 0;
        return i;
    }

    private boolean isToday(int year, int month, int day)
    {
        if ((day == mTodayDayOfMonth) && (month == mTodayMonth)
                && (year == mTodayYear))
            return true;
        else
            return false;
    }

    private void setDateWheelAdapter(WheelView paramWheelView)
    {
        String[] arrayOfString = new String[mDateItems.length];
        int i = calcuateDateItems(arrayOfString);
        ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(
                getContext(), arrayOfString);
        adapter.setItemResource(R.layout.date_time_item);
        paramWheelView.setViewAdapter(adapter);
        paramWheelView.setCurrentItem(i);
    }

    public long getDateTime()
    {
        Calendar localCalendar = Calendar.getInstance();
        localCalendar.clear();
        int[] date = mDateItems[dateWheel.getCurrentItem()];
        localCalendar.set(date[0], date[1], date[2], 0, 0);
        return localCalendar.getTimeInMillis();
    }

    public void setDateTime(long time)
    {
        Calendar localCalendar = Calendar.getInstance();
        localCalendar.setTimeInMillis(time);
        mPrevYear = mCurrYear;
        mCurrDayOfMonth = localCalendar.get(Calendar.DAY_OF_MONTH);
        mCurrMonth = localCalendar.get(Calendar.MONTH);
        mCurrYear = localCalendar.get(Calendar.YEAR);
        setDateWheelAdapter(dateWheel);
        notifyListener();
    }

    private void notifyListener()
    {
        if ((mPrevYear != mCurrYear) && (mYearChangedListener != null))
        {
            mYearChangedListener.onChanged(0, mCurrYear);
            mPrevYear = mCurrYear;
        }
        if (mDateChangedListener != null)
        {
            Calendar localCalendar = Calendar.getInstance();
            localCalendar.set(mCurrYear, mCurrMonth, mCurrDayOfMonth, 0, 0);
            mDateChangedListener.onChanged(localCalendar);
        }
    }

    public final void setDateTimeCtrlBackground(int resid)
    {
        View localView = findViewById(R.id.date_time_ctrl_left);
        if (localView != null)
            localView.setBackgroundResource(resid);
    }

    public void setOnDateChangedListener(OnDateChangedListener listener)
    {
        mDateChangedListener = listener;
        if (mDateChangedListener != null)
        {
            Calendar localCalendar = Calendar.getInstance();
            localCalendar.set(mCurrYear, mCurrMonth, mCurrDayOfMonth, 0, 0);
            mDateChangedListener.onChanged(localCalendar);
        }
    }

    public void setOnYearChangedListener(OnYearChangedListener listener)
    {
        mYearChangedListener = listener;
        if (mYearChangedListener != null)
            mYearChangedListener.onChanged(mPrevYear, mCurrYear);
    }

    public void setTodayDate()
    {
        mPrevYear = mCurrYear;
        mCurrDayOfMonth = mTodayDayOfMonth;
        mCurrMonth = mTodayMonth;
        mCurrYear = mTodayYear;
        setDateWheelAdapter(dateWheel);
        notifyListener();
    }

    public static abstract interface OnDateChangedListener
    {
        public abstract void onChanged(Calendar currDate);
    }

    public static abstract interface OnYearChangedListener
    {
        public abstract void onChanged(Integer prevYear, Integer currYear);
    }

    protected void setWheelViewGlobal(WheelView paramWheelView,
            boolean paramBoolean)
    {
        paramWheelView.setBackgroundResource(R.drawable.transparent_background);
        paramWheelView
                .setCenterDrawableResource(R.drawable.transparent_background);
        paramWheelView.setTopShadowColors(TRANSPARENT_COLORS);
        paramWheelView.setBottomShadowColors(TRANSPARENT_COLORS);
        paramWheelView.setCyclic(paramBoolean);
    }
}
