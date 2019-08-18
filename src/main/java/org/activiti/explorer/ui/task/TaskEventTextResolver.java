 package org.activiti.explorer.ui.task;
 
 import com.vaadin.ui.Label;
 import java.io.Serializable;
 import java.util.List;
 import org.activiti.engine.IdentityService;
 import org.activiti.engine.ProcessEngine;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.engine.identity.User;
 import org.activiti.engine.identity.UserQuery;
 import org.activiti.engine.task.Event;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class TaskEventTextResolver
   implements Serializable
 {
   private static final long serialVersionUID = -1241011503689621172L;
   protected I18nManager i18nManager;
   
   public TaskEventTextResolver()
   {
     this.i18nManager = ExplorerApp.get().getI18nManager();
   }
   
   public Label resolveText(Event event) {
     IdentityService identityService = ProcessEngines.getDefaultProcessEngine().getIdentityService();
     User user = (User)identityService.createUserQuery().userId(event.getUserId()).singleResult();
     
     String eventAuthor = "<span class='task-event-author'>" + user.getFirstName() + " " + user.getLastName() + "</span> ";
     
     String text = null;
     if ("AddUserLink".equals(event.getAction())) {
       User involvedUser = (User)identityService.createUserQuery().userId((String)event.getMessageParts().get(0)).singleResult();
       text = this.i18nManager.getMessage("event.add.user.link", new Object[] { eventAuthor, involvedUser
       
         .getFirstName() + " " + involvedUser.getLastName(), event
         .getMessageParts().get(1) });
     } else if ("DeleteUserLink".equals(event.getAction())) {
       User involvedUser = (User)identityService.createUserQuery().userId((String)event.getMessageParts().get(0)).singleResult();
       text = this.i18nManager.getMessage("event.delete.user.link", new Object[] { eventAuthor, involvedUser
       
         .getFirstName() + " " + involvedUser.getLastName(), event
         .getMessageParts().get(1) });
     } else if ("AddGroupLink".equals(event.getAction())) {
       text = this.i18nManager.getMessage("event.add.group.link", new Object[] { eventAuthor, event
       
         .getMessageParts().get(0), event
         .getMessageParts().get(1) });
     } else if ("DeleteGroupLink".equals(event.getAction())) {
       text = this.i18nManager.getMessage("event.delete.group.link", new Object[] { eventAuthor, event
       
         .getMessageParts().get(0), event
         .getMessageParts().get(1) });
     } else if ("AddAttachment".equals(event.getAction())) {
       text = this.i18nManager.getMessage("event.add.attachment", new Object[] { eventAuthor, event.getMessage() });
     } else if ("DeleteAttachment".equals(event.getAction())) {
       text = this.i18nManager.getMessage("event.delete.attachment", new Object[] { eventAuthor, event.getMessage() });
     } else if ("AddComment".equals(event.getAction())) {
       text = this.i18nManager.getMessage("event.comment", new Object[] { eventAuthor, event.getMessage() });
     } else {
       text = text + this.i18nManager.getMessage("event.default", new Object[] { eventAuthor, event.getMessage() });
     }
     return new Label(text, 3);
   }
 }


