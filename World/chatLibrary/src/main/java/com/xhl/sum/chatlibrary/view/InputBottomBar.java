package com.xhl.sum.chatlibrary.view;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.xhl.sum.chatlibrary.R;
import com.xhl.sum.chatlibrary.adapter.ChatEmotionGridAdapter;
import com.xhl.sum.chatlibrary.adapter.ChatEmotionPagerAdapter;
import com.xhl.sum.chatlibrary.controller.EmotionHelper;
import com.xhl.sum.chatlibrary.event.InputBottomBarEvent;
import com.xhl.sum.chatlibrary.event.InputBottomBarTextEvent;
import com.xhl.sum.chatlibrary.utils.SoftInputUtils;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class InputBottomBar extends LinearLayout {

    /**
     * 加号 Button
     */
    private View actionBtn;

    /**
     * 表情 Button
     */
    private View emotionBtn;

    /**
     * 文本输入框
     */
    private EmotionEditText contentEditText;

    /**
     * 发送文本的Button
     */
    private View sendTextBtn;

    /**
     * 底部的layout，包含 emotionLayout 与 actionLayout
     */
    private View moreLayout;
    /**
     * 表情 layout
     */
    private View emotionLayout;
    private ViewPager emotionPager;

    /**
     * 其他添加动作
     */
    private View actionLayout;

    public void camearBtnClick() {
        EventBus.getDefault().post(new InputBottomBarEvent(InputBottomBarEvent.INPUTBOTTOMBAR_CAMERA_ACTION, getTag()));
    }

    public void pictureBtnClick() {
        EventBus.getDefault().post(new InputBottomBarEvent(InputBottomBarEvent.INPUTBOTTOMBAR_IMAGE_ACTION, getTag()));
    }

    public InputBottomBar(Context context) {
        super(context);
        initView(context);
    }

    public InputBottomBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }


    /**
     * 隐藏底部的图片、emtion 等 layout
     */
    public void hideMoreLayout() {
        moreLayout.setVisibility(View.GONE);
    }


    private void initView(Context context) {
        View.inflate(context, R.layout.chat_input_bottom_bar_layout, this);
        //表情按钮
        emotionBtn = _findViewById(R.id.input_bar_btn_motion);
        emotionBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onEmotionClick();
            }
        });
        //其他
        actionBtn = _findViewById(R.id.input_bar_btn_action);
        actionBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddActionClick();
            }
        });
        contentEditText = _findViewById(R.id.input_bar_et_emotion);

        emotionLayout = _findViewById(R.id.input_bar_layout_emotion);
        emotionPager = _findViewById(R.id.input_bar_viewpager_emotion);
        actionLayout = _findViewById(R.id.input_bar_layout_action);
        //发送
        sendTextBtn = _findViewById(R.id.input_bar_btn_send);
        sendTextBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onSendTextClick();
            }
        });
        moreLayout = _findViewById(R.id.input_bar_layout_more);

        findViewById(R.id.ll_add_pic).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pictureBtnClick();
            }
        });
        findViewById(R.id.ll_add_take_pic).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                camearBtnClick();
            }
        });


        setEditTextChangeListener();
        initEmotionPager();

        contentEditText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hideMoreLayout();
                SoftInputUtils.showSoftInput(getContext(), contentEditText);
            }
        });
    }

    public <T> T _findViewById(int id) {

        return (T) findViewById(id);
    }

    /**
     * 初始化 emotionPager
     */
    private void initEmotionPager() {
        List<View> views = new ArrayList<View>();
        for (int i = 0; i < EmotionHelper.emojiGroups.size(); i++) {
            views.add(getEmotionGridView(i));
        }
        ChatEmotionPagerAdapter pagerAdapter = new ChatEmotionPagerAdapter(views);
        emotionPager.setOffscreenPageLimit(3);
        emotionPager.setAdapter(pagerAdapter);
    }

    private View getEmotionGridView(int pos) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View emotionView = inflater.inflate(R.layout.chat_emotion_gridview, null, false);
        GridView gridView = (GridView) emotionView.findViewById(R.id.gridview);
        final ChatEmotionGridAdapter chatEmotionGridAdapter = new ChatEmotionGridAdapter(getContext());
        List<String> pageEmotions = EmotionHelper.emojiGroups.get(pos);
        chatEmotionGridAdapter.setDatas(pageEmotions);
        gridView.setAdapter(chatEmotionGridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String emotionText = (String) parent.getAdapter().getItem(position);
                int start = contentEditText.getSelectionStart();
                StringBuffer sb = new StringBuffer(contentEditText.getText());
                sb.replace(contentEditText.getSelectionStart(), contentEditText.getSelectionEnd(), emotionText);
                contentEditText.setText(sb.toString());

                CharSequence info = contentEditText.getText();
                if (info instanceof Spannable) {
                    Spannable spannable = (Spannable) info;
                    Selection.setSelection(spannable, start + emotionText.length());
                }
            }
        });
        return gridView;
    }

    /**
     * 展示文本输入框及相关按钮，隐藏不需要的按钮及 layout
     */
    private void showTextLayout() {
        contentEditText.setVisibility(View.VISIBLE);
        sendTextBtn.setVisibility(contentEditText.getText().length() > 0 ? VISIBLE : GONE);
        moreLayout.setVisibility(View.GONE);
        contentEditText.requestFocus();
        SoftInputUtils.showSoftInput(getContext(), contentEditText);
    }

    /**
     * 展示录音相关按钮，隐藏不需要的按钮及 layout
     */
    private void showAudioLayout() {
        contentEditText.setVisibility(View.GONE);
        moreLayout.setVisibility(View.GONE);
        SoftInputUtils.hideSoftInput(getContext(), contentEditText);
    }

    /**
     * 设置 text change 事件，有文本时展示发送按钮，没有文本时展示切换语音的按钮
     */
    private void setEditTextChangeListener() {
        contentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                boolean showSend = charSequence.length() > 0;
                sendTextBtn.setVisibility(showSend ? View.VISIBLE : GONE);
                actionBtn.setVisibility(showSend ? View.GONE : VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }


    public void onAddActionClick() {
        boolean showActionView = (GONE == moreLayout.getVisibility() || GONE == actionLayout.getVisibility());
        moreLayout.setVisibility(showActionView ? VISIBLE : GONE);
        actionLayout.setVisibility(showActionView ? VISIBLE : GONE);
        emotionLayout.setVisibility(View.GONE);
        SoftInputUtils.hideSoftInput(getContext(), contentEditText);
    }

    public void onEmotionClick() {
        boolean showEmotionView = (GONE == moreLayout.getVisibility() || GONE == emotionLayout.getVisibility());
        moreLayout.setVisibility(showEmotionView ? VISIBLE : GONE);
        emotionLayout.setVisibility(showEmotionView ? VISIBLE : GONE);
        actionLayout.setVisibility(View.GONE);
        SoftInputUtils.hideSoftInput(getContext(), contentEditText);
    }

    public void onSendTextClick() {
        String content = contentEditText.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(getContext(), R.string.message_is_null, Toast.LENGTH_SHORT).show();
            return;
        }

        contentEditText.getText().clear();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sendTextBtn.setEnabled(true);
            }
        }, 200);

        EventBus.getDefault().post(new InputBottomBarTextEvent(InputBottomBarEvent.INPUTBOTTOMBAR_SEND_TEXT_ACTION, content, getTag()));
    }

}
