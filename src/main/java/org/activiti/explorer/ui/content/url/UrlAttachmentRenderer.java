 package org.activiti.explorer.ui.content.url;
 
 import com.vaadin.terminal.ExternalResource;
 import com.vaadin.terminal.Resource;
 import com.vaadin.ui.Button;
 import com.vaadin.ui.Button.ClickEvent;
 import com.vaadin.ui.Button.ClickListener;
 import com.vaadin.ui.Component;
 import com.vaadin.ui.Embedded;
 import com.vaadin.ui.HorizontalLayout;
 import com.vaadin.ui.Label;
 import com.vaadin.ui.Link;
 import com.vaadin.ui.VerticalLayout;
 import org.activiti.engine.task.Attachment;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.ui.Images;
 import org.activiti.explorer.ui.content.AttachmentRenderer;
 import org.activiti.explorer.ui.content.RelatedContentComponent;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class UrlAttachmentRenderer
   implements AttachmentRenderer
 {
   public static final String ATTACHMENT_TYPE = "url";
   
   public String getName(I18nManager i18nManager)
   {
     return i18nManager.getMessage("related.content.type.url");
   }
   
   public Resource getImage(Attachment attachment)
   {
     return Images.RELATED_CONTENT_URL;
   }
   
 
 
   public Component getOverviewComponent(final Attachment attachment, final RelatedContentComponent parent)
   {
     if ((attachment.getDescription() != null) && (!"".equals(attachment.getDescription()))) {
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
     return new Link(attachment.getName(), new ExternalResource(attachment.getUrl()));
   }
   
   public Component getDetailComponent(Attachment attachment)
   {
     VerticalLayout verticalLayout = new VerticalLayout();
     verticalLayout.setSpacing(true);
     verticalLayout.setMargin(true);
     
     verticalLayout.addComponent(new Label(attachment.getDescription()));
     
     HorizontalLayout linkLayout = new HorizontalLayout();
     linkLayout.setSpacing(true);
     verticalLayout.addComponent(linkLayout);
     
 
     linkLayout.addComponent(new Embedded(null, Images.RELATED_CONTENT_URL));
     
 
     Link link = new Link(attachment.getUrl(), new ExternalResource(attachment.getUrl()));
     link.setTargetName("_blank");
     linkLayout.addComponent(link);
     
     return verticalLayout;
   }
   
   public boolean canRenderAttachment(String type) {
     return "url".equals(type);
   }
 }


