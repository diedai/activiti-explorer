 package org.activiti.explorer.ui.management.identity;
 
 import com.vaadin.event.MouseEvents.ClickEvent;
 import com.vaadin.event.MouseEvents.ClickListener;
 import org.activiti.engine.IdentityService;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.ViewManager;
 import org.activiti.explorer.ui.custom.ConfirmationDialogPopupWindow;
 import org.activiti.explorer.ui.event.ConfirmationEvent;
 import org.activiti.explorer.ui.event.ConfirmationEventListener;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class DeleteMembershipListener
   implements ClickListener
 {
   private static final long serialVersionUID = 1L;
   protected transient IdentityService identityService;
   protected String userId;
   protected String groupId;
   protected MemberShipChangeListener membershipChangeListener;
   
   public DeleteMembershipListener(IdentityService identityService, String userId, String groupId, MemberShipChangeListener memberShipChangeListener)
   {
     this.identityService = identityService;
     this.userId = userId;
     this.groupId = groupId;
     this.membershipChangeListener = memberShipChangeListener;
   }
   
   public void click(ClickEvent event) {
     I18nManager i18nManager = ExplorerApp.get().getI18nManager();
     ViewManager viewManager = ExplorerApp.get().getViewManager();
     
 
 
     ConfirmationDialogPopupWindow confirmationPopup = new ConfirmationDialogPopupWindow(i18nManager.getMessage("user.confirm.delete.group", new Object[] { this.userId, this.groupId }));
     confirmationPopup.addListener(new ConfirmationEventListener() {
       protected void rejected(ConfirmationEvent event) {}
       
       protected void confirmed(ConfirmationEvent event) {
         DeleteMembershipListener.this.identityService.deleteMembership(DeleteMembershipListener.this.userId, DeleteMembershipListener.this.groupId);
         DeleteMembershipListener.this.membershipChangeListener.notifyMembershipChanged();
       }
       
 
     });
     viewManager.showPopupWindow(confirmationPopup);
   }
 }


