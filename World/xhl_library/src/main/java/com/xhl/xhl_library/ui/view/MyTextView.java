package com.xhl.xhl_library.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

public class MyTextView extends TextView
{

    public MyTextView(Context context, AttributeSet attrs,
                      int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public MyTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public MyTextView(Context context)
    {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        Drawable[] drawables = getCompoundDrawables();
        if (drawables != null)
        {
            Drawable drawableLeft = drawables[0];
            if (drawableLeft != null)
            {
                float textWidth = getPaint().measureText(getText().toString());
                int drawablePadding = getCompoundDrawablePadding();
                int drawableWidth = 0;
                drawableWidth = drawableLeft.getIntrinsicWidth();
                float bodyWidth = textWidth + drawableWidth + drawablePadding;
                canvas.translate((getWidth() - bodyWidth) / 2, 0);
            }
        }
        super.onDraw(canvas);
    }
}