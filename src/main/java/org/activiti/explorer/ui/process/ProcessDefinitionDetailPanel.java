 package org.activiti.explorer.ui.process;
 
 import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
 import com.vaadin.ui.Window;
 import java.text.MessageFormat;
 import org.activiti.engine.FormService;
 import org.activiti.engine.form.StartFormData;
 import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
 import org.activiti.engine.repository.ProcessDefinition;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.ui.AbstractPage;
 import org.activiti.explorer.ui.custom.ToolBar;
 import org.activiti.explorer.ui.form.FormPropertiesEventListener;
 import org.activiti.explorer.ui.form.FormPropertiesForm;
 import org.activiti.explorer.ui.form.FormPropertiesForm.FormPropertiesEvent;
 import org.activiti.explorer.ui.process.listener.ConvertProcessDefinitionToModelClickListener;
 import org.activiti.explorer.ui.process.listener.StartProcessInstanceClickListener;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class ProcessDefinitionDetailPanel
   extends AbstractProcessDefinitionDetailPanel
 {
   private static final long serialVersionUID = 1L;
   protected Button startProcessInstanceButton;
   protected Button editProcessDefinitionButton;
   protected FormPropertiesForm processDefinitionStartForm;
   
   public ProcessDefinitionDetailPanel(String processDefinitionId, ProcessDefinitionPage processDefinitionPage)
   {
     super(processDefinitionId, processDefinitionPage);
   }
   
   protected void initActions(AbstractPage parentPage) {
     ProcessDefinitionPage processDefinitionPage = (ProcessDefinitionPage)parentPage;
     
     this.startProcessInstanceButton = new Button(this.i18nManager.getMessage("process.start"));
     this.startProcessInstanceButton.addListener((ClickListener) new StartProcessInstanceClickListener(this.processDefinition, processDefinitionPage));
     
     this.editProcessDefinitionButton = new Button(this.i18nManager.getMessage("process.convert"));
     this.editProcessDefinitionButton.addListener((ClickListener) new ConvertProcessDefinitionToModelClickListener(this.processDefinition));
     
     if (!((ProcessDefinitionEntity)this.processDefinition).isGraphicalNotationDefined()) {
       this.editProcessDefinitionButton.setEnabled(false);
     }
     
 
     processDefinitionPage.getToolBar().removeAllButtons();
     processDefinitionPage.getToolBar().addButton(this.startProcessInstanceButton);
     processDefinitionPage.getToolBar().addButton(this.editProcessDefinitionButton);
   }
   
   public void showProcessStartForm(StartFormData startFormData) {
     if (this.processDefinitionStartForm == null) {
       this.processDefinitionStartForm = new FormPropertiesForm();
       this.processDefinitionStartForm.setSubmitButtonCaption(this.i18nManager.getMessage("process.start"));
       this.processDefinitionStartForm.setCancelButtonCaption(this.i18nManager.getMessage("button.cancel"));
       
 
       this.processDefinitionStartForm.addListener(new FormPropertiesEventListener() {
         private static final long serialVersionUID = 1L;
         
         protected void handleFormSubmit(FormPropertiesForm.FormPropertiesEvent event) { ProcessDefinitionDetailPanel.this.formService.submitStartFormData(ProcessDefinitionDetailPanel.this.processDefinition.getId(), event.getFormProperties());
           
 
           ExplorerApp.get().getMainWindow().showNotification(MessageFormat.format(ProcessDefinitionDetailPanel.this.i18nManager
             .getMessage("process.started.notification"), new Object[] { ProcessDefinitionDetailPanel.this.getProcessDisplayName(ProcessDefinitionDetailPanel.this.processDefinition) }));
           ProcessDefinitionDetailPanel.this.initProcessDefinitionInfo();
         }
         
         protected void handleFormCancel(FormPropertiesForm.FormPropertiesEvent event) { ProcessDefinitionDetailPanel.this.initProcessDefinitionInfo(); }
       });
     }
     
     this.processDefinitionStartForm.setFormProperties(startFormData.getFormProperties());
     
     this.startProcessInstanceButton.setEnabled(false);
     this.detailContainer.removeAllComponents();
     this.detailContainer.addComponent(this.processDefinitionStartForm);
   }
   
   public void initProcessDefinitionInfo()
   {
     super.initProcessDefinitionInfo();
     
     if (this.startProcessInstanceButton != null) {
       this.startProcessInstanceButton.setEnabled(true);
     }
   }
 }


