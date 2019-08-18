 package org.activiti.editor.ui;
 
 import com.vaadin.ui.Alignment;
 import com.vaadin.ui.Button;
 import com.vaadin.ui.CheckBox;
 import com.vaadin.ui.GridLayout;
 import com.vaadin.ui.HorizontalLayout;
 import com.vaadin.ui.Label;
 import com.vaadin.ui.TextField;
 import org.activiti.engine.repository.Model;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.ViewManager;
 import org.activiti.explorer.ui.custom.PopupWindow;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class DeployModelPopupWindow
   extends PopupWindow
 {
   private static final long serialVersionUID = 1L;
   protected GridLayout layout;
   protected Label descriptionLabel;
   protected TextField processNameTextField;
   protected CheckBox generateReportsCheckBox;
   protected Button deployButton;
   protected Button cancelButton;
   
   public DeployModelPopupWindow(Model modelData)
   {
     setWidth(400.0F, 0);
     setModal(true);
     setResizable(false);
     
     addStyleName("light");
     
     this.layout = new GridLayout(2, 2);
     this.layout.setSpacing(true);
     this.layout.setSizeFull();
     this.layout.setMargin(false, false, true, false);
     addComponent(this.layout);
     
     I18nManager i18nManager = ExplorerApp.get().getI18nManager();
     setCaption(i18nManager.getMessage("model.deploy.popup.caption"));
     
 
     Label nameLabel = new Label(i18nManager.getMessage("model.deploy.name"));
     this.layout.addComponent(nameLabel, 0, 0);
     
     this.processNameTextField = new TextField();
     if (modelData.getName() != null) {
       this.processNameTextField.setValue(modelData.getName());
     }
     this.processNameTextField.focus();
     this.layout.addComponent(this.processNameTextField, 1, 0);
     
 
     Label generateReportsLabel = new Label(i18nManager.getMessage("model.deploy.generate.reports"));
     this.layout.addComponent(generateReportsLabel, 0, 1);
     
     this.generateReportsCheckBox = new CheckBox();
     this.generateReportsCheckBox.setValue(Boolean.valueOf(true));
     this.layout.addComponent(this.generateReportsCheckBox, 1, 1);
     
 
     initButtons(i18nManager);
   }
   
 
 
   public void showPopupWindow()
   {
     ExplorerApp.get().getViewManager().showPopupWindow(this);
   }
   
   protected void initButtons(I18nManager i18nManager) {
     HorizontalLayout buttonLayout = new HorizontalLayout();
     buttonLayout.setSpacing(true);
     buttonLayout.setWidth(100.0F, 8);
     addComponent(buttonLayout);
     
     this.deployButton = new Button(i18nManager.getMessage("model.deploy.button.deploy"));
     buttonLayout.addComponent(this.deployButton);
     buttonLayout.setComponentAlignment(this.deployButton, Alignment.BOTTOM_CENTER);
   }
   
   public void closePopupWindow() {
     close();
   }
   
   public Button getDeployButton() {
     return this.deployButton;
   }
   
   public void setDeployButton(Button deployButton) {
     this.deployButton = deployButton;
   }
   
   public Button getCancelButton() {
     return this.cancelButton;
   }
   
   public void setCancelButton(Button cancelButton) {
     this.cancelButton = cancelButton;
   }
   
   public String getProcessName() {
     return this.processNameTextField.getValue().toString();
   }
   
   public boolean isGenerateReports() {
     return this.generateReportsCheckBox.booleanValue();
   }
 }


