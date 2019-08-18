 package org.activiti.explorer.ui.process;
 
 import com.vaadin.data.util.ObjectProperty;
 import java.io.Serializable;
 import org.activiti.engine.RepositoryService;
 import org.activiti.engine.repository.ProcessDefinition;
 import org.activiti.engine.repository.ProcessDefinitionQuery;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class DefaultProcessDefinitionFilter
   implements ProcessDefinitionFilter, Serializable
 {
   protected static final String PROPERTY_ID = "id";
   protected static final String PROPERTY_NAME = "name";
   protected static final String PROPERTY_KEY = "key";
   
   public ProcessDefinitionQuery getQuery(RepositoryService repositoryService)
   {
     return (ProcessDefinitionQuery)((ProcessDefinitionQuery)getBaseQuery(repositoryService).orderByProcessDefinitionName().asc()).orderByProcessDefinitionKey().asc();
   }
   
   public ProcessDefinitionQuery getCountQuery(RepositoryService repositoryService) {
     return getBaseQuery(repositoryService);
   }
   
 
 
   protected ProcessDefinitionQuery getBaseQuery(RepositoryService repositoryService)
   {
     return repositoryService.createProcessDefinitionQuery().latestVersion().active();
   }
   
   public ProcessDefinitionListQuery.ProcessDefinitionListItem createItem(ProcessDefinition processDefinition) {
     ProcessDefinitionListQuery.ProcessDefinitionListItem item = new ProcessDefinitionListQuery.ProcessDefinitionListItem();
     item.addItemProperty("id", new ObjectProperty(processDefinition.getId()));
     item.addItemProperty("name", new ObjectProperty(getProcessDisplayName(processDefinition)));
     item.addItemProperty("key", new ObjectProperty(processDefinition.getKey()));
     return item;
   }
   
   protected String getProcessDisplayName(ProcessDefinition processDefinition) {
     if (processDefinition.getName() != null) {
       return processDefinition.getName();
     }
     return processDefinition.getKey();
   }
 }


