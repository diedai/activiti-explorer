 package org.activiti.explorer.ui.task.listener;
 
 import com.vaadin.ui.Button.ClickEvent;
 import com.vaadin.ui.Button.ClickListener;
 import java.util.Arrays;
 import java.util.List;
 import org.activiti.engine.ProcessEngine;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.engine.TaskService;
 import org.activiti.engine.task.Task;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.ViewManager;
 import org.activiti.explorer.ui.custom.SelectUsersPopupWindow;
 import org.activiti.explorer.ui.event.SubmitEvent;
 import org.activiti.explorer.ui.event.SubmitEventListener;
 import org.activiti.explorer.ui.task.TaskDetailPanel;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class ChangeOwnershipListener
   implements ClickListener
 {
   private static final long serialVersionUID = 1L;
   protected Task task;
   protected TaskDetailPanel taskDetailPanel;
   protected I18nManager i18nManager;
   
   public ChangeOwnershipListener(Task task, TaskDetailPanel taskDetailPanel)
   {
     this.task = task;
     this.taskDetailPanel = taskDetailPanel;
     this.i18nManager = ExplorerApp.get().getI18nManager();
   }
   
   public void buttonClick(ClickEvent event)
   {
     List<String> ignoredIds = null;
     if (this.task.getOwner() != null) {
       ignoredIds = Arrays.asList(new String[] { this.task.getOwner() });
     }
     
 
     final SelectUsersPopupWindow involvePeoplePopupWindow = new SelectUsersPopupWindow(this.i18nManager.getMessage("task.owner.transfer"), false, ignoredIds);
     
     involvePeoplePopupWindow.addListener(new SubmitEventListener()
     {
       private static final long serialVersionUID = 1L;
       
       protected void submitted(SubmitEvent event) {
         String selectedUser = involvePeoplePopupWindow.getSelectedUserId();
         ChangeOwnershipListener.this.task.setOwner(selectedUser);
         ProcessEngines.getDefaultProcessEngine().getTaskService().setOwner(ChangeOwnershipListener.this.task.getId(), selectedUser);
         
 
         ChangeOwnershipListener.this.taskDetailPanel.notifyOwnerChanged();
       }
       
 
       protected void cancelled(SubmitEvent event) {}
     });
     ExplorerApp.get().getViewManager().showPopupWindow(involvePeoplePopupWindow);
   }
 }


