 package org.activiti.explorer.ui.process;
 
 import com.vaadin.data.Item;
 import com.vaadin.data.util.ObjectProperty;
 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.List;
 import java.util.Map;
 import org.activiti.engine.HistoryService;
 import org.activiti.engine.RepositoryService;
 import org.activiti.engine.history.HistoricProcessInstance;
 import org.activiti.engine.history.HistoricProcessInstanceQuery;
 import org.activiti.engine.repository.ProcessDefinition;
 import org.activiti.engine.repository.ProcessDefinitionQuery;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.data.AbstractLazyLoadingQuery;
 import org.activiti.explorer.identity.LoggedInUser;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class MyProcessInstancesListQuery
   extends AbstractLazyLoadingQuery
 {
   protected transient HistoryService historyService;
   protected transient RepositoryService repositoryService;
   protected Map<String, ProcessDefinition> cachedProcessDefinitions;
   
   public MyProcessInstancesListQuery(HistoryService historyService, RepositoryService repositoryService)
   {
     this.historyService = historyService;
     this.repositoryService = repositoryService;
     this.cachedProcessDefinitions = new HashMap();
   }
   
 
 
 
   public List<Item> loadItems(int start, int count)
   {
     List<HistoricProcessInstance> processInstances = this.historyService.createHistoricProcessInstanceQuery().startedBy(ExplorerApp.get().getLoggedInUser().getId()).unfinished().list();
     
     List<Item> items = new ArrayList();
     for (HistoricProcessInstance processInstance : processInstances) {
       items.add(createItem(processInstance));
     }
     return items;
   }
   
 
 
   public Item loadSingleResult(String id)
   {
     HistoricProcessInstance processInstance = (HistoricProcessInstance)this.historyService.createHistoricProcessInstanceQuery().startedBy(ExplorerApp.get().getLoggedInUser().getId()).unfinished().processInstanceId(id).singleResult();
     if (processInstance != null) {
       return createItem(processInstance);
     }
     return null;
   }
   
   protected ProcessInstanceItem createItem(HistoricProcessInstance processInstance) {
     ProcessInstanceItem item = new ProcessInstanceItem();
     item.addItemProperty("id", new ObjectProperty(processInstance.getId(), String.class));
     
     ProcessDefinition processDefinition = getProcessDefinition(processInstance.getProcessDefinitionId());
     
 
     String itemName = getProcessDisplayName(processDefinition) + " (" + processInstance.getId() + ")" + (processInstance.getBusinessKey() != null ? processInstance.getBusinessKey() : "");
     item.addItemProperty("name", new ObjectProperty(itemName, String.class));
     return item;
   }
   
   protected String getProcessDisplayName(ProcessDefinition processDefinition) {
     if (processDefinition.getName() != null) {
       return processDefinition.getName();
     }
     return processDefinition.getKey();
   }
   
   protected ProcessDefinition getProcessDefinition(String id)
   {
     ProcessDefinition processDefinition = (ProcessDefinition)this.cachedProcessDefinitions.get(id);
     if (processDefinition == null) {
       processDefinition = (ProcessDefinition)this.repositoryService.createProcessDefinitionQuery().processDefinitionId(id).singleResult();
       this.cachedProcessDefinitions.put(id, processDefinition);
     }
     return processDefinition;
   }
   
 
 
   public int size()
   {
     return (int)this.historyService.createHistoricProcessInstanceQuery().startedBy(ExplorerApp.get().getLoggedInUser().getId()).unfinished().count();
   }
   
   public void setSorting(Object[] propertyId, boolean[] ascending) {
     throw new UnsupportedOperationException();
   }
 }


