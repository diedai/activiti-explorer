 package org.activiti.explorer.ui.reports;
 
 import com.vaadin.ui.Embedded;
 import com.vaadin.ui.GridLayout;
 import com.vaadin.ui.HorizontalLayout;
 import com.vaadin.ui.Label;
 import com.vaadin.ui.VerticalLayout;
 import org.activiti.engine.HistoryService;
 import org.activiti.engine.ProcessEngine;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.engine.history.HistoricProcessInstance;
 import org.activiti.engine.history.HistoricProcessInstanceQuery;
 import org.activiti.engine.history.HistoricVariableInstance;
 import org.activiti.engine.history.HistoricVariableInstanceQuery;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.ui.Images;
 import org.activiti.explorer.ui.custom.DetailPanel;
 import org.activiti.explorer.ui.form.FormPropertiesForm;
 import org.activiti.explorer.util.time.HumanTime;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class SavedReportDetailPanel
   extends DetailPanel
 {
   private static final long serialVersionUID = 1L;
   protected HistoricProcessInstance historicProcessInstance;
   protected I18nManager i18nManager;
   protected VerticalLayout detailPanelLayout;
   protected HorizontalLayout detailContainer;
   protected FormPropertiesForm processDefinitionStartForm;
   
   public SavedReportDetailPanel(String historicProcessInstance)
   {
     this.i18nManager = ExplorerApp.get().getI18nManager();
     
     this.historicProcessInstance = 
       ((HistoricProcessInstance)ProcessEngines.getDefaultProcessEngine().getHistoryService().createHistoricProcessInstanceQuery().processInstanceId(historicProcessInstance).singleResult());
     
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
   }
   
   protected void initHeader()
   {
     GridLayout details = new GridLayout(2, 2);
     details.setWidth(100.0F, 8);
     details.addStyleName("title-block");
     details.setSpacing(true);
     details.setMargin(false, false, true, false);
     details.setColumnExpandRatio(1, 1.0F);
     this.detailPanelLayout.addComponent(details);
     
 
     Embedded image = new Embedded(null, Images.REPORT_50);
     details.addComponent(image, 0, 0, 0, 1);
     
 
     Label nameLabel = new Label(SavedReportListItem.getReportDisplayName(this.historicProcessInstance));
     nameLabel.addStyleName("h2");
     details.addComponent(nameLabel, 1, 0);
     
 
     HorizontalLayout propertiesLayout = new HorizontalLayout();
     propertiesLayout.setSpacing(true);
     details.addComponent(propertiesLayout);
     
 
     String createLabel = this.i18nManager.getMessage("reporting.report.created", new Object[] { new HumanTime(this.i18nManager).format(this.historicProcessInstance.getEndTime()) });
     Label versionLabel = new Label(createLabel);
     versionLabel.addStyleName("process-start-time");
     propertiesLayout.addComponent(versionLabel);
   }
   
 
 
 
 
   protected void initForm()
   {
     HistoricVariableInstance historicVariableInstance = (HistoricVariableInstance)ProcessEngines.getDefaultProcessEngine().getHistoryService().createHistoricVariableInstanceQuery().processInstanceId(this.historicProcessInstance.getId()).variableName("reportData").singleResult();
     
 
     byte[] reportData = (byte[])historicVariableInstance.getValue();
     ChartComponent chart = ChartGenerator.generateChart(reportData);
     chart.setWidth(100.0F, 8);
     chart.setHeight(100.0F, 8);
     
 
     this.detailContainer.addComponent(chart);
   }
 }


