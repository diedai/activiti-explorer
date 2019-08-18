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
 
 
 
 
 
 
 
 
 
 
 
 public class ActiveProcessDefinitionDetailPanel
   extends AbstractProcessDefinitionDetailPanel
 {
   private static final long serialVersionUID = 1L;
   
   public ActiveProcessDefinitionDetailPanel(String processDefinitionId, ActiveProcessDefinitionPage activeProcessDefinitionPage)
   {
     super(processDefinitionId, activeProcessDefinitionPage);
   }
   
   protected void initActions(final AbstractPage parentPage) {
     ActiveProcessDefinitionPage processDefinitionPage = (ActiveProcessDefinitionPage)parentPage;
     
     Button suspendButton = new Button(this.i18nManager.getMessage("process.suspend"));
     suspendButton.addListener(new Button.ClickListener()
     {
       private static final long serialVersionUID = 1L;
       
       public void buttonClick(ClickEvent event)
       {
         ChangeProcessSuspensionStatePopupWindow popupWindow = new ChangeProcessSuspensionStatePopupWindow(ActiveProcessDefinitionDetailPanel.this.processDefinition.getId(), parentPage, true);
         ExplorerApp.get().getViewManager().showPopupWindow(popupWindow);
 
       }
       
 
     });
     boolean suspendJobPending = false;
     
     List<Job> jobs = ProcessEngines.getDefaultProcessEngine().getManagementService().createJobQuery().processDefinitionId(this.processDefinition.getId()).list();
     for (Job job : jobs)
     {
       if (((JobEntity)job).getJobHandlerType().equals("suspend-processdefinition")) {
         suspendJobPending = true;
         break;
       }
     }
     suspendButton.setEnabled(!suspendJobPending);
     
 
     processDefinitionPage.getToolBar().removeAllButtons();
     processDefinitionPage.getToolBar().addButton(suspendButton);
   }
 }


