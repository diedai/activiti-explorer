 package org.activiti.explorer.navigation;
 
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.ViewManager;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class SuspendedProcessDefinitionNavigator
   extends ManagementNavigator
 {
   public static final String SUSPENDED_PROC_DEF_URI_PART = "suspendedProcessDefinition";
   
   public String getTrigger()
   {
     return "suspendedProcessDefinition";
   }
   
   public void handleManagementNavigation(UriFragment uriFragment) {
     String processDefinitionId = uriFragment.getUriPart(1);
     
     if (processDefinitionId != null) {
       ExplorerApp.get().getViewManager().showSuspendedProcessDefinitionsPage(processDefinitionId);
     } else {
       ExplorerApp.get().getViewManager().showSuspendedProcessDefinitionsPage();
     }
   }
 }


