 package org.activiti.editor.ui;
 
 import com.fasterxml.jackson.databind.ObjectMapper;
 import com.fasterxml.jackson.databind.node.ObjectNode;
 import com.vaadin.terminal.ExternalResource;
 import com.vaadin.ui.Alignment;
 import com.vaadin.ui.Button;
 import com.vaadin.ui.Button.ClickEvent;
 import com.vaadin.ui.Button.ClickListener;
 import com.vaadin.ui.HorizontalLayout;
 import com.vaadin.ui.Label;
 import com.vaadin.ui.VerticalLayout;
 import com.vaadin.ui.Window;
 import java.io.InputStream;
 import java.io.InputStreamReader;
 import java.net.URL;
 import java.util.Map;
 import javax.xml.stream.XMLInputFactory;
 import javax.xml.stream.XMLStreamReader;
 import org.activiti.bpmn.converter.BpmnXMLConverter;
 import org.activiti.bpmn.model.BpmnModel;
 import org.activiti.bpmn.model.Process;
 import org.activiti.editor.constants.ModelDataJsonConstants;
 import org.activiti.editor.language.json.converter.BpmnJsonConverter;
 import org.activiti.engine.ProcessEngine;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.engine.RepositoryService;
 import org.activiti.engine.RuntimeService;
 import org.activiti.engine.repository.Model;
 import org.activiti.engine.repository.ProcessDefinition;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.NotificationManager;
 import org.activiti.explorer.ViewManager;
 import org.activiti.explorer.ui.custom.PopupWindow;
 import org.activiti.explorer.util.XmlUtil;
 import org.apache.commons.lang3.StringUtils;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class ConvertProcessDefinitionPopupWindow
   extends PopupWindow
   implements ModelDataJsonConstants
 {
   private static final long serialVersionUID = 1L;
   protected transient RepositoryService repositoryService = ProcessEngines.getDefaultProcessEngine().getRepositoryService();
   protected transient RuntimeService runtimeService = ProcessEngines.getDefaultProcessEngine().getRuntimeService();
   protected I18nManager i18nManager;
   protected NotificationManager notificationManager;
   protected VerticalLayout windowLayout;
   protected ProcessDefinition processDefinition;
   
   public ConvertProcessDefinitionPopupWindow(ProcessDefinition processDefinition)
   {
     this.processDefinition = processDefinition;
     this.windowLayout = ((VerticalLayout)getContent());
     this.i18nManager = ExplorerApp.get().getI18nManager();
     this.notificationManager = ExplorerApp.get().getNotificationManager();
     
     initWindow();
     addConvertWarning();
     addButtons();
   }
   
   protected void initWindow() {
     this.windowLayout.setSpacing(true);
     addStyleName("light");
     setModal(true);
     center();
     
     String name = this.processDefinition.getName();
     if (StringUtils.isEmpty(name)) {
       name = this.processDefinition.getKey();
     }
     setCaption(this.i18nManager.getMessage("process.convert.popup.caption", new Object[] { name }));
   }
   
   protected void addConvertWarning() {
     Label convertLabel = new Label(this.i18nManager.getMessage("process.convert.popup.message"));
     convertLabel.addStyleName("light");
     addComponent(convertLabel);
     
 
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
         ConvertProcessDefinitionPopupWindow.this.close();
       }
       
 
     });
     Button convertButton = new Button(this.i18nManager.getMessage("process.convert.popup.convert.button"));
     convertButton.addStyleName("small");
     convertButton.addListener(new Button.ClickListener()
     {
       private static final long serialVersionUID = 1L;
       
       public void buttonClick(ClickEvent event)
       {
         try {
           InputStream bpmnStream = ConvertProcessDefinitionPopupWindow.this.repositoryService.getResourceAsStream(ConvertProcessDefinitionPopupWindow.this.processDefinition.getDeploymentId(), ConvertProcessDefinitionPopupWindow.this.processDefinition.getResourceName());
           XMLInputFactory xif = XmlUtil.createSafeXmlInputFactory();
           InputStreamReader in = new InputStreamReader(bpmnStream, "UTF-8");
           XMLStreamReader xtr = xif.createXMLStreamReader(in);
           BpmnModel bpmnModel = new BpmnXMLConverter().convertToBpmnModel(xtr);
           
           if ((bpmnModel.getMainProcess() == null) || (bpmnModel.getMainProcess().getId() == null)) {
             ConvertProcessDefinitionPopupWindow.this.notificationManager.showErrorNotification("model.import.failed", ConvertProcessDefinitionPopupWindow.this.i18nManager
               .getMessage("model.import.invalid.bpmn.explanation"));
 
           }
           else if (bpmnModel.getLocationMap().isEmpty()) {
             ConvertProcessDefinitionPopupWindow.this.notificationManager.showErrorNotification("model.import.invalid.bpmndi", ConvertProcessDefinitionPopupWindow.this.i18nManager
               .getMessage("model.import.invalid.bpmndi.explanation"));
           }
           else {
             BpmnJsonConverter converter = new BpmnJsonConverter();
             ObjectNode modelNode = converter.convertToJson(bpmnModel);
             Model modelData = ConvertProcessDefinitionPopupWindow.this.repositoryService.newModel();
             
             ObjectNode modelObjectNode = new ObjectMapper().createObjectNode();
             modelObjectNode.put("name", ConvertProcessDefinitionPopupWindow.this.processDefinition.getName());
             modelObjectNode.put("revision", 1);
             modelObjectNode.put("description", ConvertProcessDefinitionPopupWindow.this.processDefinition.getDescription());
             modelData.setMetaInfo(modelObjectNode.toString());
             modelData.setName(ConvertProcessDefinitionPopupWindow.this.processDefinition.getName());
             
             ConvertProcessDefinitionPopupWindow.this.repositoryService.saveModel(modelData);
             
             ConvertProcessDefinitionPopupWindow.this.repositoryService.addModelEditorSource(modelData.getId(), modelNode.toString().getBytes("utf-8"));
             
             ConvertProcessDefinitionPopupWindow.this.close();
             ExplorerApp.get().getViewManager().showEditorProcessDefinitionPage(modelData.getId());
             
             URL explorerURL = ExplorerApp.get().getURL();
             
             URL url = new URL(explorerURL.getProtocol(), explorerURL.getHost(), explorerURL.getPort(), explorerURL.getPath().replace("/ui", "") + "modeler.html?modelId=" + modelData.getId());
             ExplorerApp.get().getMainWindow().open(new ExternalResource(url));
           }
         }
         catch (Exception e)
         {
           ConvertProcessDefinitionPopupWindow.this.notificationManager.showErrorNotification("error", e);
         }
         
       }
       
     });
     HorizontalLayout buttonLayout = new HorizontalLayout();
     buttonLayout.setSpacing(true);
     buttonLayout.addComponent(cancelButton);
     buttonLayout.addComponent(convertButton);
     addComponent(buttonLayout);
     this.windowLayout.setComponentAlignment(buttonLayout, Alignment.BOTTOM_RIGHT);
   }
 }


