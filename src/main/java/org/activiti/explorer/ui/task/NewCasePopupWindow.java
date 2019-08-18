 package org.activiti.explorer.ui.task;
 
 import com.vaadin.data.Validator.InvalidValueException;
 import com.vaadin.event.Action;
 import com.vaadin.event.Action.Handler;
 import com.vaadin.event.ShortcutAction;
 import com.vaadin.ui.Alignment;
 import com.vaadin.ui.Button;
 import com.vaadin.ui.Button.ClickEvent;
 import com.vaadin.ui.Button.ClickListener;
 import com.vaadin.ui.DateField;
 import com.vaadin.ui.Form;
 import com.vaadin.ui.HorizontalLayout;
 import com.vaadin.ui.Layout;
 import com.vaadin.ui.TextArea;
 import com.vaadin.ui.TextField;
 import java.util.Date;
 import org.activiti.engine.ProcessEngine;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.engine.TaskService;
 import org.activiti.engine.task.Task;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.ViewManager;
 import org.activiti.explorer.identity.LoggedInUser;
 import org.activiti.explorer.ui.custom.PopupWindow;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class NewCasePopupWindow
   extends PopupWindow
 {
   private static final long serialVersionUID = 1L;
   protected transient TaskService taskService;
   protected I18nManager i18nManager;
   protected Form form;
   protected TextField nameField;
   protected TextArea descriptionArea;
   protected DateField dueDateField;
   protected PriorityComboBox priorityComboBox;
   protected Button createTaskButton;
   
   public NewCasePopupWindow()
   {
     this.taskService = ProcessEngines.getDefaultProcessEngine().getTaskService();
     this.i18nManager = ExplorerApp.get().getI18nManager();
     
     setModal(true);
     center();
     setResizable(false);
     setCaption(this.i18nManager.getMessage("task.new"));
     addStyleName("light");
     setWidth(430.0F, 0);
     setHeight(320.0F, 0);
     
     initForm();
     initCreateTaskButton();
     initEnterKeyListener();
   }
   
   protected void initForm() {
     this.form = new Form();
     this.form.setValidationVisibleOnCommit(true);
     this.form.setImmediate(true);
     addComponent(this.form);
     
 
     this.nameField = new TextField(this.i18nManager.getMessage("task.name"));
     this.nameField.focus();
     this.nameField.setRequired(true);
     this.nameField.setRequiredError(this.i18nManager.getMessage("task.name.required"));
     this.form.addField("name", this.nameField);
     
 
     this.descriptionArea = new TextArea(this.i18nManager.getMessage("task.description"));
     this.descriptionArea.setColumns(25);
     this.form.addField("description", this.descriptionArea);
     
 
     this.dueDateField = new DateField(this.i18nManager.getMessage("task.duedate"));
     this.dueDateField.setResolution(4);
     this.form.addField("duedate", this.dueDateField);
     
 
     this.priorityComboBox = new PriorityComboBox(this.i18nManager);
     this.form.addField("priority", this.priorityComboBox);
   }
   
   protected void initCreateTaskButton() {
     HorizontalLayout buttonLayout = new HorizontalLayout();
     buttonLayout.setWidth(100.0F, 8);
     this.form.getFooter().setWidth(100.0F, 8);
     this.form.getFooter().addComponent(buttonLayout);
     
     Button createButton = new Button(this.i18nManager.getMessage("button.create"));
     buttonLayout.addComponent(createButton);
     buttonLayout.setComponentAlignment(createButton, Alignment.BOTTOM_RIGHT);
     
     createButton.addListener(new Button.ClickListener() {
       public void buttonClick(ClickEvent event) {
         NewCasePopupWindow.this.handleFormSubmit();
       }
     });
   }
   
   protected void initEnterKeyListener() {
     addActionHandler(new Action.Handler() {
       public void handleAction(Action action, Object sender, Object target) {
         NewCasePopupWindow.this.handleFormSubmit();
       }
       
       public Action[] getActions(Object target, Object sender) { return new Action[] { new ShortcutAction("enter", 13, null) }; }
     });
   }
   
   protected void handleFormSubmit()
   {
     try
     {
       this.form.commit();
       
 
       Task task = this.taskService.newTask();
       task.setName(this.nameField.getValue().toString());
       task.setDescription(this.descriptionArea.getValue().toString());
       task.setDueDate((Date)this.dueDateField.getValue());
       task.setPriority(this.priorityComboBox.getPriority());
       task.setOwner(ExplorerApp.get().getLoggedInUser().getId());
       this.taskService.saveTask(task);
       
 
       close();
       ExplorerApp.get().getViewManager().showTasksPage(task.getId());
     }
     catch (InvalidValueException e)
     {
       setHeight(350.0F, 0);
     }
   }
 }


