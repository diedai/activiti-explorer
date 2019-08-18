 package org.activiti.explorer.ui.process.listener;
 
 import com.vaadin.ui.Button.ClickEvent;
 import com.vaadin.ui.Button.ClickListener;
 import java.util.List;
 import org.activiti.engine.FormService;
 import org.activiti.engine.ProcessEngine;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.engine.RuntimeService;
 import org.activiti.engine.TaskService;
 import org.activiti.engine.form.StartFormData;
 import org.activiti.engine.repository.ProcessDefinition;
 import org.activiti.engine.runtime.ProcessInstance;
 import org.activiti.engine.task.Task;
 import org.activiti.engine.task.TaskQuery;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.NotificationManager;
 import org.activiti.explorer.ViewManager;
 import org.activiti.explorer.identity.LoggedInUser;
 import org.activiti.explorer.ui.process.ProcessDefinitionPage;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class StartProcessInstanceClickListener
   implements ClickListener
 {
   private static final long serialVersionUID = 1L;
   protected transient RuntimeService runtimeService;
   protected transient TaskService taskService;
   protected transient FormService formService;
   protected NotificationManager notificationManager;
   protected ProcessDefinition processDefinition;
   protected ProcessDefinitionPage parentPage;
   
   public StartProcessInstanceClickListener(ProcessDefinition processDefinition, ProcessDefinitionPage processDefinitionPage)
   {
     this.runtimeService = ProcessEngines.getDefaultProcessEngine().getRuntimeService();
     this.taskService = ProcessEngines.getDefaultProcessEngine().getTaskService();
     this.formService = ProcessEngines.getDefaultProcessEngine().getFormService();
     this.notificationManager = ExplorerApp.get().getNotificationManager();
     
     this.processDefinition = processDefinition;
     this.parentPage = processDefinitionPage;
   }
   
 
   public void buttonClick(ClickEvent event)
   {
     StartFormData startFormData = this.formService.getStartFormData(this.processDefinition.getId());
     if ((startFormData != null) && (((startFormData.getFormProperties() != null) && (!startFormData.getFormProperties().isEmpty())) || (startFormData.getFormKey() != null))) {
       this.parentPage.showStartForm(this.processDefinition, startFormData);
     }
     else
     {
       ProcessInstance processInstance = this.runtimeService.startProcessInstanceById(this.processDefinition.getId());
       
 
       this.notificationManager.showInformationNotification("process.started.notification", new Object[] { getProcessDisplayName(this.processDefinition) });
       
 
 
 
 
       List<Task> loggedInUsersTasks = ((TaskQuery)((TaskQuery)this.taskService.createTaskQuery().taskAssignee(ExplorerApp.get().getLoggedInUser().getId())).processInstanceId(processInstance.getId())).list();
       if (!loggedInUsersTasks.isEmpty()) {
         ExplorerApp.get().getViewManager().showInboxPage(((Task)loggedInUsersTasks.get(0)).getId());
       }
     }
   }
   
   protected String getProcessDisplayName(ProcessDefinition processDefinition) {
     if (processDefinition.getName() != null) {
       return processDefinition.getName();
     }
     return processDefinition.getKey();
   }
 }


