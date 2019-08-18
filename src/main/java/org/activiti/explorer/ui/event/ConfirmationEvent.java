 package org.activiti.explorer.ui.event;
 
 import com.vaadin.ui.Component;
 import com.vaadin.ui.Component.Event;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class ConfirmationEvent
   extends Component.Event
 {
   private static final long serialVersionUID = 1L;
   private boolean confirmed;
   
   public ConfirmationEvent(Component source, boolean confirmed)
   {
     super(source);
     this.confirmed = confirmed;
   }
   
   public boolean isConfirmed() {
     return this.confirmed;
   }
 }


