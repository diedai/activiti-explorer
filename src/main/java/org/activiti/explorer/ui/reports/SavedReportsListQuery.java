 package org.activiti.explorer.ui.reports;
 
 import com.vaadin.data.Item;
 import java.util.ArrayList;
 import java.util.List;
 import org.activiti.engine.HistoryService;
 import org.activiti.engine.ProcessEngine;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.engine.history.HistoricProcessInstance;
 import org.activiti.engine.history.HistoricProcessInstanceQuery;
 import org.activiti.engine.impl.identity.Authentication;
 import org.activiti.explorer.data.AbstractLazyLoadingQuery;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class SavedReportsListQuery
   extends AbstractLazyLoadingQuery
 {
   private static final long serialVersionUID = -7865037930384885968L;
   protected transient HistoryService historyService;
   
   public SavedReportsListQuery()
   {
     this.historyService = ProcessEngines.getDefaultProcessEngine().getHistoryService();
   }
   
   public int size() {
     return (int)createQuery().count();
   }
   
   public List<Item> loadItems(int start, int count) {
     List<HistoricProcessInstance> processInstances = createQuery().listPage(start, count);
     
     List<Item> reportItems = new ArrayList();
     for (HistoricProcessInstance instance : processInstances) {
       reportItems.add(new SavedReportListItem(instance));
     }
     
     return reportItems;
   }
   
 
 
 
 
   protected HistoricProcessInstanceQuery createQuery()
   {
     return this.historyService.createHistoricProcessInstanceQuery().finished().startedBy(Authentication.getAuthenticatedUserId()).variableValueNotEquals("reportData", null);
   }
   
   public Item loadSingleResult(String id) {
     return new SavedReportListItem((HistoricProcessInstance)this.historyService.createHistoricProcessInstanceQuery().processInstanceId(id).singleResult());
   }
   
   public void setSorting(Object[] propertyIds, boolean[] ascending) {
     throw new UnsupportedOperationException();
   }
 }


