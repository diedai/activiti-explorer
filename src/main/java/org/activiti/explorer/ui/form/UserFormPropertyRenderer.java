 package org.activiti.explorer.ui.form;
 
 import com.vaadin.ui.Field;
 import org.activiti.engine.form.FormProperty;
 import org.activiti.explorer.form.UserFormType;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class UserFormPropertyRenderer
   extends AbstractFormPropertyRenderer
 {
   public UserFormPropertyRenderer()
   {
     super(UserFormType.class);
   }
   
   public Field getPropertyField(FormProperty formProperty)
   {
     SelectUserField selectUserField = new SelectUserField(getPropertyLabel(formProperty));
     selectUserField.setRequired(formProperty.isRequired());
     selectUserField.setRequiredError(getMessage("form.field.required", new Object[] { getPropertyLabel(formProperty) }));
     selectUserField.setEnabled(formProperty.isWritable());
     
     if (formProperty.getValue() != null) {
       selectUserField.setValue(formProperty.getValue());
     }
     
     return selectUserField;
   }
 }


