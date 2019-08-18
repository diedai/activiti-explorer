 package org.activiti.explorer.navigation;
 
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.ViewManager;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class ActiveProcessDefinitionNavigator
   extends ManagementNavigator
 {
   public static final String ACTIVE_PROC_DEF_URI_PART = "activeProcessDefinition";
   
   public String getTrigger()
   {
     return "activeProcessDefinition";
   }
   
   public void handleManagementNavigation(UriFragment uriFragment) {
     String processDefinitionId = uriFragment.getUriPart(1);
     
     if (processDefinitionId != null) {
       ExplorerApp.get().getViewManager().showActiveProcessDefinitionsPage(processDefinitionId);
     } else {
       ExplorerApp.get().getViewManager().showActiveProcessDefinitionsPage();
     }
   }
 }


