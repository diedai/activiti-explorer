 package org.activiti.explorer.navigation;
 
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.ViewManager;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class SavedReportNavigator
   implements Navigator
 {
   private static final long serialVersionUID = 1L;
   public static final String SAVED_REPORT_URI_PART = "savedReport";
   
   public String getTrigger()
   {
     return "savedReport";
   }
   
   public void handleNavigation(UriFragment uriFragment) {
     String modelId = uriFragment.getUriPart(1);
     
     if (modelId != null) {
       ExplorerApp.get().getViewManager().showSavedReportPage(modelId);
     } else {
       ExplorerApp.get().getViewManager().showSavedReportPage();
     }
   }
 }


