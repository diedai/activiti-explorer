 package org.activiti.explorer.ui.event;
 
 import com.vaadin.ui.Component;
 import com.vaadin.ui.Component.Event;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class SubmitEvent
   extends Component.Event
 {
   private static final long serialVersionUID = 1L;
   public static final String SUBMITTED = "submit";
   public static final String CANCELLED = "cancel";
   private String type;
   private Object data;
   
   public SubmitEvent(Component source, String type) { this(source, type, null); }
   
   public SubmitEvent(Component source, String type, Object data) {
     super(source);
     this.type = type;
     this.data = data;
   }
   
   public String getType() {
     return this.type;
   }
   
 
 
   public Object getData()
   {
     return this.data;
   }
 }


