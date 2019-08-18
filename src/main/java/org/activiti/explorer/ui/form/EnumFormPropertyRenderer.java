 package org.activiti.explorer.ui.form;
 
 import com.vaadin.ui.ComboBox;
 import com.vaadin.ui.Field;
 import java.util.Map;
 import java.util.Map.Entry;
 import org.activiti.engine.form.FormProperty;
 import org.activiti.engine.form.FormType;
 import org.activiti.engine.impl.form.EnumFormType;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class EnumFormPropertyRenderer
   extends AbstractFormPropertyRenderer
 {
   public EnumFormPropertyRenderer()
   {
     super(EnumFormType.class);
   }
   
 
   public Field getPropertyField(FormProperty formProperty)
   {
     ComboBox comboBox = new ComboBox(getPropertyLabel(formProperty));
     comboBox.setRequired(formProperty.isRequired());
     comboBox.setRequiredError(getMessage("form.field.required", new Object[] { getPropertyLabel(formProperty) }));
     comboBox.setEnabled(formProperty.isWritable());
     comboBox.setNullSelectionAllowed(false);
     
     Object firstItemId = null;
     Object itemToSelect = null;
     Map<String, String> values = (Map)formProperty.getType().getInformation("values");
     if (values != null) {
       for (Map.Entry<String, String> enumEntry : values.entrySet())
       {
         comboBox.addItem(enumEntry.getKey());
         
         if (firstItemId == null) {
           firstItemId = enumEntry.getKey();
         }
         
         String selectedValue = formProperty.getValue();
         if ((selectedValue != null) && (selectedValue.equals(enumEntry.getKey()))) {
           itemToSelect = enumEntry.getKey();
         }
         
         if (enumEntry.getValue() != null) {
           comboBox.setItemCaption(enumEntry.getKey(), (String)enumEntry.getValue());
         }
       }
     }
     
 
     if (itemToSelect != null) {
       comboBox.select(itemToSelect);
     }
     else if (firstItemId != null) {
       comboBox.select(firstItemId);
     }
     
     return comboBox;
   }
 }


