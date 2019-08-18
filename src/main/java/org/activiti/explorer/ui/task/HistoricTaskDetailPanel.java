 package org.activiti.explorer.ui.task;
 
 import com.vaadin.data.Item;
 import com.vaadin.data.Property;
 import com.vaadin.ui.Alignment;
 import com.vaadin.ui.Button;
 import com.vaadin.ui.Button.ClickEvent;
 import com.vaadin.ui.Button.ClickListener;
 import com.vaadin.ui.Component;
 import com.vaadin.ui.CssLayout;
 import com.vaadin.ui.Embedded;
 import com.vaadin.ui.GridLayout;
 import com.vaadin.ui.Label;
 import com.vaadin.ui.Table;
 import com.vaadin.ui.VerticalLayout;
 import java.util.ArrayList;
 import java.util.List;
 import org.activiti.engine.HistoryService;
 import org.activiti.engine.ProcessEngine;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.engine.TaskService;
 import org.activiti.engine.history.HistoricTaskInstance;
 import org.activiti.engine.history.HistoricTaskInstanceQuery;
 import org.activiti.engine.task.Attachment;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.NotificationManager;
 import org.activiti.explorer.ViewManager;
 import org.activiti.explorer.ui.Images;
 import org.activiti.explorer.ui.content.AttachmentDetailPopupWindow;
 import org.activiti.explorer.ui.content.AttachmentRenderer;
 import org.activiti.explorer.ui.content.AttachmentRendererManager;
 import org.activiti.explorer.ui.content.RelatedContentComponent;
 import org.activiti.explorer.ui.custom.DetailPanel;
 import org.activiti.explorer.ui.custom.PrettyTimeLabel;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class HistoricTaskDetailPanel
   extends DetailPanel
 {
   private static final long serialVersionUID = 1L;
   protected HistoricTaskInstance historicTask;
   protected transient HistoryService historyService;
   protected transient TaskService taskService;
   protected transient ViewManager viewManager;
   protected I18nManager i18nManager;
   protected NotificationManager notificationManager;
   protected AttachmentRendererManager attachmentRendererManager;
   protected TaskPage taskPage;
   protected VerticalLayout centralLayout;
   protected VerticalLayout peopleLayout;
   protected GridLayout peopleGrid;
   protected VerticalLayout subTasksLayout;
   protected GridLayout subTaskGrid;
   protected VerticalLayout relatedContentLayout;
   
   public HistoricTaskDetailPanel(HistoricTaskInstance historicTask, TaskPage taskPage)
   {
     this.historicTask = historicTask;
     this.taskPage = taskPage;
     
     this.historyService = ProcessEngines.getDefaultProcessEngine().getHistoryService();
     this.taskService = ProcessEngines.getDefaultProcessEngine().getTaskService();
     this.viewManager = ExplorerApp.get().getViewManager();
     this.i18nManager = ExplorerApp.get().getI18nManager();
     this.notificationManager = ExplorerApp.get().getNotificationManager();
     this.attachmentRendererManager = ExplorerApp.get().getAttachmentRendererManager();
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
     initDescription();
     initParentTaskLink();
     initPeopleDetails();
     initSubTasks();
     initRelatedContent();
   }
   
   protected void initHeader() {
     GridLayout taskDetails = new GridLayout(5, 2);
     taskDetails.setWidth(100.0F, 8);
     taskDetails.addStyleName("title-block");
     taskDetails.setSpacing(true);
     taskDetails.setMargin(false, false, true, false);
     
 
     Embedded image = new Embedded(null, Images.TASK_50);
     taskDetails.addComponent(image, 0, 0, 0, 1);
     
 
     Label nameLabel = new Label(this.historicTask.getName());
     nameLabel.addStyleName("h2");
     taskDetails.addComponent(nameLabel, 1, 0, 4, 0);
     
 
 
     PrettyTimeLabel dueDateLabel = new PrettyTimeLabel(this.i18nManager.getMessage("task.duedate.short"), this.historicTask.getDueDate(), this.i18nManager.getMessage("task.duedate.unknown"), false);
     dueDateLabel.addStyleName("task-duedate");
     taskDetails.addComponent(dueDateLabel, 1, 1);
     
 
     Integer lowMedHighPriority = convertPriority(this.historicTask.getPriority());
     Label priorityLabel = new Label();
     switch (lowMedHighPriority.intValue()) {
     case 1: 
       priorityLabel.setValue(this.i18nManager.getMessage("task.priority.low"));
       priorityLabel.addStyleName("task-priority-low");
       break;
     case 2: 
       priorityLabel.setValue(this.i18nManager.getMessage("task.priority.medium"));
       priorityLabel.addStyleName("task-priority-medium");
       break;
     case 3: 
     default: 
       priorityLabel.setValue(this.i18nManager.getMessage("task.priority.high"));
       priorityLabel.addStyleName("task-priority-high");
     }
     taskDetails.addComponent(priorityLabel, 2, 1);
     
 
 
     PrettyTimeLabel createLabel = new PrettyTimeLabel(this.i18nManager.getMessage("task.created.short"), this.historicTask.getStartTime(), "", true);
     createLabel.addStyleName("task-create-time");
     taskDetails.addComponent(createLabel, 3, 1);
     
 
     Label spacer = new Label();
     spacer.setContentMode(3);
     spacer.setValue("&nbsp;");
     spacer.setSizeUndefined();
     taskDetails.addComponent(spacer);
     
     taskDetails.setColumnExpandRatio(1, 1.0F);
     taskDetails.setColumnExpandRatio(2, 1.0F);
     taskDetails.setColumnExpandRatio(3, 1.0F);
     taskDetails.setColumnExpandRatio(4, 1.0F);
     this.centralLayout.addComponent(taskDetails);
   }
   
   protected void initDescription() {
     CssLayout descriptionLayout = new CssLayout();
     descriptionLayout.addStyleName("block-holder");
     descriptionLayout.setWidth(100.0F, 8);
     
     if (this.historicTask.getDescription() != null) {
       Label descriptionLabel = new Label(this.historicTask.getDescription());
       descriptionLayout.addComponent(descriptionLabel);
     }
     
     this.centralLayout.addComponent(descriptionLayout);
   }
   
   protected void initParentTaskLink() {
     if (this.historicTask.getParentTaskId() != null)
     {
 
       final HistoricTaskInstance parentTask = (HistoricTaskInstance)((HistoricTaskInstanceQuery)this.historyService.createHistoricTaskInstanceQuery().taskId(this.historicTask.getParentTaskId())).singleResult();
       
       Button showParentTaskButton = new Button(this.i18nManager.getMessage("task.subtask.of.parent.task", new Object[] {parentTask
         .getName() }));
       showParentTaskButton.addStyleName("link");
       showParentTaskButton.addListener(new Button.ClickListener() {
         public void buttonClick(ClickEvent event) {
           HistoricTaskDetailPanel.this.viewManager.showTaskPage(parentTask.getId());
         }
         
       });
       this.centralLayout.addComponent(showParentTaskButton);
     }
   }
   
   protected void initPeopleDetails() {
     this.peopleLayout = new VerticalLayout();
     this.peopleLayout.addStyleName("block-holder");
     addComponent(this.peopleLayout);
     
     initPeopleTitle();
     initPeopleGrid();
     initOwner();
     initAssignee();
   }
   
   protected void initPeopleTitle() {
     Label title = new Label(this.i18nManager.getMessage("task.people"));
     title.addStyleName("h3");
     title.setWidth(100.0F, 8);
     this.peopleLayout.addComponent(title);
   }
   
   protected void initPeopleGrid() {
     this.peopleGrid = new GridLayout();
     this.peopleGrid.setColumns(2);
     this.peopleGrid.setSpacing(true);
     this.peopleGrid.setMargin(true, false, false, false);
     this.peopleGrid.setWidth(100.0F, 8);
     this.peopleLayout.addComponent(this.peopleGrid);
   }
   
   protected void initOwner() {
     String ownerRole = this.historicTask.getOwner() != null ? "task.owner" : "task.no.owner";
     UserDetailsComponent owner = new UserDetailsComponent(this.historicTask.getOwner(), this.i18nManager.getMessage(ownerRole));
     this.peopleGrid.addComponent(owner);
   }
   
   protected void initAssignee() {
     String assigneeRole = this.historicTask.getAssignee() != null ? "task.assignee" : "task.no.assignee";
     UserDetailsComponent assignee = new UserDetailsComponent(this.historicTask.getAssignee(), this.i18nManager.getMessage(assigneeRole));
     this.peopleGrid.addComponent(assignee);
   }
   
   protected void initSubTasks()
   {
     this.subTasksLayout = new VerticalLayout();
     this.subTasksLayout.addStyleName("block-holder");
     addComponent(this.subTasksLayout);
     initSubTaskTitle();
     
 
 
     List<HistoricTaskInstance> subTasks = this.historyService.createHistoricTaskInstanceQuery().taskParentTaskId(this.historicTask.getId()).list();
     
     if (!subTasks.isEmpty()) {
       initSubTaskGrid();
       populateSubTasks(subTasks);
     } else {
       initNoSubTasksLabel();
     }
   }
   
   protected void initSubTaskTitle() {
     Label title = new Label(this.i18nManager.getMessage("task.subtasks"));
     title.addStyleName("h3");
     title.setWidth(100.0F, 8);
     this.subTasksLayout.addComponent(title);
   }
   
   protected void initSubTaskGrid() {
     this.subTaskGrid = new GridLayout();
     this.subTaskGrid.setColumns(2);
     this.subTasksLayout.addComponent(this.subTaskGrid);
   }
   
   protected void populateSubTasks(List<HistoricTaskInstance> subTasks) {
     for (final HistoricTaskInstance subTask : subTasks)
     {
       Embedded icon = new Embedded(null, Images.TASK_22);
       icon.setWidth(22.0F, 0);
       icon.setWidth(22.0F, 0);
       this.subTaskGrid.addComponent(icon);
       
 
       Button subTaskLink = new Button(subTask.getName());
       subTaskLink.addStyleName("link");
       subTaskLink.addListener(new Button.ClickListener() {
         public void buttonClick(ClickEvent event) {
           ExplorerApp.get().getViewManager().showTaskPage(subTask.getId());
         }
       });
       this.subTaskGrid.addComponent(subTaskLink);
       this.subTaskGrid.setComponentAlignment(subTaskLink, Alignment.MIDDLE_LEFT);
     }
   }
   
   protected void initNoSubTasksLabel() {
     Label noSubTasksLabel = new Label(this.i18nManager.getMessage("task.no.subtasks"));
     noSubTasksLabel.addStyleName("light");
     this.subTasksLayout.addComponent(noSubTasksLabel);
   }
   
   protected void initRelatedContent() {
     this.relatedContentLayout = new VerticalLayout();
     this.relatedContentLayout.addStyleName("block-holder");
     addComponent(this.relatedContentLayout);
     initRelatedContentTitle();
     
     List<Attachment> attachments = new ArrayList();
     attachments.addAll(this.taskService.getTaskAttachments(this.historicTask.getId()));
     if (this.historicTask.getProcessInstanceId() != null) {
       attachments.addAll(this.taskService.getProcessInstanceAttachments(this.historicTask.getProcessInstanceId()));
     }
     
     if (!attachments.isEmpty()) {
       Table table = initRelatedContentTable();
       populateRelatedContent(table, attachments);
     } else {
       initNoRelatedContentLabel();
     }
   }
   
   protected void initRelatedContentTitle() {
     Label title = new Label(ExplorerApp.get().getI18nManager().getMessage("task.related.content"));
     title.addStyleName("h3");
     title.setSizeFull();
     this.relatedContentLayout.addComponent(title);
   }
   
   protected Table initRelatedContentTable() {
     Table table = new Table();
     table.setWidth(100.0F, 8);
     table.addStyleName("related-content-list");
     
 
     table.setVisible(false);
     table.setColumnHeaderMode(-1);
     
     table.addContainerProperty("type", Embedded.class, null);
     table.setColumnWidth("type", 16);
     table.addContainerProperty("name", Component.class, null);
     
     this.relatedContentLayout.addComponent(table);
     return table;
   }
   
   protected void populateRelatedContent(Table table, List<Attachment> attachments)
   {
     if (!attachments.isEmpty()) {
       table.setVisible(true);
     }
     
     for (Attachment attachment : attachments) {
       AttachmentRenderer renderer = this.attachmentRendererManager.getRenderer(attachment);
       Item attachmentItem = table.addItem(attachment.getId());
       
 
       RelatedContentComponent relatedContentComponent = new RelatedContentComponent() {
         public void showAttachmentDetail(Attachment attachment) {
           AttachmentDetailPopupWindow popup = new AttachmentDetailPopupWindow(attachment);
           ExplorerApp.get().getViewManager().showPopupWindow(popup);
         }
         
       };
       attachmentItem.getItemProperty("name").setValue(renderer.getOverviewComponent(attachment, relatedContentComponent));
       attachmentItem.getItemProperty("type").setValue(new Embedded(null, renderer.getImage(attachment)));
     }
     table.setPageLength(table.size());
   }
   
   protected void initNoRelatedContentLabel() {
     Label noContentLabel = new Label(this.i18nManager.getMessage("task.no.related.content"));
     noContentLabel.addStyleName("light");
     this.relatedContentLayout.addComponent(noContentLabel);
   }
   
 
 
 
 
   protected Integer convertPriority(int priority)
   {
     return Integer.valueOf(1);
   }
 }


