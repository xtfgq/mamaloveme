package com.netlab.loveofmum.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ListUtils {
	public static <T>   List removeDuplicateWithOrder(List<T> list) {  
	     Set<T> set = new HashSet<T>();  
	      List<T> newList = new ArrayList<T>();  
	   for (Iterator iter = list.iterator(); iter.hasNext();) {  
	          Object element = iter.next();  
	          if (set.add((T) element))  
	             newList.add((T) element);  
	       }   
	      list.clear();  
	      list.addAll(newList);  
	    
	      return newList;
	}

	public static List<String> getList(String value) {
		 String[] strs = value.split("\\,");
		 List<String> list=new ArrayList<String>();
		 for (String img:strs) {
			 list.add(img);
	        }
		return list;
	
	}
 
}
