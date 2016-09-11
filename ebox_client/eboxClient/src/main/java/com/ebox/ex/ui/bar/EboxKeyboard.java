package com.ebox.ex.ui.bar;

import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.text.Editable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ebox.Anetwork.RequestManager;
import com.ebox.AppApplication;
import com.ebox.R;
import com.ebox.ex.network.model.enums.MainBoardType;
import com.ebox.pub.service.global.GlobalField;
import com.ebox.pub.utils.MGViewUtil;
import com.ebox.pub.utils.RingUtil;

import java.lang.reflect.Method;

/**
 * Created by Android on 2015/10/23.
 */
public class EboxKeyboard extends RelativeLayout{

    private ImageView mImageView;
    private KeyboardView mKeyboard;
    private Keyboard mKey;

    private EditText mEdit;
    private boolean isNumberAudio = false;// 是否播放数字音效
    private OnEboxKeyListener mkeyListener;

    private Context mContext;

    public EboxKeyboard(Context context) {
        super(context);
        initViews(context);
    }

    public EboxKeyboard(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public EboxKeyboard(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews(context);
    }

    public void hideAdv() {
       mImageView.setVisibility(View.GONE);
    }

    public void setEditText(EditText editText){
        mEdit=editText;
        hideInput((Activity) mContext, mEdit);
    }

    public void setMkeyListener(OnEboxKeyListener listener){
        mkeyListener=listener;
    }

    public void setNumberAudio(boolean isNumberAudio)
    {
        this.isNumberAudio = isNumberAudio;
    }

    private void initViews(Context context)
    {
        mContext=context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(AppApplication.getInstance().is55Screen)
        {
            inflater.inflate(R.layout.pub_keyboard_adv_55, this, true);
        }else {
            inflater.inflate(R.layout.pub_keyboard_adv, this, true);
        }

        mImageView = (ImageView) findViewById(R.id.keyboard_adv);
        mKeyboard = (KeyboardView) findViewById(R.id.keyboard_view);
        //设置键盘
        if (AppApplication.getInstance().is55Screen)
        {
            mKey = new Keyboard(context, R.xml.symbols_55);
        }else
        {
            mKey = new Keyboard(context, R.xml.symbols_17);
        }
        mKeyboard.setKeyboard(mKey);
        mKeyboard.setEnabled(true);
        mKeyboard.setPreviewEnabled(false);
        mKeyboard.setOnKeyboardActionListener(listener);
        mKeyboard.setDrawingCacheEnabled(true);
        mKeyboard.setBackgroundResource(R.color.transparent);

        MGViewUtil.scaleContentView(this);
        initAdv();
    }

    private void initAdv(){
        if (GlobalField.showConfig == null) {
            return;
        }
        String url=GlobalField.showConfig.ad_top;
        if (!TextUtils.isEmpty(url) && URLUtil.isValidUrl(url))
        {
            RequestManager.loadImage(mImageView,url,0);
        }

    }

    private void hideInput(Activity activity,EditText ed)
    {
        //ed.setInputType(InputType.TYPE_NULL);
        activity.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        try {
            Class<EditText> cls = EditText.class;
            Method setShowSoftInputOnFocus;
            if(GlobalField.config.getMaim_board() == MainBoardType.A31S_VER1) {
                setShowSoftInputOnFocus = cls.getMethod(
                        "setShowSoftInputOnFocus", boolean.class);
            } else {
                setShowSoftInputOnFocus = cls.getMethod(
                        "setSoftInputShownOnFocus", boolean.class);
            }
            setShowSoftInputOnFocus.setAccessible(true);
            setShowSoftInputOnFocus.invoke(ed, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private KeyboardView.OnKeyboardActionListener listener = new KeyboardView.OnKeyboardActionListener() {
        @Override
        public void swipeUp() {
        }

        @Override
        public void swipeRight() {
        }

        @Override
        public void swipeLeft() {
        }

        @Override
        public void swipeDown() {
        }

        @Override
        public void onText(CharSequence text) {
        }

        @Override
        public void onRelease(int primaryCode) {
        }

        @Override
        public void onPress(int primaryCode) {
        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            if (mEdit==null) {
                return;
            }
            Editable editable = mEdit.getText();
            int start = mEdit.getSelectionStart();
            // 删除
            if (primaryCode == Keyboard.KEYCODE_DELETE) {
                if (editable != null && editable.length() > 0) {
                    if (start > 0) {
                        editable.delete(start - 1, start);
                    }
                }
            }
            // 清空
            else if (primaryCode == Keyboard.KEYCODE_CANCEL) {
                editable.clear();
            } else {
                editable.insert(start, Character.toString((char) primaryCode));
            }

            if (isNumberAudio) {
                if (primaryCode == 48) {
                    RingUtil.playRingtone(RingUtil.id_0);
                } else if (primaryCode == 49) {
                    RingUtil.playRingtone(RingUtil.id_1);
                } else if (primaryCode == 50) {
                    RingUtil.playRingtone(RingUtil.id_2);
                } else if (primaryCode == 51) {
                    RingUtil.playRingtone(RingUtil.id_3);
                } else if (primaryCode == 52) {
                    RingUtil.playRingtone(RingUtil.id_4);
                } else if (primaryCode == 53) {
                    RingUtil.playRingtone(RingUtil.id_5);
                } else if (primaryCode == 54) {
                    RingUtil.playRingtone(RingUtil.id_6);
                } else if (primaryCode == 55) {
                    RingUtil.playRingtone(RingUtil.id_7);
                } else if (primaryCode == 56) {
                    RingUtil.playRingtone(RingUtil.id_8);
                } else if (primaryCode == 57) {
                    RingUtil.playRingtone(RingUtil.id_9);
                } else {
                    // 播放按键音
                    final AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
                    audioManager.playSoundEffect(AudioManager.FX_KEY_CLICK);
                }
            } else {
                // 播放按键音
                final AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
                audioManager.playSoundEffect(AudioManager.FX_KEY_CLICK);
            }

            if(mkeyListener != null)
            {
                mkeyListener.onKey(primaryCode);
            }
        }
    };

    public interface OnEboxKeyListener{

        void onKey(int keyCode);
    }
}
