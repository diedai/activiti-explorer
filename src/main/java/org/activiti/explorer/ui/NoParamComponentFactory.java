 package org.activiti.explorer.ui;
 
 import org.activiti.engine.ActivitiException;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public abstract class NoParamComponentFactory<T>
   implements ComponentFactory<T>
 {
   protected Class<? extends T> clazz;
   
   public void initialise(String environment)
   {
     if (environment.equals("alfresco")) {
       this.clazz = getAlfrescoComponentClass();
     } else {
       this.clazz = getDefaultComponentClass();
     }
   }
   
   public T create() {
     try {
       return (T)this.clazz.newInstance();
     } catch (Exception e) {
       throw new ActivitiException("Couldn't instantiate class " + this.clazz, e);
     }
   }
   
   protected abstract Class<? extends T> getAlfrescoComponentClass();
   
   protected abstract Class<? extends T> getDefaultComponentClass();
 }


