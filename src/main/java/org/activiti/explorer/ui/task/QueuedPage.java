 package org.activiti.explorer.ui.task;
 
 import org.activiti.explorer.data.LazyLoadingQuery;
 import org.activiti.explorer.navigation.UriFragment;
 import org.activiti.explorer.ui.task.data.QueuedListQuery;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class QueuedPage
   extends TaskPage
 {
   private static final long serialVersionUID = 1L;
   protected String groupId;
   
   public QueuedPage(String groupId)
   {
     this.groupId = groupId;
   }
   
   public QueuedPage(String groupId, String taskId) {
     super(taskId);
     this.groupId = groupId;
   }
   
   protected LazyLoadingQuery createLazyLoadingQuery()
   {
     return new QueuedListQuery(this.groupId);
   }
   
   protected UriFragment getUriFragment(String taskId)
   {
     UriFragment taskFragment = new UriFragment("tasks");
     if (taskId != null) {
       taskFragment.addUriPart(taskId);
     }
     
     taskFragment.addParameter("category", "queued");
     
     if (this.groupId != null) {
       taskFragment.addParameter("group", this.groupId);
     }
     return taskFragment;
   }
 }


