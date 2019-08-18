 package org.activiti.explorer.ui.process;
 
 import org.activiti.engine.HistoryService;
 import org.activiti.engine.ProcessEngine;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.engine.RepositoryService;
 import org.activiti.explorer.data.LazyLoadingContainer;
 import org.activiti.explorer.data.LazyLoadingQuery;
 import org.activiti.explorer.navigation.UriFragment;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class MyProcessInstancesPage
   extends ProcessInstancePage
 {
   private static final long serialVersionUID = 1L;
   protected String processInstanceId;
   protected transient RepositoryService repositoryService;
   protected transient HistoryService historyService;
   
   public MyProcessInstancesPage()
   {
     this.historyService = ProcessEngines.getDefaultProcessEngine().getHistoryService();
     this.repositoryService = ProcessEngines.getDefaultProcessEngine().getRepositoryService();
   }
   
   public MyProcessInstancesPage(String processInstanceId) {
     this();
     this.processInstanceId = processInstanceId;
   }
   
   protected LazyLoadingQuery createLazyLoadingQuery()
   {
     return new MyProcessInstancesListQuery(this.historyService, this.repositoryService);
   }
   
   protected void initUi()
   {
     super.initUi();
     
     if (this.processInstanceId != null) {
       selectElement(this.processInstanceListContainer.getIndexForObjectId(this.processInstanceId));
     } else {
       selectElement(0);
     }
   }
   
   protected UriFragment getUriFragment(String processInstanceId)
   {
     UriFragment fragment = new UriFragment("myProcess");
     if (processInstanceId != null) {
       fragment.addUriPart(processInstanceId);
     }
     return fragment;
   }
 }


