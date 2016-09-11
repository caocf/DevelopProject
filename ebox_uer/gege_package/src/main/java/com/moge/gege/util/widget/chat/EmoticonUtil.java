package com.moge.gege.util.widget.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.Log;

import com.moge.gege.R;
import com.moge.gege.util.FunctionUtil;

public class EmoticonUtil
{
    private static final int pageSize = 20;
    private static EmoticonUtil instance;

    private HashMap<String, String> mEmoticonMap = new HashMap<String, String>();
    private List<EmoticonEntity> mEmoticonList = new ArrayList<EmoticonEntity>();
    private List<List<EmoticonEntity>> mEmoticonPageList = new ArrayList<List<EmoticonEntity>>();

    private int mEmoticonSizeInTextView = 40;
    private int mEmoticonSizeInEditText = 25;

    private EmoticonUtil()
    {
    }

    public boolean isEmoticonEmpty()
    {
        return mEmoticonPageList.size() == 0;
    }

    public static EmoticonUtil instance()
    {
        if (instance == null)
        {
            instance = new EmoticonUtil();
        }
        return instance;
    }

    public void init(Context context)
    {
        synchronized (mEmoticonMap)
        {
            parseData(getEmoticonFile(context), context);
        }

        mEmoticonSizeInTextView = FunctionUtil.dip2px(context, context
                .getResources().getDimension(R.dimen.emoticon_textview_size));

        mEmoticonSizeInEditText = FunctionUtil.dip2px(context, context
                .getResources().getDimension(R.dimen.emoticon_edittext_size));
    }

    public List<List<EmoticonEntity>> getEmoticonDataSource()
    {
        return mEmoticonPageList;
    }

    public SpannableString getEmoticonString(Context context, int resId,
            String spannableString)
    {
        if (TextUtils.isEmpty(spannableString))
        {
            return null;
        }

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                resId);
        bitmap = Bitmap.createScaledBitmap(bitmap, mEmoticonSizeInEditText,
                mEmoticonSizeInEditText, true);
        ImageSpan imageSpan = new ImageSpan(context, bitmap);
        SpannableString spannable = new SpannableString(spannableString);
        spannable.setSpan(imageSpan, 0, spannableString.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    public SpannableString formatEmoticonString(Context context,
            CharSequence str)
    {
        SpannableString spannableString = new SpannableString(str);
        // 正则表达式比配字符串里是否含有表情，如： 我好[开心]啊
        String zhengze = "\\[[^\\]]+\\]";
        Pattern sinaPatten = Pattern.compile(zhengze, Pattern.CASE_INSENSITIVE);
        try
        {
            dealExpression(context, spannableString, sinaPatten, 0);
        }
        catch (Exception e)
        {
            Log.e("dealExpression", e.getMessage());
        }
        return spannableString;
    }

    private void dealExpression(Context context,
            SpannableString spannableString, Pattern patten, int start)
            throws Exception
    {
        Matcher matcher = patten.matcher(spannableString);
        while (matcher.find())
        {
            String key = matcher.group();
            // 返回第一个字符的索引的文本匹配整个正则表达式,ture 则继续递归
            if (matcher.start() < start)
            {
                continue;
            }
            String value = mEmoticonMap.get(key);
            if (TextUtils.isEmpty(value))
            {
                continue;
            }
            int resId = context.getResources().getIdentifier(value, "drawable",
                    context.getPackageName());
            // 通过上面匹配得到的字符串来生成图片资源id
            // Field field=R.drawable.class.getDeclaredField(value);
            // int resId=Integer.parseInt(field.get(null).toString());
            if (resId != 0)
            {
                Bitmap bitmap = BitmapFactory.decodeResource(
                        context.getResources(), resId);
                bitmap = Bitmap.createScaledBitmap(bitmap,
                        mEmoticonSizeInTextView, mEmoticonSizeInTextView, true);
                ImageSpan imageSpan = new ImageSpan(bitmap);
                int end = matcher.start() + key.length();
                spannableString.setSpan(imageSpan, matcher.start(), end,
                        Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                if (end < spannableString.length())
                {
                    dealExpression(context, spannableString, patten, end);
                }
                break;
            }
        }
    }

    private void parseData(List<String> data, Context context)
    {
        if (data == null)
        {
            return;
        }
        EmoticonEntity emojEentry;
        try
        {
            mEmoticonList.clear();

            for (String str : data)
            {
                String[] text = str.split(",");
                String fileName = text[0]
                        .substring(0, text[0].lastIndexOf("."));
                mEmoticonMap.put(text[1], fileName);
                int resID = context.getResources().getIdentifier(fileName,
                        "drawable", context.getPackageName());

                if (resID != 0)
                {
                    emojEentry = new EmoticonEntity();
                    emojEentry.setResId(resID);
                    emojEentry.setResName(fileName);
                    emojEentry.setName(text[1]);
                    mEmoticonList.add(emojEentry);
                }
            }
            int pageCount = (int) Math.ceil(mEmoticonList.size() / 20 + 0.1);

            mEmoticonPageList.clear();
            for (int i = 0; i < pageCount; i++)
            {
                mEmoticonPageList.add(getPerPageData(i));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private List<EmoticonEntity> getPerPageData(int page)
    {
        int startIndex = page * pageSize;
        int endIndex = startIndex + pageSize;

        if (endIndex > mEmoticonList.size())
        {
            endIndex = mEmoticonList.size();
        }
        List<EmoticonEntity> list = new ArrayList<EmoticonEntity>();
        list.addAll(mEmoticonList.subList(startIndex, endIndex));
        if (list.size() < pageSize)
        {
            for (int i = list.size(); i < pageSize; i++)
            {
                EmoticonEntity object = new EmoticonEntity();
                list.add(object);
            }
        }
        if (list.size() == pageSize)
        {
            EmoticonEntity object = new EmoticonEntity();
            object.setResId(R.drawable.face_del_icon);
            list.add(object);
        }
        return list;
    }

    private static List<String> getEmoticonFile(Context context)
    {
        try
        {
            List<String> list = new ArrayList<String>();
            InputStream in = context.getResources().getAssets().open("emoji");
            BufferedReader br = new BufferedReader(new InputStreamReader(in,
                    "UTF-8"));
            String str = null;
            while ((str = br.readLine()) != null)
            {
                list.add(str);
            }

            return list;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
