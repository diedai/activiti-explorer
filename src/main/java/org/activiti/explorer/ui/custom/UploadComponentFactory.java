 package org.activiti.explorer.ui.custom;
 
 import org.activiti.explorer.ui.ComponentFactory;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class UploadComponentFactory
   implements ComponentFactory<UploadComponent>
 {
   private static final long serialVersionUID = 1L;
   protected boolean enableDrop = true;
   
   public void initialise(String environment)
   {
     if (environment.equals("alfresco")) {
       this.enableDrop = false;
     }
   }
   
   public UploadComponent create()
   {
     return new UploadComponent(this.enableDrop);
   }
 }


