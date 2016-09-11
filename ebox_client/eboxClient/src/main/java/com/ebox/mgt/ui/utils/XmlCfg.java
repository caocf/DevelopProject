package com.ebox.mgt.ui.utils;

import android.annotation.SuppressLint;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class XmlCfg {
	
	public List<Config> getOptions(InputStream inStream) throws Throwable  
	   {  
	       SAXParserFactory factory = SAXParserFactory.newInstance();//工厂模式还是单例模式？  
	       SAXParser parser =factory.newSAXParser();  
	       ConfigParse personParser =new ConfigParse();  
	       parser.parse(inStream, personParser);  
	       inStream.close();  
	       return personParser.getConfig();  
	   } 
	
		
	   private final class ConfigParse extends DefaultHandler  
	   {  
	     
	      
	    private List<Config> list = null;  
	    Config person =null;  
	    private String tag=null;  
	      
	    public List<Config> getConfig() {  
	        return list;  
	    }  
	    @Override  
	    public void startDocument() throws SAXException {  
	        list =new ArrayList<Config>();  
	    }  
	  
	    @SuppressLint("UseValueOf")
		@Override  
	    public void startElement(String uri, String localName, String qName,  
	            Attributes attributes) throws SAXException {  
	        if("option".equals(localName))  
	        {  
	            //xml元素节点开始时触发，是“person”  
	            person = new Config();  
	        }  
	        tag =localName;//保存元素节点名称  
	    }  
	    @Override  
	    public void endElement(String uri, String localName, String qName)  
	            throws SAXException {  
	        //元素节点结束时触发，是“person”  
	        if("option".equals(localName))  
	        {  
	            list.add(person);  
	            person=null;  
	        }  
	        tag =null;//结束时，需要清空tag  
	        }  
	    @SuppressLint("UseValueOf")
		@Override  
	    public void characters(char[] ch, int start, int length)  
	            throws SAXException {  
	        if(tag!=null)  
	        {  
	            String data = new String(ch,start,length);  
	           if("name".equals(tag))  
	           {  
	               person.setName(data);  
	                 
	           }else if("value".equals(tag))  
	           {  
	               person.setValue(new Integer(data));  
	           }  
	        }  
	    }  
	  
	      
	  
	      
	         
	         
	   }  
}
