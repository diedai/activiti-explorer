 package org.activiti.explorer.ui.process;
 
 import com.vaadin.data.Property;
 import com.vaadin.data.util.ObjectProperty;
 import com.vaadin.data.util.PropertysetItem;
 import org.activiti.engine.runtime.ProcessInstance;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 class ProcessInstanceItem
   extends PropertysetItem
   implements Comparable<ProcessInstanceItem>
 {
   private static final long serialVersionUID = 1L;
   
   public ProcessInstanceItem() {}
   
   public ProcessInstanceItem(ProcessInstance processInstance)
   {
     addItemProperty("id", new ObjectProperty(processInstance.getId(), String.class));
     addItemProperty("businessKey", new ObjectProperty(processInstance.getBusinessKey(), String.class));
   }
   
   public int compareTo(ProcessInstanceItem other)
   {
     String id = (String)getItemProperty("id").getValue();
     String otherId = (String)other.getItemProperty("id").getValue();
     return id.compareTo(otherId);
   }
 }


