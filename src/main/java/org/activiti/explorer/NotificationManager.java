 package org.activiti.explorer;
 
 import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;
 import java.io.Serializable;
 import java.text.MessageFormat;
 import org.activiti.explorer.ui.MainWindow;
 import org.springframework.beans.factory.annotation.Autowired;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class NotificationManager
   implements Serializable
 {
   private static final long serialVersionUID = 1L;
   @Autowired
   protected MainWindow mainWindow;
   @Autowired
   protected I18nManager i18nManager;
   
   public void showErrorNotification(String captionKey, String description)
   {
     this.mainWindow.showNotification(this.i18nManager.getMessage(captionKey), "<br/>" + description, 3);
   }
   
 
   public void showErrorNotification(String captionKey, Exception exception)
   {
     this.mainWindow.showNotification(this.i18nManager.getMessage(captionKey), "<br/>" + exception
       .getMessage(), 3);
   }
   
 
   public void showWarningNotification(String captionKey, String descriptionKey)
   {
     Window.Notification notification = new Window.Notification(this.i18nManager.getMessage(captionKey), this.i18nManager.getMessage(descriptionKey), 2);
     
     notification.setDelayMsec(-1);
     this.mainWindow.showNotification(notification);
   }
   
   public void showWarningNotification(String captionKey, String descriptionKey, Object... params)
   {
     Window.Notification notification = new Window.Notification(this.i18nManager.getMessage(captionKey) + "<br/>", MessageFormat.format(this.i18nManager.getMessage(descriptionKey), params), 2);
     
     notification.setDelayMsec(5000);
     this.mainWindow.showNotification(notification);
   }
   
   public void showInformationNotification(String key) {
     this.mainWindow.showNotification(this.i18nManager.getMessage(key), 1);
   }
   
   public void showInformationNotification(String key, Object... params) {
     this.mainWindow.showNotification(MessageFormat.format(this.i18nManager.getMessage(key), params), 1);
   }
   
   public void setMainWindow(MainWindow mainWindow)
   {
     this.mainWindow = mainWindow;
   }
   
   public void setI18nManager(I18nManager i18nManager) {
     this.i18nManager = i18nManager;
   }
 }


