package com.ebox.pub.service.task.images;

public class OrderPictureHelper {
	public final static String separator = "_";

	public static String getItemId(String pic)
	{
		int firstIndex = pic.indexOf(separator);
		int lastIndex = pic.lastIndexOf(separator);
		
		return pic.substring(firstIndex+1, lastIndex);
	}

	public static String getOrderId(String pic){
		int firstIndex = pic.indexOf(separator);
		return pic.substring(0,firstIndex);
	}

	public static String getOrderType(String pic){
		int s=pic.lastIndexOf("_")+1;
		int e=pic.lastIndexOf(".");

		String type = pic.substring(s, e);

		if (type.equals("0")) {
			return OrderPictureType.store;
		} else {
			return  OrderPictureType.pickup;
		}
	}

}
