 package org.activiti.explorer.ui.process.listener;
 
 import com.fasterxml.jackson.core.JsonProcessingException;
 import com.fasterxml.jackson.databind.ObjectMapper;
 import com.fasterxml.jackson.databind.node.ObjectNode;
 import com.vaadin.terminal.ExternalResource;
 import com.vaadin.ui.Button.ClickEvent;
 import com.vaadin.ui.Button.ClickListener;
 import com.vaadin.ui.ComponentContainer;
 import com.vaadin.ui.Label;
 import com.vaadin.ui.Window;
 import java.io.IOException;
 import java.net.MalformedURLException;
 import java.net.URL;
 import org.activiti.editor.language.json.converter.BpmnJsonConverter;
 import org.activiti.editor.ui.SelectEditorComponent;
 import org.activiti.editor.ui.SelectEditorComponent.EditorSelectedListener;
 import org.activiti.engine.ProcessEngine;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.engine.RepositoryService;
 import org.activiti.engine.repository.Model;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.NotificationManager;
 import org.activiti.explorer.ViewManager;
 import org.activiti.explorer.ui.custom.PopupWindow;
 import org.activiti.workflow.simple.converter.WorkflowDefinitionConversion;
 import org.activiti.workflow.simple.converter.WorkflowDefinitionConversionFactory;
 import org.activiti.workflow.simple.converter.json.SimpleWorkflowJsonConverter;
 import org.activiti.workflow.simple.definition.WorkflowDefinition;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 
 
 
 
 public class EditModelClickListener
   implements ClickListener
 {
   private static final long serialVersionUID = 1L;
   protected static final Logger LOGGER = LoggerFactory.getLogger(EditModelClickListener.class);
   protected Model model;
   protected NotificationManager notificationManager;
   
   public EditModelClickListener(Model model)
   {
     this.notificationManager = ExplorerApp.get().getNotificationManager();
     this.model = model;
   }
   
   public void buttonClick(ClickEvent event) {
     if ("table-editor".equals(this.model.getCategory())) {
       showSelectEditorPopupWindow();
     } else {
       try {
         showModeler();
       } catch (MalformedURLException e) {
         LOGGER.error("Error showing modeler", e);
         ExplorerApp.get().getNotificationManager().showErrorNotification("process.editor.loading.error", e);
       }
     }
   }
   
   protected WorkflowDefinition loadWorkflowDefinition() throws JsonProcessingException, IOException {
     RepositoryService repositoryService = ProcessEngines.getDefaultProcessEngine().getRepositoryService();
     return ExplorerApp.get().getSimpleWorkflowJsonConverter().readWorkflowDefinition(repositoryService.getModelEditorSource(this.model.getId()));
   }
   
   protected void showSelectEditorPopupWindow() {
     final PopupWindow selectEditorPopupWindow = new PopupWindow();
     selectEditorPopupWindow.setModal(true);
     selectEditorPopupWindow.setResizable(false);
     selectEditorPopupWindow.setWidth("350px");
     selectEditorPopupWindow.setHeight("250px");
     selectEditorPopupWindow.addStyleName("light");
     selectEditorPopupWindow.center();
     
     final SelectEditorComponent selectEditorComponent = new SelectEditorComponent(false);
     selectEditorComponent.getModelerDescriptionLabel().setValue(
       ExplorerApp.get().getI18nManager().getMessage("process.editor.conversion.warning.modeler"));
     selectEditorComponent.getModelerDescriptionLabel().addStyleName("red");
     selectEditorComponent.preferTableDrivenEditor();
     selectEditorPopupWindow.getContent().addComponent(selectEditorComponent);
     
     selectEditorComponent.setEditorSelectedListener(new SelectEditorComponent.EditorSelectedListener()
     {
       public void editorSelectionChanged() {
         try {
           WorkflowDefinition workflowDefinition = EditModelClickListener.this.loadWorkflowDefinition();
           
 
           if (selectEditorComponent.isModelerPreferred())
           {
 
             WorkflowDefinitionConversion conversion = ExplorerApp.get().getWorkflowDefinitionConversionFactory().createWorkflowDefinitionConversion(workflowDefinition);
             conversion.convert();
             
             RepositoryService repositoryService = ProcessEngines.getDefaultProcessEngine().getRepositoryService();
             EditModelClickListener.this.model.setCategory(null);
             
             ObjectMapper objectMapper = new ObjectMapper();
             ObjectNode metaInfoJson = objectMapper.createObjectNode();
             metaInfoJson.put("name", EditModelClickListener.this.model.getName());
             EditModelClickListener.this.model.setMetaInfo(metaInfoJson.toString());
             repositoryService.saveModel(EditModelClickListener.this.model);
             
             BpmnJsonConverter bpmnJsonConverter = new BpmnJsonConverter();
             ObjectNode json = bpmnJsonConverter.convertToJson(conversion.getBpmnModel());
             repositoryService.addModelEditorSource(EditModelClickListener.this.model.getId(), json.toString().getBytes("utf-8"));
             
 
             EditModelClickListener.this.showModeler();
 
           }
           else
           {
             ExplorerApp.get().getViewManager().showSimpleTableProcessEditor(EditModelClickListener.this.model.getId(), workflowDefinition);
           }
         }
         catch (Exception e) {
           EditModelClickListener.LOGGER.error("Error showing editor", e);
           ExplorerApp.get().getNotificationManager().showErrorNotification("process.editor.loading.error", e);
         } finally {
           ExplorerApp.get().getMainWindow().removeWindow(selectEditorPopupWindow);
         }
         
       }
       
     });
     ExplorerApp.get().getViewManager().showPopupWindow(selectEditorPopupWindow);
   }
   
   protected void showModeler() throws MalformedURLException {
     URL explorerURL = ExplorerApp.get().getURL();
     
     URL url = new URL(explorerURL.getProtocol(), explorerURL.getHost(), explorerURL.getPort(), explorerURL.getPath().replace("/ui", "") + "modeler.html?modelId=" + this.model.getId());
     ExplorerApp.get().getMainWindow().open(new ExternalResource(url));
   }
 }


