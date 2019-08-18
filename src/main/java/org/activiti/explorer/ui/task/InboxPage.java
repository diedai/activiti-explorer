 package org.activiti.explorer.ui.task;
 
 import org.activiti.explorer.data.LazyLoadingQuery;
 import org.activiti.explorer.navigation.UriFragment;
 import org.activiti.explorer.ui.task.data.InboxListQuery;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class InboxPage
   extends TaskPage
 {
   private static final long serialVersionUID = 1L;
   
   public InboxPage() {}
   
   public InboxPage(String taskId)
   {
     super(taskId);
   }
   
   protected LazyLoadingQuery createLazyLoadingQuery()
   {
     return new InboxListQuery();
   }
   
   protected UriFragment getUriFragment(String taskId)
   {
     UriFragment taskFragment = new UriFragment("tasks");
     
     if (taskId != null) {
       taskFragment.addUriPart(taskId);
     }
     
     taskFragment.addParameter("category", "inbox");
     return taskFragment;
   }
 }


