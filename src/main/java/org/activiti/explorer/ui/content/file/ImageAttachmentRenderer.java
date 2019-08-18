 package org.activiti.explorer.ui.content.file;
 
 import com.vaadin.terminal.ExternalResource;
 import com.vaadin.terminal.Resource;
 import com.vaadin.terminal.StreamResource;
import com.vaadin.terminal.StreamResource.StreamSource;
import com.vaadin.ui.Alignment;
 import com.vaadin.ui.Component;
 import com.vaadin.ui.Embedded;
 import com.vaadin.ui.HorizontalLayout;
 import com.vaadin.ui.Label;
 import com.vaadin.ui.Link;
 import com.vaadin.ui.VerticalLayout;
 import java.io.InputStream;
 import org.activiti.engine.ProcessEngine;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.engine.TaskService;
 import org.activiti.engine.task.Attachment;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.ui.Images;
 import org.activiti.explorer.ui.content.GenericAttachmentRenderer;
 import org.activiti.explorer.ui.util.InputStreamStreamSource;
 import org.activiti.explorer.util.ImageUtil;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class ImageAttachmentRenderer
   extends GenericAttachmentRenderer
 {
   public boolean canRenderAttachment(String type)
   {
     return (type != null) && (type.startsWith("image/"));
   }
   
   public Resource getImage(Attachment attachment)
   {
     return Images.RELATED_CONTENT_PICTURE;
   }
   
   public Component getDetailComponent(Attachment attachment)
   {
     VerticalLayout verticalLayout = new VerticalLayout();
     verticalLayout.setSizeUndefined();
     verticalLayout.setSpacing(true);
     verticalLayout.setMargin(true);
     
     Label description = new Label(attachment.getDescription());
     description.setSizeUndefined();
     verticalLayout.addComponent(description);
     
 
     TaskService taskService = ProcessEngines.getDefaultProcessEngine().getTaskService();
     
     String mimeType = extractMineType(attachment.getType());
     
     InputStream imageStream = ImageUtil.resizeImage(taskService.getAttachmentContent(attachment.getId()), mimeType, 900, 550);
     
     Resource resource = new StreamResource((StreamSource) new InputStreamStreamSource(imageStream), attachment.getName() + extractExtention(attachment.getType()), ExplorerApp.get());
     Embedded image = new Embedded(null, resource);
     verticalLayout.addComponent(image);
     
 
     HorizontalLayout LinkLayout = new HorizontalLayout();
     LinkLayout.setSpacing(true);
     verticalLayout.addComponent(LinkLayout);
     verticalLayout.setComponentAlignment(LinkLayout, Alignment.MIDDLE_CENTER);
     
     Label fullSizeLabel = new Label(ExplorerApp.get().getI18nManager().getMessage("related.content.show.full.size"));
     LinkLayout.addComponent(fullSizeLabel);
     
     Link link = null;
     if (attachment.getUrl() != null) {
       link = new Link(attachment.getUrl(), new ExternalResource(attachment.getUrl()));
     } else {
       taskService = ProcessEngines.getDefaultProcessEngine().getTaskService();
       
       Resource res = new StreamResource((StreamSource) new InputStreamStreamSource(taskService.getAttachmentContent(attachment.getId())), attachment.getName() + extractExtention(attachment.getType()), ExplorerApp.get());
       
       link = new Link(attachment.getName(), res);
     }
     
     link.setIcon(Images.RELATED_CONTENT_PICTURE);
     link.setTargetName("_blank");
     LinkLayout.addComponent(link);
     
     return verticalLayout;
   }
   
   protected String extractMineType(String type) {
     if (type != null) {
       int index = type.lastIndexOf(";");
       if (index >= 0) {
         return type.substring(0, index);
       }
     }
     return type;
   }
 }


