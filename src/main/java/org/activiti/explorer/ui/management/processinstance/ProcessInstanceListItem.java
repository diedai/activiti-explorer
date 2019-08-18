 package org.activiti.explorer.ui.management.processinstance;
 
 import com.vaadin.data.Property;
 import com.vaadin.data.util.ObjectProperty;
 import com.vaadin.data.util.PropertysetItem;
 import org.activiti.engine.runtime.ProcessInstance;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 class ProcessInstanceListItem
   extends PropertysetItem
   implements Comparable<ProcessInstanceListItem>
 {
   private static final long serialVersionUID = 1L;
   public static final String PROPERTY_ID = "id";
   public static final String PROPERTY_NAME = "name";
   
   public ProcessInstanceListItem(ProcessInstance processInstance, String processDefinitionName)
   {
     addItemProperty("id", new ObjectProperty(processInstance.getId(), String.class));
     addItemProperty("name", new ObjectProperty(processDefinitionName + " (id=" + processInstance.getId() + ")", String.class));
   }
   
   public int compareTo(ProcessInstanceListItem other)
   {
     String id = (String)getItemProperty("id").getValue();
     String otherId = (String)other.getItemProperty("id").getValue();
     return id.compareTo(otherId);
   }
 }


