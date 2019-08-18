 package org.activiti.editor.ui;
 
 import com.fasterxml.jackson.databind.ObjectMapper;
 import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.FinishedEvent;
 import com.vaadin.ui.Upload.FinishedListener;
 import com.vaadin.ui.Upload.Receiver;
 import java.io.ByteArrayInputStream;
 import java.io.ByteArrayOutputStream;
 import java.io.IOException;
 import java.io.InputStreamReader;
 import java.io.OutputStream;
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
 import org.activiti.engine.repository.Model;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.NotificationManager;
 import org.activiti.explorer.ViewManager;
 import org.activiti.explorer.util.XmlUtil;
 import org.apache.commons.lang3.StringUtils;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class ImportUploadReceiver
   implements Upload.Receiver, Upload.FinishedListener, ModelDataJsonConstants
 {
   private static final long serialVersionUID = 1L;
   protected transient RepositoryService repositoryService;
   protected I18nManager i18nManager;
   protected NotificationManager notificationManager;
   protected ViewManager viewManager;
   protected ByteArrayOutputStream outputStream;
   protected String fileName;
   protected boolean validFile = false;
   protected Model modelData;
   
   public ImportUploadReceiver() {
     this.repositoryService = ProcessEngines.getDefaultProcessEngine().getRepositoryService();
     this.i18nManager = ExplorerApp.get().getI18nManager();
     this.notificationManager = ExplorerApp.get().getNotificationManager();
     this.viewManager = ExplorerApp.get().getViewManager();
   }
   
   public OutputStream receiveUpload(String filename, String mimeType) {
     this.fileName = filename;
     this.outputStream = new ByteArrayOutputStream();
     return this.outputStream;
   }
   
   public void uploadFinished(Upload.FinishedEvent event) {
     deployUploadedFile();
     if (this.validFile) {
       showUploadedDeployment();
     }
   }
   
   protected void deployUploadedFile() {
     try {
       try {
         if ((this.fileName.endsWith(".bpmn20.xml")) || (this.fileName.endsWith(".bpmn"))) {
           this.validFile = true;
           
           XMLInputFactory xif = XmlUtil.createSafeXmlInputFactory();
           InputStreamReader in = new InputStreamReader(new ByteArrayInputStream(this.outputStream.toByteArray()), "UTF-8");
           XMLStreamReader xtr = xif.createXMLStreamReader(in);
           BpmnModel bpmnModel = new BpmnXMLConverter().convertToBpmnModel(xtr);
           
           if ((bpmnModel.getMainProcess() == null) || (bpmnModel.getMainProcess().getId() == null)) {
             this.notificationManager.showErrorNotification("model.import.failed", this.i18nManager
               .getMessage("model.import.invalid.bpmn.explanation"));
 
           }
           else if (bpmnModel.getLocationMap().isEmpty()) {
             this.notificationManager.showErrorNotification("model.import.invalid.bpmndi", this.i18nManager
               .getMessage("model.import.invalid.bpmndi.explanation"));
           }
           else {
             String processName = null;
             if (StringUtils.isNotEmpty(bpmnModel.getMainProcess().getName())) {
               processName = bpmnModel.getMainProcess().getName();
             } else {
               processName = bpmnModel.getMainProcess().getId();
             }
             
             this.modelData = this.repositoryService.newModel();
             ObjectNode modelObjectNode = new ObjectMapper().createObjectNode();
             modelObjectNode.put("name", processName);
             modelObjectNode.put("revision", 1);
             this.modelData.setMetaInfo(modelObjectNode.toString());
             this.modelData.setName(processName);
             
             this.repositoryService.saveModel(this.modelData);
             
             BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
             ObjectNode editorNode = jsonConverter.convertToJson(bpmnModel);
             
             this.repositoryService.addModelEditorSource(this.modelData.getId(), editorNode.toString().getBytes("utf-8"));
           }
         }
         else {
           this.notificationManager.showErrorNotification("model.import.invalid.file", this.i18nManager
             .getMessage("model.import.invalid.file.explanation"));
         }
       } catch (Exception e) {
         String errorMsg = e.getMessage().replace(System.getProperty("line.separator"), "<br/>");
         this.notificationManager.showErrorNotification("model.import.failed", errorMsg);
       }
       return;
     } finally { if (this.outputStream != null) {
         try {
           this.outputStream.close();
         } catch (IOException e) {
           this.notificationManager.showErrorNotification("Server-side error", e.getMessage());
         }
       }
     }
   }
   
   protected void showUploadedDeployment() {
     this.viewManager.showEditorProcessDefinitionPage(this.modelData.getId());
   }
 }


