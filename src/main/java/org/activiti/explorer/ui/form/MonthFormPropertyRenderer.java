 package org.activiti.explorer.ui.form;
 
 import com.vaadin.ui.ComboBox;
 import com.vaadin.ui.Field;
 import java.util.Calendar;
 import org.activiti.engine.form.FormProperty;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.form.MonthFormType;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class MonthFormPropertyRenderer
   extends AbstractFormPropertyRenderer
 {
   private static final long serialVersionUID = 1L;
   
   public MonthFormPropertyRenderer()
   {
     super(MonthFormType.class);
   }
   
   public Field getPropertyField(FormProperty formProperty) {
     ComboBox comboBox = new MonthCombobox(getPropertyLabel(formProperty));
     comboBox.setRequired(formProperty.isRequired());
     comboBox.setRequiredError(getMessage("form.field.required", new Object[] { getPropertyLabel(formProperty) }));
     comboBox.setEnabled(formProperty.isWritable());
     
 
     I18nManager i18nManager = ExplorerApp.get().getI18nManager();
     for (int i = 0; i < 12; i++) {
       comboBox.addItem(Integer.valueOf(i));
       comboBox.setItemCaption(Integer.valueOf(i), i18nManager.getMessage("month." + i));
     }
     
 
     comboBox.setNullSelectionAllowed(false);
     Calendar cal = Calendar.getInstance();
     comboBox.select(Integer.valueOf(cal.get(2)));
     
     return comboBox;
   }
   
   public class MonthCombobox
     extends ComboBox
   {
     private static final long serialVersionUID = 1L;
     
     public MonthCombobox(String s)
     {
       super();
       this.pageLength = 20;
     }
   }
 }


