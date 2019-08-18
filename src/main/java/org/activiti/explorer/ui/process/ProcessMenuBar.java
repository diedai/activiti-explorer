 package org.activiti.explorer.ui.process;
 
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.ViewManager;
 import org.activiti.explorer.ui.custom.ToolBar;
 import org.activiti.explorer.ui.custom.ToolbarEntry.ToolbarCommand;
 import org.activiti.explorer.ui.custom.ToolbarEntry;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class ProcessMenuBar
   extends ToolBar
 {
   private static final long serialVersionUID = 1L;
   public static final String ENTRY_MY_PROCESS_INSTANCES = "myProcessInstances";
   public static final String DEPLOYED_PROCESS_DEFINITIONS = "deployedProcessDefinitions";
   public static final String EDITOR_PROCESS_DEFINITIONS = "editorProcessDefinitions";
   protected I18nManager i18nManager;
   protected ViewManager viewManager;
   
   public ProcessMenuBar()
   {
     this.i18nManager = ExplorerApp.get().getI18nManager();
     this.viewManager = ExplorerApp.get().getViewManager();
     
     init();
   }
   
   protected void init() {
     setWidth("100%");
     
     addToolbarEntry("myProcessInstances", this.i18nManager.getMessage("process.menu.my.instances"), new ToolbarEntry.ToolbarCommand() {
       public void toolBarItemSelected() {
         ProcessMenuBar.this.viewManager.showMyProcessInstancesPage();
       }
       
     });
     addToolbarEntry("deployedProcessDefinitions", this.i18nManager.getMessage("process.menu.deployed.definitions"), new ToolbarEntry.ToolbarCommand() {
       public void toolBarItemSelected() {
         ProcessMenuBar.this.viewManager.showDeployedProcessDefinitionPage();
       }
       
     });
     addToolbarEntry("editorProcessDefinitions", this.i18nManager.getMessage("process.menu.editor.definitions"), new ToolbarEntry.ToolbarCommand() {
       public void toolBarItemSelected() {
         ProcessMenuBar.this.viewManager.showEditorProcessDefinitionPage();
       }
     });
   }
 }


