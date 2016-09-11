package com.moge.gege.util.widget.chat;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.moge.gege.R;
import com.moge.gege.util.DialogUtil;
import com.moge.gege.util.widget.chat.EmoticonView.OnEmoticonListener;

public class IMFooterView extends LinearLayout implements OnClickListener,
        OnEmoticonListener
{
    private Button mSendBtn;
    private Button mMoreBtn;
    private Button mEmoticonBtn;
    private EditText mMessageEdit;
    private EmoticonView mEmoticonView;

    private Context mContext;
    private IMFooterViewListener mListener;
    private boolean mTextMode = false;
    private InputMethodManager mInputManager;

    private Dialog mMenuDialog;

    public IMFooterView(Context context)
    {
        super(context);
        mContext = context;
    }

    public IMFooterView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mContext = context;
    }

    public void setListner(IMFooterViewListener listener)
    {
        this.mListener = listener;
    }

    public void setImfooterVisible()
    {
        mMessageEdit.requestFocus();
        if (mEmoticonView.getVisibility() == View.GONE)
        {
            mInputManager.showSoftInput(mMessageEdit, 0);
        }
    }

    public void setImfooterGone()
    {
        mInputManager.hideSoftInputFromWindow(mMessageEdit.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
        if (mEmoticonView.getVisibility() == View.VISIBLE)
        {
            mEmoticonView.setVisibility(View.GONE);
        }
    }

    public void setHintText(CharSequence hint)
    {
        mMessageEdit.setHint(hint);
    }

    @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();
        initViews(mContext);
    }

    private void initViews(Context context)
    {
        mInputManager = ((InputMethodManager) getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE));
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.custom_imfooter, this, true);
        mMoreBtn = (Button) findViewById(R.id.moreBtn);
        mMoreBtn.setOnClickListener(this);
        mMoreBtn.setOnTouchListener(new OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (mListener != null)
                {
                    mListener.onTouch();
                }
                return false;
            }
        });

        mSendBtn = (Button) findViewById(R.id.sendBtn);
        mSendBtn.setOnClickListener(this);
        mSendBtn.setOnTouchListener(new OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (mListener != null)
                {
                    mListener.onTouch();
                }
                return false;
            }
        });

        mEmoticonBtn = (Button) findViewById(R.id.emoticonBtn);
        mEmoticonBtn.setOnClickListener(this);
        mEmoticonBtn.setOnTouchListener(new OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (mListener != null)
                {
                    mListener.onTouch();
                }

                return false;
            }
        });

        mMessageEdit = (EditText) findViewById(R.id.messageEdit);
        mMessageEdit.setOnTouchListener(new EditTouchListener());
        mMessageEdit.addTextChangedListener(new TextWatcher()
        {
            public void afterTextChanged(Editable s)
            {

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after)
            {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                    int count)
            {
                if (TextUtils.isEmpty(s))
                {
                    mTextMode = false;
                }
                else
                {
                    mTextMode = true;
                }

                mMoreBtn.setVisibility(mTextMode ? View.GONE : View.VISIBLE);
                mSendBtn.setVisibility(mTextMode ? View.VISIBLE : View.GONE);
            }

        });

        mEmoticonView = (EmoticonView) findViewById(R.id.emoticonView);
        mEmoticonView.setListener(this);
    }

    private class EditTouchListener implements
            android.view.View.OnTouchListener
    {

        public boolean onTouch(View view, MotionEvent motionevent)
        {
            mEmoticonView.setVisibility(View.GONE);
            if (mListener != null)
            {
                mListener.onTouch();
            }
            return false;
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.moreBtn:
                sendMessageImage();
                break;
            case R.id.sendBtn:
                sendMessageText();
                break;
            case R.id.emoticonBtn:
                if (mEmoticonView.getVisibility() == View.GONE)
                {
                    mEmoticonView.setVisibility(View.VISIBLE);
                    mInputManager.hideSoftInputFromWindow(
                            mMessageEdit.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
                else
                {
                    mEmoticonView.setVisibility(View.GONE);
                }
                break;
            default:

                break;
        }
    }

    private void sendMessageImage()
    {
        if (mMenuDialog == null)
        {
            mMenuDialog = DialogUtil.createMenuDialog(mContext, "",
                    R.array.image_select_menu, new OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            mMenuDialog.dismiss();
                            switch (((Integer) v.getTag()).intValue())
                            {
                                case 0:
                                    mListener.onSelectImageMenu(0);
                                    break;
                                case 1:
                                    mListener.onSelectImageMenu(1);
                                    break;
                                case 2:
                                    break;
                                default:
                                    break;
                            }
                        }

                    });
        }
        mMenuDialog.show();
    }

    private void sendMessageText()
    {
        String strContent = mMessageEdit.getText().toString().trim();
        if (TextUtils.isEmpty(strContent))
        {
            return;
        }

        if (mListener != null)
        {
            if(mListener.onPublish(strContent))
            {
                mMessageEdit.setText("");
            }
        }
    }

    private void switchToTextMode()
    {
        // modeBtn.setImageResource(R.drawable.im_setmode_voice_btn);
        // inputLay.setVisibility(View.VISIBLE);
        // faceBtn.setVisibility(View.VISIBLE);
        // msgED.setVisibility(View.VISIBLE);
        // // wordCount.setVisibility(View.VISIBLE);
        // publish.setVisibility(View.VISIBLE);
        // msgED.requestFocus();
        // inputManager.showSoftInput(msgED, 0);
        //
        // voiceBtn.setVisibility(View.GONE);
        // faceView.setVisibility(View.GONE);
        // faceView.setData(false);
    }

    public interface IMFooterViewListener
    {
        void onSelectImageMenu(int index);

        boolean onPublish(String content);

        void onTouch();
    }

    private int getEditTextCursorIndex(EditText mEditText)
    {
        return mEditText.getSelectionStart();
    }

    private void insertText(EditText mEditText, CharSequence mText)
    {
        mEditText.getText().insert(getEditTextCursorIndex(mEditText), mText);
    }

    public boolean onKeyBack()
    {
        if (mEmoticonView.getVisibility() == View.VISIBLE)
        {
            mEmoticonView.setVisibility(View.GONE);
            return true;
        }

        return false;
    }

    @Override
    public void onEmoticonClick(CharSequence cs)
    {
        if (mMessageEdit.getVisibility() == View.VISIBLE)
        {
            insertText(mMessageEdit, cs);
        }
    }

    @Override
    public void onEmoticonDelete()
    {
        int action = KeyEvent.ACTION_DOWN;
        int code = KeyEvent.KEYCODE_DEL;
        KeyEvent event = new KeyEvent(action, code);
        mMessageEdit.onKeyDown(KeyEvent.KEYCODE_DEL, event);
    }
}
