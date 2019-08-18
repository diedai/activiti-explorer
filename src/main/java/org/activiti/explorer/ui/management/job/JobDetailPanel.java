 package org.activiti.explorer.ui.management.job;
 
 import com.vaadin.ui.Button;
 import com.vaadin.ui.Button.ClickEvent;
 import com.vaadin.ui.Button.ClickListener;
 import com.vaadin.ui.Embedded;
 import com.vaadin.ui.GridLayout;
 import com.vaadin.ui.HorizontalLayout;
 import com.vaadin.ui.Label;
 import com.vaadin.ui.Panel;
 import com.vaadin.ui.VerticalLayout;
 import org.activiti.engine.ActivitiException;
 import org.activiti.engine.ManagementService;
 import org.activiti.engine.ProcessEngine;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.engine.impl.persistence.entity.JobEntity;
 import org.activiti.engine.impl.persistence.entity.MessageEntity;
 import org.activiti.engine.impl.persistence.entity.TimerEntity;
 import org.activiti.engine.runtime.Job;
 import org.activiti.engine.runtime.JobQuery;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.NotificationManager;
 import org.activiti.explorer.ViewManager;
 import org.activiti.explorer.ui.Images;
 import org.activiti.explorer.ui.custom.DetailPanel;
 import org.activiti.explorer.ui.custom.PrettyTimeLabel;
 import org.activiti.explorer.ui.custom.ToolBar;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class JobDetailPanel
   extends DetailPanel
 {
   private static final long serialVersionUID = 1L;
   protected transient ManagementService managementService;
   protected I18nManager i18nManager;
   protected NotificationManager notificationManager;
   protected Job job;
   protected JobPage jobPage;
   
   public JobDetailPanel(String jobId, JobPage jobPage)
   {
     this.managementService = ProcessEngines.getDefaultProcessEngine().getManagementService();
     this.i18nManager = ExplorerApp.get().getI18nManager();
     this.notificationManager = ExplorerApp.get().getNotificationManager();
     
     this.job = ((Job)this.managementService.createJobQuery().jobId(jobId).singleResult());
     this.jobPage = jobPage;
     
     init();
   }
   
   protected void init() {
     addHeader();
     addJobState();
     addActions();
   }
   
   protected void addActions() {
     Button deleteButton = new Button(this.i18nManager.getMessage("job.delete"));
     deleteButton.setIcon(Images.DELETE);
     deleteButton.addListener(new Button.ClickListener() {
       private static final long serialVersionUID = 1L;
       
       public void buttonClick(ClickEvent event) { JobDetailPanel.this.managementService.deleteJob(JobDetailPanel.this.job.getId());
         
         JobDetailPanel.this.notificationManager.showInformationNotification("job.deleted");
         JobDetailPanel.this.jobPage.refreshSelectNext();
       }
       
     });
     Button executeButton = new Button(this.i18nManager.getMessage("job.execute"));
     executeButton.setIcon(Images.EXECUTE);
     executeButton.addListener(new Button.ClickListener() {
       private static final long serialVersionUID = 1L;
       
       public void buttonClick(ClickEvent event) {
         try { JobDetailPanel.this.managementService.executeJob(JobDetailPanel.this.job.getId());
           JobDetailPanel.this.jobPage.refreshSelectNext();
         } catch (ActivitiException ae) {
           String errorMessage = ae.getMessage() + (ae.getCause() != null ? " (" + ae.getCause().getClass().getName() + ")" : "");
           JobDetailPanel.this.notificationManager.showErrorNotification("job.error", errorMessage);
           
 
           JobDetailPanel.this.jobPage.refreshCurrentJobDetails();
         }
         
       }
     });
     this.jobPage.getToolBar().removeAllButtons();
     this.jobPage.getToolBar().addButton(executeButton);
     this.jobPage.getToolBar().addButton(deleteButton);
   }
   
   protected void addHeader() {
     GridLayout jobDetails = new GridLayout(3, 2);
     jobDetails.setWidth(100.0F, 8);
     jobDetails.addStyleName("title-block");
     jobDetails.setSpacing(true);
     jobDetails.setMargin(false, false, true, false);
     
 
     Embedded image = new Embedded(null, Images.JOB_50);
     jobDetails.addComponent(image, 0, 0, 0, 1);
     
 
     Label nameLabel = new Label(getJobLabel(this.job));
     nameLabel.addStyleName("h2");
     jobDetails.addComponent(nameLabel, 1, 0, 2, 0);
     
 
 
     PrettyTimeLabel dueDateLabel = new PrettyTimeLabel(this.i18nManager.getMessage("job.duedate"), this.job.getDuedate(), this.i18nManager.getMessage("job.no.dudedate"), false);
     dueDateLabel.addStyleName("job-duedate");
     jobDetails.addComponent(dueDateLabel, 1, 1);
     
     jobDetails.setColumnExpandRatio(1, 1.0F);
     jobDetails.setColumnExpandRatio(2, 1.0F);
     
     addDetailComponent(jobDetails);
   }
   
   protected String getJobLabel(Job theJob)
   {
     if ((theJob instanceof TimerEntity))
       return this.i18nManager.getMessage("job.timer", new Object[] { theJob.getId() });
     if ((theJob instanceof MessageEntity)) {
       return this.i18nManager.getMessage("job.message", new Object[] { theJob.getId() });
     }
     return this.i18nManager.getMessage("job.default.name", new Object[] { theJob.getId() });
   }
   
 
   protected void addJobState()
   {
     Label processDefinitionHeader = new Label(this.i18nManager.getMessage("job.header.execution"));
     processDefinitionHeader.addStyleName("h3");
     processDefinitionHeader.addStyleName("block-holder");
     processDefinitionHeader.setWidth(100.0F, 8);
     addComponent(processDefinitionHeader);
     
     VerticalLayout layout = new VerticalLayout();
     layout.setSpacing(true);
     layout.setSizeFull();
     layout.setMargin(true, false, true, false);
     
     addDetailComponent(layout);
     setDetailExpandRatio(layout, 1.0F);
     
 
     if (this.job.getExceptionMessage() != null)
     {
       Label retrieslabel = new Label(getRetriesLabel(this.job));
       layout.addComponent(retrieslabel);
       
 
 
       Label exceptionMessageLabel = new Label(this.i18nManager.getMessage("job.error") + ": " + this.job.getExceptionMessage());
       exceptionMessageLabel.addStyleName("job-exception-message");
       layout.addComponent(exceptionMessageLabel);
       
 
       String stack = this.managementService.getJobExceptionStacktrace(this.job.getId());
       
       Label stackTraceLabel = new Label(stack);
       stackTraceLabel.setContentMode(1);
       stackTraceLabel.addStyleName("job-exception-trace");
       stackTraceLabel.setSizeFull();
       
       Panel stackPanel = new Panel();
       stackPanel.setWidth(100.0F, 8);
       stackPanel.setSizeFull();
       stackPanel.setScrollable(true);
       stackPanel.addComponent(stackTraceLabel);
       
       layout.addComponent(stackPanel);
       layout.setExpandRatio(stackPanel, 1.0F);
 
     }
     else if (this.job.getProcessDefinitionId() != null)
     {
 
       JobEntity jobEntity = (JobEntity)this.job;
       if (jobEntity.getJobHandlerType().equals("suspend-processdefinition")) {
         addLinkToProcessDefinition(layout, this.i18nManager.getMessage("job.suspend.processdefinition"), false);
       } else if (jobEntity.getJobHandlerType().equals("activate-processdefinition")) {
         addLinkToProcessDefinition(layout, this.i18nManager.getMessage("job.activate.processdefinition"), true);
       } else {
         addNotYetExecutedLabel(layout);
       }
     }
     else
     {
       addNotYetExecutedLabel(layout);
     }
   }
   
 
   protected void addLinkToProcessDefinition(VerticalLayout verticalLayout, String labelText, final boolean isSuspendedProcessDefinition)
   {
     HorizontalLayout layout = new HorizontalLayout();
     verticalLayout.addComponent(layout);
     
     Label processDefinitionLabel = new Label(labelText);
     processDefinitionLabel.setSizeUndefined();
     layout.addComponent(processDefinitionLabel);
     
     layout.addComponent(new Label("&nbsp;", 3));
     
     Button showProcessDefinitionLink = new Button(this.job.getProcessDefinitionId());
     showProcessDefinitionLink.addStyleName("link");
     showProcessDefinitionLink.addListener(new Button.ClickListener() {
       private static final long serialVersionUID = 1L;
       
       public void buttonClick(ClickEvent event) { if (isSuspendedProcessDefinition) {
           ExplorerApp.get().getViewManager().showSuspendedProcessDefinitionsPage(JobDetailPanel.this.job.getProcessDefinitionId());
         } else {
           ExplorerApp.get().getViewManager().showActiveProcessDefinitionsPage(JobDetailPanel.this.job.getProcessDefinitionId());
         }
       }
     });
     layout.addComponent(showProcessDefinitionLink);
   }
   
   private void addNotYetExecutedLabel(VerticalLayout layout) {
     Label noException = new Label(this.i18nManager.getMessage("job.not.executed"));
     layout.addComponent(noException);
     layout.setExpandRatio(noException, 1.0F);
   }
   
   protected String getRetriesLabel(Job theJob) { 
     String retriesString;
     if (theJob.getRetries() <= 0) {
       retriesString = this.i18nManager.getMessage("job.no.retries");
     } else {
       retriesString = this.i18nManager.getMessage("job.retries", new Object[] { Integer.valueOf(theJob.getRetries()) });
     }
     return retriesString;
   }
 }


