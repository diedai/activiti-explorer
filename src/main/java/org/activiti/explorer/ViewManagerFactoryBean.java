 package org.activiti.explorer;
 
 import java.io.Serializable;
 import org.activiti.explorer.ui.MainWindow;
 import org.activiti.explorer.ui.alfresco.AlfrescoViewManager;
 import org.springframework.beans.factory.FactoryBean;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class ViewManagerFactoryBean
   implements FactoryBean<ViewManager>, Serializable
 {
   protected String environment;
   protected MainWindow mainWindow;
   
   public ViewManager getObject()
     throws Exception
   {
     DefaultViewManager viewManagerImpl;
     DefaultViewManager viewManagerImpl1;
     if (this.environment.equals("alfresco")) {
       viewManagerImpl1 = new AlfrescoViewManager();
     } else {
       viewManagerImpl1 = new DefaultViewManager();
     }
     viewManagerImpl1.setMainWindow(this.mainWindow);
     return viewManagerImpl1;
   }
   
   public Class<?> getObjectType() {
     return ViewManager.class;
   }
   
   public boolean isSingleton() {
     return true;
   }
   
   public void setEnvironment(String environment) {
     this.environment = environment;
   }
   
   public void setMainWindow(MainWindow mainWindow) {
     this.mainWindow = mainWindow;
   }
 }


