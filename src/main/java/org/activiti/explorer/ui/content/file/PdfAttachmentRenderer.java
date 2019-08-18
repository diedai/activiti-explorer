 package org.activiti.explorer.ui.content.file;
 
 import com.vaadin.terminal.Resource;
 import org.activiti.engine.task.Attachment;
 import org.activiti.explorer.ui.Images;
 import org.activiti.explorer.ui.content.GenericAttachmentRenderer;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class PdfAttachmentRenderer
   extends GenericAttachmentRenderer
 {
   private static final String PDF_ATTACHMENT_TYPE = "application/pdf";
   
   public boolean canRenderAttachment(String type)
   {
     if (type != null) {
       return type.startsWith("application/pdf");
     }
     return false;
   }
   
   public Resource getImage(Attachment attachment)
   {
     return Images.RELATED_CONTENT_PDF;
   }
 }


