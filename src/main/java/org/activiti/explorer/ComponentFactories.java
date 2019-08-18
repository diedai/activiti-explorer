 package org.activiti.explorer;
 
 import java.io.Serializable;
 import java.util.HashMap;
 import java.util.Map;
 import org.activiti.explorer.ui.ComponentFactory;
 import org.activiti.explorer.ui.custom.UploadComponentFactory;
 import org.activiti.explorer.ui.mainlayout.MainMenuBarFactory;
 import org.activiti.explorer.ui.management.ManagementMenuBarFactory;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class ComponentFactories
   implements Serializable
 {
   private static final long serialVersionUID = 7863017440773004716L;
   public static final String MAIN_MENU = "mainMenu";
   protected Map<Class, ComponentFactory> factories = new HashMap();
   
   protected String environment;
   
   public ComponentFactories()
   {
     this.factories.put(MainMenuBarFactory.class, new MainMenuBarFactory());
     this.factories.put(ManagementMenuBarFactory.class, new ManagementMenuBarFactory());
     this.factories.put(UploadComponentFactory.class, new UploadComponentFactory());
   }
   
   public <T> ComponentFactory<T> get(Class<? extends ComponentFactory<T>> clazz) {
     return (ComponentFactory)this.factories.get(clazz);
   }
   
   public <T> void add(Class<? extends ComponentFactory<T>> clazz, ComponentFactory<T> factory) {
     this.factories.put(clazz, factory);
     factory.initialise(this.environment);
   }
   
   public void setEnvironment(String environment) {
     this.environment = environment;
     
 
     for (ComponentFactory componentFactory : this.factories.values()) {
       componentFactory.initialise(environment);
     }
   }
 }


