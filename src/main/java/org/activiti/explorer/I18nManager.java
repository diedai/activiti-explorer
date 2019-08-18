 package org.activiti.explorer;
 
 import java.io.Serializable;
 import java.util.Locale;
 import org.springframework.context.MessageSource;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class I18nManager
   implements Serializable
 {
   private static final long serialVersionUID = 1L;
   protected MessageSource messageSource;
   protected Locale locale;
   
   public String getMessage(String key)
   {
     checkLocale();
     return this.messageSource.getMessage(key, null, this.locale);
   }
   
   public String getMessage(String key, Object... arguments) {
     checkLocale();
     return this.messageSource.getMessage(key, arguments, this.locale);
   }
   
   public void setLocale(Locale locale) {
     this.locale = locale;
   }
   
   protected void checkLocale() {
     if (this.locale == null) {
       this.locale = ExplorerApp.get().getLocale();
     }
   }
   
   public void setMessageSource(MessageSource messageSource) {
     this.messageSource = messageSource;
   }
 }


