 package org.activiti.explorer.ui.process;
 
 import com.vaadin.ui.ComponentContainer;
 import com.vaadin.ui.Embedded;
 import com.vaadin.ui.GridLayout;
 import com.vaadin.ui.HorizontalLayout;
 import com.vaadin.ui.Label;
 import com.vaadin.ui.VerticalLayout;
 import org.activiti.engine.FormService;
 import org.activiti.engine.ManagementService;
 import org.activiti.engine.ProcessEngine;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.engine.RepositoryService;
 import org.activiti.engine.repository.Deployment;
 import org.activiti.engine.repository.DeploymentQuery;
 import org.activiti.engine.repository.ProcessDefinition;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.ui.AbstractPage;
 import org.activiti.explorer.ui.Images;
 import org.activiti.explorer.ui.custom.DetailPanel;
 import org.activiti.explorer.ui.custom.PrettyTimeLabel;
 import org.activiti.explorer.ui.form.FormPropertiesForm;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public abstract class AbstractProcessDefinitionDetailPanel
   extends DetailPanel
 {
   private static final long serialVersionUID = 1L;
   protected ProcessDefinition processDefinition;
   protected Deployment deployment;
   protected AbstractPage parentPage;
   protected transient RepositoryService repositoryService;
   protected transient ManagementService managementService;
   protected transient FormService formService;
   protected I18nManager i18nManager;
   protected VerticalLayout detailPanelLayout;
   protected HorizontalLayout detailContainer;
   protected HorizontalLayout actionsContainer;
   protected Label nameLabel;
   protected FormPropertiesForm processDefinitionStartForm;
   protected ProcessDefinitionInfoComponent definitionInfoComponent;
   
   public AbstractProcessDefinitionDetailPanel(String processDefinitionId, AbstractPage parentPage)
   {
     this.repositoryService = ProcessEngines.getDefaultProcessEngine().getRepositoryService();
     this.managementService = ProcessEngines.getDefaultProcessEngine().getManagementService();
     this.formService = ProcessEngines.getDefaultProcessEngine().getFormService();
     this.i18nManager = ExplorerApp.get().getI18nManager();
     
     this.parentPage = parentPage;
     this.processDefinition = this.repositoryService.getProcessDefinition(processDefinitionId);
     
     if (this.processDefinition != null) {
       this.deployment = ((Deployment)this.repositoryService.createDeploymentQuery().deploymentId(this.processDefinition.getDeploymentId()).singleResult());
     }
     
     initUi();
   }
   
   protected void initUi() {
     setSizeFull();
     addStyleName("white");
     
     this.detailPanelLayout = new VerticalLayout();
     this.detailPanelLayout.setWidth(100.0F, 8);
     this.detailPanelLayout.setMargin(true);
     setDetailContainer(this.detailPanelLayout);
     
 
     initHeader();
     
     this.detailContainer = new HorizontalLayout();
     this.detailContainer.addStyleName("light");
     this.detailPanelLayout.addComponent(this.detailContainer);
     this.detailContainer.setSizeFull();
     
     initActions(this.parentPage);
     initProcessDefinitionInfo();
   }
   
 
 
   protected abstract void initActions(AbstractPage paramAbstractPage);
   
 
   public void initProcessDefinitionInfo()
   {
     if (this.definitionInfoComponent == null) {
       this.definitionInfoComponent = new ProcessDefinitionInfoComponent(this.processDefinition, this.deployment);
     }
     
     this.detailContainer.removeAllComponents();
     this.detailContainer.addComponent(this.definitionInfoComponent);
   }
   
   protected void initHeader() {
     GridLayout details = new GridLayout(2, 2);
     details.setWidth(100.0F, 8);
     details.addStyleName("title-block");
     details.setSpacing(true);
     details.setMargin(false, false, true, false);
     details.setColumnExpandRatio(1, 1.0F);
     this.detailPanelLayout.addComponent(details);
     
 
     Embedded image = new Embedded(null, Images.PROCESS_50);
     details.addComponent(image, 0, 0, 0, 1);
     
 
     Label nameLabel = new Label(getProcessDisplayName(this.processDefinition));
     nameLabel.addStyleName("h2");
     details.addComponent(nameLabel, 1, 0);
     
 
     HorizontalLayout propertiesLayout = new HorizontalLayout();
     propertiesLayout.setSpacing(true);
     details.addComponent(propertiesLayout);
     
 
     String versionString = this.i18nManager.getMessage("process.version", new Object[] { Integer.valueOf(this.processDefinition.getVersion()) });
     Label versionLabel = new Label(versionString);
     versionLabel.addStyleName("process-version");
     propertiesLayout.addComponent(versionLabel);
     
 
 
     PrettyTimeLabel deployTimeLabel = new PrettyTimeLabel(this.i18nManager.getMessage("process.deploy.time"), this.deployment.getDeploymentTime(), null, true);
     deployTimeLabel.addStyleName("process-deploy-time");
     propertiesLayout.addComponent(deployTimeLabel);
   }
   
   protected String getProcessDisplayName(ProcessDefinition processDefinition) {
     if (processDefinition.getName() != null) {
       return processDefinition.getName();
     }
     return processDefinition.getKey();
   }
   
   protected void addEmptySpace(ComponentContainer container)
   {
     Label emptySpace = new Label("&nbsp;", 3);
     emptySpace.setSizeUndefined();
     container.addComponent(emptySpace);
   }
 }


