 package org.activiti.explorer.ui.reports;
 
 import com.vaadin.ui.Button;
 import com.vaadin.ui.Button.ClickEvent;
 import com.vaadin.ui.Button.ClickListener;
 import com.vaadin.ui.Embedded;
 import com.vaadin.ui.GridLayout;
 import com.vaadin.ui.HorizontalLayout;
 import com.vaadin.ui.Label;
 import com.vaadin.ui.VerticalLayout;
 import java.util.List;
 import java.util.Map;
 import org.activiti.engine.FormService;
 import org.activiti.engine.HistoryService;
 import org.activiti.engine.ProcessEngine;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.engine.RepositoryService;
 import org.activiti.engine.RuntimeService;
 import org.activiti.engine.form.StartFormData;
 import org.activiti.engine.history.HistoricVariableInstance;
 import org.activiti.engine.history.HistoricVariableInstanceQuery;
 import org.activiti.engine.repository.ProcessDefinition;
 import org.activiti.engine.repository.ProcessDefinitionQuery;
 import org.activiti.engine.runtime.ProcessInstance;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.ViewManager;
 import org.activiti.explorer.ui.AbstractPage;
 import org.activiti.explorer.ui.Images;
 import org.activiti.explorer.ui.custom.DetailPanel;
 import org.activiti.explorer.ui.custom.ToolBar;
 import org.activiti.explorer.ui.form.FormPropertiesEventListener;
 import org.activiti.explorer.ui.form.FormPropertiesForm;
 import org.activiti.explorer.ui.form.FormPropertiesForm.FormPropertiesEvent;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class ReportDetailPanel
   extends DetailPanel
 {
   private static final long serialVersionUID = 1L;
   protected ProcessDefinition processDefinition;
   protected AbstractPage parentPage;
   protected I18nManager i18nManager;
   protected VerticalLayout detailPanelLayout;
   protected HorizontalLayout detailContainer;
   protected FormPropertiesForm processDefinitionStartForm;
   protected Map<String, String> savedFormProperties;
   
   public ReportDetailPanel(String processDefinitionId, AbstractPage parentPage)
   {
     this.parentPage = parentPage;
     this.i18nManager = ExplorerApp.get().getI18nManager();
     
 
     this.processDefinition = ((ProcessDefinition)ProcessEngines.getDefaultProcessEngine().getRepositoryService().createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult());
     
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
     
     initForm();
     initActions();
   }
   
   protected void initHeader() {
     GridLayout details = new GridLayout(2, 2);
     details.setWidth(100.0F, 8);
     details.addStyleName("title-block");
     details.setSpacing(true);
     details.setMargin(false, false, true, false);
     details.setColumnExpandRatio(1, 1.0F);
     this.detailPanelLayout.addComponent(details);
     
 
     Embedded image = new Embedded(null, Images.REPORT_50);
     details.addComponent(image, 0, 0, 0, 1);
     
 
     Label nameLabel = new Label(getReportDisplayName());
     nameLabel.addStyleName("h2");
     details.addComponent(nameLabel, 1, 0);
     
 
     HorizontalLayout propertiesLayout = new HorizontalLayout();
     propertiesLayout.setSpacing(true);
     details.addComponent(propertiesLayout);
     
 
     String versionString = this.i18nManager.getMessage("process.version", new Object[] { Integer.valueOf(this.processDefinition.getVersion()) });
     Label versionLabel = new Label(versionString);
     versionLabel.addStyleName("process-version");
     propertiesLayout.addComponent(versionLabel);
   }
   
   protected void initActions() {
     final Button saveButton = new Button(this.i18nManager.getMessage("button.save"));
     saveButton.addListener(new Button.ClickListener()
     {
       private static final long serialVersionUID = 1L;
       
       public void buttonClick(ClickEvent event) {
         SaveReportPopupWindow saveReportPopupWindow = new SaveReportPopupWindow();
         saveReportPopupWindow.setProcessDefinitionId(ReportDetailPanel.this.processDefinition.getId());
         saveReportPopupWindow.setOriginalFormProperties(ReportDetailPanel.this.savedFormProperties);
         saveReportPopupWindow.setComponentToDisableOnClose(saveButton);
         ExplorerApp.get().getViewManager().showPopupWindow(saveReportPopupWindow);
 
       }
       
 
     });
     this.parentPage.getToolBar().removeAllButtons();
     this.parentPage.getToolBar().addButton(saveButton);
   }
   
   protected void initForm()
   {
     ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
     StartFormData startFormData = processEngine.getFormService().getStartFormData(this.processDefinition.getId());
     
     if ((startFormData != null) && (((startFormData.getFormProperties() != null) && 
       (!startFormData.getFormProperties().isEmpty())) || (startFormData.getFormKey() != null))) {
       this.processDefinitionStartForm = new FormPropertiesForm();
       this.detailContainer.addComponent(this.processDefinitionStartForm);
       
       this.processDefinitionStartForm.setFormProperties(startFormData.getFormProperties());
       
       this.processDefinitionStartForm.setSubmitButtonCaption(this.i18nManager.getMessage("reporting.generatereport"));
       this.processDefinitionStartForm.hideCancelButton();
       this.processDefinitionStartForm.addListener(new FormPropertiesEventListener()
       {
         private static final long serialVersionUID = 1L;
         
 
         protected void handleFormSubmit(FormPropertiesForm.FormPropertiesEvent event)
         {
           ReportDetailPanel.this.savedFormProperties = event.getFormProperties();
           ProcessInstance processInstance = ReportDetailPanel.this.startProcessInstanceWithFormProperties(ReportDetailPanel.this.processDefinition.getId(), event.getFormProperties());
           ReportDetailPanel.this.generateReport(processInstance);
         }
         
 
 
 
 
         protected void handleFormCancel(FormPropertiesForm.FormPropertiesEvent event) {}
       });
     }
     else
     {
       ProcessInstance processInstance = startProcessInstance(this.processDefinition.getId());
       generateReport(processInstance);
     }
   }
   
   protected ProcessInstance startProcessInstanceWithFormProperties(String processDefinitonId, Map<String, String> formProperties)
   {
     return ProcessEngines.getDefaultProcessEngine().getFormService().submitStartFormData(processDefinitonId, formProperties);
   }
   
   protected ProcessInstance startProcessInstance(String processDefinitionId) {
     return ProcessEngines.getDefaultProcessEngine().getRuntimeService().startProcessInstanceById(processDefinitionId);
   }
   
 
 
 
 
 
   protected void generateReport(ProcessInstance processInstance)
   {
     HistoricVariableInstance historicVariableInstance = (HistoricVariableInstance)ProcessEngines.getDefaultProcessEngine().getHistoryService().createHistoricVariableInstanceQuery().processInstanceId(processInstance.getId()).variableName("reportData").singleResult();
     
 
     byte[] reportData = (byte[])historicVariableInstance.getValue();
     ChartComponent chart = ChartGenerator.generateChart(reportData);
     chart.setWidth(100.0F, 8);
     chart.setHeight(100.0F, 8);
     
 
     if (this.processDefinitionStartForm != null) {
       this.detailContainer.removeComponent(this.processDefinitionStartForm);
       this.processDefinitionStartForm = null;
     }
     this.detailContainer.addComponent(chart);
     
 
 
     ProcessEngines.getDefaultProcessEngine().getHistoryService().deleteHistoricProcessInstance(processInstance.getId());
   }
   
   protected String getReportDisplayName() {
     if (this.processDefinition.getName() != null) {
       return this.processDefinition.getName();
     }
     return this.processDefinition.getKey();
   }
 }


