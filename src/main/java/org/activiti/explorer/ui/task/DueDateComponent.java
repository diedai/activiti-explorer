 package org.activiti.explorer.ui.task;
 
 import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
 import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.LayoutEvents;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
 import com.vaadin.event.LayoutEvents.LayoutClickListener;
 import com.vaadin.ui.CssLayout;
 import com.vaadin.ui.DateField;
 import com.vaadin.ui.Label;
 import java.util.Date;
 import org.activiti.engine.TaskService;
 import org.activiti.engine.task.Task;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.ui.custom.PrettyTimeLabel;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class DueDateComponent
   extends CssLayout
 {
   private static final long serialVersionUID = 1L;
   protected Task task;
   protected I18nManager i18nManager;
   protected transient TaskService taskService;
   protected Label dueDateLabel;
   protected DateField dueDateField;
   
   public DueDateComponent(Task task, I18nManager i18nManager, TaskService taskService)
   {
     this.task = task;
     this.i18nManager = i18nManager;
     this.taskService = taskService;
     
     setSizeUndefined();
     initDueDateLabel();
     initDueDateField();
     initLayoutClickListener();
     initDueDateFieldListener();
   }
   
   protected void initDueDateLabel()
   {
     this.dueDateLabel = new PrettyTimeLabel(this.i18nManager.getMessage("task.duedate.short"), this.task.getDueDate(), this.i18nManager.getMessage("task.duedate.unknown"), false);
     this.dueDateLabel.addStyleName("task-duedate");
     this.dueDateLabel.setSizeUndefined();
     this.dueDateLabel.addStyleName("clickable");
     addComponent(this.dueDateLabel);
   }
   
   protected void initDueDateField() {
     this.dueDateField = new DateField();
     if (this.task.getDueDate() != null) {
       this.dueDateField.setValue(this.task.getDueDate());
     } else {
       this.dueDateField.setValue(new Date());
     }
     this.dueDateField.setWidth(125.0F, 0);
     this.dueDateField.setResolution(4);
     this.dueDateField.setImmediate(true);
   }
   
   protected void initLayoutClickListener() {
     addListener(new LayoutEvents.LayoutClickListener() {
       public void layoutClick(LayoutEvents.LayoutClickEvent event) {
         if ((event.getClickedComponent() != null) && (event.getClickedComponent().equals(DueDateComponent.this.dueDateLabel)))
         {
           DueDateComponent.this.replaceComponent(DueDateComponent.this.dueDateLabel, DueDateComponent.this.dueDateField);
         }
       }
     });
   }
   
   protected void initDueDateFieldListener() {
     this.dueDateField.addListener(new Property.ValueChangeListener() {
       public void valueChange(Property.ValueChangeEvent event) {
         if (DueDateComponent.this.dueDateField.getValue() != null)
         {
           DueDateComponent.this.task.setDueDate((Date)DueDateComponent.this.dueDateField.getValue());
           DueDateComponent.this.taskService.saveTask(DueDateComponent.this.task);
           
 
           DueDateComponent.this.dueDateLabel.setValue(DueDateComponent.this.task.getDueDate());
           DueDateComponent.this.replaceComponent(DueDateComponent.this.dueDateField, DueDateComponent.this.dueDateLabel);
         }
       }
     });
   }
 }


