 package org.activiti.explorer.ui.event;
 
 import com.vaadin.ui.Component;
import com.vaadin.ui.Component.Event;
 import com.vaadin.ui.Component.Listener;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public abstract class ConfirmationEventListener
   implements Listener
 {
   private static final long serialVersionUID = 1L;
   
   public final void componentEvent(Component.Event event)
   {
     if ((event instanceof ConfirmationEvent)) {
       ConfirmationEvent ce = (ConfirmationEvent)event;
       if (ce.isConfirmed()) {
         confirmed(ce);
       } else {
         rejected(ce);
       }
     }
   }
   
   protected void confirmed(ConfirmationEvent event) {}
   
   protected void rejected(ConfirmationEvent event) {}
 }


