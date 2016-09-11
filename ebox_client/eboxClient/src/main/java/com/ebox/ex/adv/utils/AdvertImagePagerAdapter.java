/*
 * Copyright 2014 trinea.cn All right reserved. This software is the confidential and proprietary information of
 * trinea.cn ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with trinea.cn.
 */
package com.ebox.ex.adv.utils;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ebox.pub.utils.ImageLoaderUtil;

/**
 * AdvertImagePagerAdapter
 * 
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2014-2-23
 */
public class AdvertImagePagerAdapter extends RecyclingPagerAdapter {

	private Context context;
	private List<String> imageBitmaps;

	private int size;
	private boolean isInfiniteLoop;
	private AutoScrollViewPager mPager;

	public AdvertImagePagerAdapter(Context context, List<String> imageBitmaps,
			AutoScrollViewPager mPager) {
		this.context = context;
		// this.imageIdList = imageIdList;
		this.imageBitmaps = imageBitmaps;
		this.size = imageBitmaps.size();
		isInfiniteLoop = false;
		this.mPager = mPager;
	}

	@Override
	public int getCount() {
		// Infinite loop
		return isInfiniteLoop ? Integer.MAX_VALUE : imageBitmaps.size();
	}

	/**
	 * get really position
	 * 
	 * @param position
	 * @return
	 */
	private int getPosition(int position) {
		return position % size;
	}

	@Override
	public View getView(final int position, View view, ViewGroup container) {
		ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = holder.imageView = new ImageView(context);
			holder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		// holder.imageView.setImageResource(imageIdList.get(getPosition(position)));
//		holder.imageView
//				.setImageBitmap(imageBitmaps.get(getPosition(position)));

		ImageLoaderUtil.instance().displayImage(imageBitmaps.get(getPosition(position)), holder.imageView);
		// holder.imageView.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// }
		// });
		mPager.setObjectForPosition(view, position);
		return view;
	}

	private static class ViewHolder {

		ImageView imageView;
	}

	/**
	 * @return the isInfiniteLoop
	 */
	public boolean isInfiniteLoop() {
		return isInfiniteLoop;
	}

	/**
	 * @param isInfiniteLoop
	 *            the isInfiniteLoop to set
	 */
	public AdvertImagePagerAdapter setInfiniteLoop(boolean isInfiniteLoop) {
		this.isInfiniteLoop = isInfiniteLoop;
		return this;
	}
}
