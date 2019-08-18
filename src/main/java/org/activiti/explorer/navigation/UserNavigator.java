 package org.activiti.explorer.navigation;
 
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.ViewManager;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class UserNavigator
   extends ManagementNavigator
 {
   public static final String USER_URI_PART = "user";
   
   public String getTrigger()
   {
     return "user";
   }
   
   public void handleManagementNavigation(UriFragment uriFragment) {
     String userId = uriFragment.getUriPart(1);
     
     if (userId != null) {
       ExplorerApp.get().getViewManager().showUserPage(userId);
     } else {
       ExplorerApp.get().getViewManager().showUserPage();
     }
   }
 }


