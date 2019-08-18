 package org.activiti.explorer.ui.task;
 
 import com.vaadin.event.Action;
 import com.vaadin.event.Action.Handler;
 import com.vaadin.event.ShortcutAction;
 import com.vaadin.ui.Alignment;
 import com.vaadin.ui.Button;
 import com.vaadin.ui.Button.ClickEvent;
 import com.vaadin.ui.Button.ClickListener;
 import com.vaadin.ui.CustomComponent;
 import com.vaadin.ui.Embedded;
 import com.vaadin.ui.GridLayout;
 import com.vaadin.ui.HorizontalLayout;
 import com.vaadin.ui.Label;
 import com.vaadin.ui.Panel;
 import com.vaadin.ui.TextField;
 import com.vaadin.ui.VerticalLayout;
 import java.util.List;
 import org.activiti.engine.HistoryService;
 import org.activiti.engine.ProcessEngine;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.engine.TaskService;
 import org.activiti.engine.history.HistoricTaskInstance;
 import org.activiti.engine.history.HistoricTaskInstanceQuery;
 import org.activiti.engine.task.Task;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.ViewManager;
 import org.activiti.explorer.identity.LoggedInUser;
 import org.activiti.explorer.ui.Images;
 import org.activiti.explorer.ui.task.listener.DeleteSubTaskClickListener;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class SubTaskComponent
   extends CustomComponent
 {
   private static final long serialVersionUID = 1L;
   protected I18nManager i18nManager;
   protected transient TaskService taskService;
   protected transient HistoryService historyService;
   protected Task parentTask;
   protected TaskDetailPanel taskDetailPanel;
   protected VerticalLayout layout;
   protected Label title;
   protected Panel addSubTaskPanel;
   protected Button addSubTaskButton;
   protected TextField newTaskTextField;
   protected GridLayout subTaskLayout;
   
   public SubTaskComponent(Task parentTask)
   {
     this.parentTask = parentTask;
     this.i18nManager = ExplorerApp.get().getI18nManager();
     this.taskService = ProcessEngines.getDefaultProcessEngine().getTaskService();
     this.historyService = ProcessEngines.getDefaultProcessEngine().getHistoryService();
     
     initUi();
   }
   
   protected void initUi() {
     addStyleName("block-holder");
     addStyleName("involve-people");
     
     initLayout();
     initHeader();
     initSubTasks();
   }
   
   protected void initLayout() {
     this.layout = new VerticalLayout();
     setCompositionRoot(this.layout);
   }
   
   protected void initHeader() {
     HorizontalLayout headerLayout = new HorizontalLayout();
     headerLayout.setWidth(100.0F, 8);
     this.layout.addComponent(headerLayout);
     
     initTitle(headerLayout);
     initAddSubTaskPanel(headerLayout);
   }
   
   protected void initTitle(HorizontalLayout headerLayout) {
     this.title = new Label(this.i18nManager.getMessage("task.subtasks"));
     this.title.addStyleName("h3");
     this.title.setWidth(100.0F, 8);
     headerLayout.addComponent(this.title);
     headerLayout.setExpandRatio(this.title, 1.0F);
   }
   
   protected void initAddSubTaskPanel(HorizontalLayout headerLayout)
   {
     this.addSubTaskPanel = new Panel();
     this.addSubTaskPanel.setContent(new VerticalLayout());
     this.addSubTaskPanel.setSizeUndefined();
     this.addSubTaskPanel.addStyleName("light");
     this.addSubTaskPanel.addStyleName("no-border");
     headerLayout.addComponent(this.addSubTaskPanel);
     
     initAddSubTaskPanelKeyboardActions();
     initAddButton();
   }
   
   protected void initAddSubTaskPanelKeyboardActions() {
     this.addSubTaskPanel.addActionHandler(new Action.Handler() {
       public void handleAction(Action action, Object sender, Object target) {
         if ("escape".equals(action.getCaption())) {
           SubTaskComponent.this.resetAddButton();
         } else if (("enter".equals(action.getCaption())) && 
           (SubTaskComponent.this.newTaskTextField != null) && (SubTaskComponent.this.newTaskTextField.getValue() != null) && 
           (!"".equals(SubTaskComponent.this.newTaskTextField.getValue().toString())))
         {
           LoggedInUser loggedInUser = ExplorerApp.get().getLoggedInUser();
           
 
           Task newTask = SubTaskComponent.this.taskService.newTask();
           newTask.setParentTaskId(SubTaskComponent.this.parentTask.getId());
           if (SubTaskComponent.this.parentTask.getAssignee() != null) {
             newTask.setAssignee(SubTaskComponent.this.parentTask.getAssignee());
           } else {
             newTask.setAssignee(loggedInUser.getId());
           }
           if (SubTaskComponent.this.parentTask.getOwner() != null) {
             newTask.setOwner(SubTaskComponent.this.parentTask.getOwner());
           } else {
             newTask.setOwner(loggedInUser.getId());
           }
           newTask.setName(SubTaskComponent.this.newTaskTextField.getValue().toString());
           SubTaskComponent.this.taskService.saveTask(newTask);
           
 
           SubTaskComponent.this.resetAddButton();
           
 
           SubTaskComponent.this.refreshSubTasks();
         }
       }
       
       public Action[] getActions(Object target, Object sender) {
         return new Action[] { new ShortcutAction("enter", 13, null), new ShortcutAction("escape", 27, null) };
       }
     });
   }
   
 
 
   protected void initAddButton()
   {
     this.addSubTaskButton = new Button();
     this.addSubTaskButton.addStyleName("add");
     this.addSubTaskPanel.addComponent(this.addSubTaskButton);
     this.addSubTaskButton.addListener(new Button.ClickListener()
     {
       public void buttonClick(ClickEvent event) {
         SubTaskComponent.this.addSubTaskPanel.removeAllComponents();
         
 
         Label createSubTaskLabel = new Label("Create new subtask:");
         createSubTaskLabel.addStyleName("light");
         SubTaskComponent.this.addSubTaskPanel.addComponent(createSubTaskLabel);
         SubTaskComponent.this.newTaskTextField = new TextField();
         SubTaskComponent.this.newTaskTextField.focus();
         SubTaskComponent.this.addSubTaskPanel.addComponent(SubTaskComponent.this.newTaskTextField);
       }
     });
   }
   
   protected void resetAddButton() {
     this.addSubTaskPanel.removeAllComponents();
     initAddButton();
   }
   
 
   protected void initSubTasks()
   {
     List<HistoricTaskInstance> subTasks = this.historyService.createHistoricTaskInstanceQuery().taskParentTaskId(this.parentTask.getId()).list();
     initSubTasksLayout();
     populateSubTasks(subTasks);
   }
   
   protected void initSubTasksLayout() {
     this.subTaskLayout = new GridLayout();
     this.subTaskLayout.setColumns(3);
     this.subTaskLayout.addStyleName("subtasks");
     this.subTaskLayout.setWidth(99.0F, 8);
     this.subTaskLayout.setColumnExpandRatio(2, 1.0F);
     this.subTaskLayout.setSpacing(true);
     this.layout.addComponent(this.subTaskLayout);
   }
   
   protected void populateSubTasks(List<HistoricTaskInstance> subTasks) {
     if (!subTasks.isEmpty()) {
       for (final HistoricTaskInstance subTask : subTasks)
       {
         Embedded icon = null;
         
         if (subTask.getEndTime() != null) {
           icon = new Embedded(null, Images.TASK_FINISHED_22);
         } else {
           icon = new Embedded(null, Images.TASK_22);
         }
         icon.setWidth(22.0F, 0);
         icon.setWidth(22.0F, 0);
         this.subTaskLayout.addComponent(icon);
         
 
         Button subTaskLink = new Button(subTask.getName());
         subTaskLink.addStyleName("link");
         subTaskLink.addListener(new Button.ClickListener() {
           public void buttonClick(ClickEvent event) {
             ExplorerApp.get().getViewManager().showTaskPage(subTask.getId());
           }
         });
         this.subTaskLayout.addComponent(subTaskLink);
         this.subTaskLayout.setComponentAlignment(subTaskLink, Alignment.MIDDLE_LEFT);
         
         if (subTask.getEndTime() == null)
         {
           Embedded deleteIcon = new Embedded(null, Images.DELETE);
           deleteIcon.addStyleName("clickable");
           deleteIcon.addListener(new DeleteSubTaskClickListener(subTask, this));
           this.subTaskLayout.addComponent(deleteIcon);
           this.subTaskLayout.setComponentAlignment(deleteIcon, Alignment.MIDDLE_RIGHT);
         }
         else {
           this.subTaskLayout.newLine();
         }
       }
     } else {
       Label noSubTasksLabel = new Label(this.i18nManager.getMessage("task.no.subtasks"));
       noSubTasksLabel.setSizeUndefined();
       noSubTasksLabel.addStyleName("light");
       this.subTaskLayout.addComponent(noSubTasksLabel);
     }
   }
   
   public void refreshSubTasks()
   {
     this.subTaskLayout.removeAllComponents();
     
 
     List<HistoricTaskInstance> subTasks = this.historyService.createHistoricTaskInstanceQuery().taskParentTaskId(this.parentTask.getId()).list();
     populateSubTasks(subTasks);
   }
 }


