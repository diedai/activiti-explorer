 package org.activiti.explorer.ui.form;
 
 import com.vaadin.ui.Field;
 import com.vaadin.ui.TextField;
 import org.activiti.engine.form.FormProperty;
 import org.activiti.engine.impl.form.StringFormType;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class StringFormPropertyRenderer
   extends AbstractFormPropertyRenderer
 {
   public StringFormPropertyRenderer()
   {
     super(StringFormType.class);
   }
   
   public Field getPropertyField(FormProperty formProperty)
   {
     TextField textField = new TextField(getPropertyLabel(formProperty));
     textField.setRequired(formProperty.isRequired());
     textField.setEnabled(formProperty.isWritable());
     textField.setRequiredError(getMessage("form.field.required", new Object[] { getPropertyLabel(formProperty) }));
     
     if (formProperty.getValue() != null) {
       textField.setValue(formProperty.getValue());
     }
     
     return textField;
   }
 }


