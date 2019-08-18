 package org.activiti.explorer.ui.content;
 
 import com.vaadin.terminal.ExternalResource;
 import com.vaadin.terminal.Resource;
 import com.vaadin.terminal.StreamResource;
import com.vaadin.terminal.StreamResource.StreamSource;
import com.vaadin.ui.Button;
 import com.vaadin.ui.Button.ClickEvent;
 import com.vaadin.ui.Button.ClickListener;
 import com.vaadin.ui.Component;
 import com.vaadin.ui.Embedded;
 import com.vaadin.ui.HorizontalLayout;
 import com.vaadin.ui.Label;
 import com.vaadin.ui.Link;
 import com.vaadin.ui.VerticalLayout;
 import org.activiti.engine.ProcessEngine;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.engine.TaskService;
 import org.activiti.engine.task.Attachment;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.ui.Images;
 import org.activiti.explorer.ui.util.InputStreamStreamSource;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class GenericAttachmentRenderer
   implements AttachmentRenderer
 {
   public boolean canRenderAttachment(String type)
   {
     return true;
   }
   
   public String getName(I18nManager i18nManager) {
     return i18nManager.getMessage("related.content.type.file");
   }
   
   public Resource getImage(Attachment attachment) {
     return Images.RELATED_CONTENT_FILE;
   }
   
   public Component getOverviewComponent(final Attachment attachment, final RelatedContentComponent parent) {
     Button attachmentLink = new Button(attachment.getName());
     attachmentLink.addStyleName("link");
     
     attachmentLink.addListener(new Button.ClickListener() {
       private static final long serialVersionUID = 1L;
       
       public void buttonClick(ClickEvent event) {
         parent.showAttachmentDetail(attachment);
       }
     });
     return attachmentLink;
   }
   
   public Component getDetailComponent(Attachment attachment) {
     VerticalLayout verticalLayout = new VerticalLayout();
     verticalLayout.setSizeUndefined();
     verticalLayout.setSpacing(true);
     verticalLayout.setMargin(true);
     
     Label description = new Label(attachment.getDescription());
     description.setSizeUndefined();
     verticalLayout.addComponent(description);
     
     HorizontalLayout linkLayout = new HorizontalLayout();
     linkLayout.setSpacing(true);
     verticalLayout.addComponent(linkLayout);
     
 
     linkLayout.addComponent(new Embedded(null, getImage(attachment)));
     
 
     Link link = null;
     if (attachment.getUrl() != null) {
       link = new Link(attachment.getUrl(), new ExternalResource(attachment.getUrl()));
     } else {
       TaskService taskService = ProcessEngines.getDefaultProcessEngine().getTaskService();
       
       Resource res = new StreamResource((StreamSource) new InputStreamStreamSource(taskService.getAttachmentContent(attachment.getId())), attachment.getName() + extractExtention(attachment.getType()), ExplorerApp.get());
       
       link = new Link(attachment.getName(), res);
     }
     
 
     link.setTargetName("_blank");
     linkLayout.addComponent(link);
     
     return verticalLayout;
   }
   
   protected String extractExtention(String type)
   {
     int lastIndex = type.lastIndexOf(";");
     if ((lastIndex > 0) && (lastIndex < type.length() - 1)) {
       return "." + type.substring(lastIndex + 1);
     }
     
 
     lastIndex = type.lastIndexOf('/');
     if ((lastIndex > 0) && (lastIndex < type.length() - 1)) {
       return "." + type.substring(lastIndex + 1);
     }
     return "." + type;
   }
 }


