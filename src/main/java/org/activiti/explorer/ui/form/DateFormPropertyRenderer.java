 package org.activiti.explorer.ui.form;
 
 import com.vaadin.ui.Field;
 import com.vaadin.ui.PopupDateField;
 import java.text.ParseException;
 import java.text.SimpleDateFormat;
 import java.util.Date;
 import org.activiti.engine.form.FormProperty;
 import org.activiti.engine.form.FormType;
 import org.activiti.engine.impl.form.DateFormType;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class DateFormPropertyRenderer
   extends AbstractFormPropertyRenderer
 {
   public DateFormPropertyRenderer()
   {
     super(DateFormType.class);
   }
   
 
   public Field getPropertyField(FormProperty formProperty)
   {
     PopupDateField dateField = new PopupDateField(getPropertyLabel(formProperty));
     String datePattern = (String)formProperty.getType().getInformation("datePattern");
     dateField.setDateFormat(datePattern);
     dateField.setRequired(formProperty.isRequired());
     dateField.setRequiredError(getMessage("form.field.required", new Object[] { getPropertyLabel(formProperty) }));
     dateField.setEnabled(formProperty.isWritable());
     
     if (formProperty.getValue() != null)
     {
       SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
       try
       {
         Date date = dateFormat.parse(formProperty.getValue());
         dateField.setValue(date);
       }
       catch (ParseException localParseException) {}
     }
     
     return dateField;
   }
   
   public String getFieldValue(FormProperty formProperty, Field field)
   {
     PopupDateField dateField = (PopupDateField)field;
     Date selectedDate = (Date)dateField.getValue();
     
     if (selectedDate != null)
     {
       String datePattern = (String)formProperty.getType().getInformation("datePattern");
       SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
       return dateFormat.format(selectedDate);
     }
     
     return null;
   }
 }


