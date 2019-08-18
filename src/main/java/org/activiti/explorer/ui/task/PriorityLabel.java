 package org.activiti.explorer.ui.task;
 
 import com.vaadin.ui.Label;
 import org.activiti.engine.task.Task;
 import org.activiti.explorer.I18nManager;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class PriorityLabel
   extends Label
 {
   private static final long serialVersionUID = 1L;
   protected int priority;
   protected I18nManager i18nManager;
   
   public PriorityLabel(Task task, I18nManager i18nManager)
   {
     this.i18nManager = i18nManager;
     this.priority = task.getPriority();
     
     addStyleName("clickable");
     setSizeUndefined();
     setValue(Integer.valueOf(this.priority));
   }
   
   public int getPriority() {
     return this.priority;
   }
   
   public void setValue(Object newValue)
   {
     if ((newValue instanceof Integer)) {
       this.priority = ((Integer)newValue).intValue();
       if (this.priority < 50) {
         super.setValue(this.i18nManager.getMessage("task.priority.low"));
         addStyleName("task-priority-low");
       } else if (this.priority == 50) {
         super.setValue(this.i18nManager.getMessage("task.priority.medium"));
         addStyleName("task-priority-medium");
       } else if (this.priority > 50) {
         super.setValue(this.i18nManager.getMessage("task.priority.high"));
         addStyleName("task-priority-high");
       }
     } else {
       throw new IllegalArgumentException("Can only set integer as new value for PriorityLabel");
     }
   }
 }


