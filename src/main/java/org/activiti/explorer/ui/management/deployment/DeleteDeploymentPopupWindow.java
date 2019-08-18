 package org.activiti.explorer.ui.management.deployment;
 
 import com.vaadin.ui.Alignment;
 import com.vaadin.ui.Button;
 import com.vaadin.ui.Button.ClickEvent;
 import com.vaadin.ui.Button.ClickListener;
 import com.vaadin.ui.Embedded;
 import com.vaadin.ui.HorizontalLayout;
 import com.vaadin.ui.Label;
 import com.vaadin.ui.VerticalLayout;
 import java.util.List;
 import org.activiti.engine.ProcessEngine;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.engine.RepositoryService;
 import org.activiti.engine.RuntimeService;
 import org.activiti.engine.repository.Deployment;
 import org.activiti.engine.repository.ProcessDefinition;
 import org.activiti.engine.repository.ProcessDefinitionQuery;
 import org.activiti.engine.runtime.ProcessInstanceQuery;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.ui.Images;
 import org.activiti.explorer.ui.custom.PopupWindow;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class DeleteDeploymentPopupWindow
   extends PopupWindow
 {
   private static final long serialVersionUID = 1L;
   protected transient RepositoryService repositoryService = ProcessEngines.getDefaultProcessEngine().getRepositoryService();
   protected transient RuntimeService runtimeService = ProcessEngines.getDefaultProcessEngine().getRuntimeService();
   protected I18nManager i18nManager;
   protected DeploymentPage deploymentPage;
   protected VerticalLayout windowLayout;
   protected Deployment deployment;
   
   public DeleteDeploymentPopupWindow(Deployment deployment, DeploymentPage deploymentPage) {
     this.deployment = deployment;
     this.deploymentPage = deploymentPage;
     this.windowLayout = ((VerticalLayout)getContent());
     this.i18nManager = ExplorerApp.get().getI18nManager();
     
     initWindow();
     addDeleteWarning();
     addButtons();
   }
   
   protected void initWindow() {
     this.windowLayout.setSpacing(true);
     addStyleName("light");
     setModal(true);
     center();
     setCaption(this.i18nManager.getMessage("deployment.delete.popup.caption", new Object[] { this.deployment.getName() }));
   }
   
   protected void addDeleteWarning()
   {
     List<ProcessDefinition> processDefinitions = this.repositoryService.createProcessDefinitionQuery().deploymentId(this.deployment.getId()).list();
     
     int nrOfProcessInstances = 0;
     for (ProcessDefinition processDefinition : processDefinitions)
     {
 
       nrOfProcessInstances = (int)(nrOfProcessInstances + this.runtimeService.createProcessInstanceQuery().processDefinitionId(processDefinition.getId()).count());
     }
     
     if (nrOfProcessInstances == 0) {
       Label noInstancesLabel = new Label(this.i18nManager.getMessage("deployment.no.instances"));
       noInstancesLabel.addStyleName("light");
       addComponent(noInstancesLabel);
     } else {
       HorizontalLayout warningLayout = new HorizontalLayout();
       warningLayout.setSpacing(true);
       addComponent(warningLayout);
       
       Embedded warningIcon = new Embedded(null, Images.WARNING);
       warningIcon.setType(1);
       warningLayout.addComponent(warningIcon);
       
       Label warningLabel = new Label(this.i18nManager.getMessage("deployment.delete.popup.warning", new Object[] { Integer.valueOf(nrOfProcessInstances) }), 3);
       warningLabel.setSizeUndefined();
       warningLabel.addStyleName("light");
       warningLayout.addComponent(warningLabel);
     }
     
 
     Label emptySpace = new Label("&nbsp;", 3);
     addComponent(emptySpace);
   }
   
   protected void addButtons()
   {
     Button cancelButton = new Button(this.i18nManager.getMessage("button.cancel"));
     cancelButton.addStyleName("small");
     cancelButton.addListener(new Button.ClickListener() {
       public void buttonClick(ClickEvent event) {
         DeleteDeploymentPopupWindow.this.close();
       }
       
 
     });
     Button deleteButton = new Button(this.i18nManager.getMessage("deployment.delete.popup.delete.button"));
     deleteButton.addStyleName("small");
     deleteButton.addListener(new Button.ClickListener()
     {
       public void buttonClick(ClickEvent event) {
         DeleteDeploymentPopupWindow.this.repositoryService.deleteDeployment(DeleteDeploymentPopupWindow.this.deployment.getId(), true);
         DeleteDeploymentPopupWindow.this.close();
         DeleteDeploymentPopupWindow.this.deploymentPage.refreshSelectNext();
       }
       
 
     });
     HorizontalLayout buttonLayout = new HorizontalLayout();
     buttonLayout.setSpacing(true);
     buttonLayout.addComponent(cancelButton);
     buttonLayout.addComponent(deleteButton);
     addComponent(buttonLayout);
     this.windowLayout.setComponentAlignment(buttonLayout, Alignment.BOTTOM_RIGHT);
   }
 }


