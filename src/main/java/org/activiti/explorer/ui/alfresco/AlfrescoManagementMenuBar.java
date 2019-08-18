 package org.activiti.explorer.ui.alfresco;
 
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.ViewManager;
import org.activiti.explorer.ui.custom.ToolbarEntry;
import org.activiti.explorer.ui.custom.ToolbarEntry.ToolbarCommand;
 import org.activiti.explorer.ui.management.ManagementMenuBar;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class AlfrescoManagementMenuBar
   extends ManagementMenuBar
 {
   private static final long serialVersionUID = 1L;
   public static final String ENTRY_PROCESS_DEFINITIONS = "processDefinitions";
   public static final String ENTRY_PROCESS_INSTANCES = "processInstances";
   
   protected void initToolbarEntries()
   {
     addDeploymentsToolbarEntry();
     addJobsToolbarEntry();
     addProcessDefinitionsEntry();
     addProcessInstancesEntry();
     addDatabaseToolbarEntry();
   }
   
   protected void addProcessDefinitionsEntry() {
     addToolbarEntry("processDefinitions", this.i18nManager.getMessage("process.menu.deployed.definitions"), new ToolbarEntry.ToolbarCommand() {
       public void toolBarItemSelected() {
         AlfrescoManagementMenuBar.this.viewManager.showDeployedProcessDefinitionPage();
       }
     });
   }
   
   protected void addProcessInstancesEntry() {
     addToolbarEntry("processInstances", this.i18nManager.getMessage("process.menu.instances"), new ToolbarEntry.ToolbarCommand() {
       public void toolBarItemSelected() {
         AlfrescoManagementMenuBar.this.viewManager.showProcessInstancePage();
       }
     });
   }
 }


