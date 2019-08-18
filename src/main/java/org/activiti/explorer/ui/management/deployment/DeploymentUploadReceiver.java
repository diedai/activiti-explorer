 package org.activiti.explorer.ui.management.deployment;
 
 import com.vaadin.ui.Upload;
 import com.vaadin.ui.Upload.FinishedEvent;
 import com.vaadin.ui.Upload.FinishedListener;
 import com.vaadin.ui.Upload.Receiver;
 import java.io.ByteArrayInputStream;
 import java.io.ByteArrayOutputStream;
 import java.io.IOException;
 import java.io.OutputStream;
 import java.util.zip.ZipInputStream;
 import org.activiti.engine.ActivitiException;
 import org.activiti.engine.ProcessEngine;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.engine.RepositoryService;
 import org.activiti.engine.repository.Deployment;
 import org.activiti.engine.repository.DeploymentBuilder;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.NotificationManager;
 import org.activiti.explorer.ViewManager;
 import org.activiti.explorer.ui.ComponentFactory;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class DeploymentUploadReceiver
   implements Upload.Receiver, Upload.FinishedListener
 {
   private static final long serialVersionUID = 1L;
   protected transient RepositoryService repositoryService;
   protected I18nManager i18nManager;
   protected NotificationManager notificationManager;
   protected ViewManager viewManager;
   protected ByteArrayOutputStream outputStream;
   protected String fileName;
   protected boolean validFile = false;
   protected Deployment deployment;
   
   public DeploymentUploadReceiver() {
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
     DeploymentBuilder deploymentBuilder = this.repositoryService.createDeployment().name(this.fileName);
     DeploymentFilter deploymentFilter = (DeploymentFilter)ExplorerApp.get().getComponentFactory(DeploymentFilterFactory.class).create();
     try {
       try {
         if ((this.fileName.endsWith(".bpmn20.xml")) || (this.fileName.endsWith(".bpmn"))) {
           this.validFile = true;
           deploymentBuilder.addInputStream(this.fileName, new ByteArrayInputStream(this.outputStream.toByteArray()));
         } else if ((this.fileName.endsWith(".bar")) || (this.fileName.endsWith(".zip"))) {
           this.validFile = true;
           deploymentBuilder.addZipInputStream(new ZipInputStream(new ByteArrayInputStream(this.outputStream.toByteArray())));
         } else {
           this.notificationManager.showErrorNotification("deployment.upload.invalid.file", this.i18nManager
             .getMessage("deployment.upload.invalid.file.explanation"));
         }
         
 
         if (this.validFile) {
           deploymentFilter.beforeDeploy(deploymentBuilder);
           this.deployment = deploymentBuilder.deploy();
         }
       } catch (ActivitiException e) {
         String errorMsg = e.getMessage().replace(System.getProperty("line.separator"), "<br/>");
         this.notificationManager.showErrorNotification("deployment.upload.failed", errorMsg);
         throw e;
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
     this.viewManager.showDeploymentPage(this.deployment.getId());
   }
 }


