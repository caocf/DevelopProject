package com.ebox.pub.utils;

import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.widget.EditText;

public class EditTextUtil {
	public static void limitCount(final EditText text, final int count, 
			final inputFinishListener listener) 
	{
		text.addTextChangedListener(new TextWatcher()
		{
			private CharSequence temp;
			private boolean isEdit = true;

			public void afterTextChanged(Editable s)
			{
				if (temp.length() > count)
				{
					isEdit = false;
					s.delete(temp.length() - 1, temp.length());
					text.setText(s);
				}
				
				if(temp.length() == count && listener != null)
				{
					listener.onInputFinish();
				}
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after)
			{
				temp = s;
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count)
			{
				if (isEdit == false)
				{
					Editable etext = text.getText();

					int pos = etext.length();

					Selection.setSelection(etext, pos);
				}
			}
		});
	}
	
	public interface inputFinishListener
	{
		void onInputFinish();
	}
}
