package com.moge.gege.util.widget;

import android.content.Context;
import android.widget.ListView;

public class MyListView extends ListView
{

    /**
     * @param context
     */
    public MyListView(Context context)
    {
        super(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public MyListView(android.content.Context context,
            android.util.AttributeSet attrs)
    {
        super(context, attrs);
    }

    /**
     * 
     * @see android.widget.GridView#onMeasure(int, int)
     */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
