 package org.activiti.explorer.ui.alfresco;
 
 import org.activiti.explorer.DefaultViewManager;
 import org.activiti.explorer.ui.MainWindow;
 import org.activiti.explorer.ui.management.processinstance.ProcessInstancePage;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class AlfrescoViewManager
   extends DefaultViewManager
 {
   public void showDefaultPage()
   {
     this.mainWindow.showDefaultContent();
     showDeploymentPage();
   }
   
   public void showDeployedProcessDefinitionPage()
   {
     switchView(new AlfrescoProcessDefinitionPage(), "process", "processDefinitions");
   }
   
   public void showDeployedProcessDefinitionPage(String processDefinitionId)
   {
     switchView(new AlfrescoProcessDefinitionPage(processDefinitionId), "process", "processDefinitions");
   }
   
   public void showProcessInstancePage()
   {
     switchView(new ProcessInstancePage(), "manage", "processInstances");
   }
   
   public void showProcessInstancePage(String processInstanceId)
   {
     switchView(new ProcessInstancePage(), "manage", "processInstances");
   }
 }


