 package org.activiti.explorer.navigation;
 
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.ViewManager;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class MyProcessesNavigator
   implements Navigator
 {
   public static final String MY_PROCESSES_URI_PART = "myProcess";
   
   public String getTrigger()
   {
     return "myProcess";
   }
   
   public void handleNavigation(UriFragment uriFragment) {
     String processInstanceId = uriFragment.getUriPart(1);
     
     if (processInstanceId != null) {
       ExplorerApp.get().getViewManager().showMyProcessInstancesPage(processInstanceId);
     } else {
       ExplorerApp.get().getViewManager().showMyProcessInstancesPage();
     }
   }
 }


