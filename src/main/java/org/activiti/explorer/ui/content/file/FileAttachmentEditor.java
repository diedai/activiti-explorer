 package org.activiti.explorer.ui.content.file;
 
 import com.vaadin.terminal.Resource;
 import org.activiti.engine.task.Attachment;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.ui.Images;
 import org.activiti.explorer.ui.content.AttachmentEditor;
 import org.activiti.explorer.ui.content.AttachmentEditorComponent;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class FileAttachmentEditor
   implements AttachmentEditor
 {
   public static final String FILE_ATTACHMENT_TYPE = "file";
   
   public String getName()
   {
     return "file";
   }
   
   public String getTitle(I18nManager i18nManager) {
     return i18nManager.getMessage("related.content.type.file");
   }
   
   public Resource getImage() {
     return Images.RELATED_CONTENT_FILE;
   }
   
   public AttachmentEditorComponent getEditor(Attachment attachment, String taskId, String processInstanceId) {
     return new FileAttachmentEditorComponent(attachment, taskId, processInstanceId);
   }
 }


