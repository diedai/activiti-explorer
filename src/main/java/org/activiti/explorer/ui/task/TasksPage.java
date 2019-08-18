 package org.activiti.explorer.ui.task;
 
 import org.activiti.explorer.data.LazyLoadingQuery;
 import org.activiti.explorer.navigation.UriFragment;
 import org.activiti.explorer.ui.task.data.TasksListQuery;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class TasksPage
   extends TaskPage
 {
   private static final long serialVersionUID = 1L;
   
   public TasksPage() {}
   
   public TasksPage(String taskId)
   {
     super(taskId);
   }
   
   protected LazyLoadingQuery createLazyLoadingQuery()
   {
     return new TasksListQuery();
   }
   
   protected UriFragment getUriFragment(String taskId)
   {
     UriFragment taskFragment = new UriFragment("tasks");
     
     if (taskId != null) {
       taskFragment.addUriPart(taskId);
     }
     
     taskFragment.addParameter("category", "tasks");
     return taskFragment;
   }
 }


