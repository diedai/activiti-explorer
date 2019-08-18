 package org.activiti.explorer.ui.custom;
 
 import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.identity.Picture;
import org.activiti.engine.identity.User;
import org.activiti.explorer.ExplorerApp;
import org.activiti.explorer.ViewManager;
import org.activiti.explorer.ui.util.InputStreamStreamSource;

import com.vaadin.event.MouseEvents;
import com.vaadin.event.MouseEvents.ClickEvent;
import com.vaadin.terminal.Resource;
import com.vaadin.terminal.StreamResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class UserProfileLink
   extends HorizontalLayout
 {
   private static final long serialVersionUID = 1L;
   protected transient IdentityService identityService;
   protected ViewManager viewManager;
   
   public UserProfileLink(IdentityService identityService, boolean renderPicture, String userId)
   {
     this.identityService = identityService;
     this.viewManager = ExplorerApp.get().getViewManager();
     
     setSizeUndefined();
     setSpacing(true);
     addStyleName("profile-link");
     
     initPicture(identityService, renderPicture, userId);
     initUserLink(userId);
   }
   
   protected void initPicture(IdentityService identityService, boolean renderPicture, final String userName) {
     if (renderPicture) {
       Picture picture = identityService.getUserPicture(userName);
       if (picture != null)
       {
         Resource imageResource = new StreamResource(new InputStreamStreamSource(picture.getInputStream()), userName + picture.getMimeType(), ExplorerApp.get());
         
         Embedded image = new Embedded(null, imageResource);
         image.addStyleName("clickable");
         image.setType(1);
         image.setHeight(30.0F, 0);
         image.setWidth(30.0F, 0);
         image.addListener(new MouseEvents.ClickListener() {
           private static final long serialVersionUID = 7341560240277898495L;
           
           public void click(ClickEvent event) { UserProfileLink.this.viewManager.showProfilePopup(userName);
           }
 
         });
         addComponent(image);
         setComponentAlignment(image, Alignment.MIDDLE_LEFT);
       }
     }
   }
   
 
   protected void initUserLink(final String userId)
   {
     User user = (User)ProcessEngines.getDefaultProcessEngine().getIdentityService().createUserQuery().userId(userId).singleResult();
     Button userButton = new Button(user.getFirstName() + " " + user.getLastName());
     Button.ClickListener buttonClickListener = new Button.ClickListener()
     {
       private static final long serialVersionUID = 1L;
       
       public void buttonClick(ClickEvent event) {
         UserProfileLink.this.viewManager.showProfilePopup(userId);
       }

	@Override
	public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
		// TODO Auto-generated method stub
		
	}
     };
     userButton.addStyleName("link");
     userButton.addListener(buttonClickListener);
     addComponent(userButton);
     setComponentAlignment(userButton, Alignment.MIDDLE_LEFT);
   }
 }


