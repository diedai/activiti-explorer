 package org.activiti.explorer.ui.management.processdefinition;
 
 import com.vaadin.data.Item;
 import java.util.ArrayList;
 import java.util.List;
 import org.activiti.engine.ProcessEngine;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.engine.RepositoryService;
 import org.activiti.engine.repository.ProcessDefinition;
 import org.activiti.engine.repository.ProcessDefinitionQuery;
 import org.activiti.explorer.data.AbstractLazyLoadingQuery;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class ActiveProcessDefinitionListQuery
   extends AbstractLazyLoadingQuery
 {
   protected transient RepositoryService repositoryService;
   
   public ActiveProcessDefinitionListQuery()
   {
     this.repositoryService = ProcessEngines.getDefaultProcessEngine().getRepositoryService();
   }
   
   public int size() {
     return (int)this.repositoryService.createProcessDefinitionQuery().active().count();
   }
   
 
 
 
   public List<Item> loadItems(int start, int count)
   {
     List<ProcessDefinition> processDefinitions = ((ProcessDefinitionQuery)((ProcessDefinitionQuery)this.repositoryService.createProcessDefinitionQuery().active().orderByProcessDefinitionName().asc()).orderByProcessDefinitionVersion().asc()).listPage(start, count);
     
     List<Item> processDefinitionItems = new ArrayList();
     for (ProcessDefinition processDefinition : processDefinitions) {
       processDefinitionItems.add(new ProcessDefinitionListItem(processDefinition));
     }
     
     return processDefinitionItems;
   }
   
   public Item loadSingleResult(String id)
   {
     return new ProcessDefinitionListItem((ProcessDefinition)this.repositoryService.createProcessDefinitionQuery().processDefinitionId(id).singleResult());
   }
   
   public void setSorting(Object[] propertyIds, boolean[] ascending) {
     throw new UnsupportedOperationException();
   }
 }


