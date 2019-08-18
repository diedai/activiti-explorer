 package org.activiti.explorer.ui.task;
 
 import com.vaadin.ui.ComboBox;
 import java.util.Arrays;
 import org.activiti.explorer.I18nManager;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class PriorityComboBox
   extends ComboBox
 {
   private static final long serialVersionUID = 1L;
   protected I18nManager i18nManager;
   
   public PriorityComboBox(I18nManager i18nManager)
   {
     super(null, Arrays.asList(new String[] {i18nManager
       .getMessage("task.priority.low"), i18nManager
       .getMessage("task.priority.medium"), i18nManager
       .getMessage("task.priority.high") }));
     this.i18nManager = i18nManager;
     setValue(i18nManager.getMessage("task.priority.low"));
     setNullSelectionAllowed(false);
     setInvalidAllowed(false);
     setImmediate(true);
     setWidth(125.0F, 0);
   }
   
   public PriorityComboBox(I18nManager i18nManager, Object value) {
     this(i18nManager);
     
     setValue(value);
   }
   
   public int getPriority() {
     String value = getValue().toString();
     if (this.i18nManager.getMessage("task.priority.low").equals(value))
       return 0;
     if (this.i18nManager.getMessage("task.priority.medium").equals(value)) {
       return 50;
     }
     return 100;
   }
 }


