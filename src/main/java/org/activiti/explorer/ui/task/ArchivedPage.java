 package org.activiti.explorer.ui.task;
 
 import com.vaadin.ui.Component;
 import org.activiti.engine.HistoryService;
 import org.activiti.engine.ProcessEngine;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.engine.history.HistoricTaskInstance;
 import org.activiti.engine.history.HistoricTaskInstanceQuery;
 import org.activiti.explorer.data.LazyLoadingQuery;
 import org.activiti.explorer.navigation.UriFragment;
 import org.activiti.explorer.ui.task.data.ArchivedListQuery;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class ArchivedPage
   extends TaskPage
 {
   private static final long serialVersionUID = 1L;
   
   public ArchivedPage() {}
   
   public ArchivedPage(String taskId)
   {
     super(taskId);
   }
   
   protected LazyLoadingQuery createLazyLoadingQuery()
   {
     return new ArchivedListQuery();
   }
   
   protected Component createDetailComponent(String id)
   {
     HistoryService historyService = ProcessEngines.getDefaultProcessEngine().getHistoryService();
     HistoricTaskInstance historicTaskInstance = (HistoricTaskInstance)((HistoricTaskInstanceQuery)historyService.createHistoricTaskInstanceQuery().taskId(id)).singleResult();
     this.taskEventPanel.setTaskId(historicTaskInstance.getId());
     return new HistoricTaskDetailPanel(historicTaskInstance, this);
   }
   
   protected UriFragment getUriFragment(String taskId)
   {
     UriFragment taskFragment = new UriFragment("tasks");
     
     if (taskId != null) {
       taskFragment.addUriPart(taskId);
     }
     
     taskFragment.addParameter("category", "archived");
     return taskFragment;
   }
 }


