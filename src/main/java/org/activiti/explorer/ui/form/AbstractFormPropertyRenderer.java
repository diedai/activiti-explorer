 package org.activiti.explorer.ui.form;
 
 import com.vaadin.ui.Field;
 import com.vaadin.ui.Form;
 import org.activiti.engine.form.FormProperty;
 import org.activiti.engine.form.FormType;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public abstract class AbstractFormPropertyRenderer
   implements FormPropertyRenderer
 {
   private Class<? extends FormType> formType;
   
   public AbstractFormPropertyRenderer(Class<? extends FormType> formType)
   {
     this.formType = formType;
   }
   
   public Class<? extends FormType> getFormType() {
     return this.formType;
   }
   
   public String getPropertyLabel(FormProperty formProperty) {
     if (formProperty.getName() != null) {
       return formProperty.getName();
     }
     return formProperty.getId();
   }
   
 
   public String getFieldValue(FormProperty formProperty, Field field)
   {
     Object value = field.getValue();
     if (value != null) {
       return value.toString();
     }
     return null;
   }
   
   public abstract Field getPropertyField(FormProperty paramFormProperty);
   
   protected String getMessage(String key, Object... params) {
     return ExplorerApp.get().getI18nManager().getMessage(key, params);
   }
   
 
 
 
   public Form getForm()
   {
     return this.theForm;
   }
   
 
 
 
   public void setForm(Form p_form)
   {
     this.theForm = p_form;
   }
   
 
 
 
   private transient Form theForm = null;
 }


