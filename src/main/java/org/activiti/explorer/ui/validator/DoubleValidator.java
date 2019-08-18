 package org.activiti.explorer.ui.validator;
 
 import com.vaadin.data.validator.AbstractStringValidator;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class DoubleValidator
   extends AbstractStringValidator
 {
   private static final long serialVersionUID = 8306001395582004472L;
   
   public DoubleValidator(String errorMessage)
   {
     super(errorMessage);
   }
   
   protected boolean isValidString(String value)
   {
     try {
       Double.parseDouble(value);
       return true;
     } catch (Exception e) {}
     return false;
   }
 }


