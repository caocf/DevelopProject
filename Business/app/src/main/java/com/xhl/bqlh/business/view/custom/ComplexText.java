package com.xhl.bqlh.business.view.custom;

import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;

/**
 * Created by Summer on 2016/7/28.
 */
public class ComplexText {

    private ComplexText() {
    }

    public static class TextBuilder {
        private String text;//内容
        private int textColor = 0;//字体颜色
        private int[] textColorLen;
        private int textBackgroundColor = 0;//字体背景色
        private int textSizePx = 0;//字体大小像素
        private int textSizeDp = 0;//字体大小Dip
        private boolean bold = false;//粗体
        private boolean strikeLine = false;//删除线
        private boolean underLine = false;//下划线

        public TextBuilder() {
        }

        public TextBuilder(String text) {
            this.text = text;
        }

        public TextBuilder setText(String text) {
            this.text = text;
            return this;
        }

        public TextBuilder setTextColor(int textColor) {
            this.textColor = textColor;
            return this;
        }

        public TextBuilder setTextColor(int textColor, int start, int end) {
            textColorLen = new int[2];
            textColorLen[0] = start;
            textColorLen[1] = end;
            this.textColor = textColor;
            return this;
        }


        public TextBuilder setTextBackgroundColor(int textBackgroundColor) {
            this.textBackgroundColor = textBackgroundColor;
            return this;
        }

        public TextBuilder setTextSizePx(int textSizePx) {
            this.textSizePx = textSizePx;
            return this;
        }

        public TextBuilder setTextSizeDp(int textSizeDp) {
            this.textSizeDp = textSizeDp;
            return this;
        }

        public TextBuilder setBold(boolean bold) {
            this.bold = bold;
            return this;
        }

        public TextBuilder setStrikeLine(boolean strikeLine) {
            this.strikeLine = strikeLine;
            return this;
        }

        public TextBuilder setUnderLine(boolean underLine) {
            this.underLine = underLine;
            return this;
        }

        public SpannableString Build() {
            int length = text.length();
            SpannableString st = new SpannableString(text);
            if (textColor != 0) {
                int start = 0;
                int end = length;
                if (textColorLen != null) {
                    start = textColorLen[0];
                    end = textColorLen[1];
                }
                st.setSpan(new ForegroundColorSpan(textColor), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }
            if (textBackgroundColor != 0) {
                st.setSpan(new BackgroundColorSpan(textBackgroundColor), 0, length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }
            if (textSizePx != 0) {
                st.setSpan(new AbsoluteSizeSpan(textSizePx), 0, length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }
            if (textSizeDp != 0) {
                st.setSpan(new AbsoluteSizeSpan(textSizeDp, true), 0, length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }
            if (bold) {
                st.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }
            if (strikeLine) {
                st.setSpan(new StrikethroughSpan(), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if (underLine) {
                st.setSpan(new UnderlineSpan(), 0, length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }
            return st;
        }
    }

    public static SpannableStringBuilder getText(TextBuilder... builders) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        for (TextBuilder text : builders) {
            builder.append(text.Build());
        }
        return builder;
    }

}
