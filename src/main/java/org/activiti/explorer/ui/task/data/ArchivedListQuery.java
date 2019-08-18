 package org.activiti.explorer.ui.task.data;
 
 import com.vaadin.data.Item;
 import java.util.ArrayList;
 import java.util.List;
 import org.activiti.engine.HistoryService;
 import org.activiti.engine.ProcessEngine;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.engine.history.HistoricTaskInstance;
 import org.activiti.engine.history.HistoricTaskInstanceQuery;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.data.AbstractLazyLoadingQuery;
 import org.activiti.explorer.identity.LoggedInUser;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class ArchivedListQuery
   extends AbstractLazyLoadingQuery
 {
   protected String userId;
   protected transient HistoryService historyService;
   
   public ArchivedListQuery()
   {
     this.userId = ExplorerApp.get().getLoggedInUser().getId();
     this.historyService = ProcessEngines.getDefaultProcessEngine().getHistoryService();
   }
   
   public int size() {
     return (int)createQuery().count();
   }
   
   public List<Item> loadItems(int start, int count) {
     List<HistoricTaskInstance> historicTaskInstances = createQuery().listPage(start, count);
     List<Item> items = new ArrayList();
     for (HistoricTaskInstance historicTaskInstance : historicTaskInstances) {
       items.add(new TaskListItem(historicTaskInstance));
     }
     return items;
   }
   
   public Item loadSingleResult(String id) {
     return new TaskListItem((HistoricTaskInstance)((HistoricTaskInstanceQuery)createQuery().taskId(id)).singleResult());
   }
   
   public void setSorting(Object[] propertyIds, boolean[] ascending) {
     throw new UnsupportedOperationException();
   }
   
   protected HistoricTaskInstanceQuery createQuery() {
     return ((HistoricTaskInstanceQuery)this.historyService.createHistoricTaskInstanceQuery().taskOwner(this.userId)).finished();
   }
 }


