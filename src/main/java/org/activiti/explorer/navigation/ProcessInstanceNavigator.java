 package org.activiti.explorer.navigation;
 
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.ViewManager;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class ProcessInstanceNavigator
 {
   public static final String PROCESS_INSTANCE_URL_PART = "processinstance";
   
   public String getTrigger()
   {
     return "processinstance";
   }
   
   public void handleManagementNavigation(UriFragment uriFragment) {
     String processInstanceId = uriFragment.getUriPart(1);
     if (processInstanceId != null) {
       ExplorerApp.get().getViewManager().showProcessInstancePage(processInstanceId);
     } else {
       ExplorerApp.get().getViewManager().showProcessInstancePage();
     }
   }
 }


