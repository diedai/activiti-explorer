 package org.activiti.explorer.ui.content;
 
 import com.vaadin.ui.Component;
 import com.vaadin.ui.ComponentContainer;
 import org.activiti.engine.task.Attachment;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.ui.custom.PopupWindow;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class AttachmentDetailPopupWindow
   extends PopupWindow
 {
   private static final long serialVersionUID = 1L;
   
   public AttachmentDetailPopupWindow(Attachment attachment)
   {
     super(attachment.getName());
     
     addStyleName("light");
     center();
     setModal(true);
     setResizable(false);
     
     AttachmentRenderer renderer = ExplorerApp.get().getAttachmentRendererManager().getRenderer(attachment.getType());
     Component detail = renderer.getDetailComponent(attachment);
     
     if ((detail instanceof ComponentContainer)) {
       setContent((ComponentContainer)detail);
     } else {
       addComponent(detail);
     }
     getContent().setSizeUndefined();
   }
 }


