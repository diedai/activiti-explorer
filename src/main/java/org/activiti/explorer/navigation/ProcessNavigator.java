 package org.activiti.explorer.navigation;
 
 import java.io.Serializable;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.ViewManager;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class ProcessNavigator
   implements Navigator, Serializable
 {
   private static final long serialVersionUID = -4980737250067435656L;
   public static final String process_URI_PART = "process";
   
   public String getTrigger()
   {
     return "process";
   }
   
   public void handleNavigation(UriFragment uriFragment) {
     String processDefinitionId = uriFragment.getUriPart(1);
     
     if (processDefinitionId != null) {
       ExplorerApp.get().getViewManager().showDeployedProcessDefinitionPage(processDefinitionId);
     } else {
       ExplorerApp.get().getViewManager().showDeployedProcessDefinitionPage();
     }
   }
 }


