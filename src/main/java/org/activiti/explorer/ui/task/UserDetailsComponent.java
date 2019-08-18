 package org.activiti.explorer.ui.task;
 
 import java.io.InputStream;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.identity.Picture;
import org.activiti.engine.identity.User;
import org.activiti.explorer.ExplorerApp;
import org.activiti.explorer.ViewManager;
import org.activiti.explorer.ui.Images;

import com.vaadin.event.MouseEvents;
import com.vaadin.event.MouseEvents.ClickEvent;
import com.vaadin.terminal.Resource;
import com.vaadin.terminal.StreamResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class UserDetailsComponent
   extends HorizontalLayout
 {
   private static final long serialVersionUID = 1L;
   protected transient IdentityService identityService;
   protected ViewManager viewManager;
   protected User user;
   protected String skypeId;
   protected String role;
   protected String buttonCaption;
   protected Button.ClickListener clickListener;
   
   public UserDetailsComponent(String userId, String role)
   {
     this.role = role;
     this.identityService = ProcessEngines.getDefaultProcessEngine().getIdentityService();
     this.viewManager = ExplorerApp.get().getViewManager();
     
     if (userId != null) {
       this.user = ((User)this.identityService.createUserQuery().userId(userId).singleResult());
       this.skypeId = this.identityService.getUserInfo(userId, "skype");
     }
   }
   
   public UserDetailsComponent(String userId, String role, String buttonCaption, Button.ClickListener clickListener) {
     this(userId, role);
     this.buttonCaption = buttonCaption;
     this.clickListener = clickListener;
   }
   
   public void attach()
   {
     super.attach();
     setSpacing(true);
     addUserPicture();
     addUserDetails();
   }
   
   protected void addUserPicture() {
     Resource pictureResource = Images.USER_32;
     if (this.user != null) {
       final Picture userPicture = this.identityService.getUserPicture(this.user.getId());
       if (userPicture != null)
       {
 
 
 
         pictureResource = new StreamResource(new StreamResource.StreamSource()
         {
           public InputStream getStream()
           {
             return userPicture.getInputStream();
           }
         }, this.user.getId(), ExplorerApp.get());
       }
     }
     
     Embedded picture = new Embedded(null, pictureResource);
     
     picture.setType(1);
     picture.addStyleName("task-event-picture");
     if (this.user != null)
     {
       picture.setHeight("32px");
       picture.setWidth("32px");
     }
     addComponent(picture);
     
 
     if (this.user != null) {
       picture.addStyleName("clickable");
       picture.addListener(new MouseEvents.ClickListener() {
         public void click(ClickEvent event) {
           UserDetailsComponent.this.viewManager.showProfilePopup(UserDetailsComponent.this.user.getId());
         }
       });
     }
   }
   
   protected void addUserDetails() {
     VerticalLayout detailsLayout = new VerticalLayout();
     addComponent(detailsLayout);
     
 
     HorizontalLayout nameLayout = new HorizontalLayout();
     nameLayout.setSpacing(true);
     detailsLayout.addComponent(nameLayout);
     
 
     Label nameLabel = null;
     if (this.user != null) {
       nameLabel = new Label(this.user.getFirstName() + " " + this.user.getLastName());
       nameLabel.addStyleName("bold");
     } else {
       nameLabel = new Label("&nbsp;", 3);
     }
     nameLayout.addComponent(nameLabel);
     
 
     HorizontalLayout actionsLayout = new HorizontalLayout();
     actionsLayout.setSpacing(true);
     detailsLayout.addComponent(actionsLayout);
     
 
     Label roleLabel = new Label(this.role);
     actionsLayout.addComponent(roleLabel);
     
 
     if (this.clickListener != null) {
       Button button = new Button(this.buttonCaption);
       button.addStyleName("small");
       button.addListener(this.clickListener);
       actionsLayout.addComponent(button);
     }
   }
 }


