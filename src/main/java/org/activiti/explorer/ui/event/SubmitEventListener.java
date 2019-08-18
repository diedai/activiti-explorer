 package org.activiti.explorer.ui.event;
 
 import com.vaadin.ui.Component.Event;
 import com.vaadin.ui.Component.Listener;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public abstract class SubmitEventListener
   implements Listener
 {
   private static final long serialVersionUID = 1L;
   
   public final void componentEvent(Event event)
   {
     if ((event instanceof SubmitEvent)) {
       SubmitEvent gfe = (SubmitEvent)event;
       if ("submit".equals(gfe.getType())) {
         submitted(gfe);
       } else {
         cancelled(gfe);
       }
     }
   }
   
   protected abstract void submitted(SubmitEvent paramSubmitEvent);
   
   protected abstract void cancelled(SubmitEvent paramSubmitEvent);
 }


