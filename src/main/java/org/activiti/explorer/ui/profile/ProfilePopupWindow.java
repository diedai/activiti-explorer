 package org.activiti.explorer.ui.profile;
 
 import org.activiti.explorer.ui.custom.PopupWindow;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class ProfilePopupWindow
   extends PopupWindow
 {
   private static final long serialVersionUID = 3129077881658239761L;
   
   public ProfilePopupWindow(String userId)
   {
     addStyleName("light");
     setModal(true);
     setHeight("80%");
     setWidth("50%");
     center();
     addComponent(new ProfilePanel(userId));
   }
 }


