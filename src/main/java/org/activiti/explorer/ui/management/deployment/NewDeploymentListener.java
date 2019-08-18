 package org.activiti.explorer.ui.management.deployment;
 
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.ViewManager;
 import org.activiti.explorer.ui.custom.ToolbarEntry.ToolbarCommand;
 import org.activiti.explorer.ui.custom.UploadPopupWindow;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class NewDeploymentListener
   implements ToolbarCommand
 {
   private static final long serialVersionUID = 1L;
   protected I18nManager i18nManager;
   protected ViewManager viewManager;
   
   public NewDeploymentListener()
   {
     this.i18nManager = ExplorerApp.get().getI18nManager();
     this.viewManager = ExplorerApp.get().getViewManager();
   }
   
   public void toolBarItemSelected() {
     DeploymentUploadReceiver receiver = new DeploymentUploadReceiver();
     
 
     UploadPopupWindow uploadPopupWindow = new UploadPopupWindow(this.i18nManager.getMessage("deployment.upload"), this.i18nManager.getMessage("deployment.upload.description"), receiver);
     
 
 
 
     uploadPopupWindow.addFinishedListener(receiver);
     this.viewManager.showPopupWindow(uploadPopupWindow);
   }
 }


