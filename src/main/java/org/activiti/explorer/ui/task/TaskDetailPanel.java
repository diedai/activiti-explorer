 package org.activiti.explorer.ui.task;
 
 import com.vaadin.event.LayoutEvents;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
 import com.vaadin.event.LayoutEvents.LayoutClickListener;
 import com.vaadin.ui.Alignment;
 import com.vaadin.ui.Button;
 import com.vaadin.ui.Button.ClickEvent;
 import com.vaadin.ui.Button.ClickListener;
 import com.vaadin.ui.ComponentContainer;
 import com.vaadin.ui.CssLayout;
 import com.vaadin.ui.Embedded;
 import com.vaadin.ui.GridLayout;
 import com.vaadin.ui.HorizontalLayout;
 import com.vaadin.ui.Label;
 import com.vaadin.ui.TextArea;
 import com.vaadin.ui.VerticalLayout;
 import java.util.List;
 import java.util.Map;
 import org.activiti.engine.FormService;
 import org.activiti.engine.ProcessEngine;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.engine.RepositoryService;
 import org.activiti.engine.TaskService;
 import org.activiti.engine.form.TaskFormData;
 import org.activiti.engine.repository.ProcessDefinition;
 import org.activiti.engine.repository.ProcessDefinitionQuery;
 import org.activiti.engine.task.Task;
 import org.activiti.engine.task.TaskQuery;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.NotificationManager;
 import org.activiti.explorer.ViewManager;
 import org.activiti.explorer.identity.LoggedInUser;
 import org.activiti.explorer.ui.Images;
 import org.activiti.explorer.ui.custom.DetailPanel;
 import org.activiti.explorer.ui.custom.PrettyTimeLabel;
 import org.activiti.explorer.ui.form.FormPropertiesEventListener;
 import org.activiti.explorer.ui.form.FormPropertiesForm;
 import org.activiti.explorer.ui.form.FormPropertiesForm.FormPropertiesEvent;
 import org.activiti.explorer.ui.task.listener.ClaimTaskClickListener;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class TaskDetailPanel
   extends DetailPanel
 {
   private static final long serialVersionUID = 1L;
   protected Task task;
   protected transient TaskService taskService;
   protected transient FormService formService;
   protected transient RepositoryService repositoryService;
   protected ViewManager viewManager;
   protected I18nManager i18nManager;
   protected NotificationManager notificationManager;
   protected TaskPage taskPage;
   protected VerticalLayout centralLayout;
   protected FormPropertiesForm taskForm;
   protected TaskInvolvedPeopleComponent involvedPeople;
   protected SubTaskComponent subTaskComponent;
   protected TaskRelatedContentComponent relatedContent;
   protected Button completeButton;
   protected Button claimButton;
   
   public TaskDetailPanel(Task task, TaskPage taskPage)
   {
     this.task = task;
     this.taskPage = taskPage;
     
     this.taskService = ProcessEngines.getDefaultProcessEngine().getTaskService();
     this.formService = ProcessEngines.getDefaultProcessEngine().getFormService();
     this.repositoryService = ProcessEngines.getDefaultProcessEngine().getRepositoryService();
     this.viewManager = ExplorerApp.get().getViewManager();
     this.i18nManager = ExplorerApp.get().getI18nManager();
     this.notificationManager = ExplorerApp.get().getNotificationManager();
   }
   
   public void attach()
   {
     super.attach();
     init();
   }
   
   protected void init() {
     setSizeFull();
     addStyleName("white");
     
 
     this.centralLayout = new VerticalLayout();
     this.centralLayout.setMargin(true);
     setDetailContainer(this.centralLayout);
     
     initHeader();
     initDescriptionAndClaimButton();
     initProcessLink();
     initParentTaskLink();
     initPeopleDetails();
     initSubTasks();
     initRelatedContent();
     initTaskForm();
   }
   
   protected void initHeader()
   {
     GridLayout taskDetails = new GridLayout(2, 2);
     taskDetails.setWidth(100.0F, 8);
     taskDetails.addStyleName("title-block");
     taskDetails.setSpacing(true);
     taskDetails.setMargin(false, false, true, false);
     taskDetails.setColumnExpandRatio(1, 1.0F);
     this.centralLayout.addComponent(taskDetails);
     
 
     Embedded image = new Embedded(null, Images.TASK_50);
     taskDetails.addComponent(image, 0, 0, 0, 1);
     
 
     Label nameLabel = new Label(this.task.getName());
     nameLabel.addStyleName("h2");
     taskDetails.addComponent(nameLabel, 1, 0);
     taskDetails.setComponentAlignment(nameLabel, Alignment.MIDDLE_LEFT);
     
 
     HorizontalLayout propertiesLayout = new HorizontalLayout();
     propertiesLayout.setSpacing(true);
     taskDetails.addComponent(propertiesLayout);
     
     propertiesLayout.addComponent(new DueDateComponent(this.task, this.i18nManager, this.taskService));
     propertiesLayout.addComponent(new PriorityComponent(this.task, this.i18nManager, this.taskService));
     
     initCreateTime(propertiesLayout);
   }
   
   protected void initCreateTime(HorizontalLayout propertiesLayout)
   {
     PrettyTimeLabel createLabel = new PrettyTimeLabel(this.i18nManager.getMessage("task.created.short"), this.task.getCreateTime(), "", true);
     createLabel.addStyleName("task-create-time");
     propertiesLayout.addComponent(createLabel);
   }
   
   protected void initDescriptionAndClaimButton() {
     HorizontalLayout layout = new HorizontalLayout();
     layout.addStyleName("block-holder");
     layout.setWidth(100.0F, 8);
     layout.setSpacing(true);
     this.centralLayout.addComponent(layout);
     
     initClaimButton(layout);
     initDescription(layout);
   }
   
   protected void initClaimButton(HorizontalLayout layout) {
     if ((!isCurrentUserAssignee()) && (canUserClaimTask())) {
       this.claimButton = new Button(this.i18nManager.getMessage("task.claim"));
       this.claimButton.addListener(new ClaimTaskClickListener(this.task.getId(), this.taskService));
       layout.addComponent(this.claimButton);
       layout.setComponentAlignment(this.claimButton, Alignment.MIDDLE_LEFT);
     }
   }
   
   protected void initDescription(HorizontalLayout layout) {
     final CssLayout descriptionLayout = new CssLayout();
     descriptionLayout.setWidth(100.0F, 8);
     layout.addComponent(descriptionLayout);
     layout.setExpandRatio(descriptionLayout, 1.0F);
     layout.setComponentAlignment(descriptionLayout, Alignment.MIDDLE_LEFT);
     
     String descriptionText = null;
     if ((this.task.getDescription() != null) && (!"".equals(this.task.getDescription()))) {
       descriptionText = this.task.getDescription();
     } else {
       descriptionText = this.i18nManager.getMessage("task.no.description");
     }
     final Label descriptionLabel = new Label(descriptionText);
     descriptionLabel.addStyleName("clickable");
     descriptionLayout.addComponent(descriptionLabel);
     
     descriptionLayout.addListener(new LayoutEvents.LayoutClickListener() {
       public void layoutClick(LayoutEvents.LayoutClickEvent event) {
         if ((event.getClickedComponent() != null) && (event.getClickedComponent().equals(descriptionLabel)))
         {
           final VerticalLayout editLayout = new VerticalLayout();
           editLayout.setSpacing(true);
           
 
           final TextArea descriptionTextArea = new TextArea();
           descriptionTextArea.setNullRepresentation("");
           descriptionTextArea.setWidth(100.0F, 8);
           descriptionTextArea.setValue(TaskDetailPanel.this.task.getDescription());
           editLayout.addComponent(descriptionTextArea);
           
 
           Button okButton = new Button(TaskDetailPanel.this.i18nManager.getMessage("button.ok"));
           editLayout.addComponent(okButton);
           editLayout.setComponentAlignment(okButton, Alignment.BOTTOM_RIGHT);
           
 
           descriptionLayout.replaceComponent(descriptionLabel, editLayout);
           
 
           okButton.addListener(new Button.ClickListener()
           {
             public void buttonClick(ClickEvent event) {
               TaskDetailPanel.this.task.setDescription(descriptionTextArea.getValue().toString());
               TaskDetailPanel.this.taskService.saveTask(TaskDetailPanel.this.task);
               
 
               descriptionLabel.setValue(TaskDetailPanel.this.task.getDescription());
               descriptionLayout.replaceComponent(editLayout, descriptionLabel);
             }
           });
         }
       }
     });
   }
   
   protected void initProcessLink() {
     if (this.task.getProcessInstanceId() != null)
     {
 
       ProcessDefinition processDefinition = (ProcessDefinition)this.repositoryService.createProcessDefinitionQuery().processDefinitionId(this.task.getProcessDefinitionId()).singleResult();
       
       Button showProcessInstanceButton = new Button(this.i18nManager.getMessage("task.part.of.process", new Object[] {
         getProcessDisplayName(processDefinition) }));
       showProcessInstanceButton.addStyleName("link");
       showProcessInstanceButton.addListener(new Button.ClickListener() {
         public void buttonClick(ClickEvent event) {
           TaskDetailPanel.this.viewManager.showMyProcessInstancesPage(TaskDetailPanel.this.task.getProcessInstanceId());
         }
         
       });
       this.centralLayout.addComponent(showProcessInstanceButton);
       addEmptySpace(this.centralLayout);
     }
   }
   
   protected String getProcessDisplayName(ProcessDefinition processDefinition) {
     if (processDefinition.getName() != null) {
       return processDefinition.getName();
     }
     return processDefinition.getKey();
   }
   
   protected void initParentTaskLink()
   {
     if (this.task.getParentTaskId() != null)
     {
       final Task parentTask = (Task)((TaskQuery)this.taskService.createTaskQuery().taskId(this.task.getParentTaskId())).singleResult();
       
       Button showParentTaskButton = new Button(this.i18nManager.getMessage("task.subtask.of.parent.task", new Object[] {parentTask
         .getName() }));
       showParentTaskButton.addStyleName("link");
       showParentTaskButton.addListener(new Button.ClickListener() {
         public void buttonClick(ClickEvent event) {
           TaskDetailPanel.this.viewManager.showTaskPage(parentTask.getId());
         }
         
       });
       this.centralLayout.addComponent(showParentTaskButton);
       addEmptySpace(this.centralLayout);
     }
   }
   
   protected void initPeopleDetails() {
     this.involvedPeople = new TaskInvolvedPeopleComponent(this.task, this);
     this.centralLayout.addComponent(this.involvedPeople);
   }
   
   protected void initSubTasks()
   {
     this.subTaskComponent = new SubTaskComponent(this.task);
     this.centralLayout.addComponent(this.subTaskComponent);
   }
   
   protected void initRelatedContent() {
     this.relatedContent = new TaskRelatedContentComponent(this.task, this);
     this.centralLayout.addComponent(this.relatedContent);
   }
   
   protected void initTaskForm()
   {
     TaskFormData formData = this.formService.getTaskFormData(this.task.getId());
     if ((formData != null) && (formData.getFormProperties() != null) && (!formData.getFormProperties().isEmpty())) {
       this.taskForm = new FormPropertiesForm();
       this.taskForm.setSubmitButtonCaption(this.i18nManager.getMessage("task.complete"));
       this.taskForm.setCancelButtonCaption(this.i18nManager.getMessage("task.form.reset"));
       this.taskForm.setFormHelp(this.i18nManager.getMessage("task.form.help"));
       this.taskForm.setFormProperties(formData.getFormProperties());
       
       this.taskForm.addListener(new FormPropertiesEventListener()
       {
         private static final long serialVersionUID = -3893467157397686736L;
         
         protected void handleFormSubmit(FormPropertiesForm.FormPropertiesEvent event)
         {
           Map<String, String> properties = event.getFormProperties();
           TaskDetailPanel.this.formService.submitTaskFormData(TaskDetailPanel.this.task.getId(), properties);
           TaskDetailPanel.this.notificationManager.showInformationNotification("task.task.completed", new Object[] { TaskDetailPanel.this.task.getName() });
           TaskDetailPanel.this.taskPage.refreshSelectNext();
         }
         
 
         protected void handleFormCancel(FormPropertiesForm.FormPropertiesEvent event)
         {
           TaskDetailPanel.this.taskForm.clear();
         }
         
       });
       this.taskForm.setEnabled(isCurrentUserAssignee());
       
 
       this.centralLayout.addComponent(this.taskForm);
 
     }
     else
     {
       CssLayout buttonLayout = new CssLayout();
       buttonLayout.addStyleName("block-holder");
       buttonLayout.setWidth(100.0F, 8);
       this.centralLayout.addComponent(buttonLayout);
       
       this.completeButton = new Button(this.i18nManager.getMessage("task.complete"));
       
       this.completeButton.addListener(new Button.ClickListener()
       {
         private static final long serialVersionUID = 1L;
         
         public void buttonClick(ClickEvent event)
         {
           if (TaskDetailPanel.this.task.getOwner() == null) {
             TaskDetailPanel.this.task.setOwner(TaskDetailPanel.this.task.getAssignee());
             TaskDetailPanel.this.taskService.setOwner(TaskDetailPanel.this.task.getId(), TaskDetailPanel.this.task.getAssignee());
           }
           
           TaskDetailPanel.this.taskService.complete(TaskDetailPanel.this.task.getId());
           TaskDetailPanel.this.notificationManager.showInformationNotification("task.task.completed", new Object[] { TaskDetailPanel.this.task.getName() });
           TaskDetailPanel.this.taskPage.refreshSelectNext();
         }
         
       });
       this.completeButton.setEnabled((isCurrentUserAssignee()) || (isCurrentUserOwner()));
       buttonLayout.addComponent(this.completeButton);
     }
   }
   
   protected boolean isCurrentUserAssignee() {
     String currentUser = ExplorerApp.get().getLoggedInUser().getId();
     return currentUser.equals(this.task.getAssignee());
   }
   
   protected boolean isCurrentUserOwner() {
     String currentUser = ExplorerApp.get().getLoggedInUser().getId();
     return currentUser.equals(this.task.getOwner());
   }
   
 
 
   protected boolean canUserClaimTask()
   {
     return ((TaskQuery)((TaskQuery)this.taskService.createTaskQuery().taskCandidateUser(ExplorerApp.get().getLoggedInUser().getId())).taskId(this.task.getId())).count() == 1L;
   }
   
   protected void addEmptySpace(ComponentContainer container) {
     Label emptySpace = new Label("&nbsp;", 3);
     emptySpace.setSizeUndefined();
     container.addComponent(emptySpace);
   }
   
   public void notifyPeopleInvolvedChanged() {
     this.involvedPeople.refreshPeopleGrid();
     this.taskPage.getTaskEventPanel().refreshTaskEvents();
   }
   
   public void notifyAssigneeChanged() {
     if (ExplorerApp.get().getLoggedInUser().getId().equals(this.task.getAssignee())) {
       this.viewManager.showInboxPage(this.task.getId());
     } else {
       this.involvedPeople.refreshAssignee();
       this.taskPage.getTaskEventPanel().refreshTaskEvents();
     }
   }
   
   public void notifyOwnerChanged() {
     if (ExplorerApp.get().getLoggedInUser().getId().equals(this.task.getOwner())) {
       this.viewManager.showTasksPage(this.task.getId());
     } else {
       this.involvedPeople.refreshOwner();
       this.taskPage.getTaskEventPanel().refreshTaskEvents();
     }
   }
   
   public void notifyRelatedContentChanged() {
     this.relatedContent.refreshTaskAttachments();
     this.taskPage.getTaskEventPanel().refreshTaskEvents();
   }
 }


