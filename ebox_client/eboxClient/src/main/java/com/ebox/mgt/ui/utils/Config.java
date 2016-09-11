package com.ebox.mgt.ui.utils;

public final class Config {
	private String name;  
	private Integer value; 
	
	public String getName() {  
	    return name;  
	}  
	  
	  
	public void setName(String name) {  
	    this.name = name;  
	}  
	  
	 
	public Integer getValue() {  
	    return value;  
	}  
	  
	  
	public void setValue(Integer age) {  
	    this.value = age;  
	}  
	
	public Config()  
	{  
	}  
	public Config(String name, Integer value) {  
	      
	    this.name = name;  
	    this.value = value;  
	}  
	  
	  
}
