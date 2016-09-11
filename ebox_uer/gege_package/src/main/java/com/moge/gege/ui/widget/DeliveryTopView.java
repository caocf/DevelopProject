package com.moge.gege.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class DeliveryTopView extends LinearLayout implements View.OnClickListener
{
    private Context mContext;

    private DeliveryTopViewListener mListener;

    private Button sendDeliveryBtn;
    private Button scanOpenBoxBtn;
    private TextView searchDeliveryText;
    private Button scanDeliveryBtn;

    private MyCountDownTimer mStartDownTimer;

    public DeliveryTopView(Context context)
    {
        super(context);
        this.mContext = context;
        onCreate();
    }

    public DeliveryTopView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.mContext = context;
        onCreate();
    }

    public void setListener(DeliveryTopViewListener listener)
    {
        mListener = listener;
    }

    public interface DeliveryTopViewListener
    {
        void onSendDeliveryClick();

        void onScanOpenBoxClick();

        void onSearchDeliveryClick();

        void onScanDeliveryClick();
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


    private void initView()
    {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.custom_delivery_top_view, this, true);

        sendDeliveryBtn = (Button)findViewById(R.id.sendDeliveryBtn);
        sendDeliveryBtn.setOnClickListener(this);
        scanOpenBoxBtn = (Button)findViewById(R.id.scanOpenBoxBtn);
        scanOpenBoxBtn.setOnClickListener(this);
        searchDeliveryText = (TextView)findViewById(R.id.searchDeliveryText);
        searchDeliveryText.setOnClickListener(this);
        scanDeliveryBtn = (Button)findViewById(R.id.scanDeliveryBtn);
        scanDeliveryBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v){

        switch (v.getId()){
            case R.id.sendDeliveryBtn:
                if(mListener != null){
                    mListener.onSendDeliveryClick();
                }
                break;
            case R.id.scanOpenBoxBtn:
                if(mListener != null){
                    mListener.onScanOpenBoxClick();
                }
                break;
            case R.id.searchDeliveryText:
                if(mListener != null){
                    mListener.onSearchDeliveryClick();
                }
                break;
            case R.id.scanDeliveryBtn:
                if(mListener != null){
                    mListener.onScanDeliveryClick();
                }
                break;
            default:
                break;
        }

    }


}
