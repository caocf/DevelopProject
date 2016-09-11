package com.moge.gege.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.moge.gege.R;
import com.moge.gege.config.GlobalConfig;
import com.moge.gege.model.StyleInfoModel;
import com.moge.gege.model.TradingPromotionModel;
import com.moge.gege.model.enums.PromotionStyleType;
import com.moge.gege.network.util.RequestManager;
import com.moge.gege.util.FunctionUtil;
import com.moge.gege.util.MyCountDownTimer;

public class TradingPromotionView extends RelativeLayout
{
    private Context mContext;
    private TradingPromotionListener mListener;

    private ImageView mPromotionImage;
    private LinearLayout mPromotionLayout;
    private TextView mPromotionText;
    private TextView mHour1Text;
    private TextView mHour2Text;
    private TextView mHour3Text;
    private TextView mHour4Text;
    private TextView mMinuteFirstText;
    private TextView mMinuteLastText;
    private TextView mSecondFirstText;
    private TextView mSecondLastText;

    private LinearLayout mFixPromotionLayout;
    private TextView mLeftInfoText;
    private TextView mLeftTimeText;

    private MyCountDownTimer mStartDownTimer;

    public TradingPromotionView(Context context)
    {
        super(context);
        this.mContext = context;
        onCreate();
    }

    public TradingPromotionView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.mContext = context;
        onCreate();
    }

    public TradingPromotionView(Context context, AttributeSet attrs,
            int defStyle)
    {
        super(context, attrs, defStyle);
        this.mContext = context;
        onCreate();
    }

    public void setListener(TradingPromotionListener listener)
    {
        mListener = listener;
    }

    public interface TradingPromotionListener
    {
        void onPromotionUnStart(int hour, int minute, int second);

        void onPromotionIng(int hour, int minute, int second);

        void onPromotionFinish();
    }

    @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();
    }

    private void onCreate()
    {
        initView();
    }

    public void setImageUrl(String url)
    {
        RequestManager.loadImage(mPromotionImage,
                RequestManager.getImageUrl(url)  + GlobalConfig.IMAGE_STYLE480, R.drawable.icon_default);
    }

    private void initView()
    {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.custom_promotion_image, this, true);

        mPromotionImage = (ImageView) findViewById(R.id.promotionImage);
        mPromotionLayout = (LinearLayout) findViewById(R.id.promotionLayout);
        mPromotionText = (TextView) findViewById(R.id.promotionText);
        mHour1Text = (TextView) findViewById(R.id.hour1Text);
        mHour2Text = (TextView) findViewById(R.id.hour2Text);
        mHour3Text = (TextView) findViewById(R.id.hour3Text);
        mHour4Text = (TextView) findViewById(R.id.hour4Text);
        mMinuteFirstText = (TextView) findViewById(R.id.minuteFirstText);
        mMinuteLastText = (TextView) findViewById(R.id.minuteLastText);
        mSecondFirstText = (TextView) findViewById(R.id.secondFirstText);
        mSecondLastText = (TextView) findViewById(R.id.secondLastText);

        mFixPromotionLayout = (LinearLayout) findViewById(
                R.id.fixPromotionLayout);
        mLeftInfoText = (TextView) findViewById(R.id.leftInfoText);
        mLeftTimeText = (TextView) findViewById(R.id.leftTimeText);
    }

    public void setPromotionInfo(TradingPromotionModel promotionInfo)
    {
        if (promotionInfo == null)
        {
            return;
        }

        setImageUrl(promotionInfo.getImage());

        StyleInfoModel styleInfo = promotionInfo.getStyle_info();
        if (promotionInfo.getStyle().equalsIgnoreCase(
                PromotionStyleType.SECOND_KILL)
                && styleInfo != null)
        {
            setPromotionInfo((int) styleInfo.getX(), (int) styleInfo.getY(),
                    (long) (styleInfo.getStart_time() * 1000),
                    (long) (styleInfo.getEnd_time() * 1000));
        }
    }

    public void setPromotionInfo(int x, int y, long millisStartTime,
            long millisEndTime)
    {
        // show count down ui
        mPromotionLayout.setVisibility(View.VISIBLE);

        // update position
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mPromotionLayout
                .getLayoutParams();
        if (x >= 0)
        {
            params.leftMargin = x;
        }
        if (y >= 0)
        {
            params.topMargin = y;
        }
        mPromotionLayout.setLayoutParams(params);

        // update value
        long currentTime = System.currentTimeMillis();

        if (mStartDownTimer == null)
        {
            mStartDownTimer = new MyCountDownTimer(millisStartTime
                    - currentTime, millisEndTime - currentTime, 1000)
            {
                @Override
                public void onTick(boolean unStart, long millisUntilFinished)
                {
                    long leftSeconds = millisUntilFinished / 1000;
                    int hour = (int) (leftSeconds / 3600);
                    int minute = (int) ((leftSeconds - 3600 * hour) / 60);
                    int second = (int) (leftSeconds - hour * 3600
                            - minute * 60);
                    updateTime(hour, minute, second);

                    if (unStart)
                    {
                        mPromotionText.setText(getResources().getString(
                                R.string.promotion_start_left_time));

                        if (mListener != null)
                        {
                            mListener.onPromotionUnStart(hour, minute, second);
                        }
                    }
                    else
                    {
                        mPromotionText.setText(getResources().getString(
                                R.string.promotion_end_left_time));

                        if (mListener != null)
                        {
                            mListener.onPromotionIng(hour, minute, second);
                        }
                    }
                }

                @Override
                public void onFinish()
                {
                    mPromotionText.setText(getResources().getString(
                            R.string.promotion_end));
                    updateTime(0, 0, 0);

                    if (mListener != null)
                    {
                        mListener.onPromotionFinish();
                    }
                }
            };
        }
        else
        {
            mStartDownTimer.setValue(millisStartTime - currentTime,
                    millisEndTime - currentTime);
        }

        mStartDownTimer.cancel();
        mStartDownTimer.start();
    }

    public void setFixPromotionInfo(long millisStartTime,
            long millisEndTime)
    {
        // show count down ui(move to parent ui)
//        mFixPromotionLayout.setVisibility(View.VISIBLE);

        // update value
        long currentTime = System.currentTimeMillis();

        if (mStartDownTimer == null)
        {
            mStartDownTimer = new MyCountDownTimer(millisStartTime
                    - currentTime, millisEndTime - currentTime, 1000)
            {
                @Override
                public void onTick(boolean unStart, long millisUntilFinished)
                {
                    long leftSeconds = millisUntilFinished / 1000;
                    int hour = (int) (leftSeconds / 3600);
                    int minute = (int) ((leftSeconds - 3600 * hour) / 60);
                    int second = (int) (leftSeconds - hour * 3600
                            - minute * 60);
                    mLeftTimeText.setText(String.format("%02d:%02d:%02d", hour, minute, second));

                    if (unStart)
                    {
                        mLeftInfoText.setText(getResources().getString(
                                R.string.promotion_start_left_time));

                        if (mListener != null)
                        {
                            mListener.onPromotionUnStart(hour, minute, second);
                        }
                    }
                    else
                    {
                        mLeftInfoText.setText(getResources().getString(
                                R.string.promotion_end_left_time));

                        if (mListener != null)
                        {
                            mListener.onPromotionIng(hour, minute, second);
                        }
                    }
                }

                @Override
                public void onFinish()
                {
                    mLeftInfoText.setText(getResources().getString(
                            R.string.promotion_end));
                    mLeftTimeText.setText(String.format("%02d:%02d:%02d", 0, 0, 0));

                    if (mListener != null)
                    {
                        mListener.onPromotionFinish();
                    }
                }
            };
        }
        else
        {
            mStartDownTimer.setValue(millisStartTime - currentTime,
                    millisEndTime - currentTime);
        }

        mStartDownTimer.cancel();
        mStartDownTimer.start();
    }

    private void updateTime(int hour, int minute, int second)
    {
        String hours = FunctionUtil.fixNumber(hour);
        String minutes = FunctionUtil.fixNumber(minute);
        String seconds = FunctionUtil.fixNumber(second);

        if (hours.length() > 3)
        {
            mHour1Text.setText(hours.substring(0, 1));
            mHour2Text.setText(hours.substring(1, 2));

            mHour1Text.setVisibility(View.VISIBLE);
            mHour2Text.setVisibility(View.VISIBLE);
        }
        else if (hours.length() > 2)
        {
            mHour2Text.setText(hours.substring(0, 1));

            mHour1Text.setVisibility(View.GONE);
            mHour2Text.setVisibility(View.VISIBLE);
        }
        else
        {
            mHour1Text.setVisibility(View.GONE);
            mHour2Text.setVisibility(View.GONE);
        }

        mHour3Text.setText(hours.substring(hours.length() - 2,
                hours.length() - 1));
        mHour4Text.setText(hours.substring(hours.length() - 1));

        mMinuteFirstText.setText(minutes.substring(0, 1));
        mMinuteLastText.setText(minutes.substring(1));
        mSecondFirstText.setText(seconds.substring(0, 1));
        mSecondLastText.setText(seconds.substring(1));
    }

}
