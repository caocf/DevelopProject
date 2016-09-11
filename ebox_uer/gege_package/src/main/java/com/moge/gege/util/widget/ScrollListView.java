package com.moge.gege.util.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.moge.gege.R;

public class ScrollListView extends LinearLayout
{
    private BaseAdapter adapter;
    private OnItemClickListener mOnItemClickListener;

    private int dividerHeight = 10;
    private int dividerColor = R.color.life_group_bg_color;

    public ScrollListView(Context context)
    {
        super(context);
        initAttr(null);
    }

    public ScrollListView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initAttr(attrs);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.ScrollListView, 0, 0);

        dividerHeight = a.getDimensionPixelSize(
                R.styleable.ScrollListView_divider_height, -1);

        dividerColor = a.getColor(R.styleable.ScrollListView_divider_color,
                R.color.life_group_bg_color);

        a.recycle();
    }

    public void initAttr(AttributeSet attrs)
    {
        setOrientation(VERTICAL);
    }

    public BaseAdapter getAdapter()
    {
        return adapter;
    }

    public void setAdapter(BaseAdapter adpater)
    {
        this.adapter = adpater;
    }

    private void bindView()
    {
        if (adapter == null)
        {
            return;
        }
        if (getChildCount() > 0)
        {
            removeAllViews();
        }
        final int count = adapter.getCount();
        LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT);

        LayoutParams dividerParams = new LayoutParams(LayoutParams.FILL_PARENT,
                dividerHeight);
        for (int i = 0; i < count; i++)
        {
            View convertView = adapter.getView(i, null, this);
            final int index = i;
            final LinearLayout layout = new LinearLayout(getContext());
            layout.setLayoutParams(params);
            layout.setOrientation(VERTICAL);
            if (convertView != null)
            {
                convertView.setOnClickListener(new OnClickListener()
                {

                    @Override
                    public void onClick(View v)
                    {
                        if (mOnItemClickListener != null)
                        {
                            mOnItemClickListener.onItemClick(
                                    ScrollListView.this, layout, index,
                                    adapter.getItem(index));
                        }
                    }
                });

                layout.addView(convertView);

                if (i != count - 1)
                {
                    // add divider
                    View imageView = new View(getContext());
                    imageView.setBackgroundColor(dividerColor);
                    imageView.setLayoutParams(dividerParams);
                    layout.addView(imageView);
                }

                addView(layout, index);
            }
        }

    }

    public void notifyDataChange()
    {
        bindView();
    }

    public View getNeedChangeView(String tag)
    {
        if (tag == null)
        {
            return null;
        }
        return this.findViewWithTag(tag);
    }

    public void notifyViewByIndex(int index)
    {
        removeViewAt(index);
        View convertView = adapter.getView(index, null, this);
        addView(convertView, index);
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.mOnItemClickListener = listener;
    }

    public interface OnItemClickListener
    {
        void onItemClick(ViewGroup parent, View view, int position, Object o);

    }
}
