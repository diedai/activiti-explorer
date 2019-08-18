 package org.activiti.explorer.navigation;
 
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.ViewManager;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class ReportNavigator
   implements Navigator
 {
   private static final long serialVersionUID = 1L;
   public static final String REPORT_URI_PART = "report";
   
   public String getTrigger()
   {
     return "report";
   }
   
   public void handleNavigation(UriFragment uriFragment) {
     String reportId = uriFragment.getUriPart(1);
     
     if (reportId != null) {
       ExplorerApp.get().getViewManager().showRunReportPage(reportId);
     } else {
       ExplorerApp.get().getViewManager().showRunReportPage();
     }
   }
 }


