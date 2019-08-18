 package org.activiti.explorer.ui.reports;
 
 import com.vaadin.data.Property;
 import com.vaadin.data.util.ObjectProperty;
 import com.vaadin.data.util.PropertysetItem;
 import org.activiti.engine.repository.ProcessDefinition;
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class ReportListItem
   extends PropertysetItem
   implements Comparable<ReportListItem>
 {
   private static final long serialVersionUID = 1L;
   
   public ReportListItem(ProcessDefinition processDefinition)
   {
     addItemProperty("id", new ObjectProperty(processDefinition.getId(), String.class));
     addItemProperty("key", new ObjectProperty(processDefinition.getKey(), String.class));
     addItemProperty("name", new ObjectProperty(processDefinition.getName(), String.class));
     addItemProperty("version", new ObjectProperty(Integer.valueOf(processDefinition.getVersion()), Integer.class));
   }
   
   public int compareTo(ReportListItem other) {
     String name = (String)getItemProperty("name").getValue();
     String otherName = (String)other.getItemProperty("name").getValue();
     
     int comparison = name.compareTo(otherName);
     if (comparison != 0) {
       return comparison;
     }
     String key = (String)getItemProperty("key").getValue();
     String otherKey = (String)other.getItemProperty("key").getValue();
     comparison = key.compareTo(otherKey);
     
     if (comparison != 0) {
       return comparison;
     }
     Integer version = (Integer)getItemProperty("version").getValue();
     Integer otherVersion = (Integer)other.getItemProperty("version").getValue();
     return version.compareTo(otherVersion);
   }
 }


