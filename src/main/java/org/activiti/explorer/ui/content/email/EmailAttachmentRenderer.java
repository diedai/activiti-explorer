 package org.activiti.explorer.ui.content.email;
 
 import com.vaadin.terminal.Resource;
 import com.vaadin.ui.Component;
 import org.activiti.engine.task.Attachment;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.ui.Images;
 import org.activiti.explorer.ui.content.GenericAttachmentRenderer;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class EmailAttachmentRenderer
   extends GenericAttachmentRenderer
 {
   public static final String EMAIL_TYPE = "email";
   
   public boolean canRenderAttachment(String type)
   {
     return "email".equals(type);
   }
   
   public String getName(I18nManager i18nManager) {
     return i18nManager.getMessage("related.content.type.email");
   }
   
   public Resource getImage(Attachment attachment) {
     return Images.IMAP;
   }
   
   public Component getDetailComponent(Attachment attachment)
   {
     return new EmailDetailPanel(attachment);
   }
 }


