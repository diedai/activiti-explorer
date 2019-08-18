 package org.activiti.explorer.ui.process;
 
 import com.vaadin.data.Item;
 import com.vaadin.data.Property;
 import com.vaadin.data.util.PropertysetItem;
 import java.util.ArrayList;
 import java.util.List;
 import org.activiti.engine.RepositoryService;
 import org.activiti.engine.repository.ProcessDefinition;
 import org.activiti.engine.repository.ProcessDefinitionQuery;
 import org.activiti.explorer.data.AbstractLazyLoadingQuery;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class ProcessDefinitionListQuery
   extends AbstractLazyLoadingQuery
 {
   protected transient RepositoryService repositoryService;
   protected ProcessDefinitionFilter filter;
   
   public ProcessDefinitionListQuery(RepositoryService repositoryService, ProcessDefinitionFilter filter)
   {
     this.repositoryService = repositoryService;
     this.filter = filter;
   }
   
   public List<Item> loadItems(int start, int count) {
     List<ProcessDefinition> processDefinitions = this.filter.getQuery(this.repositoryService).listPage(start, count);
     
     List<Item> items = new ArrayList();
     for (ProcessDefinition processDefinition : processDefinitions) {
       items.add(this.filter.createItem(processDefinition));
     }
     return items;
   }
   
   public Item loadSingleResult(String id) {
     ProcessDefinition definition = (ProcessDefinition)this.repositoryService.createProcessDefinitionQuery().processDefinitionId(id).singleResult();
     if (definition != null) {
       return this.filter.createItem(definition);
     }
     return null;
   }
   
   public int size() {
     return (int)this.filter.getCountQuery(this.repositoryService).count();
   }
   
   public void setSorting(Object[] propertyId, boolean[] ascending) {
     throw new UnsupportedOperationException();
   }
   
   public static class ProcessDefinitionListItem
     extends PropertysetItem
     implements Comparable<ProcessDefinitionListItem>
   {
     private static final long serialVersionUID = 1L;
     
     public int compareTo(ProcessDefinitionListItem other)
     {
       String name = (String)getItemProperty("name").getValue();
       String otherName = (String)other.getItemProperty("name").getValue();
       int comparison = name.compareTo(otherName);
       
 
 
       if (comparison != 0) {
         return comparison;
       }
       String key = (String)getItemProperty("key").getValue();
       String otherKey = (String)other.getItemProperty("key").getValue();
       return key.compareTo(otherKey);
     }
   }
 }


