 package org.activiti.explorer.form;
 
 import org.activiti.engine.ActivitiObjectNotFoundException;
 import org.activiti.engine.IdentityService;
 import org.activiti.engine.ProcessEngine;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.engine.form.AbstractFormType;
 import org.activiti.engine.identity.User;
 import org.activiti.engine.identity.UserQuery;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class UserFormType
   extends AbstractFormType
 {
   private static final long serialVersionUID = 1L;
   public static final String TYPE_NAME = "user";
   
   public String getName()
   {
     return "user";
   }
   
 
   public Object convertFormValueToModelValue(String propertyValue)
   {
     if (propertyValue != null)
     {
 
 
 
       long count = ProcessEngines.getDefaultProcessEngine().getIdentityService().createUserQuery().userId(propertyValue).count();
       
       if (count == 0L) {
         throw new ActivitiObjectNotFoundException("User " + propertyValue + " does not exist", User.class);
       }
       return propertyValue;
     }
     return null;
   }
   
   public String convertModelValueToFormValue(Object modelValue)
   {
     return (String)modelValue;
   }
 }


