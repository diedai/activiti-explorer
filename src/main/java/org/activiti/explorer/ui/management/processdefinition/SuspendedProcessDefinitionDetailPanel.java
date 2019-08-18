 package org.activiti.explorer.ui.management.processdefinition;
 
 import com.vaadin.ui.Button;
 import com.vaadin.ui.Button.ClickEvent;
 import com.vaadin.ui.Button.ClickListener;
 import java.util.List;
 import org.activiti.engine.ManagementService;
 import org.activiti.engine.ProcessEngine;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.engine.impl.persistence.entity.JobEntity;
 import org.activiti.engine.repository.ProcessDefinition;
 import org.activiti.engine.runtime.Job;
 import org.activiti.engine.runtime.JobQuery;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.ViewManager;
 import org.activiti.explorer.ui.AbstractPage;
 import org.activiti.explorer.ui.custom.ToolBar;
 import org.activiti.explorer.ui.process.AbstractProcessDefinitionDetailPanel;
 
 
 
 
 
 
 
 
 
 
 
 public class SuspendedProcessDefinitionDetailPanel
   extends AbstractProcessDefinitionDetailPanel
 {
   private static final long serialVersionUID = 1L;
   
   public SuspendedProcessDefinitionDetailPanel(String processDefinitionId, SuspendedProcessDefinitionPage suspendedProcessDefinitionPage)
   {
     super(processDefinitionId, suspendedProcessDefinitionPage);
   }
   
   protected void initActions(final AbstractPage parentPage) {
     SuspendedProcessDefinitionPage processDefinitionPage = (SuspendedProcessDefinitionPage)parentPage;
     
     Button activateButton = new Button(this.i18nManager.getMessage("process.activate"));
     activateButton.addListener(new Button.ClickListener()
     {
       private static final long serialVersionUID = 1L;
       
       public void buttonClick(ClickEvent event)
       {
         ChangeProcessSuspensionStatePopupWindow popupWindow = new ChangeProcessSuspensionStatePopupWindow(SuspendedProcessDefinitionDetailPanel.this.processDefinition.getId(), parentPage, false);
         ExplorerApp.get().getViewManager().showPopupWindow(popupWindow);
 
       }
       
 
     });
     boolean activateJobPending = false;
     
     List<Job> jobs = ProcessEngines.getDefaultProcessEngine().getManagementService().createJobQuery().processDefinitionId(this.processDefinition.getId()).list();
     for (Job job : jobs)
     {
       if (((JobEntity)job).getJobHandlerType().equals("activate-processdefinition")) {
         activateJobPending = true;
         break;
       }
     }
     activateButton.setEnabled(!activateJobPending);
     
 
     processDefinitionPage.getToolBar().removeAllButtons();
     processDefinitionPage.getToolBar().addButton(activateButton);
   }
 }


