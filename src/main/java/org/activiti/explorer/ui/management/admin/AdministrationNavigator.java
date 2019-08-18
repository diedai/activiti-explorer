 package org.activiti.explorer.ui.management.admin;
 
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.ViewManager;
 import org.activiti.explorer.navigation.ManagementNavigator;
 import org.activiti.explorer.navigation.UriFragment;
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class AdministrationNavigator
   extends ManagementNavigator
 {
   public static final String MANAGEMENT_URI_PART = "admin_management";
   
   public String getTrigger()
   {
     return "admin_management";
   }
   
   public void handleManagementNavigation(UriFragment uriFragment) {
     String managementId = uriFragment.getUriPart(1);
     
     if (managementId != null) {
       ExplorerApp.get().getViewManager().showAdministrationPage(managementId);
     } else {
       ExplorerApp.get().getViewManager().showAdministrationPage();
     }
   }
 }


