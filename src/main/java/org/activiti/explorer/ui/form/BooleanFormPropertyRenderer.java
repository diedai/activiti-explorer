 package org.activiti.explorer.ui.form;
 
 import com.vaadin.ui.CheckBox;
 import com.vaadin.ui.Field;
 import org.activiti.engine.form.FormProperty;
 import org.activiti.engine.impl.form.BooleanFormType;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class BooleanFormPropertyRenderer
   extends AbstractFormPropertyRenderer
 {
   public BooleanFormPropertyRenderer()
   {
     super(BooleanFormType.class);
   }
   
 
   public Field getPropertyField(FormProperty formProperty)
   {
     CheckBox checkBox = new CheckBox(getPropertyLabel(formProperty));
     checkBox.setRequired(formProperty.isRequired());
     checkBox.setEnabled(formProperty.isWritable());
     
     if (formProperty.getValue() != null) {
       Boolean value = new Boolean(Boolean.parseBoolean(formProperty.getValue()));
       checkBox.setValue(value);
     }
     
     return checkBox;
   }
 }


