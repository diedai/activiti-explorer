 package org.activiti.explorer.ui.task.data;
 
 import com.vaadin.data.Property;
 import com.vaadin.data.util.ObjectProperty;
 import com.vaadin.data.util.PropertysetItem;
 import org.activiti.engine.history.HistoricTaskInstance;
 import org.activiti.engine.task.Task;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class TaskListItem
   extends PropertysetItem
   implements Comparable<TaskListItem>
 {
   private static final long serialVersionUID = 1L;
   
   public TaskListItem(Task task)
   {
     addItemProperty("id", new ObjectProperty(task.getId(), String.class));
     addItemProperty("name", new ObjectProperty(task.getName(), String.class));
   }
   
   public TaskListItem(HistoricTaskInstance historicTaskInstance) {
     addItemProperty("id", new ObjectProperty(historicTaskInstance.getId(), String.class));
     addItemProperty("name", new ObjectProperty(historicTaskInstance.getName(), String.class));
   }
   
   public int compareTo(TaskListItem other) {
     String taskId = (String)getItemProperty("id").getValue();
     String otherTaskId = (String)other.getItemProperty("id").getValue();
     return taskId.compareTo(otherTaskId);
   }
 }


