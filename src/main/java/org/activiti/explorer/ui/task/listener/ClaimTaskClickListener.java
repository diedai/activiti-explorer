 package org.activiti.explorer.ui.task.listener;
 
 import com.vaadin.ui.Button.ClickEvent;
 import com.vaadin.ui.Button.ClickListener;
 import org.activiti.engine.ActivitiException;
 import org.activiti.engine.TaskService;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.NotificationManager;
 import org.activiti.explorer.ViewManager;
 import org.activiti.explorer.identity.LoggedInUser;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class ClaimTaskClickListener
   implements ClickListener
 {
   private static final long serialVersionUID = 6322369324898642379L;
   protected String taskId;
   protected transient TaskService taskService;
   protected I18nManager i18nManager;
   protected ViewManager viewManager;
   protected NotificationManager notificationManager;
   
   public ClaimTaskClickListener(String taskId, TaskService taskService)
   {
     this.taskId = taskId;
     this.taskService = taskService;
     this.viewManager = ExplorerApp.get().getViewManager();
     this.i18nManager = ExplorerApp.get().getI18nManager();
     this.notificationManager = ExplorerApp.get().getNotificationManager();
   }
   
   public void buttonClick(ClickEvent event) {
     try {
       this.taskService.claim(this.taskId, ExplorerApp.get().getLoggedInUser().getId());
       this.notificationManager.showInformationNotification("task.claim.success");
       this.viewManager.showInboxPage(this.taskId);
     } catch (ActivitiException ae) {
       this.notificationManager.showErrorNotification("task.claim.failed", ae.getMessage());
     }
   }
 }


