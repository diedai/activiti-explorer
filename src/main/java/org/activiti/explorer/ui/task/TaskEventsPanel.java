 package org.activiti.explorer.ui.task;
 
 import com.vaadin.event.Action;
 import com.vaadin.event.Action.Handler;
 import com.vaadin.event.ShortcutAction;
 import com.vaadin.terminal.StreamResource;
 import com.vaadin.terminal.StreamResource.StreamSource;
 import com.vaadin.ui.Alignment;
 import com.vaadin.ui.Button;
 import com.vaadin.ui.Button.ClickEvent;
 import com.vaadin.ui.Button.ClickListener;
 import com.vaadin.ui.Embedded;
 import com.vaadin.ui.GridLayout;
 import com.vaadin.ui.HorizontalLayout;
 import com.vaadin.ui.Label;
 import com.vaadin.ui.Panel;
 import com.vaadin.ui.TextField;
 import com.vaadin.ui.VerticalLayout;
 import java.io.InputStream;
 import java.util.List;
 import java.util.Map;
 import org.activiti.engine.IdentityService;
 import org.activiti.engine.ProcessEngine;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.engine.TaskService;
 import org.activiti.engine.identity.Picture;
 import org.activiti.engine.task.Event;
 import org.activiti.explorer.Constants;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.ViewManager;
 import org.activiti.explorer.ui.Images;
 import org.activiti.explorer.util.time.HumanTime;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class TaskEventsPanel
   extends Panel
 {
   private static final long serialVersionUID = 1L;
   protected transient IdentityService identityService;
   protected transient TaskService taskService;
   protected I18nManager i18nManager;
   protected ViewManager viewManager;
   protected TaskEventTextResolver taskEventTextResolver;
   protected String taskId;
   protected List<org.activiti.engine.task.Event> taskEvents;
   protected TextField commentInputField;
   protected Button addCommentButton;
   protected GridLayout eventGrid;
   
   public TaskEventsPanel()
   {
     this.taskService = ProcessEngines.getDefaultProcessEngine().getTaskService();
     this.identityService = ProcessEngines.getDefaultProcessEngine().getIdentityService();
     this.i18nManager = ExplorerApp.get().getI18nManager();
     this.viewManager = ExplorerApp.get().getViewManager();
     this.taskEventTextResolver = new TaskEventTextResolver();
     
     ((VerticalLayout)getContent()).setSpacing(true);
     ((VerticalLayout)getContent()).setMargin(true);
     setHeight(100.0F, 8);
     
     addStyleName("task-event-panel");
     
     addTitle();
     addInputField();
     initEventGrid();
     addTaskEvents();
   }
   
   public void refreshTaskEvents()
   {
     this.eventGrid.removeAllComponents();
     addTaskEvents();
   }
   
 
 
 
   public void setTaskId(String taskId)
   {
     this.taskId = taskId;
     refreshTaskEvents();
   }
   
   protected void addTitle() {
     Label eventTitle = new Label(this.i18nManager.getMessage("event.title"));
     eventTitle.addStyleName("h2");
     addComponent(eventTitle);
   }
   
   protected void initEventGrid() {
     this.eventGrid = new GridLayout();
     this.eventGrid.setColumns(2);
     this.eventGrid.setSpacing(true);
     this.eventGrid.setMargin(true, false, false, false);
     this.eventGrid.setWidth("100%");
     this.eventGrid.setColumnExpandRatio(1, 1.0F);
     this.eventGrid.addStyleName("event-grid");
     
     addComponent(this.eventGrid);
   }
   
   protected void addTaskEvents() {
     if (this.taskId != null) {
       this.taskEvents = this.taskService.getTaskEvents(this.taskId);
       for (org.activiti.engine.task.Event event : this.taskEvents) {
         addTaskEventPicture((Event) event, this.eventGrid);
         addTaskEventText((Event) event, this.eventGrid);
       }
     }
     this.addCommentButton.setEnabled(this.taskId != null);
     this.commentInputField.setEnabled(this.taskId != null);
   }
   
   protected void addTaskEventPicture(Event taskEvent, GridLayout eventGrid) {
     final Picture userPicture = this.identityService.getUserPicture(((org.activiti.engine.task.Event) taskEvent).getUserId());
     Embedded authorPicture = null;
     
     if (userPicture != null)
     {
 
 
 
 
       StreamResource imageresource = new StreamResource(new StreamResource.StreamSource()
       {
         private static final long serialVersionUID = 1L;
         
         public InputStream getStream()
         {
           return userPicture.getInputStream();
         }
       }, "event_" + ((org.activiti.engine.task.Event) taskEvent).getUserId() + "." + (String)Constants.MIMETYPE_EXTENSION_MAPPING.get(userPicture.getMimeType()), ExplorerApp.get());
       authorPicture = new Embedded(null, imageresource);
     } else {
       authorPicture = new Embedded(null, Images.USER_50);
     }
     
     authorPicture.setType(1);
     authorPicture.setHeight("48px");
     authorPicture.setWidth("48px");
     authorPicture.addStyleName("task-event-picture");
     eventGrid.addComponent(authorPicture);
   }
   
   protected void addTaskEventText(Event taskEvent, GridLayout eventGrid) {
     VerticalLayout layout = new VerticalLayout();
     layout.addStyleName("task-event");
     layout.setWidth("100%");
     eventGrid.addComponent(layout);
     
 
     Label text = this.taskEventTextResolver.resolveText((org.activiti.engine.task.Event) taskEvent);
     text.setWidth("100%");
     layout.addComponent(text);
     
 
     Label time = new Label(new HumanTime(this.i18nManager).format(((org.activiti.engine.task.Event) taskEvent).getTime()));
     time.setSizeUndefined();
     time.addStyleName("task-event-time");
     layout.addComponent(time);
   }
   
   protected void addInputField()
   {
     HorizontalLayout layout = new HorizontalLayout();
     layout.setSpacing(true);
     layout.setWidth(100.0F, 8);
     addComponent(layout);
     
     Panel textFieldPanel = new Panel();
     textFieldPanel.addStyleName("light");
     textFieldPanel.setContent(new VerticalLayout());
     textFieldPanel.setWidth(100.0F, 8);
     layout.addComponent(textFieldPanel);
     layout.setExpandRatio(textFieldPanel, 1.0F);
     
     this.commentInputField = new TextField();
     this.commentInputField.setWidth(100.0F, 8);
     textFieldPanel.addComponent(this.commentInputField);
     
 
     textFieldPanel.addActionHandler(new Action.Handler() {
       public void handleAction(Action action, Object sender, Object target) {
         TaskEventsPanel.this.addNewComment(TaskEventsPanel.this.commentInputField.getValue().toString());
       }
       
       public Action[] getActions(Object target, Object sender) { return new Action[] { new ShortcutAction("enter", 13, null) };
       }
 
     });
     this.addCommentButton = new Button(this.i18nManager.getMessage("task.comment.add"));
     layout.addComponent(this.addCommentButton);
     layout.setComponentAlignment(this.addCommentButton, Alignment.MIDDLE_LEFT);
     this.addCommentButton.addListener(new Button.ClickListener() {
       public void buttonClick(ClickEvent event) {
         TaskEventsPanel.this.addNewComment(TaskEventsPanel.this.commentInputField.getValue().toString());
       }
     });
   }
   
   protected void addNewComment(String text) {
     this.taskService.addComment(this.taskId, null, text);
     refreshTaskEvents();
     this.commentInputField.setValue("");
     this.commentInputField.focus();
   }
 }


