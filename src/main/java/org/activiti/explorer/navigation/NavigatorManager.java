 package org.activiti.explorer.navigation;
 
 import java.io.Serializable;
 import java.util.HashMap;
 import java.util.Map;
 import org.activiti.explorer.ui.management.admin.AdministrationNavigator;
 import org.springframework.beans.factory.InitializingBean;
 import org.springframework.stereotype.Component;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 @Component
 public class NavigatorManager
   implements InitializingBean, Serializable
 {
   private static final long serialVersionUID = 1L;
   protected Map<String, Navigator> navigationHandlers = new HashMap();
   protected Navigator defaultHandler;
   
   public void addNavigator(Navigator handler) {
     this.navigationHandlers.put(handler.getTrigger(), handler);
   }
   
   public Navigator getNavigator(String trigger) {
     if (trigger != null) {
       return (Navigator)this.navigationHandlers.get(trigger);
     }
     return null;
   }
   
   public Navigator getDefaultNavigator() {
     if (this.defaultHandler == null) {
       throw new IllegalStateException("No default navigation handler has been set");
     }
     return this.defaultHandler;
   }
   
   public void setDefaultNavigator(Navigator handler) {
     this.defaultHandler = handler;
   }
   
 
   public void afterPropertiesSet()
     throws Exception
   {
     addNavigator(new TaskNavigator());
     addNavigator(new ProcessNavigator());
     addNavigator(new ProcessModelNavigator());
     addNavigator(new DeploymentNavigator());
     addNavigator(new DatabaseNavigator());
     addNavigator(new JobNavigator());
     addNavigator(new UserNavigator());
     addNavigator(new GroupNavigator());
     addNavigator(new AdministrationNavigator());
     addNavigator(new MyProcessesNavigator());
     addNavigator(new SavedReportNavigator());
     addNavigator(new ReportNavigator());
     addNavigator(new ActiveProcessDefinitionNavigator());
     addNavigator(new SuspendedProcessDefinitionNavigator());
   }
 }


