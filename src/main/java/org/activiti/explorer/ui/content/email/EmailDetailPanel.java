 package org.activiti.explorer.ui.content.email;
 
 import com.vaadin.ui.AbstractLayout;
 import com.vaadin.ui.Alignment;
 import com.vaadin.ui.GridLayout;
 import com.vaadin.ui.Label;
 import com.vaadin.ui.Layout.SpacingHandler;
 import com.vaadin.ui.Panel;
 import java.io.InputStream;
 import java.io.InputStreamReader;
 import org.activiti.engine.ProcessEngine;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.engine.TaskService;
 import org.activiti.engine.impl.util.json.JSONObject;
 import org.activiti.engine.impl.util.json.JSONTokener;
 import org.activiti.engine.task.Attachment;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class EmailDetailPanel
   extends Panel
 {
   private static final long serialVersionUID = 1L;
   protected I18nManager i18nManager;
   protected transient TaskService taskService;
   protected Label content;
   protected Attachment attachment;
   protected GridLayout gridLayout;
   
   public EmailDetailPanel(Attachment attachment)
   {
     setSizeFull();
     ((AbstractLayout)getContent()).setMargin(true);
     ((SpacingHandler)getContent()).setSpacing(true);
     addStyleName("light");
     
     this.attachment = attachment;
     this.i18nManager = ExplorerApp.get().getI18nManager();
     this.taskService = ProcessEngines.getDefaultProcessEngine().getTaskService();
     
     this.gridLayout = new GridLayout(2, 4);
     this.gridLayout.setSpacing(true);
     addComponent(this.gridLayout);
     
     InputStream contentStream = this.taskService.getAttachmentContent(attachment.getId());
     
     JSONObject emailJson = new JSONObject(new JSONTokener(new InputStreamReader(contentStream)));
     
     String html = emailJson.getString("htmlContent");
     String subject = emailJson.getString("subject");
     String recipients = emailJson.getString("recipients");
     String sentDate = emailJson.getString("sentDate");
     String receivedDate = emailJson.getString("receivedDate");
     
 
     addSimpleRow("email.subject", subject);
     addSimpleRow("email.recipients", recipients);
     addSimpleRow("email.sent.date", sentDate);
     addSimpleRow("email.received.date", receivedDate);
     
 
     addHtmlContent(html);
   }
   
   protected void addHtmlContent(String html)
   {
     Panel panel = new Panel();
     panel.setWidth(800.0F, 0);
     panel.setHeight(300.0F, 0);
     
     this.content = new Label(html, 3);
     this.content.setHeight(100.0F, 8);
     
     panel.addComponent(this.content);
     addComponent(panel);
   }
   
   protected void addSimpleRow(String labelMessageKey, String content) {
     addLabel(labelMessageKey);
     
     Label subjectLabel = new Label(content);
     subjectLabel.setSizeUndefined();
     subjectLabel.addStyleName("bold");
     
     this.gridLayout.addComponent(subjectLabel);
     this.gridLayout.setComponentAlignment(subjectLabel, Alignment.MIDDLE_LEFT);
   }
   
   protected void addLabel(String messageKey) {
     Label theLabel = new Label(this.i18nManager.getMessage(messageKey));
     theLabel.setSizeUndefined();
     this.gridLayout.addComponent(theLabel);
   }
 }


