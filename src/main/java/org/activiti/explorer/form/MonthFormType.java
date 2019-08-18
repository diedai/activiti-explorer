 package org.activiti.explorer.form;
 
 import org.activiti.engine.form.AbstractFormType;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class MonthFormType
   extends AbstractFormType
 {
   private static final long serialVersionUID = 1L;
   public static final String TYPE_NAME = "month";
   
   public String getName()
   {
     return "month";
   }
   
   public Object convertFormValueToModelValue(String propertyValue)
   {
     Integer month = Integer.valueOf(propertyValue);
     return month;
   }
   
   public String convertModelValueToFormValue(Object modelValue)
   {
     if (modelValue == null) {
       return null;
     }
     return modelValue.toString();
   }
 }


