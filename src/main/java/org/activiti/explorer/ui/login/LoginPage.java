 package org.activiti.explorer.ui.login;
 
 import com.vaadin.ui.CustomLayout;
 import com.vaadin.ui.LoginForm.LoginEvent;
 import com.vaadin.ui.LoginForm.LoginListener;
 import java.io.IOException;
 import java.io.InputStream;
 import org.activiti.engine.ActivitiException;
 import org.activiti.engine.IdentityService;
 import org.activiti.engine.ProcessEngine;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.NotificationManager;
 import org.activiti.explorer.ViewManager;
 import org.activiti.explorer.identity.LoggedInUser;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class LoginPage
   extends CustomLayout
 {
   private static final long serialVersionUID = 1L;
   protected static final Logger LOGGER = LoggerFactory.getLogger(LoginPage.class);
   
   protected transient IdentityService identityService = ProcessEngines.getDefaultProcessEngine().getIdentityService();
   
   protected I18nManager i18nManager;
   
   protected ViewManager viewManager;
   
   protected NotificationManager notificationManager;
   
   protected LoginHandler loginHandler;
   
 
   public LoginPage()
   {
     InputStream loginHtmlStream = getClass().getResourceAsStream("/VAADIN/themes/activiti/layouts/login.html");
     
     if (loginHtmlStream != null) {
       try {
         initTemplateContentsFromInputStream(loginHtmlStream);
       } catch (IOException e) {
         throw new ActivitiException("Error while loading login page template from classpath resource", e);
       }
     } else {
       setTemplateName("login");
     }
     
     this.i18nManager = ExplorerApp.get().getI18nManager();
     this.viewManager = ExplorerApp.get().getViewManager();
     this.notificationManager = ExplorerApp.get().getNotificationManager();
     this.loginHandler = ExplorerApp.get().getLoginHandler();
     
     addStyleName("login-general");
     initUi();
   }
   
 
   protected void initUi()
   {
     ExplorerLoginForm loginForm = new ExplorerLoginForm();
     addComponent(loginForm, "login-content");
     
 
     loginForm.addListener((LoginListener) new ActivitiLoginListener());
   }
   
   protected void refreshUi()
   {
     removeAllComponents();
     initUi();
   }
   
   class ActivitiLoginListener implements LoginListener {
     private static final long serialVersionUID = 1L;
     
     ActivitiLoginListener() {}
     
     public void onLogin(LoginEvent event) {
       try { String userName = event.getLoginParameter("username");
         String password = event.getLoginParameter("password");
         
         LoggedInUser loggedInUser = LoginPage.this.loginHandler.authenticate(userName, password);
         if (loggedInUser != null) {
           ExplorerApp.get().setUser(loggedInUser);
           LoginPage.this.viewManager.showDefaultPage();
         } else {
           LoginPage.this.refreshUi();
           LoginPage.this.notificationManager.showErrorNotification("login.failed.header", LoginPage.this.i18nManager.getMessage("login.failed.invalid"));
         }
       } catch (Exception e) {
         LoginPage.LOGGER.error("Error at login", e);
       }
     }
   }
 }


