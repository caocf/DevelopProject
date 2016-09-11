package com.moge.gege.util.pay;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.android.volley.VolleyError;
import com.moge.gege.R;
import com.moge.gege.model.BaseModel;
import com.moge.gege.network.ErrorCode;
import com.moge.gege.network.request.GetPayCodeRequest;
import com.moge.gege.network.request.GetPayVoiceCodeRequest;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.RequestManager;
import com.moge.gege.network.util.ResponseEventHandler;
import com.moge.gege.ui.widget.BalancePayDialog;
import com.moge.gege.util.LogUtil;
import com.moge.gege.util.ToastUtil;
import com.moge.gege.util.widget.CustomDialog;

import org.w3c.dom.Text;

/**
 * Created by dev on 15/3/27.
 */
public class BalancePayController {

    private Context mContext;
    private String mPayId;
    private String mCodeMobile;
    private BalancePayListener mPayListener;

    private BalancePayDialog mPayDialog;
    private static final int mTimeCount = 61;
    private static final int mVoiceTimeCount = 21;
    private int mCurrentTimeCount = mTimeCount;
    private int mRetry = 0;
    private int mRetryVoice = 0;

    public static interface BalancePayListener {
        public void onToPayWithBalance(String payId, String code);

        public void onToCancelBalancePay(String payId);
    }

    public BalancePayController(Context context) {
        this.mContext = context;
    }

    public BalancePayController(Context context, String payId, String codeMobile, BalancePayListener payListener) {
        this.mContext = context;
        this.mPayId = payId;
        this.mCodeMobile = codeMobile;
        this.mPayListener = payListener;
    }

    public BalancePayController setPayId(String payId) {
        mPayId = payId;
        return this;
    }

    public BalancePayController startWork() {
        mPayDialog = new BalancePayDialog(mContext, R.string.balance_pay, R.string.blance_pay_sms_tips, R.string.confirm_pay, R.string.general_cancel);
        mPayDialog.setCanceledOnTouchOutside(false);
        mPayDialog.setCancelable(false);

        mPayDialog.setPositiveButtonListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String code = mPayDialog.getCode();
                if (TextUtils.isEmpty(code)) {
                    ToastUtil.showToastShort(R.string.checkcode_empty);
                    return;
                }

                if (mPayListener != null) {
                    mPayListener.onToPayWithBalance(mPayId, code);
                }

            }
        });

        mPayDialog.setNegativeButtonListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                new CustomDialog.Builder(mContext)
                        .setTitle(R.string.general_tips)
                        .setMessage(R.string.cancel_pay_confirm)
                        .setPositiveButton(R.string.general_confirm,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {

                                        if (mPayListener != null) {
                                            mPayListener.onToCancelBalancePay(mPayId);
                                        }

                                        dialog.dismiss();
                                        finishWork();
                                    }
                                })
                        .setNegativeButton(R.string.general_cancel,
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();
                                    }

                                }).create().show();
            }
        });

        mPayDialog.setGetCodeButtonListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                doPaySmsCodeRequest(mPayId);
            }
        });

        mPayDialog.show();

        return this;
    }

    public void finishWork() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }

        if (mPayDialog != null) {
            mPayDialog.dismiss();
        }

    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                if (mCurrentTimeCount == 0) {
                    mPayDialog.getGetCodeBtn().setEnabled(true);
                    mPayDialog.getGetCodeBtn().setText(R.string.retry_get_checkcode);
                    showVoiceCodeChoiceDialog();
                } else {
                    mCurrentTimeCount--;
                    mPayDialog.getGetCodeBtn().setText(String.valueOf(mCurrentTimeCount)
                            + "S");
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mHandler.sendEmptyMessage(0);
                        }
                    }, 1000);
                }
            } else {

                if (mCurrentTimeCount == 0) {
                    mPayDialog.getGetCodeBtn().setEnabled(true);
                    mPayDialog.getGetCodeBtn().setText(R.string.retry_get_checkcode);
                } else {
                    mCurrentTimeCount--;
                    mPayDialog.getGetCodeBtn().setText(String.valueOf(mCurrentTimeCount)
                            + "S");
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Message msg = Message.obtain();
                            msg.what = 1;
                            mHandler.sendMessage(msg);
                        }
                    }, 1000);
                }
            }

            super.handleMessage(msg);
        }
    };


    private void showVoiceCodeChoiceDialog() {
        CustomDialog dialog = new CustomDialog.Builder(mContext)
                .setTitle(R.string.general_tips)
                .setMessage(R.string.try_voice_code_confirm)
                .setPositiveButton(R.string.general_confirm,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                doPayVoiceCodeRequest(mPayId);
                                dialog.dismiss();
                            }
                        })
                .setNegativeButton(R.string.general_cancel,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                            }

                        }).create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void doPaySmsCodeRequest(String payId) {

        mPayDialog.getGetCodeBtn().setEnabled(false);

        GetPayCodeRequest request = new GetPayCodeRequest(payId, mRetry,
                new ResponseEventHandler<BaseModel>() {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<BaseModel> request,
                            BaseModel result) {

                        mRetry++;

                        if (result != null
                                && result.getStatus() == ErrorCode.SUCCESS) {

                            ToastUtil
                                    .showToastShort(R.string.send_checkcode_success);
                            mPayDialog.setMessage(String.format("验证码已发送到%s，请查收", mCodeMobile));

                            // update button
                            mCurrentTimeCount = mTimeCount;
                            mPayDialog.getGetCodeBtn().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mHandler.sendEmptyMessage(0);
                                }
                            }, 1000);
                        } else {
                            ToastUtil.showToastShort(result.getMsg());
                            mPayDialog.getGetCodeBtn().setEnabled(true);
                        }
                    }

                    @Override
                    public void onResponseError(VolleyError error) {
                        ToastUtil.showToastShort(error.getMessage());
                        mRetry++;
                        mPayDialog.getGetCodeBtn().setEnabled(true);
                    }

                });

        RequestManager.addRequest(request, mContext);
    }

    private void doPayVoiceCodeRequest(String payId) {

        mPayDialog.getGetCodeBtn().setEnabled(false);

        GetPayVoiceCodeRequest request = new GetPayVoiceCodeRequest(payId, mRetryVoice,
                new ResponseEventHandler<BaseModel>() {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<BaseModel> request,
                            BaseModel result) {

                        mRetryVoice++;

                        if (result != null
                                && result.getStatus() == ErrorCode.SUCCESS) {

                            ToastUtil
                                    .showToastShort(R.string.send_voice_code_success);
                            mPayDialog.setMessage(String.format("验证码已发送到%s，请接听", mCodeMobile));

                            // update button
                            mCurrentTimeCount = mVoiceTimeCount;
                            mPayDialog.getGetCodeBtn().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Message msg = Message.obtain();
                                    msg.what = 1;
                                    mHandler.sendMessage(msg);
                                }
                            }, 1000);
                        } else {
                            ToastUtil.showToastShort(result.getMsg());
                            mPayDialog.getGetCodeBtn().setEnabled(true);
                        }
                    }

                    @Override
                    public void onResponseError(VolleyError error) {
                        ToastUtil.showToastShort(error.getMessage());
                        mRetryVoice++;
                        mPayDialog.getGetCodeBtn().setEnabled(true);
                    }

                });

        RequestManager.addRequest(request, mContext);
    }


}
