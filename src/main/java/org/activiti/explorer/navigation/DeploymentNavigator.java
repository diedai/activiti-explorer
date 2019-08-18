 package org.activiti.explorer.navigation;
 
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.ViewManager;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class DeploymentNavigator
   extends ManagementNavigator
 {
   public static final String DEPLOYMENT_URI_PART = "deployment";
   
   public String getTrigger()
   {
     return "deployment";
   }
   
   public void handleManagementNavigation(UriFragment uriFragment) {
     String deploymentId = uriFragment.getUriPart(1);
     
     if (deploymentId != null) {
       ExplorerApp.get().getViewManager().showDeploymentPage(deploymentId);
     } else {
       ExplorerApp.get().getViewManager().showDeploymentPage();
     }
   }
 }


