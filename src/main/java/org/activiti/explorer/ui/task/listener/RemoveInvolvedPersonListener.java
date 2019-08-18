 package org.activiti.explorer.ui.task.listener;
 
 import com.vaadin.ui.Button.ClickEvent;
 import com.vaadin.ui.Button.ClickListener;
 import org.activiti.engine.IdentityService;
 import org.activiti.engine.ProcessEngine;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.engine.TaskService;
 import org.activiti.engine.identity.User;
 import org.activiti.engine.identity.UserQuery;
 import org.activiti.engine.task.IdentityLink;
 import org.activiti.engine.task.Task;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.ViewManager;
 import org.activiti.explorer.ui.custom.ConfirmationDialogPopupWindow;
 import org.activiti.explorer.ui.event.ConfirmationEvent;
 import org.activiti.explorer.ui.event.ConfirmationEventListener;
 import org.activiti.explorer.ui.task.TaskDetailPanel;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class RemoveInvolvedPersonListener
   implements ClickListener
 {
   private static final long serialVersionUID = 1L;
   protected IdentityLink identityLink;
   protected Task task;
   protected TaskDetailPanel taskDetailPanel;
   protected I18nManager i18nManager;
   protected ViewManager viewManager;
   protected transient IdentityService identityService;
   protected transient TaskService taskService;
   
   public RemoveInvolvedPersonListener(IdentityLink identityLink, Task task, TaskDetailPanel taskDetailPanel)
   {
     this.identityLink = identityLink;
     this.task = task;
     this.taskDetailPanel = taskDetailPanel;
     this.i18nManager = ExplorerApp.get().getI18nManager();
     this.viewManager = ExplorerApp.get().getViewManager();
     this.taskService = ProcessEngines.getDefaultProcessEngine().getTaskService();
     this.identityService = ProcessEngines.getDefaultProcessEngine().getIdentityService();
   }
   
   public void buttonClick(ClickEvent event) {
     User user = (User)this.identityService.createUserQuery().userId(this.identityLink.getUserId()).singleResult();
     String name = user.getFirstName() + " " + user.getLastName();
     
 
 
     ConfirmationDialogPopupWindow confirmationPopup = new ConfirmationDialogPopupWindow(this.i18nManager.getMessage("task.involved.remove.confirmation.title", new Object[] { name }), this.i18nManager.getMessage("task.involved.remove.confirmation.description", new Object[] { name, this.task.getName() }));
     
     confirmationPopup.addListener(new ConfirmationEventListener() {
       private static final long serialVersionUID = 1L;
       
       protected void rejected(ConfirmationEvent event) {}
       
       protected void confirmed(ConfirmationEvent event) { RemoveInvolvedPersonListener.this.taskService.deleteUserIdentityLink(RemoveInvolvedPersonListener.this.identityLink.getTaskId(), RemoveInvolvedPersonListener.this.identityLink.getUserId(), RemoveInvolvedPersonListener.this.identityLink.getType());
         RemoveInvolvedPersonListener.this.taskDetailPanel.notifyPeopleInvolvedChanged();
       }
     });
     this.viewManager.showPopupWindow(confirmationPopup);
   }
 }


