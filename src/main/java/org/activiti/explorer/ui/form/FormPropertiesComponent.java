 package org.activiti.explorer.ui.form;
 
 import com.vaadin.data.Validator.InvalidValueException;
 import com.vaadin.ui.Alignment;
 import com.vaadin.ui.Component;
 import com.vaadin.ui.Field;
 import com.vaadin.ui.Form;
 import com.vaadin.ui.VerticalLayout;
 import java.util.HashMap;
 import java.util.List;
 import java.util.Map;
 import org.activiti.engine.form.FormProperty;
 import org.activiti.engine.form.FormType;
 import org.activiti.explorer.ExplorerApp;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class FormPropertiesComponent
   extends VerticalLayout
 {
   private static final long serialVersionUID = 1L;
   protected FormPropertyRendererManager formPropertyRendererManager;
   protected List<FormProperty> formProperties;
   protected Map<FormProperty, Component> propertyComponents;
   protected Form form;
   
   public FormPropertiesComponent()
   {
     this.formPropertyRendererManager = ExplorerApp.get().getFormPropertyRendererManager();
     
     setSizeFull();
     initForm();
   }
   
   public List<FormProperty> getFormProperties() {
     return this.formProperties;
   }
   
   public void setFormProperties(List<FormProperty> formProperties) {
     this.formProperties = formProperties;
     
     this.form.removeAllProperties();
     
 
     if (formProperties != null) {
       for (FormProperty formProperty : formProperties) {
         FormPropertyRenderer renderer = getRenderer(formProperty);
         
 
         renderer.setForm(this.form);
         
         Field editorComponent = renderer.getPropertyField(formProperty);
         if (editorComponent != null)
         {
           this.form.addField(formProperty.getId(), editorComponent);
         }
       }
     }
   }
   
 
 
 
 
 
   public Map<String, String> getFormPropertyValues()
     throws InvalidValueException
   {
     this.form.commit();
     
     Map<String, String> formPropertyValues = new HashMap();
     
 
     for (FormProperty formProperty : this.formProperties) {
       if (formProperty.isWritable()) {
         Field field = this.form.getField(formProperty.getId());
         FormPropertyRenderer renderer = getRenderer(formProperty);
         String fieldValue = renderer.getFieldValue(formProperty, field);
         
         formPropertyValues.put(formProperty.getId(), fieldValue);
       }
     }
     return formPropertyValues;
   }
   
   public void setFormEnabled(boolean enabled)
   {
     if (enabled) {
       this.form.setEnabled(enabled);
     }
   }
   
   protected void initForm() {
     this.form = new Form();
     this.form.setSizeFull();
     
     addComponent(this.form);
     setComponentAlignment(this.form, Alignment.TOP_CENTER);
   }
   
   protected FormPropertyRenderer getRenderer(FormProperty formProperty) {
     FormType formPropertyType = formProperty.getType();
     if (formPropertyType == null) {
       return this.formPropertyRendererManager.getTypeLessFormPropertyRenderer();
     }
     return this.formPropertyRendererManager.getPropertyRendererForType(formProperty.getType());
   }
 }


