 package org.activiti.explorer.ui.task.listener;
 
 import com.vaadin.event.MouseEvents.ClickEvent;
 import com.vaadin.event.MouseEvents.ClickListener;
 import org.activiti.engine.ProcessEngine;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.engine.TaskService;
 import org.activiti.engine.history.HistoricTaskInstance;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.ViewManager;
 import org.activiti.explorer.ui.custom.ConfirmationDialogPopupWindow;
 import org.activiti.explorer.ui.event.ConfirmationEvent;
 import org.activiti.explorer.ui.event.ConfirmationEventListener;
 import org.activiti.explorer.ui.task.SubTaskComponent;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class DeleteSubTaskClickListener
   implements ClickListener
 {
   private static final long serialVersionUID = 1L;
   protected HistoricTaskInstance subTask;
   protected SubTaskComponent subTaskComponent;
   
   public DeleteSubTaskClickListener(HistoricTaskInstance subTask, SubTaskComponent subTaskComponent)
   {
     this.subTask = subTask;
     this.subTaskComponent = subTaskComponent;
   }
   
   public void click(ClickEvent event) {
     I18nManager i18nManager = ExplorerApp.get().getI18nManager();
     ViewManager viewManager = ExplorerApp.get().getViewManager();
     final TaskService taskService = ProcessEngines.getDefaultProcessEngine().getTaskService();
     
 
     ConfirmationDialogPopupWindow popup = new ConfirmationDialogPopupWindow(i18nManager.getMessage("task.confirm.delete.subtask", new Object[] {this.subTask.getName() }));
     popup.addListener(new ConfirmationEventListener() {
       private static final long serialVersionUID = 1L;
       
       protected void rejected(ConfirmationEvent event) {}
       
       protected void confirmed(ConfirmationEvent event) {
         taskService.deleteTask(DeleteSubTaskClickListener.this.subTask.getId());
         
 
         DeleteSubTaskClickListener.this.subTaskComponent.refreshSubTasks();
       }
       
     });
     viewManager.showPopupWindow(popup);
   }
 }


