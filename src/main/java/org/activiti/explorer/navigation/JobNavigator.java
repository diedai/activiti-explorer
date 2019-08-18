 package org.activiti.explorer.navigation;
 
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.ViewManager;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class JobNavigator
   extends ManagementNavigator
 {
   public static final String JOB_URL_PART = "job";
   
   public String getTrigger()
   {
     return "job";
   }
   
   public void handleManagementNavigation(UriFragment uriFragment) {
     String jobId = uriFragment.getUriPart(1);
     if (jobId != null) {
       ExplorerApp.get().getViewManager().showJobPage(jobId);
     } else {
       ExplorerApp.get().getViewManager().showJobPage();
     }
   }
 }


