 package org.activiti.explorer.ui.content.url;
 
 import com.vaadin.terminal.Resource;
 import org.activiti.engine.task.Attachment;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.ui.Images;
 import org.activiti.explorer.ui.content.AttachmentEditor;
 import org.activiti.explorer.ui.content.AttachmentEditorComponent;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class UrlAttachmentEditor
   implements AttachmentEditor
 {
   public String getName()
   {
     return "url";
   }
   
   public String getTitle(I18nManager i18nManager) {
     return i18nManager.getMessage("related.content.type.url");
   }
   
   public Resource getImage() {
     return Images.RELATED_CONTENT_URL;
   }
   
   public AttachmentEditorComponent getEditor(Attachment attachment, String taskId, String processInstanceId) {
     return new UrlAttachmentEditorComponent(taskId, processInstanceId);
   }
 }


