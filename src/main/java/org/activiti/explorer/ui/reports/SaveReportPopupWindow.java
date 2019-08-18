 package org.activiti.explorer.ui.reports;
 
 import com.vaadin.ui.Alignment;
 import com.vaadin.ui.Button;
 import com.vaadin.ui.Button.ClickEvent;
 import com.vaadin.ui.Button.ClickListener;
 import com.vaadin.ui.Component;
 import com.vaadin.ui.HorizontalLayout;
 import com.vaadin.ui.Label;
 import com.vaadin.ui.TextField;
 import com.vaadin.ui.VerticalLayout;
 import java.util.Map;
 import org.activiti.engine.FormService;
 import org.activiti.engine.HistoryService;
 import org.activiti.engine.ProcessEngine;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.engine.RuntimeService;
 import org.activiti.engine.history.HistoricProcessInstanceQuery;
 import org.activiti.engine.runtime.ProcessInstance;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.identity.LoggedInUser;
 import org.activiti.explorer.ui.custom.PopupWindow;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class SaveReportPopupWindow
   extends PopupWindow
 {
   private static final long serialVersionUID = 1L;
   protected String processDefinitionId;
   protected Map<String, String> originalFormProperties;
   protected Component componentToDisableOnClose;
   protected TextField nameField;
   
   public SaveReportPopupWindow()
   {
     I18nManager i18nManager = ExplorerApp.get().getI18nManager();
     setCaption(i18nManager.getMessage("reporting.save.popup.caption"));
     
     VerticalLayout layout = new VerticalLayout();
     addComponent(layout);
     
     createNameTextField(i18nManager, layout);
     createSaveButton(i18nManager, layout);
     
     setModal(true);
     center();
     setResizable(false);
     setWidth(400.0F, 0);
     setHeight(150.0F, 0);
     addStyleName("light");
   }
   
   protected void createNameTextField(I18nManager i18nManager, VerticalLayout layout) {
     HorizontalLayout fieldLayout = new HorizontalLayout();
     fieldLayout.setWidth(100.0F, 8);
     layout.addComponent(fieldLayout);
     fieldLayout.addComponent(new Label(i18nManager.getMessage("reporting.save.popup.name")));
     this.nameField = new TextField();
     this.nameField.setWidth(250.0F, 0);
     this.nameField.focus();
     fieldLayout.addComponent(this.nameField);
   }
   
   protected void createSaveButton(final I18nManager i18nManager, final VerticalLayout layout) {
     layout.addComponent(new Label("&nbsp", 3));
     Button saveButton = new Button(i18nManager.getMessage("button.save"));
     layout.addComponent(saveButton);
     layout.setComponentAlignment(saveButton, Alignment.MIDDLE_CENTER);
     
     saveButton.addListener(new Button.ClickListener()
     {
       private static final long serialVersionUID = 1L;
       
       public void buttonClick(ClickEvent event)
       {
         String reportName = null;
         
 
         String error = null;
         if ((SaveReportPopupWindow.this.nameField.getValue() == null) || (((String)SaveReportPopupWindow.this.nameField.getValue()).length() == 0)) {
           error = i18nManager.getMessage("reporting.save.popup.name.empty");
         } else {
           reportName = ExplorerApp.get().getLoggedInUser().getId() + "_" + SaveReportPopupWindow.this.nameField.getValue();
           if (reportName.length() > 255) {
             error = i18nManager.getMessage("reporting.save.popup.name.too.long");
           }
           else {
             boolean nameUsed = ProcessEngines.getDefaultProcessEngine().getHistoryService().createHistoricProcessInstanceQuery().processInstanceBusinessKey(reportName).count() != 0L;
             if (nameUsed) {
               error = i18nManager.getMessage("reporting.save.popup.name.exists");
             }
           }
         }
         
         if (error != null)
         {
           SaveReportPopupWindow.this.setHeight(185.0F, 0);
           layout.addComponent(new Label("&nbsp;", 3));
           
           Label errorLabel = new Label(error);
           errorLabel.addStyleName("error");
           layout.addComponent(errorLabel);
 
         }
         else
         {
           if (SaveReportPopupWindow.this.originalFormProperties != null) {
             SaveReportPopupWindow.this.startProcessInstanceWithFormProperties(reportName);
           } else {
             SaveReportPopupWindow.this.startProcessInstance(reportName);
           }
           
 
           if (SaveReportPopupWindow.this.componentToDisableOnClose != null) {
             SaveReportPopupWindow.this.componentToDisableOnClose.setEnabled(false);
           }
           SaveReportPopupWindow.this.close();
         }
       }
     });
   }
   
 
 
   protected ProcessInstance startProcessInstanceWithFormProperties(String businessKey)
   {
     return ProcessEngines.getDefaultProcessEngine().getFormService().submitStartFormData(this.processDefinitionId, businessKey, this.originalFormProperties);
   }
   
   protected ProcessInstance startProcessInstance(String businessKey) {
     return ProcessEngines.getDefaultProcessEngine().getRuntimeService().startProcessInstanceById(this.processDefinitionId, businessKey);
   }
   
   public String getProcessDefinitionId() {
     return this.processDefinitionId;
   }
   
   public void setProcessDefinitionId(String processDefinitionId) {
     this.processDefinitionId = processDefinitionId;
   }
   
   public Map<String, String> getOriginalFormProperties() {
     return this.originalFormProperties;
   }
   
   public void setOriginalFormProperties(Map<String, String> originalFormProperties) {
     this.originalFormProperties = originalFormProperties;
   }
   
   public Component getComponentToDisableOnClose() {
     return this.componentToDisableOnClose;
   }
   
   public void setComponentToDisableOnClose(Component componentToDisableOnClose) {
     this.componentToDisableOnClose = componentToDisableOnClose;
   }
 }


