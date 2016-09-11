package com.ebox.Anetwork.util.json;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
public class JsonSerializeUtil {
	
	    /** 
	         * 序列化方法 
	         * @param bean 
	         * @return String
	         */  
	       public static String bean2Json(Object bean){
	           Gson gson = new GsonBuilder().registerTypeAdapter(Timestamp.class,new TimestampTypeAdapter()).setDateFormat("yyyy-MM-dd HH:mm:ss").create();  
	           return gson.toJson(bean);  
	       }  
	     
		    /** 
	         * 带泛型的集合的序列化方法 
	         * @param list
	         * @return String
	         */  	       	       
	       public static <T> String list2Json(List<T> list){
	    	   Type type = new com.google.gson.reflect.TypeToken<T>() {}.getType();
	           Gson gson = new GsonBuilder().registerTypeAdapter(Timestamp.class,new TimestampTypeAdapter()).setDateFormat("yyyy-MM-dd HH:mm:ss").create();  
	           return gson.toJson(list,type);  
	       }
	       
	       public static <T> List<T> json2List(String json){
	    	   Type type = new com.google.gson.reflect.TypeToken<List<T>>() {}.getType();
	           Gson gson = new GsonBuilder().registerTypeAdapter(Timestamp.class,new TimestampTypeAdapter()).setDateFormat("yyyy-MM-dd HH:mm:ss").create();  
	           return gson.fromJson(json, type) ;
	       }
	       
		    /** 
	         * 带泛型的hash的序列化方法 
	         * @param map
	         * @return String
	         */  	       	       
	       public static <S,T> String map2Json(Map<S,T> map){
	    	   Type type = new com.google.gson.reflect.TypeToken<T>() {}.getType();
	           Gson gson = new GsonBuilder().registerTypeAdapter(Timestamp.class,new TimestampTypeAdapter()).setDateFormat("yyyy-MM-dd HH:mm:ss").create(); 
	           return gson.toJson(map,type);  
	       }	       
	       
	       
	       public static <S,T> Map<S,T>  json2Map(String json){
	    	   Type type = new com.google.gson.reflect.TypeToken<Map<S,T>>() {}.getType();
	           Gson gson = new GsonBuilder().registerTypeAdapter(Timestamp.class,new TimestampTypeAdapter()).setDateFormat("yyyy-MM-dd HH:mm:ss").create(); 
	           return gson.fromJson(json,type); 
	       }	
	       /** 
	        * 反序列化方法 
	        * @param json 
	        * @param _class
	        * @return T 泛型表示任意类型
	        */  
	       public static <T> T json2Bean(String json,Class<T> _class) {  
	    	   Gson gson = new GsonBuilder().registerTypeAdapter(Timestamp.class,new TimestampTypeAdapter()).setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	           return gson.fromJson(json, _class);
	       }
	       

//--------------------------------------测试---------------------------------------------//	       
/*	       public static class Book{
	    	   private int index;
	    	   private String bookName;
	    	
		    	public Book(int id,String bookName){
		    		this.index = id;
		    		this.bookName = bookName;
		    	}
				public String getBookName() {
					return bookName;
				}
		    	
	       }
	       
	       public static class Operators{
	    	   private int id;
	    	   private String name;
	    	   private Book book;
	    	
		    	public Operators(int id,String name,Book book){
		    		this.id = id;
		    		this.name = name;
		    		this.book = book;
		    	}

				public String getName() {
					return name;
				}
	       }
	       public static void main(String[] args) {
	    	   
	    	   Book book = new Book(11,"This My Book");
	    	   Operators user = new Operators(1,"John",book);
	    	   System.out.println(bean2Json(user));
	    	   String s = "{\"id\":1,\"name\":\"John\",\"book\":{\"index\":11,\"bookName\":\"This My Book\"}}";
	    	   System.out.println(json2Bean(s,Operators.class).getName());
	    	   List list = new Vector();
	    	   list.add(user);
	    	   List<Operators> list2 = new Vector();
	    	   list2.add(user);
	    	   System.out.println(bean2Json(list));
	    	   System.out.println(list2Json(list));
	    	   System.out.println(list2Json(list2));
	    	   String ss = "[{\"id\":1,\"name\":\"John\",\"book\":{\"index\":11,\"bookName\":\"This My Book\"}}]";
	    	   System.out.println(json2Bean(ss,List.class));
	       }*/
}
