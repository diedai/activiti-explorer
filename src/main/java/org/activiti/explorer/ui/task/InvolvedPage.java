 package org.activiti.explorer.ui.task;
 
 import org.activiti.explorer.data.LazyLoadingQuery;
 import org.activiti.explorer.navigation.UriFragment;
 import org.activiti.explorer.ui.task.data.InvolvedListQuery;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class InvolvedPage
   extends TaskPage
 {
   private static final long serialVersionUID = 1L;
   
   public InvolvedPage() {}
   
   public InvolvedPage(String taskId)
   {
     super(taskId);
   }
   
   protected LazyLoadingQuery createLazyLoadingQuery()
   {
     return new InvolvedListQuery();
   }
   
   protected UriFragment getUriFragment(String taskId)
   {
     UriFragment taskFragment = new UriFragment("tasks");
     
     if (taskId != null) {
       taskFragment.addUriPart(taskId);
     }
     
     taskFragment.addParameter("category", "involved");
     return taskFragment;
   }
 }


