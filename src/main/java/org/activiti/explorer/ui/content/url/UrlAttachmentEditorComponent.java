 package org.activiti.explorer.ui.content.url;
 
 import com.vaadin.data.Validator.InvalidValueException;
 import com.vaadin.ui.Field;
 import com.vaadin.ui.Form;
 import com.vaadin.ui.TextArea;
 import com.vaadin.ui.TextField;
 import org.activiti.engine.ProcessEngine;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.engine.TaskService;
 import org.activiti.engine.task.Attachment;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.ui.content.AttachmentEditorComponent;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class UrlAttachmentEditorComponent
   extends Form
   implements AttachmentEditorComponent
 {
   private static final long serialVersionUID = 1L;
   protected Attachment attachment;
   protected String taskId;
   protected String processInstanceId;
   protected I18nManager i18nManager;
   protected transient TaskService taskService;
   
   public UrlAttachmentEditorComponent(String taskId, String processInstanceId)
   {
     this(null, taskId, processInstanceId);
   }
   
   public UrlAttachmentEditorComponent(Attachment attachment, String taskId, String processInstanceId) {
     this.attachment = attachment;
     this.taskId = taskId;
     this.processInstanceId = processInstanceId;
     
     this.i18nManager = ExplorerApp.get().getI18nManager();
     this.taskService = ProcessEngines.getDefaultProcessEngine().getTaskService();
     
     setSizeFull();
     setDescription(this.i18nManager.getMessage("related.content.type.url.help"));
     
     initUrl();
     initName();
     initDescription();
   }
   
   protected void initUrl() {
     TextField urlField = new TextField(this.i18nManager.getMessage("related.content.type.url.url"));
     urlField.focus();
     urlField.setRequired(true);
     urlField.setRequiredError(this.i18nManager.getMessage("related.content.type.url.url.required"));
     urlField.setWidth(100.0F, 8);
     
     if (this.attachment != null) {
       urlField.setEnabled(false);
     }
     
     addField("url", urlField);
   }
   
   protected void initDescription() {
     TextArea descriptionField = new TextArea(this.i18nManager.getMessage("related.content.description"));
     descriptionField.setWidth(100.0F, 8);
     descriptionField.setHeight(100.0F, 0);
     addField("description", descriptionField);
   }
   
   protected void initName() {
     TextField nameField = new TextField(this.i18nManager.getMessage("related.content.name"));
     nameField.setWidth(100.0F, 8);
     addField("name", nameField);
   }
   
   public Attachment getAttachment() throws InvalidValueException
   {
     commit();
     if (this.attachment != null) {
       applyValuesToAttachment();
     }
     else
     {
       this.attachment = this.taskService.createAttachment("url", this.taskId, this.processInstanceId, 
         getAttachmentName(), getAttachmentDescription(), getAttachmentUrl());
     }
     return this.attachment;
   }
   
   protected String getAttachmentUrl() {
     return getFieldValue("url");
   }
   
   protected String getAttachmentName() {
     String name = getFieldValue("name");
     if (name == null) {
       name = getAttachmentUrl();
     }
     return name;
   }
   
   protected String getAttachmentDescription() {
     return getFieldValue("description");
   }
   
   protected String getFieldValue(String key) {
     String value = (String)getField(key).getValue();
     if ("".equals(value)) {
       return null;
     }
     return value;
   }
   
   private void applyValuesToAttachment() {
     this.attachment.setName(getAttachmentName());
     this.attachment.setDescription(getAttachmentDescription());
   }
 }


