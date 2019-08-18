 package org.activiti.explorer.navigation;
 
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.ViewManager;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class DatabaseNavigator
   extends ManagementNavigator
 {
   public static final String TABLE_URI_PART = "database";
   
   public String getTrigger()
   {
     return "database";
   }
   
   public void handleManagementNavigation(UriFragment uriFragment) {
     String tableName = uriFragment.getUriPart(1);
     
     if (tableName != null) {
       ExplorerApp.get().getViewManager().showDatabasePage(tableName);
     } else {
       ExplorerApp.get().getViewManager().showDatabasePage();
     }
   }
 }


