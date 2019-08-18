 package org.activiti.editor.ui;
 
 import com.fasterxml.jackson.databind.ObjectMapper;
 import com.fasterxml.jackson.databind.node.ObjectNode;
 import com.vaadin.terminal.ExternalResource;
 import com.vaadin.terminal.UserError;
 import com.vaadin.ui.Alignment;
 import com.vaadin.ui.Button;
 import com.vaadin.ui.Button.ClickEvent;
 import com.vaadin.ui.Button.ClickListener;
 import com.vaadin.ui.GridLayout;
 import com.vaadin.ui.HorizontalLayout;
 import com.vaadin.ui.Label;
 import com.vaadin.ui.TextArea;
 import com.vaadin.ui.TextField;
 import com.vaadin.ui.VerticalLayout;
 import com.vaadin.ui.Window;
 import java.net.URL;
 import org.activiti.editor.constants.ModelDataJsonConstants;
 import org.activiti.engine.ProcessEngine;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.engine.RepositoryService;
 import org.activiti.engine.repository.Model;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.NotificationManager;
 import org.activiti.explorer.ViewManager;
 import org.activiti.explorer.ui.custom.PopupWindow;
 import org.apache.commons.lang3.StringUtils;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class NewModelPopupWindow
   extends PopupWindow
   implements ModelDataJsonConstants
 {
   private static final long serialVersionUID = 1L;
   protected I18nManager i18nManager;
   protected NotificationManager notificationManager;
   protected VerticalLayout windowLayout;
   protected GridLayout formLayout;
   protected TextField nameTextField;
   protected TextArea descriptionTextArea;
   protected SelectEditorComponent selectEditorComponent;
   protected transient RepositoryService repositoryService = ProcessEngines.getDefaultProcessEngine().getRepositoryService();
   
   public NewModelPopupWindow() {
     this.windowLayout = ((VerticalLayout)getContent());
     this.i18nManager = ExplorerApp.get().getI18nManager();
     this.notificationManager = ExplorerApp.get().getNotificationManager();
     
     initWindow();
     addFields();
     addButtons();
   }
   
   protected void initWindow() {
     this.windowLayout.setSpacing(true);
     addStyleName("light");
     setModal(true);
     setWidth("460px");
     setHeight("470px");
     center();
     setCaption(this.i18nManager.getMessage("process.new.popup.caption"));
   }
   
   protected void addFields() {
     this.formLayout = new GridLayout(2, 3);
     this.formLayout.setSpacing(true);
     
     this.formLayout.addComponent(new Label(this.i18nManager.getMessage("task.name")));
     this.nameTextField = new TextField();
     this.nameTextField.setWidth(25.0F, 3);
     this.nameTextField.focus();
     this.formLayout.addComponent(this.nameTextField);
     
     this.formLayout.addComponent(new Label(this.i18nManager.getMessage("task.description")));
     this.descriptionTextArea = new TextArea();
     this.descriptionTextArea.setRows(8);
     this.descriptionTextArea.setWidth(25.0F, 3);
     this.descriptionTextArea.addStyleName("noResizeTextArea");
     this.formLayout.addComponent(this.descriptionTextArea);
     
     Label editorLabel = new Label(this.i18nManager.getMessage("process.editor.choice"));
     this.formLayout.addComponent(editorLabel);
     this.formLayout.setComponentAlignment(editorLabel, Alignment.MIDDLE_LEFT);
     
     this.selectEditorComponent = new SelectEditorComponent();
     this.formLayout.addComponent(this.selectEditorComponent);
     
     addComponent(this.formLayout);
     
 
     Label emptySpace = new Label("&nbsp;", 3);
     addComponent(emptySpace);
   }
   
 
   protected void addButtons()
   {
     Button createButton = new Button(this.i18nManager.getMessage("process.new.popup.create.button"));
     createButton.setWidth("200px");
     createButton.addListener(new Button.ClickListener()
     {
       private static final long serialVersionUID = 1L;
       
       public void buttonClick(ClickEvent event)
       {
         if (StringUtils.isEmpty((String)NewModelPopupWindow.this.nameTextField.getValue())) {
           NewModelPopupWindow.this.nameTextField.setComponentError(new UserError("The name field is required."));
           return;
         }
         
         if (NewModelPopupWindow.this.selectEditorComponent.isModelerPreferred()) {
           try {
             ObjectMapper objectMapper = new ObjectMapper();
             ObjectNode editorNode = objectMapper.createObjectNode();
             editorNode.put("id", "canvas");
             editorNode.put("resourceId", "canvas");
             ObjectNode stencilSetNode = objectMapper.createObjectNode();
             stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
             editorNode.put("stencilset", stencilSetNode);
             Model modelData = NewModelPopupWindow.this.repositoryService.newModel();
             
             ObjectNode modelObjectNode = objectMapper.createObjectNode();
             modelObjectNode.put("name", (String)NewModelPopupWindow.this.nameTextField.getValue());
             modelObjectNode.put("revision", 1);
             String description = null;
             if (StringUtils.isNotEmpty((String)NewModelPopupWindow.this.descriptionTextArea.getValue())) {
               description = (String)NewModelPopupWindow.this.descriptionTextArea.getValue();
             } else {
               description = "";
             }
             modelObjectNode.put("description", description);
             modelData.setMetaInfo(modelObjectNode.toString());
             modelData.setName((String)NewModelPopupWindow.this.nameTextField.getValue());
             
             NewModelPopupWindow.this.repositoryService.saveModel(modelData);
             NewModelPopupWindow.this.repositoryService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes("utf-8"));
             
             NewModelPopupWindow.this.close();
             
             ExplorerApp.get().getViewManager().showEditorProcessDefinitionPage(modelData.getId());
             URL explorerURL = ExplorerApp.get().getURL();
             
             URL url = new URL(explorerURL.getProtocol(), explorerURL.getHost(), explorerURL.getPort(), explorerURL.getPath().replace("/ui", "") + "modeler.html?modelId=" + modelData.getId());
             ExplorerApp.get().getMainWindow().open(new ExternalResource(url));
           }
           catch (Exception e) {
             NewModelPopupWindow.this.notificationManager.showErrorNotification("error", e);
           }
         }
         else {
           NewModelPopupWindow.this.close();
           ExplorerApp.get().getViewManager().showSimpleTableProcessEditor(
             (String)NewModelPopupWindow.this.nameTextField.getValue(), (String)NewModelPopupWindow.this.descriptionTextArea.getValue());
         }
         
       }
       
 
     });
     HorizontalLayout buttonLayout = new HorizontalLayout();
     buttonLayout.setSpacing(true);
     buttonLayout.addComponent(createButton);
     addComponent(buttonLayout);
     this.windowLayout.setComponentAlignment(buttonLayout, Alignment.MIDDLE_CENTER);
   }
 }


