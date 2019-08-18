 package org.activiti.explorer.ui.validator;
 
 import com.vaadin.data.validator.AbstractStringValidator;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class LongValidator
   extends AbstractStringValidator
 {
   private static final long serialVersionUID = 8306001395582004472L;
   
   public LongValidator(String errorMessage)
   {
     super(errorMessage);
   }
   
   protected boolean isValidString(String value)
   {
     try {
       Long.parseLong(value);
       return true;
     } catch (Exception e) {}
     return false;
   }
 }


