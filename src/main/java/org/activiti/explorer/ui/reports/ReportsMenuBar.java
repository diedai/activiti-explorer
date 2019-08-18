 package org.activiti.explorer.ui.reports;
 
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.ViewManager;
 import org.activiti.explorer.ui.custom.ToolBar;
import org.activiti.explorer.ui.custom.ToolbarEntry;
import org.activiti.explorer.ui.custom.ToolbarEntry.ToolbarCommand;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class ReportsMenuBar
   extends ToolBar
 {
   private static final long serialVersionUID = 1L;
   public static final String ENTRY_RUN_REPORTS = "runReports";
   public static final String ENTRY_SAVED_REPORTS = "savedResults";
   protected I18nManager i18nManager;
   protected ViewManager viewManager;
   
   public ReportsMenuBar()
   {
     this.i18nManager = ExplorerApp.get().getI18nManager();
     this.viewManager = ExplorerApp.get().getViewManager();
     setWidth("100%");
     
     initToolbarEntries();
   }
   
   protected void initToolbarEntries() {
     addRunReportsToolbarEntry();
     addSavedReportsToolbarEntry();
   }
   
   protected void addRunReportsToolbarEntry() {
     addToolbarEntry("runReports", this.i18nManager.getMessage("reporting.menu.run.reports"), new ToolbarEntry.ToolbarCommand() {
       public void toolBarItemSelected() {
         ReportsMenuBar.this.viewManager.showRunReportPage();
       }
     });
   }
   
   protected void addSavedReportsToolbarEntry() {
     addToolbarEntry("savedResults", this.i18nManager.getMessage("reporting.menu.saved.reports"), new ToolbarEntry.ToolbarCommand() {
       public void toolBarItemSelected() {
         ReportsMenuBar.this.viewManager.showSavedReportPage();
       }
     });
   }
 }


