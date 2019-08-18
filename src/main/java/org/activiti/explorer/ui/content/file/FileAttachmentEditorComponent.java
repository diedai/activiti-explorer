 package org.activiti.explorer.ui.content.file;
 
 import com.vaadin.data.Validator;
import com.vaadin.data.Validator.InvalidValueException;
 import com.vaadin.ui.Field;
 import com.vaadin.ui.Form;
 import com.vaadin.ui.Label;
 import com.vaadin.ui.TextArea;
 import com.vaadin.ui.TextField;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.FinishedEvent;
 import com.vaadin.ui.Upload.FinishedListener;
 import com.vaadin.ui.Upload.Receiver;
 import com.vaadin.ui.VerticalLayout;
 import java.io.ByteArrayInputStream;
 import java.io.ByteArrayOutputStream;
 import java.io.OutputStream;
 import org.activiti.engine.ProcessEngine;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.engine.TaskService;
 import org.activiti.engine.task.Attachment;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.ui.ComponentFactory;
 import org.activiti.explorer.ui.Images;
 import org.activiti.explorer.ui.content.AttachmentEditorComponent;
 import org.activiti.explorer.ui.custom.UploadComponent;
 import org.activiti.explorer.ui.custom.UploadComponentFactory;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class FileAttachmentEditorComponent
   extends VerticalLayout
   implements AttachmentEditorComponent
 {
   private static final long serialVersionUID = 1L;
   public static final String MIME_TYPE_EXTENTION_SPLIT_CHAR = ";";
   protected Attachment attachment;
   protected String taskId;
   protected String processInstanceId;
   protected String fileName;
   protected ByteArrayOutputStream byteArrayOutputStream;
   protected String mimeType;
   protected boolean fileUploaded = false;
   
   protected I18nManager i18nManager;
   protected transient TaskService taskService;
   protected Form form;
   protected UploadComponent uploadComponent;
   protected Label successIndicator;
   
   public FileAttachmentEditorComponent(String taskId, String processInstanceId)
   {
     this(null, taskId, processInstanceId);
   }
   
   public FileAttachmentEditorComponent(Attachment attachment, String taskId, String processInstanceId) {
     this.attachment = attachment;
     this.taskId = taskId;
     this.processInstanceId = processInstanceId;
     
     this.i18nManager = ExplorerApp.get().getI18nManager();
     this.taskService = ProcessEngines.getDefaultProcessEngine().getTaskService();
     
     this.form = new Form();
     this.form.setDescription(this.i18nManager.getMessage("related.content.type.file.help"));
     setSizeFull();
     addComponent(this.form);
     initSuccessIndicator();
     initFileUpload();
     initName();
     initDescription();
   }
   
   protected void initSuccessIndicator() {
     this.successIndicator = new Label();
     this.successIndicator.setIcon(Images.SUCCESS);
     this.successIndicator.setVisible(false);
     
     addComponent(this.successIndicator);
   }
   
   protected void initFileUpload() {
     this.uploadComponent = ((UploadComponent)ExplorerApp.get().getComponentFactory(UploadComponentFactory.class).create());
     
     Upload.Receiver receiver = new Upload.Receiver() {
       private static final long serialVersionUID = 1L;
       
       public OutputStream receiveUpload(String filename, String mType) {
         FileAttachmentEditorComponent.this.fileName = filename;
         
 
         String extention = FileAttachmentEditorComponent.this.extractExtention(filename);
         if (extention != null) {
           FileAttachmentEditorComponent.this.mimeType = (mType + ";" + extention);
         } else {
           FileAttachmentEditorComponent.this.mimeType = mType;
         }
         
 
         FileAttachmentEditorComponent.this.byteArrayOutputStream = new ByteArrayOutputStream();
         return FileAttachmentEditorComponent.this.byteArrayOutputStream;
       }
       
     };
     this.uploadComponent.setReceiver(receiver);
     this.uploadComponent.addFinishedListener(new Upload.FinishedListener()
     {
       private static final long serialVersionUID = 1L;
       
       public void uploadFinished(Upload.FinishedEvent event)
       {
         if ((FileAttachmentEditorComponent.this.getAttachmentName() == null) || ("".equals(FileAttachmentEditorComponent.this.getAttachmentName()))) {
           FileAttachmentEditorComponent.this.setAttachmentName(FileAttachmentEditorComponent.this.getFriendlyName(FileAttachmentEditorComponent.this.fileName));
         }
         
         FileAttachmentEditorComponent.this.fileUploaded = true;
         FileAttachmentEditorComponent.this.successIndicator.setVisible(true);
         FileAttachmentEditorComponent.this.successIndicator.setCaption(FileAttachmentEditorComponent.this.i18nManager.getMessage("related.content.type.file.uploaded", new Object[] { FileAttachmentEditorComponent.this.fileName }));
         FileAttachmentEditorComponent.this.form.setComponentError(null);
       }
       
     });
     addComponent(this.uploadComponent);
     setExpandRatio(this.uploadComponent, 1.0F);
   }
   
   protected String extractExtention(String fileName) {
     int lastIndex = fileName.lastIndexOf('.');
     if ((lastIndex > 0) && (lastIndex < fileName.length() - 1)) {
       return fileName.substring(lastIndex + 1);
     }
     return null;
   }
   
   protected String getFriendlyName(String name) {
     if (name != null) {
       String friendlyName = null;
       int lastDotIndex = name.lastIndexOf(".");
       if (lastDotIndex > 0) {
         friendlyName = name.substring(0, name.length() - (name.length() - lastDotIndex));
       } else {
         friendlyName = name;
       }
       return friendlyName.replace("_", " ").replace("-", " ");
     }
     return name;
   }
   
   protected void initDescription() {
     TextArea descriptionField = new TextArea(this.i18nManager.getMessage("related.content.description"));
     descriptionField.setWidth(100.0F, 8);
     descriptionField.setHeight(50.0F, 0);
     this.form.addField("description", descriptionField);
   }
   
   protected void initName() {
     TextField nameField = new TextField(this.i18nManager.getMessage("related.content.name"));
     nameField.focus();
     nameField.setRequired(true);
     nameField.setRequiredError(this.i18nManager.getMessage("related.content.name.required"));
     nameField.setWidth(100.0F, 8);
     this.form.addField("name", nameField);
   }
   
   public Attachment getAttachment() throws Validator.InvalidValueException
   {
     this.form.commit();
     
 
     if (!this.fileUploaded) {
       Validator.InvalidValueException ive = new Validator.InvalidValueException(this.i18nManager.getMessage("related.content.type.file.required"));
       this.form.setComponentError(ive);
       throw ive;
     }
     
     if (this.attachment != null) {
       applyValuesToAttachment();
     }
     else
     {
       this.attachment = this.taskService.createAttachment(this.mimeType, this.taskId, this.processInstanceId, 
         getAttachmentName(), getAttachmentDescription(), new ByteArrayInputStream(this.byteArrayOutputStream.toByteArray()));
     }
     return this.attachment;
   }
   
   protected void setAttachmentName(String name) {
     this.form.getField("name").setValue(name);
   }
   
   protected String getAttachmentName() {
     return (String)this.form.getField("name").getValue();
   }
   
   protected String getAttachmentDescription() {
     return (String)this.form.getField("description").getValue();
   }
   
   private void applyValuesToAttachment() {
     this.attachment.setName(getAttachmentName());
     this.attachment.setDescription(getAttachmentDescription());
   }
 }


