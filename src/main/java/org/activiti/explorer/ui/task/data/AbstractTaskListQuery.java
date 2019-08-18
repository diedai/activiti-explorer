 package org.activiti.explorer.ui.task.data;
 
 import com.vaadin.data.Item;
 import java.util.ArrayList;
 import java.util.List;
 import org.activiti.engine.ProcessEngine;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.engine.TaskService;
 import org.activiti.engine.task.Task;
 import org.activiti.engine.task.TaskQuery;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.data.AbstractLazyLoadingQuery;
 import org.activiti.explorer.identity.LoggedInUser;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public abstract class AbstractTaskListQuery
   extends AbstractLazyLoadingQuery
 {
   protected String userId;
   protected transient TaskService taskService;
   
   public AbstractTaskListQuery()
   {
     this.userId = ExplorerApp.get().getLoggedInUser().getId();
     this.taskService = ProcessEngines.getDefaultProcessEngine().getTaskService();
   }
   
   public int size() {
     return (int)getQuery().count();
   }
   
   public List<Item> loadItems(int start, int count) {
     List<Task> tasks = getQuery().listPage(start, count);
     List<Item> items = new ArrayList();
     for (Task task : tasks) {
       items.add(new TaskListItem(task));
     }
     return items;
   }
   
   public Item loadSingleResult(String id) {
     Task task = (Task)((TaskQuery)getQuery().taskId(id)).singleResult();
     if (task != null) {
       return new TaskListItem(task);
     }
     return null;
   }
   
   public void setSorting(Object[] propertyId, boolean[] ascending) {
     throw new UnsupportedOperationException();
   }
   
   protected abstract TaskQuery getQuery();
 }


