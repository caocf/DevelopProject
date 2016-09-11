package com.moge.gege.util.widget;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class LinkTextView extends TextView
{
    private OnLinkClickListener mLinkClickListener = new LinkTextView.OnLinkClickListener()
    {
        @Override
        public void onLinkClick()
        {

        }
    };

    public LinkTextView(Context context)
    {
        super(context);
    }

    public LinkTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public LinkTextView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public OnLinkClickListener getLinkClickListener()
    {
        return mLinkClickListener;
    }

    public void setLinkClickListener(OnLinkClickListener linkClickListener)
    {
        this.mLinkClickListener = linkClickListener;
    }

    public void setLinkText(String linktxt)
    {
        Spanned span = Html.fromHtml(linktxt);
        setText(span);
        setDefaultStyle(span);
    }

    public void setDefaultStyle(Spanned spanhtml)
    {
        CharSequence text = getText();
        int end = text.length();
        URLSpan[] urls = spanhtml.getSpans(0, end, URLSpan.class);

        if (urls.length == 0)
        {
            return;
        }

        SpannableStringBuilder style = new SpannableStringBuilder(text);
        style.clearSpans();
        for (URLSpan url : urls)
        {
            style.removeSpan(url);
            NoLinkSpan span = new NoLinkSpan(url.getURL());
            style.setSpan(span, spanhtml.getSpanStart(url),
                    spanhtml.getSpanEnd(url),
                    Spannable.SPAN_INCLUSIVE_EXCLUSIVE); // SPAN_EXCLUSIVE_EXCLUSIVE
        }
        setText(style);
    }

    public class NoLinkSpan extends ClickableSpan
    {
        private String text;

        public NoLinkSpan(String text)
        {
            super();
            this.text = text;
        }

        @Override
        public void updateDrawState(TextPaint ds)
        {
            ds.setColor(Color.BLACK);
            ds.setUnderlineText(false); // 去掉下划线
        }

        @Override
        public void onClick(View widget)
        {
            // doNothing...
        }

    }

    public interface OnLinkClickListener
    {
        void onLinkClick();
    }

}
