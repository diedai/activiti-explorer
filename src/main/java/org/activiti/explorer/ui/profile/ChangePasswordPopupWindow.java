 package org.activiti.explorer.ui.profile;
 
 import com.vaadin.event.Action;
 import com.vaadin.event.Action.Handler;
 import com.vaadin.event.ShortcutAction;
 import com.vaadin.ui.Alignment;
 import com.vaadin.ui.Button;
 import com.vaadin.ui.Button.ClickEvent;
 import com.vaadin.ui.Button.ClickListener;
 import com.vaadin.ui.GridLayout;
 import com.vaadin.ui.Label;
 import com.vaadin.ui.PasswordField;
 import com.vaadin.ui.VerticalLayout;
 import org.activiti.engine.IdentityService;
 import org.activiti.engine.ProcessEngine;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.engine.identity.User;
 import org.activiti.engine.identity.UserQuery;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.NotificationManager;
 import org.activiti.explorer.identity.LoggedInUser;
 import org.activiti.explorer.ui.custom.PopupWindow;
 import org.activiti.explorer.ui.login.LoginHandler;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class ChangePasswordPopupWindow
   extends PopupWindow
 {
   private static final long serialVersionUID = 1L;
   protected transient IdentityService identityService;
   protected LoggedInUser currentUser;
   protected I18nManager i18nManager;
   protected VerticalLayout layout;
   protected GridLayout inputGrid;
   protected PasswordField passwordField1;
   protected PasswordField passwordField2;
   protected Label errorLabel;
   
   public ChangePasswordPopupWindow()
   {
     this.identityService = ProcessEngines.getDefaultProcessEngine().getIdentityService();
     this.currentUser = ExplorerApp.get().getLoggedInUser();
     this.i18nManager = ExplorerApp.get().getI18nManager();
     
     setCaption(this.i18nManager.getMessage("password.change"));
     setModal(true);
     center();
     addStyleName("light");
     setWidth(350.0F, 0);
     setHeight(205.0F, 0);
     
     initLayout();
     initPasswordFields();
     initChangePasswordButton();
     initEnterKeyListener();
   }
   
   protected void initLayout() {
     this.layout = new VerticalLayout();
     this.layout.setMargin(true);
     this.layout.setSpacing(true);
     setContent(this.layout);
   }
   
   protected void initPasswordFields() {
     this.inputGrid = new GridLayout(2, 2);
     this.inputGrid.setSpacing(true);
     this.layout.addComponent(this.inputGrid);
     this.layout.setComponentAlignment(this.inputGrid, Alignment.MIDDLE_CENTER);
     
     Label newPasswordLabel = new Label(this.i18nManager.getMessage("profile.new.password"));
     this.inputGrid.addComponent(newPasswordLabel);
     this.passwordField1 = new PasswordField();
     this.passwordField1.setWidth(150.0F, 0);
     this.inputGrid.addComponent(this.passwordField1);
     this.passwordField1.focus();
     
     Label confirmPasswordLabel = new Label(this.i18nManager.getMessage("profile.confirm.password"));
     this.inputGrid.addComponent(confirmPasswordLabel);
     this.passwordField2 = new PasswordField();
     this.passwordField2.setWidth(150.0F, 0);
     this.inputGrid.addComponent(this.passwordField2);
   }
   
   protected void initChangePasswordButton() {
     this.errorLabel = new Label("&nbsp", 3);
     this.errorLabel.addStyleName("light");
     this.errorLabel.addStyleName("red");
     this.layout.addComponent(this.errorLabel);
     
     Button changePasswordButton = new Button(this.i18nManager.getMessage("password.change"));
     this.layout.addComponent(changePasswordButton);
     this.layout.setComponentAlignment(changePasswordButton, Alignment.MIDDLE_CENTER);
     
     changePasswordButton.addListener(new Button.ClickListener() {
       public void buttonClick(ClickEvent event) {
         ChangePasswordPopupWindow.this.handlePasswordChange();
       }
     });
   }
   
   protected void initEnterKeyListener() {
     addActionHandler(new Action.Handler() {
       public void handleAction(Action action, Object sender, Object target) {
         ChangePasswordPopupWindow.this.handlePasswordChange();
       }
       
       public Action[] getActions(Object target, Object sender) { return new Action[] { new ShortcutAction("enter", 13, null) }; }
     });
   }
   
   protected void handlePasswordChange()
   {
     if ((this.passwordField1.getValue() == null) || ("".equals(this.passwordField1.getValue().toString())) || 
       (this.passwordField2.getValue() == null) || ("".equals(this.passwordField2.getValue().toString()))) {
       this.errorLabel.setValue(this.i18nManager.getMessage("password.change.input.required"));
     } else if (!this.passwordField1.getValue().equals(this.passwordField2.getValue())) {
       this.errorLabel.setValue(this.i18nManager.getMessage("password.change.input.match"));
     } else {
       String password = this.passwordField1.getValue().toString();
       
       User user = (User)this.identityService.createUserQuery().userId(this.currentUser.getId()).singleResult();
       user.setPassword(password);
       this.identityService.saveUser(user);
       
 
       ExplorerApp.get().setUser(ExplorerApp.get().getLoginHandler().authenticate(user
         .getId(), user.getPassword()));
       
 
       close();
       
 
       ExplorerApp.get().getNotificationManager().showInformationNotification("password.changed.notification");
     }
   }
 }


