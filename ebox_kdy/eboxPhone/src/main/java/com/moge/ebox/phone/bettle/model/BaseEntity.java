package com.moge.ebox.phone.bettle.model;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONObject;

import tools.AppContext;
import tools.AppException;
import tools.Logger;

import com.google.gson.Gson;

/**
 * 
 * 基础模型类
 * 
 * 注意 所有继承于该类的子类其属性都必须是String类型。
 * 
 * @author John
 * */
public class BaseEntity<T> {

	public Class<T> entityClass;

	public BaseEntity() {
		Type genType = getClass().getGenericSuperclass();
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		entityClass = (Class) params[0];
	}

	public T parse(String string) throws AppException {
		T data;
		try {
			data = entityClass.newInstance();
			Gson gson = new Gson();
			data = gson.fromJson(string, entityClass);
		} catch (Exception e) {
			Logger.i(e);
			throw AppException.json(e);
		}
		return data;
	}

	public ArrayList<T> parseList(String string) throws AppException {
		ArrayList<T> list = new ArrayList<T>();
		try {
			JSONArray arr = new JSONArray(string);
			for (int i = 0; i < arr.length(); i++) {
				String da = arr.getString(i);
				Gson gson = new Gson();
				T data = gson.fromJson(da, entityClass);
				list.add(data);
			}

		} catch (Exception e) {
			Logger.i(e);
			throw AppException.json(e);
		}
		return list;
	}

	public T parse(JSONObject userObject) throws AppException {
		T data;
		try {
			data = entityClass.newInstance();
			String string = userObject.toString();
			Gson gson = new Gson();
			data = gson.fromJson(string, entityClass);
		} catch (Exception e) {
			Logger.i(e);
			throw AppException.json(e);
		}
		return data;
	}

	public String printString() throws AppException {
		try {
			Gson gson = new Gson();
			return gson.toJson(this);
		} catch (Exception e) {
			Logger.i(e);
			throw AppException.json(e);
		}
	}

	/**
	 * 获取类的成员变量（成员属性）和属性值
	 * 
	 * 　 Field getField(String name) 根据变量名，返回一个具体的具有public属性的成员变量 　　　 * Field[]
	 * getFields() 返回具有public属性的成员变量的数组 　　　 * Field getDeclaredField(String
	 * name) 根据变量名，返回一个成员变量（不分public和非public属性） 　　　 * Field[] getDelcaredField()
	 * 返回所有成员变量组成的数组（不分public和非public属性）
	 */
	public Map getReflectionFieldValue() {
		Map<String, Object> map = new HashMap<String, Object>();
		Class temp = this.getClass(); // 获取Class类的对象的方法之一
		String className = temp.getSimpleName();
		try {
			// --public & 非public 属性
			Field[] fa = temp.getDeclaredFields();
			for (int i = 0; i < fa.length; i++) {
				Class cl = fa[i].getType(); // 属性的类型
				int md = fa[i].getModifiers(); // 属性的修饰域
				Field f = temp.getDeclaredField(fa[i].getName()); // 属性的值
				f.setAccessible(true); // Very Important
				Object value =  f.get(this);
				String fieldSaveName = className + "." + fa[i].getName();

				if (value == null) {
					map.put(fieldSaveName, null);
				} else {
					map.put(fieldSaveName, value);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 
	 * 将信息保存到App本地存储器SharedPreferences中
	 * 
	 * */
	public void save2App(Properties properties) {

		
		Map<String, Object> map = new HashMap<String, Object>();
		Class temp = this.getClass(); // 获取Class类的对象的方法之一
		String className = temp.getSimpleName();

		try {
			// --public & 非public 属性
			Field[] fa = temp.getDeclaredFields();
			for (int i = 0; i < fa.length; i++) {
				Class cl = fa[i].getType(); // 属性的类型
				int md = fa[i].getModifiers(); // 属性的修饰域
				Field f = temp.getDeclaredField(fa[i].getName()); // 属性的值
				f.setAccessible(true); // Very Important
				String fieldSaveName = className + "." + fa[i].getName();
				Object value =  f.get(this);
				if (value!=null) {
					properties.setProperty(fieldSaveName, value.toString());
				}else {
					properties.setProperty(fieldSaveName, "");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		

	}

	/**
	 * 
	 * 从APP本地存储SharedPreferences中获取对象
	 * 
	 * */
	public void get4App(AppContext appContext) {

		Map<String, Object> map = new HashMap<String, Object>();
		Class temp = this.getClass(); // 获取Class类的对象的方法之一
		String className = temp.getSimpleName();

		try {
			// --public & 非public 属性
			Field[] fa = temp.getDeclaredFields();
			for (int i = 0; i < fa.length; i++) {
				Class cl = fa[i].getType(); // 属性的类型
				int md = fa[i].getModifiers(); // 属性的修饰域
				Field f = temp.getDeclaredField(fa[i].getName()); // 属性的值
				f.setAccessible(true); // Very Important
				String fieldSaveName = className + "." + fa[i].getName();
				String value = appContext.getProperty(fieldSaveName);
				f.set(this, value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getFieldValue(AppContext appContext,String fieldName) {
		String value = null;
		try {
			Class temp = this.getClass();
			String className = temp.getSimpleName();
			Field f = temp.getDeclaredField(fieldName);
			f.setAccessible(true);
			Object value2 = f.get(this);
			String fieldSaveName = className + "." + fieldName;

			if (value2 == null) {
				value = appContext.getProperty(fieldSaveName);
			} else {
				value = value2.toString();
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}
	
	public void clear2App(AppContext appContext){

		Map<String, Object> map = new HashMap<String, Object>();
		Class temp = this.getClass(); // 获取Class类的对象的方法之一
		String className = temp.getSimpleName();

		try {
			// --public & 非public 属性
			Field[] fa = temp.getDeclaredFields();
			for (int i = 0; i < fa.length; i++) {
				Field f = temp.getDeclaredField(fa[i].getName()); // 属性的值
				f.setAccessible(true); // Very Important
				String fieldSaveName = className + "." + fa[i].getName();
				appContext.setProperty(fieldSaveName, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
