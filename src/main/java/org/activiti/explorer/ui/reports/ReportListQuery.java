 package org.activiti.explorer.ui.reports;
 
 import com.vaadin.data.Item;
 import java.util.ArrayList;
 import java.util.List;
 import org.activiti.engine.ProcessEngine;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.engine.RepositoryService;
 import org.activiti.engine.repository.ProcessDefinition;
 import org.activiti.engine.repository.ProcessDefinitionQuery;
 import org.activiti.explorer.data.AbstractLazyLoadingQuery;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class ReportListQuery
   extends AbstractLazyLoadingQuery
 {
   private static final long serialVersionUID = -7865037930384885968L;
   private static final String REPORT_PROCESS_CATEGORY = "activiti-report";
   protected transient RepositoryService repositoryService;
   
   public ReportListQuery()
   {
     this.repositoryService = ProcessEngines.getDefaultProcessEngine().getRepositoryService();
   }
   
   public int size() {
     return (int)createQuery().count();
   }
   
   public List<Item> loadItems(int start, int count) {
     List<ProcessDefinition> processDefinitions = createQuery().listPage(start, count);
     
     List<Item> reportItems = new ArrayList();
     for (ProcessDefinition processDefinition : processDefinitions) {
       reportItems.add(new ReportListItem(processDefinition));
     }
     
     return reportItems;
   }
   
 
 
   protected ProcessDefinitionQuery createQuery()
   {
     return (ProcessDefinitionQuery)this.repositoryService.createProcessDefinitionQuery().processDefinitionCategory("activiti-report").latestVersion().orderByProcessDefinitionName().asc();
   }
   
   public Item loadSingleResult(String id)
   {
     return new ReportListItem((ProcessDefinition)this.repositoryService.createProcessDefinitionQuery().processDefinitionId(id).singleResult());
   }
   
   public void setSorting(Object[] propertyIds, boolean[] ascending) {
     throw new UnsupportedOperationException();
   }
 }


