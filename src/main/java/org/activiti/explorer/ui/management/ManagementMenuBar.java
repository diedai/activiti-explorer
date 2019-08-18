 package org.activiti.explorer.ui.management;
 
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.ViewManager;
 import org.activiti.explorer.ui.custom.ToolBar;
import org.activiti.explorer.ui.custom.ToolbarEntry;
import org.activiti.explorer.ui.custom.ToolbarEntry.ToolbarCommand;
 import org.activiti.explorer.ui.custom.ToolbarPopupEntry;
 import org.activiti.explorer.ui.management.deployment.NewDeploymentListener;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class ManagementMenuBar
   extends ToolBar
 {
   private static final long serialVersionUID = 1L;
   public static final String ENTRY_DATABASE = "database";
   public static final String ENTRY_DEPLOYMENTS = "deployments";
   public static final String ENTRY_ACTIVE_PROCESS_DEFINITIONS = "activeProcessDefinitions";
   public static final String ENTRY_SUSPENDED_PROCESS_DEFINITIONS = "suspendedProcessDefinitions";
   public static final String ENTRY_JOBS = "jobs";
   public static final String ENTRY_USERS = "users";
   public static final String ENTRY_GROUPS = "groups";
   public static final String ENTRY_ADMIN = "administration";
   public static final String ENTRY_CRYSTALBALL = "crystalball";
   protected I18nManager i18nManager;
   protected ViewManager viewManager;
   
   public ManagementMenuBar()
   {
     this.i18nManager = ExplorerApp.get().getI18nManager();
     this.viewManager = ExplorerApp.get().getViewManager();
     setWidth("100%");
     
     initToolbarEntries();
   }
   
   protected void initToolbarEntries() {
     addDatabaseToolbarEntry();
     addDeploymentsToolbarEntry();
     addActiveProcessDefinitionsEntry();
     addSuspendedProcessDefinitionsEntry();
     addJobsToolbarEntry();
     addUsersToolbarEntry();
     addGroupToolbarEntry();
     addAdministrationToolbarEntry();
     addCrystalBallToolbarEntry();
   }
   
   protected void addDatabaseToolbarEntry() {
     addToolbarEntry("database", this.i18nManager.getMessage("management.menu.database"), new ToolbarEntry.ToolbarCommand() {
       public void toolBarItemSelected() {
         ManagementMenuBar.this.viewManager.showDatabasePage();
       }
     });
   }
   
   protected void addDeploymentsToolbarEntry() {
     ToolbarPopupEntry deploymentEntry = addPopupEntry("deployments", this.i18nManager.getMessage("management.menu.deployments"));
     deploymentEntry.addMenuItem(this.i18nManager.getMessage("management.menu.deployments.show.all"), new ToolbarEntry.ToolbarCommand() {
       public void toolBarItemSelected() {
         ManagementMenuBar.this.viewManager.showDeploymentPage();
       }
     });
     deploymentEntry.addMenuItem(this.i18nManager.getMessage("management.menu.deployments.upload"), (ToolbarCommand) new NewDeploymentListener());
   }
   
   protected void addActiveProcessDefinitionsEntry() {
     addToolbarEntry("activeProcessDefinitions", this.i18nManager
       .getMessage("management.menu.active.processdefinitions"), new ToolbarEntry.ToolbarCommand() {
         public void toolBarItemSelected() {
           ManagementMenuBar.this.viewManager.showActiveProcessDefinitionsPage();
         }
       });
   }
   
   protected void addSuspendedProcessDefinitionsEntry() {
     addToolbarEntry("suspendedProcessDefinitions", this.i18nManager
       .getMessage("management.menu.suspended.processdefinitions"), new ToolbarEntry.ToolbarCommand() {
         public void toolBarItemSelected() {
           ManagementMenuBar.this.viewManager.showSuspendedProcessDefinitionsPage();
         }
       });
   }
   
   protected void addJobsToolbarEntry() {
     addToolbarEntry("jobs", this.i18nManager.getMessage("management.menu.jobs"), new ToolbarEntry.ToolbarCommand() {
       public void toolBarItemSelected() {
         ManagementMenuBar.this.viewManager.showJobPage();
       }
     });
   }
   
   protected void addUsersToolbarEntry() {
     addToolbarEntry("users", this.i18nManager.getMessage("management.menu.users"), new ToolbarEntry.ToolbarCommand() {
       public void toolBarItemSelected() {
         ManagementMenuBar.this.viewManager.showUserPage();
       }
     });
   }
   
   protected void addGroupToolbarEntry() {
     addToolbarEntry("groups", this.i18nManager.getMessage("management.menu.groups"), new ToolbarEntry.ToolbarCommand() {
       public void toolBarItemSelected() {
         ManagementMenuBar.this.viewManager.showGroupPage();
       }
     });
   }
   
   protected void addAdministrationToolbarEntry() {
     addToolbarEntry("administration", this.i18nManager.getMessage("management.menu.admin"), new ToolbarEntry.ToolbarCommand() {
       public void toolBarItemSelected() {
         ManagementMenuBar.this.viewManager.showAdministrationPage();
       }
     });
   }
   
   protected void addCrystalBallToolbarEntry() {
     addToolbarEntry("crystalball", this.i18nManager.getMessage("management.menu.crystalball"), new ToolbarEntry.ToolbarCommand() {
       public void toolBarItemSelected() {
         ManagementMenuBar.this.viewManager.showCrystalBallPage();
       }
     });
   }
 }


