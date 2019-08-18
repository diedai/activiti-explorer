 package org.activiti.editor.ui;
 
 import com.fasterxml.jackson.databind.JsonNode;
 import com.fasterxml.jackson.databind.ObjectMapper;
 import com.fasterxml.jackson.databind.node.ObjectNode;
 import com.vaadin.Application;
 import com.vaadin.data.Property;
 import com.vaadin.data.Property.ValueChangeEvent;
 import com.vaadin.data.Property.ValueChangeListener;
 import com.vaadin.terminal.DownloadStream;
 import com.vaadin.terminal.FileResource;
 import com.vaadin.ui.Alignment;
 import com.vaadin.ui.Button;
 import com.vaadin.ui.Button.ClickEvent;
 import com.vaadin.ui.Button.ClickListener;
 import com.vaadin.ui.ComponentContainer;
 import com.vaadin.ui.Embedded;
 import com.vaadin.ui.GridLayout;
 import com.vaadin.ui.HorizontalLayout;
 import com.vaadin.ui.Label;
 import com.vaadin.ui.Select;
 import com.vaadin.ui.VerticalLayout;
 import com.vaadin.ui.Window;
 import java.io.ByteArrayInputStream;
 import java.io.File;
 import java.io.UnsupportedEncodingException;
 import org.activiti.bpmn.converter.BpmnXMLConverter;
 import org.activiti.bpmn.model.BpmnModel;
 import org.activiti.bpmn.model.Process;
 import org.activiti.editor.language.json.converter.BpmnJsonConverter;
 import org.activiti.engine.ProcessEngine;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.engine.RepositoryService;
 import org.activiti.engine.repository.Deployment;
 import org.activiti.engine.repository.DeploymentBuilder;
 import org.activiti.engine.repository.Model;
 import org.activiti.engine.repository.ProcessDefinition;
 import org.activiti.engine.repository.ProcessDefinitionQuery;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.NotificationManager;
 import org.activiti.explorer.ViewManager;
 import org.activiti.explorer.reporting.ReportingUtil;
 import org.activiti.explorer.ui.Images;
 import org.activiti.explorer.ui.custom.DetailPanel;
 import org.activiti.explorer.ui.custom.ToolBar;
 import org.activiti.explorer.ui.form.FormPropertiesForm;
 import org.activiti.explorer.ui.process.listener.EditModelClickListener;
 import org.activiti.explorer.ui.process.listener.ImportModelClickListener;
 import org.activiti.explorer.ui.process.listener.NewModelClickListener;
 import org.activiti.workflow.simple.converter.WorkflowDefinitionConversion;
 import org.activiti.workflow.simple.converter.WorkflowDefinitionConversionFactory;
 import org.activiti.workflow.simple.converter.json.SimpleWorkflowJsonConverter;
 import org.activiti.workflow.simple.definition.WorkflowDefinition;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class EditorProcessDefinitionDetailPanel
   extends DetailPanel
 {
   private static final long serialVersionUID = 1L;
   protected static final Logger LOGGER = LoggerFactory.getLogger(EditorProcessDefinitionDetailPanel.class);
   
   protected Model modelData;
   
   protected EditorProcessDefinitionPage processDefinitionPage;
   
   protected I18nManager i18nManager;
   
   protected VerticalLayout detailPanelLayout;
   
   protected HorizontalLayout detailContainer;
   
   protected HorizontalLayout actionsContainer;
   
   protected Label nameLabel;
   
   protected Button newModelButton;
   protected Button importModelButton;
   protected Button editModelButton;
   protected Label actionLabel;
   protected Select actionSelect;
   protected FormPropertiesForm processDefinitionStartForm;
   protected EditorProcessDefinitionInfoComponent definitionInfoComponent;
   protected transient RepositoryService repositoryService = ProcessEngines.getDefaultProcessEngine().getRepositoryService();
   
   public EditorProcessDefinitionDetailPanel(String modelId, EditorProcessDefinitionPage processDefinitionPage) {
     this.i18nManager = ExplorerApp.get().getI18nManager();
     
     this.processDefinitionPage = processDefinitionPage;
     this.modelData = this.repositoryService.getModel(modelId);
     
     initUi();
   }
   
   protected void initUi() {
     setSizeFull();
     addStyleName("white");
     
     this.detailPanelLayout = new VerticalLayout();
     this.detailPanelLayout.setWidth(100.0F, 8);
     this.detailPanelLayout.setMargin(true);
     this.detailPanelLayout.setSpacing(true);
     setDetailContainer(this.detailPanelLayout);
     
 
     initHeader();
     
     this.detailContainer = new HorizontalLayout();
     this.detailContainer.addStyleName("light");
     this.detailPanelLayout.addComponent(this.detailContainer);
     this.detailContainer.setSizeFull();
     
     initActions();
     initProcessDefinitionInfo();
   }
   
   protected void initActions() {
     this.newModelButton = new Button(this.i18nManager.getMessage("process.new"));
     this.newModelButton.addListener((ClickListener) new NewModelClickListener());
     
     this.importModelButton = new Button(this.i18nManager.getMessage("process.import"));
     this.importModelButton.addListener((ClickListener) new ImportModelClickListener());
     
     this.editModelButton = new Button(this.i18nManager.getMessage("process.edit"));
     this.editModelButton.addListener((ClickListener) new EditModelClickListener(this.modelData));
     
     this.actionLabel = new Label(this.i18nManager.getMessage("model.action"));
     this.actionLabel.setSizeUndefined();
     
     this.actionSelect = new Select();
     this.actionSelect.addItem(this.i18nManager.getMessage("process.copy"));
     this.actionSelect.addItem(this.i18nManager.getMessage("process.delete"));
     this.actionSelect.addItem(this.i18nManager.getMessage("process.deploy"));
     this.actionSelect.addItem(this.i18nManager.getMessage("process.export"));
     
     this.actionSelect.setWidth("100px");
     this.actionSelect.setFilteringMode(0);
     this.actionSelect.setImmediate(true);
     this.actionSelect.addListener(new Property.ValueChangeListener()
     {
       private static final long serialVersionUID = 1L;
       
       public void valueChange(Property.ValueChangeEvent event) {
         if (EditorProcessDefinitionDetailPanel.this.i18nManager.getMessage("process.copy").equals(event.getProperty().getValue())) {
           ExplorerApp.get().getViewManager().showPopupWindow(new CopyModelPopupWindow(EditorProcessDefinitionDetailPanel.this.modelData));
         } else if (EditorProcessDefinitionDetailPanel.this.i18nManager.getMessage("process.delete").equals(event.getProperty().getValue())) {
           ExplorerApp.get().getViewManager().showPopupWindow(new DeleteModelPopupWindow(EditorProcessDefinitionDetailPanel.this.modelData));
         } else if (EditorProcessDefinitionDetailPanel.this.i18nManager.getMessage("process.deploy").equals(event.getProperty().getValue())) {
           EditorProcessDefinitionDetailPanel.this.deployModel();
         } else if (EditorProcessDefinitionDetailPanel.this.i18nManager.getMessage("process.export").equals(event.getProperty().getValue())) {
           EditorProcessDefinitionDetailPanel.this.exportModel();
         }
         
       }
       
     });
     this.processDefinitionPage.getToolBar().removeAllButtons();
     this.processDefinitionPage.getToolBar().removeAllAdditionalComponents();
     this.processDefinitionPage.getToolBar().addButton(this.newModelButton);
     this.processDefinitionPage.getToolBar().addButton(this.importModelButton);
     this.processDefinitionPage.getToolBar().addButton(this.editModelButton);
     this.processDefinitionPage.getToolBar().addAdditionalComponent(this.actionLabel);
     this.processDefinitionPage.getToolBar().setComponentAlignment(this.actionLabel, Alignment.MIDDLE_LEFT);
     this.processDefinitionPage.getToolBar().addAdditionalComponent(this.actionSelect);
     this.processDefinitionPage.getToolBar().setComponentAlignment(this.actionSelect, Alignment.MIDDLE_RIGHT);
   }
   
   public void initProcessDefinitionInfo()
   {
     if (this.definitionInfoComponent == null) {
       this.definitionInfoComponent = new EditorProcessDefinitionInfoComponent(this.modelData);
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
     
 
     Label nameLabel = new Label(this.modelData.getName());
     nameLabel.addStyleName("h2");
     details.addComponent(nameLabel, 1, 0);
     
 
     HorizontalLayout propertiesLayout = new HorizontalLayout();
     propertiesLayout.setSpacing(true);
     details.addComponent(propertiesLayout);
     
 
     String versionString = this.i18nManager.getMessage("process.version", new Object[] { this.modelData.getVersion() });
     Label versionLabel = new Label(versionString);
     versionLabel.addStyleName("process-version");
     propertiesLayout.addComponent(versionLabel);
   }
   
   protected void addEmptySpace(ComponentContainer container) {
     Label emptySpace = new Label("&nbsp;", 3);
     emptySpace.setSizeUndefined();
     container.addComponent(emptySpace);
   }
   
   protected void exportModel() {
     FileResource stream = new FileResource(new File(""), ExplorerApp.get())
     {
       private static final long serialVersionUID = 1L;
       
       public DownloadStream getStream()
       {
         DownloadStream ds = null;
         try
         {
           byte[] bpmnBytes = null;
           String filename = null;
           if ("table-editor".equals(EditorProcessDefinitionDetailPanel.this.modelData.getCategory()))
           {
             WorkflowDefinition workflowDefinition = ExplorerApp.get().getSimpleWorkflowJsonConverter().readWorkflowDefinition(EditorProcessDefinitionDetailPanel.this.repositoryService.getModelEditorSource(EditorProcessDefinitionDetailPanel.this.modelData.getId()));
             
             filename = workflowDefinition.getName();
             
             WorkflowDefinitionConversion conversion = ExplorerApp.get().getWorkflowDefinitionConversionFactory().createWorkflowDefinitionConversion(workflowDefinition);
             bpmnBytes = conversion.getBpmn20Xml().getBytes("utf-8");
           } else {
             JsonNode editorNode = new ObjectMapper().readTree(EditorProcessDefinitionDetailPanel.this.repositoryService.getModelEditorSource(EditorProcessDefinitionDetailPanel.this.modelData.getId()));
             BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
             BpmnModel model = jsonConverter.convertToBpmnModel(editorNode);
             filename = model.getMainProcess().getId() + ".bpmn20.xml";
             bpmnBytes = new BpmnXMLConverter().convertToXML(model);
           }
           
           ByteArrayInputStream in = new ByteArrayInputStream(bpmnBytes);
           ds = new DownloadStream(in, "application/xml", filename);
           
           ds.setParameter("Content-Disposition", "attachment; filename=" + filename);
         } catch (Exception e) {
           EditorProcessDefinitionDetailPanel.LOGGER.error("failed to export model to BPMN XML", e);
           ExplorerApp.get().getNotificationManager().showErrorNotification("process.toxml.failed", e);
         }
         return ds;
       }
     };
     stream.setCacheTime(0L);
     ExplorerApp.get().getMainWindow().open(stream);
   }
   
   protected void deployModel() {
     try {
       if ("table-editor".equals(this.modelData.getCategory())) {
         deploySimpleTableEditorModel(this.repositoryService.getModelEditorSource(this.modelData.getId()));
       } else {
         ObjectNode modelNode = (ObjectNode)new ObjectMapper().readTree(this.repositoryService.getModelEditorSource(this.modelData.getId()));
         deployModelerModel(modelNode);
       }
     }
     catch (Exception e) {
       LOGGER.error("Failed to deploy model", e);
       ExplorerApp.get().getNotificationManager().showErrorNotification("process.toxml.failed", e);
     }
   }
   
   protected void deploySimpleTableEditorModel(final byte[] model)
   {
     final DeployModelPopupWindow deployModelPopupWindow = new DeployModelPopupWindow(this.modelData);
     
     deployModelPopupWindow.getDeployButton().addListener(new Button.ClickListener()
     {
       private static final long serialVersionUID = 1L;
       
 
 
       public void buttonClick(ClickEvent event)
       {
         WorkflowDefinition workflowDefinition = ExplorerApp.get().getSimpleWorkflowJsonConverter().readWorkflowDefinition(model);
         
 
         EditorProcessDefinitionDetailPanel.this.modelData.setName(deployModelPopupWindow.getProcessName());
         workflowDefinition.setName(deployModelPopupWindow.getProcessName());
         
 
         WorkflowDefinitionConversion conversion = ExplorerApp.get().getWorkflowDefinitionConversionFactory().createWorkflowDefinitionConversion(workflowDefinition);
         conversion.convert();
         
 
         byte[] bpmnBytes = null;
         try {
           bpmnBytes = conversion.getBpmn20Xml().getBytes("utf-8");
           
           String processName = EditorProcessDefinitionDetailPanel.this.modelData.getName() + ".bpmn20.xml";
           
 
 
           Deployment deployment = EditorProcessDefinitionDetailPanel.this.repositoryService.createDeployment().name(EditorProcessDefinitionDetailPanel.this.modelData.getName()).addString(processName, new String(bpmnBytes)).deploy();
           
 
           if (deployModelPopupWindow.isGenerateReports())
           {
             ProcessDefinition processDefinition = (ProcessDefinition)EditorProcessDefinitionDetailPanel.this.repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
             ReportingUtil.generateTaskDurationReport(processDefinition.getId());
           }
           
 
           deployModelPopupWindow.closePopupWindow();
           ExplorerApp.get().getViewManager().showDeploymentPage(deployment.getId());
         }
         catch (UnsupportedEncodingException e) {
           ExplorerApp.get().getNotificationManager().showErrorNotification("process.toxml.failed", e);
           deployModelPopupWindow.closePopupWindow();
         }
         
       }
       
 
     });
     deployModelPopupWindow.showPopupWindow();
   }
   
   protected void deployModelerModel(ObjectNode modelNode) {
     BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);
     byte[] bpmnBytes = new BpmnXMLConverter().convertToXML(model);
     
     String processName = this.modelData.getName() + ".bpmn20.xml";
     
 
 
     Deployment deployment = this.repositoryService.createDeployment().name(this.modelData.getName()).addString(processName, new String(bpmnBytes)).deploy();
     
     ExplorerApp.get().getViewManager().showDeploymentPage(deployment.getId());
   }
 }


