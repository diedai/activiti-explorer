 package org.activiti.explorer.ui.management.deployment;
 
 import com.vaadin.terminal.StreamResource;
 import com.vaadin.terminal.StreamResource.StreamSource;
 import com.vaadin.ui.Button;
 import com.vaadin.ui.Button.ClickEvent;
 import com.vaadin.ui.Button.ClickListener;
 import com.vaadin.ui.Embedded;
 import com.vaadin.ui.GridLayout;
 import com.vaadin.ui.Label;
 import com.vaadin.ui.Link;
 import com.vaadin.ui.VerticalLayout;
 import java.io.InputStream;
 import java.util.Collections;
 import java.util.List;
 import org.activiti.engine.ProcessEngine;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.engine.RepositoryService;
 import org.activiti.engine.repository.Deployment;
 import org.activiti.engine.repository.DeploymentQuery;
 import org.activiti.engine.repository.ProcessDefinition;
 import org.activiti.engine.repository.ProcessDefinitionQuery;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.ViewManager;
 import org.activiti.explorer.ui.Images;
 import org.activiti.explorer.ui.custom.DetailPanel;
 import org.activiti.explorer.ui.custom.PrettyTimeLabel;
 import org.activiti.explorer.ui.custom.ToolBar;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class DeploymentDetailPanel
   extends DetailPanel
 {
   private static final long serialVersionUID = 1L;
   protected transient RepositoryService repositoryService;
   protected ViewManager viewManager;
   protected I18nManager i18nManager;
   protected Deployment deployment;
   protected DeploymentPage parent;
   
   public DeploymentDetailPanel(String deploymentId, DeploymentPage parent)
   {
     this.repositoryService = ProcessEngines.getDefaultProcessEngine().getRepositoryService();
     this.i18nManager = ExplorerApp.get().getI18nManager();
     this.viewManager = ExplorerApp.get().getViewManager();
     
     this.deployment = ((Deployment)this.repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult());
     this.parent = parent;
     
     init();
   }
   
   protected void init()
   {
     setWidth(100.0F, 8);
     
     addDeploymentName();
     addProcessDefinitionLinks();
     addResourceLinks();
     
     addActions();
   }
   
   protected void addActions()
   {
     Button deleteButton = new Button(this.i18nManager.getMessage("deployment.delete"));
     deleteButton.setIcon(Images.DELETE);
     deleteButton.addListener(new Button.ClickListener()
     {
       public void buttonClick(ClickEvent event) {
         DeploymentDetailPanel.this.viewManager.showPopupWindow(new DeleteDeploymentPopupWindow(DeploymentDetailPanel.this.deployment, DeploymentDetailPanel.this.parent));
       }
       
     });
     this.parent.getToolBar().removeAllButtons();
     this.parent.getToolBar().addButton(deleteButton);
   }
   
   protected void addDeploymentName()
   {
     GridLayout taskDetails = new GridLayout(3, 2);
     taskDetails.setWidth(100.0F, 8);
     taskDetails.addStyleName("title-block");
     taskDetails.setSpacing(true);
     taskDetails.setMargin(false, false, true, false);
     
 
     Embedded image = new Embedded(null, Images.DEPLOYMENT_50);
     taskDetails.addComponent(image, 0, 0, 0, 1);
     
 
     Label nameLabel = new Label();
     if (this.deployment.getName() != null) {
       nameLabel.setValue(this.deployment.getName());
     } else {
       nameLabel.setValue(this.i18nManager.getMessage("deployment.no.name"));
     }
     nameLabel.addStyleName("h2");
     taskDetails.addComponent(nameLabel, 1, 0, 2, 0);
     
 
 
     PrettyTimeLabel deployTimeLabel = new PrettyTimeLabel(this.i18nManager.getMessage("deployment.deploy.time"), this.deployment.getDeploymentTime(), null, true);
     deployTimeLabel.addStyleName("deployment-deploy-time");
     taskDetails.addComponent(deployTimeLabel, 1, 1);
     
     taskDetails.setColumnExpandRatio(1, 1.0F);
     taskDetails.setColumnExpandRatio(2, 1.0F);
     
     addDetailComponent(taskDetails);
   }
   
 
 
   protected void addProcessDefinitionLinks()
   {
     List<ProcessDefinition> processDefinitions = ((ProcessDefinitionQuery)this.repositoryService.createProcessDefinitionQuery().deploymentId(this.deployment.getId()).orderByProcessDefinitionName().asc()).list();
     VerticalLayout processDefinitionLinksLayout;
     if (!processDefinitions.isEmpty())
     {
 
       Label processDefinitionHeader = new Label(this.i18nManager.getMessage("deployment.header.definitions"));
       processDefinitionHeader.addStyleName("h3");
       processDefinitionHeader.addStyleName("block-holder");
       processDefinitionHeader.setWidth(100.0F, 8);
       addDetailComponent(processDefinitionHeader);
       
 
       processDefinitionLinksLayout = new VerticalLayout();
       processDefinitionLinksLayout.setSpacing(true);
       processDefinitionLinksLayout.setMargin(true, false, true, false);
       addDetailComponent(processDefinitionLinksLayout);
       
       for (final ProcessDefinition processDefinition : processDefinitions) {
         Button processDefinitionButton = new Button(getProcessDisplayName(processDefinition));
         processDefinitionButton.addListener(new Button.ClickListener() {
           public void buttonClick(ClickEvent event) {
             DeploymentDetailPanel.this.viewManager.showDeployedProcessDefinitionPage(processDefinition.getId());
           }
         });
         processDefinitionButton.addStyleName("link");
         processDefinitionLinksLayout.addComponent(processDefinitionButton);
       }
     }
   }
   
   protected String getProcessDisplayName(ProcessDefinition processDefinition) {
     if (processDefinition.getName() != null) {
       return processDefinition.getName();
     }
     return processDefinition.getKey();
   }
   
   protected void addResourceLinks()
   {
     List<String> resourceNames = this.repositoryService.getDeploymentResourceNames(this.deployment.getId());
     Collections.sort(resourceNames);
     VerticalLayout resourceLinksLayout;
     if (!resourceNames.isEmpty()) {
       Label resourceHeader = new Label(this.i18nManager.getMessage("deployment.header.resources"));
       resourceHeader.setWidth("95%");
       resourceHeader.addStyleName("h3");
       resourceHeader.addStyleName("block-holder");
       addDetailComponent(resourceHeader);
       
 
       resourceLinksLayout = new VerticalLayout();
       resourceLinksLayout.setSpacing(true);
       resourceLinksLayout.setMargin(true, false, false, false);
       addDetailComponent(resourceLinksLayout);
       
       for (final String resourceName : resourceNames) {
         StreamResource.StreamSource streamSource = new StreamResource.StreamSource() {
           public InputStream getStream() {
             return DeploymentDetailPanel.this.repositoryService.getResourceAsStream(DeploymentDetailPanel.this.deployment.getId(), resourceName);
           }
         };
         Link resourceLink = new Link(resourceName, new StreamResource(streamSource, resourceName, ExplorerApp.get()));
         resourceLinksLayout.addComponent(resourceLink);
       }
     }
   }
 }


