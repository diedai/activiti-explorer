 package org.activiti.explorer.ui.management.processdefinition;
 
 import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
 import com.vaadin.data.Property.ValueChangeListener;
 import com.vaadin.ui.Alignment;
 import com.vaadin.ui.Button;
 import com.vaadin.ui.Button.ClickEvent;
 import com.vaadin.ui.Button.ClickListener;
 import com.vaadin.ui.CheckBox;
 import com.vaadin.ui.DateField;
 import com.vaadin.ui.HorizontalLayout;
 import com.vaadin.ui.Label;
 import com.vaadin.ui.VerticalLayout;
 import java.util.Date;
 import org.activiti.engine.ProcessEngine;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.engine.RepositoryService;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.ui.AbstractPage;
 import org.activiti.explorer.ui.custom.PopupWindow;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class ChangeProcessSuspensionStatePopupWindow
   extends PopupWindow
 {
   private static final long serialVersionUID = 1L;
   protected I18nManager i18nManager;
   protected String processDefinitionId;
   protected AbstractPage parentPage;
   protected VerticalLayout verticalLayout;
   protected CheckBox nowCheckBox;
   protected CheckBox dateCheckBox;
   protected DateField dateField;
   protected CheckBox includeProcessInstancesCheckBox;
   
   public ChangeProcessSuspensionStatePopupWindow(String processDefinitionId, AbstractPage parentPage, boolean suspend)
   {
     this.processDefinitionId = processDefinitionId;
     this.parentPage = parentPage;
     this.i18nManager = ExplorerApp.get().getI18nManager();
     
     setCaption(suspend ? this.i18nManager.getMessage("process.suspend.popup") : this.i18nManager
       .getMessage("process.activate.popup"));
     setModal(true);
     center();
     setResizable(false);
     setWidth(400.0F, 0);
     setHeight(300.0F, 0);
     addStyleName("light");
     
     this.verticalLayout = new VerticalLayout();
     addComponent(this.verticalLayout);
     addTimeSection(suspend);
     addIncludeProcessInstancesSection(suspend);
     addOkButton(suspend);
   }
   
   protected void addTimeSection(boolean suspend)
   {
     Label timeLabel = new Label(suspend ? this.i18nManager.getMessage("process.suspend.popup.time.description") : this.i18nManager.getMessage("process.activate.popup.time.description"));
     this.verticalLayout.addComponent(timeLabel);
     this.verticalLayout.addComponent(new Label("&nbsp", 3));
     
     this.nowCheckBox = new CheckBox(this.i18nManager.getMessage("process.suspend.popup.time.now"), true);
     this.nowCheckBox.addStyleName("process-definition-suspend-choice");
     this.nowCheckBox.setImmediate(true);
     this.nowCheckBox.addListener(new Button.ClickListener() {
       public void buttonClick(ClickEvent event) {
         if (ChangeProcessSuspensionStatePopupWindow.this.nowCheckBox.booleanValue() == true) {
           ChangeProcessSuspensionStatePopupWindow.this.dateField.setValue(null);
           ChangeProcessSuspensionStatePopupWindow.this.dateCheckBox.setValue(Boolean.valueOf(false));
         } else {
           ChangeProcessSuspensionStatePopupWindow.this.dateCheckBox.setValue(Boolean.valueOf(true));
           ChangeProcessSuspensionStatePopupWindow.this.dateField.setValue(new Date());
         }
       }
     });
     this.verticalLayout.addComponent(this.nowCheckBox);
     
     HorizontalLayout dateLayout = new HorizontalLayout();
     this.verticalLayout.addComponent(dateLayout);
     
     this.dateCheckBox = new CheckBox(this.i18nManager.getMessage("process.suspend.popup.time.date"));
     this.dateCheckBox.addStyleName("process-definition-suspend-choice");
     this.dateCheckBox.setImmediate(true);
     this.dateCheckBox.addListener(new Button.ClickListener() {
       public void buttonClick(ClickEvent event) {
         if (ChangeProcessSuspensionStatePopupWindow.this.dateCheckBox.booleanValue() == true) {
           ChangeProcessSuspensionStatePopupWindow.this.dateField.setValue(new Date());
           ChangeProcessSuspensionStatePopupWindow.this.nowCheckBox.setValue(Boolean.valueOf(false));
         } else {
           ChangeProcessSuspensionStatePopupWindow.this.dateField.setValue(null);
           ChangeProcessSuspensionStatePopupWindow.this.nowCheckBox.setValue(Boolean.valueOf(true));
         }
       }
     });
     dateLayout.addComponent(this.dateCheckBox);
     
     this.dateField = new DateField();
     this.dateField.setImmediate(true);
     this.dateField.addListener(new Property.ValueChangeListener()
     {
       public void valueChange(Property.ValueChangeEvent event) {
         if (ChangeProcessSuspensionStatePopupWindow.this.dateField.getValue() != null) {
           ChangeProcessSuspensionStatePopupWindow.this.nowCheckBox.setValue(Boolean.valueOf(false));
           ChangeProcessSuspensionStatePopupWindow.this.dateCheckBox.setValue(Boolean.valueOf(true));
         }
         
       }
     });
     dateLayout.addComponent(this.dateField);
   }
   
   protected void addIncludeProcessInstancesSection(boolean suspend) {
     this.verticalLayout.addComponent(new Label("&nbsp", 3));
     this.verticalLayout.addComponent(new Label("&nbsp", 3));
     
 
 
     this.includeProcessInstancesCheckBox = new CheckBox(suspend ? this.i18nManager.getMessage("process.suspend.popup.process.instances.description") : this.i18nManager.getMessage("process.activate.popup.process.instances.description"), true);
     this.verticalLayout.addComponent(this.includeProcessInstancesCheckBox);
   }
   
   protected void addOkButton(final boolean suspend) {
     this.verticalLayout.addComponent(new Label("&nbsp", 3));
     this.verticalLayout.addComponent(new Label("&nbsp", 3));
     
     Button okButton = new Button(this.i18nManager.getMessage("button.ok"));
     this.verticalLayout.addComponent(okButton);
     this.verticalLayout.setComponentAlignment(okButton, Alignment.BOTTOM_CENTER);
     
     okButton.addListener(new Button.ClickListener()
     {
       private static final long serialVersionUID = 1L;
       
       public void buttonClick(ClickEvent event) {
         RepositoryService repositoryService = ProcessEngines.getDefaultProcessEngine().getRepositoryService();
         boolean includeProcessInstances = ((Boolean)ChangeProcessSuspensionStatePopupWindow.this.includeProcessInstancesCheckBox.getValue()).booleanValue();
         
         if (suspend) {
           repositoryService.suspendProcessDefinitionById(ChangeProcessSuspensionStatePopupWindow.this.processDefinitionId, includeProcessInstances, 
             (Date)ChangeProcessSuspensionStatePopupWindow.this.dateField.getValue());
         } else {
           repositoryService.activateProcessDefinitionById(ChangeProcessSuspensionStatePopupWindow.this.processDefinitionId, includeProcessInstances, 
             (Date)ChangeProcessSuspensionStatePopupWindow.this.dateField.getValue());
         }
         
         ChangeProcessSuspensionStatePopupWindow.this.close();
         ChangeProcessSuspensionStatePopupWindow.this.parentPage.refreshSelectNext();
       }
     });
   }
 }


