package com.ebox.pub.ui.keyboard;  
   
import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.media.AudioManager;
import android.text.Editable;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.ebox.R;
import com.ebox.ex.ui.bar.OperMainLayout;
import com.ebox.ex.network.model.enums.MainBoardType;
import com.ebox.pub.service.global.GlobalField;
import com.ebox.pub.utils.RingUtil;

import java.lang.reflect.Method;
   
public class KeyboardUtil {  
        private KeyboardView keyboardView;  
        private Keyboard k;
        public boolean isnun = false;// 是否数据键盘  
        public boolean isupper = false;// 是否大写
        public boolean isNumberAudio = false;// 是否播放数字音效
   
        private EditText ed;  
        private onKeyListener keyListener;
        
        private Context ctx;
        
        public void setNumberAudio(boolean isNumberAudio)
        {
        	this.isNumberAudio = isNumberAudio;
        }
   
        public KeyboardUtil(Activity act, Context ctx, EditText edit) {  
                this.ed = edit;  
                this.ctx  = ctx;
                k = new Keyboard(ctx, R.xml.symbols_17);
                keyboardView = (KeyboardView) act.findViewById(R.id.keyboard_view);  
                keyboardView.setKeyboard(k);  
                keyboardView.setEnabled(true);  
                keyboardView.setPreviewEnabled(false);  
                keyboardView.setOnKeyboardActionListener(listener);
                keyboardView.setDrawingCacheEnabled(true);
                keyboardView.setBackgroundResource(R.color.transparent);
        }  
        
        public KeyboardUtil(OperMainLayout act, Context ctx, EditText edit) {  
            this.ed = edit;  
            this.ctx  = ctx;
            k = new Keyboard(ctx, R.xml.symbols_17);
            keyboardView = (KeyboardView) act.findViewById(R.id.keyboard_view);  
            keyboardView.setKeyboard(k);  
            keyboardView.setEnabled(true);  
            keyboardView.setPreviewEnabled(false);  
            keyboardView.setOnKeyboardActionListener(listener);
            keyboardView.setDrawingCacheEnabled(true);
            keyboardView.setBackgroundResource(R.color.transparent);
        }  
   
        private OnKeyboardActionListener listener = new OnKeyboardActionListener() {  
                @Override  
                public void swipeUp() {  
                }  
   
                @Override  
                public void swipeRight() {  
                }  
   
                @Override  
                public void swipeLeft() {  
                }  
   
                @Override  
                public void swipeDown() {  
                }  
   
                @Override  
                public void onText(CharSequence text) {  
                }  
   
                @Override  
                public void onRelease(int primaryCode) {  
                }  
   
                @Override  
                public void onPress(int primaryCode) {  
                }  
   
                @Override  
                public void onKey(int primaryCode, int[] keyCodes) {  
                	
                        Editable editable = ed.getText();  
                        int start = ed.getSelectionStart();  
                        // 删除  
                        if (primaryCode == Keyboard.KEYCODE_DELETE) 
                        {
                            if (editable != null && editable.length() > 0) 
                            {  
                                if (start > 0) 
                                {  
                                    editable.delete(start - 1, start);  
                                }  
                            }  
                        }
                        // 清空  
                        else if (primaryCode == Keyboard.KEYCODE_CANCEL) 
                        {
                            editable.clear();
                        }
                        else 
                        {  
                            editable.insert(start, Character.toString((char) primaryCode));  
                        }  
                        
                        if(isNumberAudio)
                        {
	                        if(primaryCode == 48)
	                        {
	                        	RingUtil.playRingtone(RingUtil.id_0);
	                        }
	                        else if(primaryCode == 49)
	                        {
	                        	RingUtil.playRingtone(RingUtil.id_1);
	                        }
	                        else if(primaryCode == 50)
	                        {
	                        	RingUtil.playRingtone(RingUtil.id_2);
	                        }
	                        else if(primaryCode == 51)
	                        {
	                        	RingUtil.playRingtone(RingUtil.id_3);
	                        }
	                        else if(primaryCode == 52)
	                        {
	                        	RingUtil.playRingtone(RingUtil.id_4);
	                        }
	                        else if(primaryCode == 53)
	                        {
	                        	RingUtil.playRingtone(RingUtil.id_5);
	                        }
	                        else if(primaryCode == 54)
	                        {
	                        	RingUtil.playRingtone(RingUtil.id_6);
	                        }
	                        else if(primaryCode == 55)
	                        {
	                        	RingUtil.playRingtone(RingUtil.id_7);
	                        }
	                        else if(primaryCode == 56)
	                        {
	                        	RingUtil.playRingtone(RingUtil.id_8);
	                        }
	                        else if(primaryCode == 57)
	                        {
	                        	RingUtil.playRingtone(RingUtil.id_9);
	                        }
	                        else
	                        {
	                    		// 播放按键音
	    	                	final AudioManager audioManager = (AudioManager) ctx.getSystemService(Context.AUDIO_SERVICE); 
	    	                	audioManager.playSoundEffect(AudioManager.FX_KEY_CLICK);
	                        }
                        }
                        else
                        {
                        	// 播放按键音
    	                	final AudioManager audioManager = (AudioManager) ctx.getSystemService(Context.AUDIO_SERVICE); 
    	                	audioManager.playSoundEffect(AudioManager.FX_KEY_CLICK);
                        }
                        
                        if(keyListener != null)
                        {
                        	keyListener.onKey(primaryCode);
                        }
                }  
        };  
           
    public void showKeyboard() {  
        int visibility = keyboardView.getVisibility();  
        if (visibility == View.GONE || visibility == View.INVISIBLE) {  
            keyboardView.setVisibility(View.VISIBLE);  
        }  
    }  
       
    public void hideKeyboard() {  
        int visibility = keyboardView.getVisibility();  
        if (visibility == View.VISIBLE) {  
            keyboardView.setVisibility(View.INVISIBLE);  
        }  
    }  
    
    public static void hideInput(Activity activity,EditText ed)
    {
    	//ed.setInputType(InputType.TYPE_NULL);
    	activity.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		 try {
		         Class<EditText> cls = EditText.class;
		         Method setShowSoftInputOnFocus;
		         if(GlobalField.config.getMaim_board() == MainBoardType.A31S_VER1) {
		        	 setShowSoftInputOnFocus = cls.getMethod(
	                         "setShowSoftInputOnFocus", boolean.class);
		         } else {
		        	 setShowSoftInputOnFocus = cls.getMethod(
	                         "setSoftInputShownOnFocus", boolean.class);
		         }
		         setShowSoftInputOnFocus.setAccessible(true);
		         setShowSoftInputOnFocus.invoke(ed, false);
		 } catch (Exception e) {
		         e.printStackTrace();
		 }
    }
    
    public void setonKeyListener(onKeyListener listener)
    {
    	keyListener = listener;
    }
    
    public interface onKeyListener
	{
		void onKey(int primaryCode);
	}
}  