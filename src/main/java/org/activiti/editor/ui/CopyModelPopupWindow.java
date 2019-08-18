 package org.activiti.editor.ui;
 
 import com.fasterxml.jackson.databind.ObjectMapper;
 import com.fasterxml.jackson.databind.node.ObjectNode;
 import com.vaadin.terminal.UserError;
 import com.vaadin.ui.Alignment;
 import com.vaadin.ui.Button;
 import com.vaadin.ui.Button.ClickEvent;
 import com.vaadin.ui.Button.ClickListener;
 import com.vaadin.ui.Form;
 import com.vaadin.ui.HorizontalLayout;
 import com.vaadin.ui.Label;
 import com.vaadin.ui.Layout;
 import com.vaadin.ui.TextArea;
 import com.vaadin.ui.TextField;
 import com.vaadin.ui.VerticalLayout;
 import org.activiti.editor.constants.ModelDataJsonConstants;
 import org.activiti.engine.ProcessEngine;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.engine.RepositoryService;
 import org.activiti.engine.repository.Model;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.ViewManager;
 import org.activiti.explorer.ui.custom.PopupWindow;
 import org.apache.commons.lang3.StringUtils;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class CopyModelPopupWindow
   extends PopupWindow
   implements ModelDataJsonConstants
 {
   private static final long serialVersionUID = 1L;
   protected Model modelData;
   protected I18nManager i18nManager;
   protected VerticalLayout windowLayout;
   protected Form form;
   protected TextField nameTextField;
   protected TextArea descriptionTextArea;
   protected transient RepositoryService repositoryService = ProcessEngines.getDefaultProcessEngine().getRepositoryService();
   
   public CopyModelPopupWindow(Model model) {
     this.modelData = model;
     this.windowLayout = ((VerticalLayout)getContent());
     this.i18nManager = ExplorerApp.get().getI18nManager();
     
     initWindow();
     addFields();
     addButtons();
   }
   
   protected void initWindow() {
     this.windowLayout.setSpacing(true);
     addStyleName("light");
     setModal(true);
     setWidth("400px");
     setHeight("390px");
     center();
     setCaption(this.i18nManager.getMessage("process.copy.popup.caption"));
   }
   
   protected void addFields() {
     this.form = new Form();
     this.form.setCaption(this.i18nManager.getMessage("process.copy.popup.caption"));
     this.form.getLayout().setMargin(true);
     
     this.nameTextField = new TextField(this.i18nManager.getMessage("task.name"));
     this.nameTextField.setWidth(20.0F, 3);
     this.nameTextField.setRequired(true);
     this.nameTextField.setValue(this.modelData.getName());
     this.form.getLayout().addComponent(this.nameTextField);
     this.nameTextField.focus();
     
     this.descriptionTextArea = new TextArea(this.i18nManager.getMessage("task.description"));
     this.descriptionTextArea.setRows(8);
     this.descriptionTextArea.setWidth(20.0F, 3);
     this.form.getLayout().addComponent(this.descriptionTextArea);
     
     addComponent(this.form);
     
 
     Label emptySpace = new Label("&nbsp;", 3);
     addComponent(emptySpace);
   }
   
   protected void addButtons()
   {
     Button cancelButton = new Button(this.i18nManager.getMessage("button.cancel"));
     cancelButton.addStyleName("small");
     cancelButton.addListener(new Button.ClickListener()
     {
       private static final long serialVersionUID = 1L;
       
       public void buttonClick(ClickEvent event) {
         CopyModelPopupWindow.this.close();
       }
       
 
     });
     Button createButton = new Button(this.i18nManager.getMessage("process.new.popup.create.button"));
     createButton.addStyleName("small");
     createButton.addListener(new Button.ClickListener()
     {
       private static final long serialVersionUID = 1L;
       
       public void buttonClick(ClickEvent event)
       {
         if (StringUtils.isEmpty((String)CopyModelPopupWindow.this.nameTextField.getValue())) {
           CopyModelPopupWindow.this.form.setComponentError(new UserError("The name field is required."));
           return;
         }
         
         Model newModelData = CopyModelPopupWindow.this.repositoryService.newModel();
         
         ObjectNode modelObjectNode = new ObjectMapper().createObjectNode();
         modelObjectNode.put("name", (String)CopyModelPopupWindow.this.nameTextField.getValue());
         String description = null;
         if (StringUtils.isNotEmpty((String)CopyModelPopupWindow.this.descriptionTextArea.getValue())) {
           description = (String)CopyModelPopupWindow.this.descriptionTextArea.getValue();
         } else {
           description = "";
         }
         modelObjectNode.put("description", description);
         newModelData.setMetaInfo(modelObjectNode.toString());
         newModelData.setName((String)CopyModelPopupWindow.this.nameTextField.getValue());
         
         CopyModelPopupWindow.this.repositoryService.saveModel(newModelData);
         
         CopyModelPopupWindow.this.repositoryService.addModelEditorSource(newModelData.getId(), CopyModelPopupWindow.this.repositoryService.getModelEditorSource(CopyModelPopupWindow.this.modelData.getId()));
         CopyModelPopupWindow.this.repositoryService.addModelEditorSourceExtra(newModelData.getId(), CopyModelPopupWindow.this.repositoryService.getModelEditorSourceExtra(CopyModelPopupWindow.this.modelData.getId()));
         
         CopyModelPopupWindow.this.close();
         ExplorerApp.get().getViewManager().showEditorProcessDefinitionPage(newModelData.getId());
       }
       
 
     });
     HorizontalLayout buttonLayout = new HorizontalLayout();
     buttonLayout.setSpacing(true);
     buttonLayout.addComponent(cancelButton);
     buttonLayout.addComponent(createButton);
     addComponent(buttonLayout);
     this.windowLayout.setComponentAlignment(buttonLayout, Alignment.BOTTOM_RIGHT);
   }
 }


