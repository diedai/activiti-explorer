 package org.activiti.explorer.ui.management.processinstance;
 
 import com.vaadin.data.Item;
 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.List;
 import java.util.Map;
 import org.activiti.engine.ProcessEngine;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.engine.RepositoryService;
 import org.activiti.engine.RuntimeService;
 import org.activiti.engine.repository.ProcessDefinition;
 import org.activiti.engine.repository.ProcessDefinitionQuery;
 import org.activiti.engine.runtime.ProcessInstance;
 import org.activiti.engine.runtime.ProcessInstanceQuery;
 import org.activiti.explorer.data.AbstractLazyLoadingQuery;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class ProcessInstanceListQuery
   extends AbstractLazyLoadingQuery
 {
   protected transient RuntimeService runtimeService;
   protected transient RepositoryService repositoryService;
   protected Map<String, String> cachedProcessDefinitionNames = new HashMap();
   
   public ProcessInstanceListQuery() {
     this.runtimeService = ProcessEngines.getDefaultProcessEngine().getRuntimeService();
     this.repositoryService = ProcessEngines.getDefaultProcessEngine().getRepositoryService();
   }
   
   public int size() {
     return (int)constructQuery().count();
   }
   
   public List<Item> loadItems(int start, int count) {
     List<ProcessInstance> processInstances = constructQuery().listPage(start, count);
     List<Item> items = new ArrayList();
     for (ProcessInstance processInstance : processInstances) {
       items.add(new ProcessInstanceListItem(processInstance, getProcessDefinitionName(processInstance.getProcessDefinitionId())));
     }
     return items;
   }
   
   public Item loadSingleResult(String id) {
     ProcessInstance processInstance = (ProcessInstance)constructQuery().processInstanceId(id).singleResult();
     return new ProcessInstanceListItem(processInstance, processInstance.getProcessDefinitionId());
   }
   
   public void setSorting(Object[] propertyIds, boolean[] ascending) {
     throw new UnsupportedOperationException();
   }
   
   protected ProcessInstanceQuery constructQuery()
   {
     return (ProcessInstanceQuery)this.runtimeService.createProcessInstanceQuery().orderByProcessInstanceId().asc();
   }
   
   protected String getProcessDefinitionName(String processDefinitionId) {
     if (!this.cachedProcessDefinitionNames.containsKey(processDefinitionId))
     {
       ProcessDefinition definition = (ProcessDefinition)this.repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
       
       String name = definition.getName();
       if (name != null) {
         name = definition.getKey();
       }
       this.cachedProcessDefinitionNames.put(processDefinitionId, name);
     }
     return (String)this.cachedProcessDefinitionNames.get(processDefinitionId);
   }
 }


