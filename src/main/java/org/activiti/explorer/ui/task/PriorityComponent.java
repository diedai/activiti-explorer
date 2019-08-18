 package org.activiti.explorer.ui.task;
 
 import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
 import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.LayoutEvents;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
 import com.vaadin.event.LayoutEvents.LayoutClickListener;
 import com.vaadin.ui.CssLayout;
 import org.activiti.engine.TaskService;
 import org.activiti.engine.task.Task;
 import org.activiti.explorer.I18nManager;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class PriorityComponent
   extends CssLayout
 {
   private static final long serialVersionUID = 1L;
   protected Task task;
   protected I18nManager i18nManager;
   protected transient TaskService taskService;
   protected PriorityLabel priorityLabel;
   protected PriorityComboBox priorityComboBox;
   
   public PriorityComponent(Task task, I18nManager i18nManager, TaskService taskService)
   {
     this.task = task;
     this.i18nManager = i18nManager;
     this.taskService = taskService;
     
     setSizeUndefined();
     initPriorityLabel();
     initPriorityComboBox();
     initLayoutClickListener();
     initComboBoxListener();
   }
   
   protected void initPriorityLabel() {
     this.priorityLabel = new PriorityLabel(this.task, this.i18nManager);
     addComponent(this.priorityLabel);
   }
   
   protected void initPriorityComboBox() {
     this.priorityComboBox = new PriorityComboBox(this.i18nManager, this.priorityLabel.getValue());
   }
   
   protected void initLayoutClickListener() {
     addListener(new LayoutEvents.LayoutClickListener() {
       public void layoutClick(LayoutEvents.LayoutClickEvent event) {
         if ((event.getClickedComponent() != null) && (event.getClickedComponent().equals(PriorityComponent.this.priorityLabel)))
         {
           PriorityComponent.this.replaceComponent(PriorityComponent.this.priorityLabel, PriorityComponent.this.priorityComboBox);
         }
       }
     });
   }
   
   protected void initComboBoxListener() {
     this.priorityComboBox.addListener(new Property.ValueChangeListener()
     {
       public void valueChange(Property.ValueChangeEvent event) {
         PriorityComponent.this.task.setPriority(PriorityComponent.this.priorityComboBox.getPriority());
         PriorityComponent.this.taskService.saveTask(PriorityComponent.this.task);
         
 
         PriorityComponent.this.priorityLabel.setValue(Integer.valueOf(PriorityComponent.this.task.getPriority()));
         PriorityComponent.this.replaceComponent(PriorityComponent.this.priorityComboBox, PriorityComponent.this.priorityLabel);
       }
     });
   }
 }


