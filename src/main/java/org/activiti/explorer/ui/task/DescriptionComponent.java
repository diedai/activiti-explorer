 package org.activiti.explorer.ui.task;
 
 import com.vaadin.event.LayoutEvents;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
 import com.vaadin.event.LayoutEvents.LayoutClickListener;
 import com.vaadin.ui.Alignment;
 import com.vaadin.ui.Button;
 import com.vaadin.ui.Button.ClickEvent;
 import com.vaadin.ui.Button.ClickListener;
 import com.vaadin.ui.CssLayout;
 import com.vaadin.ui.Label;
 import com.vaadin.ui.TextArea;
 import com.vaadin.ui.VerticalLayout;
 import org.activiti.engine.TaskService;
 import org.activiti.engine.task.Task;
 import org.activiti.explorer.I18nManager;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class DescriptionComponent
   extends CssLayout
 {
   private static final long serialVersionUID = 1L;
   protected Task task;
   protected I18nManager i18nManager;
   protected transient TaskService taskService;
   protected Label descriptionLabel;
   protected VerticalLayout editLayout;
   
   public DescriptionComponent(Task task, I18nManager i18nManager, TaskService taskService)
   {
     this.task = task;
     this.i18nManager = i18nManager;
     this.taskService = taskService;
     
     setWidth(100.0F, 8);
     initDescriptionLabel();
     initEditLayout();
     initLayoutClickListener();
   }
   
   protected void initDescriptionLabel() {
     String descriptionText = null;
     if ((this.task.getDescription() != null) && (!"".equals(this.task.getDescription()))) {
       descriptionText = this.task.getDescription();
     } else {
       descriptionText = this.i18nManager.getMessage("task.no.description");
     }
     this.descriptionLabel = new Label(descriptionText);
     this.descriptionLabel.addStyleName("clickable");
     addComponent(this.descriptionLabel);
   }
   
   protected void initEditLayout() {
     this.editLayout = new VerticalLayout();
     this.editLayout.setSpacing(true);
   }
   
   protected void initLayoutClickListener() {
     addListener(new LayoutEvents.LayoutClickListener() {
       public void layoutClick(LayoutEvents.LayoutClickEvent event) {
         if ((event.getClickedComponent() != null) && (event.getClickedComponent().equals(DescriptionComponent.this.descriptionLabel)))
         {
           final TextArea descriptionTextArea = new TextArea();
           descriptionTextArea.setWidth(100.0F, 8);
           descriptionTextArea.setValue(DescriptionComponent.this.task.getDescription());
           DescriptionComponent.this.editLayout.addComponent(descriptionTextArea);
           
 
           Button okButton = new Button(DescriptionComponent.this.i18nManager.getMessage("button.ok"));
           DescriptionComponent.this.editLayout.addComponent(okButton);
           DescriptionComponent.this.editLayout.setComponentAlignment(okButton, Alignment.BOTTOM_RIGHT);
           
 
           DescriptionComponent.this.replaceComponent(DescriptionComponent.this.descriptionLabel, DescriptionComponent.this.editLayout);
           
 
           okButton.addListener(new Button.ClickListener()
           {
             public void buttonClick(ClickEvent event) {
               DescriptionComponent.this.task.setDescription(descriptionTextArea.getValue().toString());
               DescriptionComponent.this.taskService.saveTask(DescriptionComponent.this.task);
               
 
               DescriptionComponent.this.descriptionLabel.setValue(DescriptionComponent.this.task.getDescription());
               DescriptionComponent.this.replaceComponent(DescriptionComponent.this.editLayout, DescriptionComponent.this.descriptionLabel);
             }
           });
         }
       }
     });
   }
 }


