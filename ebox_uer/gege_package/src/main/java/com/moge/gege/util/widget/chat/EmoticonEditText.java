package com.moge.gege.util.widget.chat;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.EditText;

public class EmoticonEditText extends EditText
{

    private Context mContext;

    public EmoticonEditText(Context context)
    {
        super(context);
        mContext = context;
    }

    public EmoticonEditText(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        mContext = context;
    }

    public EmoticonEditText(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mContext = context;
    }

    @Override
    public void setText(CharSequence text, BufferType type)
    {
        if (!TextUtils.isEmpty(text))
        {
            super.setText(
                    EmoticonUtil.instance()
                            .formatEmoticonString(mContext, text), type);
        }
        else
        {
            super.setText(text, type);
        }
    }
}
