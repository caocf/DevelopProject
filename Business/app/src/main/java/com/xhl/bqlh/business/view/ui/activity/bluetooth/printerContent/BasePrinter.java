package com.xhl.bqlh.business.view.ui.activity.bluetooth.printerContent;

import android.graphics.Paint;

import com.xhl.xhl_library.utils.log.Logger;

/**
 * Created by Sum on 16/6/25.
 */
public abstract class BasePrinter implements Printer {

    private static String SPACE = " ";
    protected static String DIVIDER_LINE = "------------------------------------";
    protected static String RETURN = "\r\n\r\n";

    //指定文本的最大宽度，在最大宽度上进行分割段落
    private static final float mDefaultMaxWidth = 306;

    private static final String Num = "12345678912345678912345678912345";
    private static final String Chi = "蓝牙是一种低成本大容量的短距离无";

    protected float mNumLen = 0;//数字长度
    protected float mTxtLen = 0;//文本长度

    public BasePrinter() {

        Paint paint = new Paint();
        paint.setTextSize(14);
//        //以虚线为计算最大单位
//        mDefaultMaxWidth = paint.measureText(DIVIDER_LINE);

        mNumLen = paint.measureText("1");
        mTxtLen = paint.measureText(Num) / Chi.length();

        Logger.v("numLen:" + mNumLen + " chiLen:" + mTxtLen + " maxLen:" + mDefaultMaxWidth);
    }

    //文本打印的位置
    public enum TxtGravity {
        START, CENTER, END
    }

    protected String company(){
        return centerLineText("南京新货郎电子商务有限公司");
    }

    /**
     * 获取打印的中间文本
     */
    public String centerLineText(String content) {

        //计算文本长度
        float totalTextLen = getTextLen(content);
        //计算左右偏移量
        int emptyLen = (int) ((mDefaultMaxWidth - totalTextLen) / (2 * mNumLen));
        String emptyText = "";
        for (int i = 0; i < emptyLen; i++) {
            emptyText = emptyText + SPACE;
        }
        return emptyText + content + emptyText;
    }

    public String averageLineText(TxtGravity gravity, String... contents) {
        //文本个数
        int size = contents.length;
        if (size == 0) {
            return null;
        }
        //计算平分长度
        float itemWidth = mDefaultMaxWidth / size;
//        Logger.v("item width:" + itemWidth);
        StringBuilder builder = new StringBuilder();
        for (String itemText : contents) {

            float textLen = getTextLen(itemText);
            int emptyLen;
            String emptyText;
            switch (gravity) {
                case START:
                    emptyLen = (int) ((itemWidth - textLen) / mNumLen);
                    emptyText = "";
                    for (int i = 0; i < emptyLen; i++) {
                        emptyText = emptyText + SPACE;
                    }
                    builder.append(itemText).append(emptyText);
                    break;
                case CENTER:
                    emptyLen = (int) ((itemWidth - textLen) / (2 * mNumLen));
                    emptyText = "";
                    for (int i = 0; i < emptyLen; i++) {
                        emptyText = emptyText + SPACE;
                    }
                    builder.append(emptyText).append(itemText).append(emptyText);
                    break;
                case END:
                    emptyLen = (int) ((itemWidth - textLen) / mNumLen);
                    emptyText = "";
                    for (int i = 0; i < emptyLen; i++) {
                        emptyText = emptyText + SPACE;
                    }
                    builder.append(emptyText).append(itemText);
                    break;
            }
        }
//        Logger.v("averageLineText:" + builder.toString());
        return builder.toString();
    }

    //获取文本长度
    private float getTextLen(String content) {
        float totalTextLen = 0;
        int textLen = content.length();
        for (int i = 0; i < textLen; i++) {
            if (isChinese(content.charAt(i))) {
                totalTextLen += mTxtLen;
            } else {
                totalTextLen += mNumLen;
            }
        }
        return totalTextLen;
    }

    private static boolean isChinese(char var0) {
        return var0 >= 19968 && var0 <= 171941;
    }
}
