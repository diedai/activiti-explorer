 package org.activiti.explorer.navigation;
 
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.ViewManager;
 import org.activiti.explorer.identity.LoggedInUser;
 
 
 
 
 
 
 
 
 
 
 
 
 
 public abstract class ManagementNavigator
   implements Navigator
 {
   public void handleNavigation(UriFragment uriFragment)
   {
     if (!ExplorerApp.get().getLoggedInUser().isAdmin())
     {
       ExplorerApp.get().getViewManager().showInboxPage();
     } else {
       handleManagementNavigation(uriFragment);
     }
   }
   
   public abstract void handleManagementNavigation(UriFragment paramUriFragment);
 }


