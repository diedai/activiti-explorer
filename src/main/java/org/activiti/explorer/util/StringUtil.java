 package org.activiti.explorer.util;
 
 import java.util.Collection;
 import java.util.Iterator;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class StringUtil
 {
   public static String toReadableString(Collection collection)
   {
     Iterator it = collection.iterator();
     StringBuilder strb = new StringBuilder();
     while (it.hasNext()) {
       Object next = it.next();
       strb.append(next.toString() + ", ");
     }
     if (strb.length() > 2) {
       strb.delete(strb.length() - 2, strb.length());
     }
     return strb.toString();
   }
 }


