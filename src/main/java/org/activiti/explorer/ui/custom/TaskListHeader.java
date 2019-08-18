 package org.activiti.explorer.ui.custom;
 
 import com.vaadin.event.Action;
 import com.vaadin.event.Action.Handler;
 import com.vaadin.event.ShortcutAction;
 import com.vaadin.ui.Alignment;
 import com.vaadin.ui.CssLayout;
 import com.vaadin.ui.HorizontalLayout;
 import com.vaadin.ui.MenuBar;
 import com.vaadin.ui.MenuBar.MenuItem;
 import com.vaadin.ui.Panel;
 import com.vaadin.ui.TextField;
 import org.activiti.engine.ProcessEngine;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.engine.TaskService;
 import org.activiti.engine.task.Task;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.ViewManager;
 import org.activiti.explorer.identity.LoggedInUser;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class TaskListHeader
   extends Panel
 {
   private static final long serialVersionUID = 1L;
   protected I18nManager i18nManager;
   protected transient TaskService taskService;
   protected HorizontalLayout layout;
   protected TextField inputField;
   
   public TaskListHeader()
   {
     this.i18nManager = ExplorerApp.get().getI18nManager();
     this.taskService = ProcessEngines.getDefaultProcessEngine().getTaskService();
     
     addStyleName("light");
     addStyleName("searchbox");
     
     this.layout = new HorizontalLayout();
     this.layout.setHeight(36.0F, 0);
     this.layout.setWidth(99.0F, 8);
     this.layout.setSpacing(true);
     this.layout.setMargin(false, true, false, true);
     setContent(this.layout);
     
     initInputField();
     initKeyboardListener();
   }
   
 
   protected void initInputField()
   {
     CssLayout csslayout = new CssLayout();
     csslayout.setHeight(24.0F, 0);
     csslayout.setWidth(100.0F, 8);
     this.layout.addComponent(csslayout);
     
     this.inputField = new TextField();
     this.inputField.setWidth(100.0F, 8);
     this.inputField.addStyleName("searchbox");
     this.inputField.setInputPrompt(this.i18nManager.getMessage("task.create.new"));
     this.inputField.focus();
     csslayout.addComponent(this.inputField);
     
     this.layout.setComponentAlignment(csslayout, Alignment.MIDDLE_LEFT);
     this.layout.setExpandRatio(csslayout, 1.0F);
   }
   
   protected void initKeyboardListener() {
     addActionHandler(new Action.Handler() {
       public void handleAction(Action action, Object sender, Object target) {
         if ((TaskListHeader.this.inputField.getValue() != null) && (!"".equals(TaskListHeader.this.inputField.getValue().toString())))
         {
 
           Task task = TaskListHeader.this.taskService.newTask();
           task.setName(TaskListHeader.this.inputField.getValue().toString());
           task.setOwner(ExplorerApp.get().getLoggedInUser().getId());
           TaskListHeader.this.taskService.saveTask(task);
           
 
           ExplorerApp.get().getViewManager().showTasksPage(task.getId());
         }
       }
       
       public Action[] getActions(Object target, Object sender) { return new Action[] { new ShortcutAction("enter", 13, null) }; }
     });
   }
   
   protected void initSortMenu()
   {
     MenuBar menuBar = new MenuBar();
     menuBar.addStyleName("searchbox-sortmenu");
     
 
     MenuBar.MenuItem rootItem = menuBar.addItem("Sort by", null);
     rootItem.addItem("Id", null);
     rootItem.addItem("Name", null);
     rootItem.addItem("Due date", null);
     rootItem.addItem("Creation date", null);
     
     this.layout.addComponent(menuBar);
     this.layout.setComponentAlignment(menuBar, Alignment.MIDDLE_RIGHT);
   }
   
   public void focus()
   {
     this.inputField.focus();
   }
 }


