 package org.activiti.explorer.ui.form;
 
 import com.vaadin.ui.Field;
 import com.vaadin.ui.TextField;
 import org.activiti.engine.form.FormProperty;
 import org.activiti.engine.impl.form.DoubleFormType;
 import org.activiti.explorer.ui.validator.DoubleValidator;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class DoubleFormPropertyRenderer
   extends AbstractFormPropertyRenderer
 {
   public DoubleFormPropertyRenderer()
   {
     super(DoubleFormType.class);
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
     
 
     textField.addValidator(new DoubleValidator("Value must be a double"));
     textField.setImmediate(true);
     
     return textField;
   }
   
   protected boolean isLong(String value) {
     try {
       Double.parseDouble(value);
       return true;
     } catch (NumberFormatException nfe) {}
     return false;
   }
 }


