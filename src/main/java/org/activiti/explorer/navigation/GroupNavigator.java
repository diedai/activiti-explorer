 package org.activiti.explorer.navigation;
 
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.ViewManager;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class GroupNavigator
   extends ManagementNavigator
 {
   public static final String GROUP_URI_PART = "group";
   
   public String getTrigger()
   {
     return "group";
   }
   
   public void handleManagementNavigation(UriFragment uriFragment) {
     String groupId = uriFragment.getUriPart(1);
     
     if (groupId != null) {
       ExplorerApp.get().getViewManager().showGroupPage(groupId);
     } else {
       ExplorerApp.get().getViewManager().showGroupPage();
     }
   }
 }


