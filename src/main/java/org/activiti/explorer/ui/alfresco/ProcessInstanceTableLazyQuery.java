 package org.activiti.explorer.ui.alfresco;
 
 import com.vaadin.data.Item;
 import java.util.ArrayList;
 import java.util.List;
 import org.activiti.engine.ProcessEngine;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.engine.RuntimeService;
 import org.activiti.engine.runtime.ProcessInstance;
 import org.activiti.engine.runtime.ProcessInstanceQuery;
 import org.activiti.explorer.data.AbstractLazyLoadingQuery;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class ProcessInstanceTableLazyQuery
   extends AbstractLazyLoadingQuery
 {
   protected transient RuntimeService runtimeService;
   protected String processDefinitionId;
   
   public ProcessInstanceTableLazyQuery()
   {
     this.runtimeService = ProcessEngines.getDefaultProcessEngine().getRuntimeService();
   }
   
   public ProcessInstanceTableLazyQuery(String processDefinitionId) {
     this();
     this.processDefinitionId = processDefinitionId;
   }
   
   public int size() {
     return (int)constructQuery().count();
   }
   
   public Item loadSingleResult(String id) {
     return new AlfrescoProcessInstanceTableItem((ProcessInstance)constructQuery().processInstanceId(id).singleResult());
   }
   
   public List<Item> loadItems(int start, int count) {
     List<ProcessInstance> processInstances = constructQuery().listPage(start, count);
     List<Item> items = new ArrayList(processInstances.size());
     for (ProcessInstance processInstance : processInstances) {
       items.add(new AlfrescoProcessInstanceTableItem(processInstance));
     }
     return items;
   }
   
   public void setSorting(Object[] propertyIds, boolean[] ascending) {
     throw new UnsupportedOperationException();
   }
   
   protected ProcessInstanceQuery constructQuery()
   {
     ProcessInstanceQuery query = (ProcessInstanceQuery)this.runtimeService.createProcessInstanceQuery().orderByProcessInstanceId().asc();
     if (this.processDefinitionId != null) {
       query.processDefinitionId(this.processDefinitionId);
     }
     return query;
   }
   
   public void setProcessDefintionId(String processDefinitionId) {
     this.processDefinitionId = processDefinitionId;
   }
 }


