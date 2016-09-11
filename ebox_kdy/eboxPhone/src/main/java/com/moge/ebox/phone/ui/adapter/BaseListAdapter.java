package com.moge.ebox.phone.ui.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.SparseArray;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * The Class BaseListAdapter.
 * 
 * @param <T>
 *            the generic type
 */
public abstract class BaseListAdapter<T> extends BaseAdapter implements
		KeyValues {

	protected final List<T> list;

	public BaseListAdapter() {
		super();
		this.list = new ArrayList<T>();
	}

	public boolean addAll(List<? extends T> list) {
		return this.list.addAll(list);
	}

	public void clear() {
		if (list != null) {
			this.list.clear();
		}
	}

	public int getCount() {
		return list == null ? 0 : list.size();
	}

	public int getItemChildCount() {
		return 1;
	}

	public T getItem(int i) {
		return this.list.get(i);
	}

	public long getItemId(int id) {
		return id;
	}

	public boolean isEmpty() {
		return list == null ? true : list.isEmpty();
	}

	public T remove(int i) {
		return list.remove(i);
	}

	public boolean remove(T t) {
		return list.remove(t);
	}

	public void addItem(T t) {
		this.list.add(t);
	}

	public void addItem(int location, T t) {
		this.list.add(location, t);
	}

	protected void setValues(Map<View, String> valuesMap, JSONObject json)
			throws JSONException {

		Set<Entry<View, String>> entrySet = valuesMap.entrySet();
		for (Entry<View, String> entry : entrySet) {
			View key = entry.getKey();
			String value = entry.getValue();
			if (TextUtils.isEmpty(value)) {
				String values = json.getString(value);
				((TextView) key).setText(values);
			}
		}
	}

 
	public void hightColor(TextView tv,String mathcer,String res) {
		SpannableString s = new SpannableString(res);
		Pattern p = Pattern.compile(mathcer);
		Matcher m = p.matcher(s);
		boolean hasMatch=false;
		int color=Color.parseColor("#8ec640");
		while (m.find()&&!hasMatch)
		{
			hasMatch=true;
			int start = m.start();
			int end = m.end();
			s.setSpan(new ForegroundColorSpan(color), start, end,
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		tv.setText(s);
	}

	protected static class ViewHolder {
		// I added a generic return type to reduce the casting noise in client
		@SuppressWarnings("unchecked")
		public static <T extends View> T get(View view, int id) {
			SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
			if (viewHolder == null) {
				viewHolder = new SparseArray<View>();
				view.setTag(viewHolder);
			}
			View childView = viewHolder.get(id);
			if (childView == null) {
				childView = view.findViewById(id);
				viewHolder.put(id, childView);
			}
			return (T) childView;
		}
	}
}
