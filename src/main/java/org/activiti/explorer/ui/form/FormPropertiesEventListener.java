 package org.activiti.explorer.ui.form;
 
 import com.vaadin.ui.Component.Event;
 import com.vaadin.ui.Component.Listener;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public abstract class FormPropertiesEventListener
   implements Listener
 {
   private static final long serialVersionUID = 7560512657831865244L;
   
   public final void componentEvent(Event event)
   {
     if ((event instanceof FormPropertiesForm.FormPropertiesEvent)) {
       FormPropertiesForm.FormPropertiesEvent propertyEvent = (FormPropertiesForm.FormPropertiesEvent)event;
       if ("SUBMIT".equals(propertyEvent.getType())) {
         handleFormSubmit(propertyEvent);
       } else {
         handleFormCancel(propertyEvent);
       }
     }
   }
   
   protected abstract void handleFormSubmit(FormPropertiesForm.FormPropertiesEvent paramFormPropertiesEvent);
   
   protected abstract void handleFormCancel(FormPropertiesForm.FormPropertiesEvent paramFormPropertiesEvent);
 }


