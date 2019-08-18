 package org.activiti.explorer.ui.process.simple.editor.listener;
 
 import com.vaadin.ui.Button;
 import com.vaadin.ui.Button.ClickEvent;
 import com.vaadin.ui.Button.ClickListener;
 import org.activiti.explorer.ui.process.simple.editor.table.TaskTable;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class AddTaskClickListener
   implements ClickListener
 {
   private static final long serialVersionUID = 7191125475609217512L;
   protected TaskTable taskTable;
   
   public AddTaskClickListener(TaskTable taskTable)
   {
     this.taskTable = taskTable;
   }
   
   public void buttonClick(ClickEvent event) {
     Object itemId = event.getButton().getData();
     this.taskTable.addDefaultTaskRowAfter(itemId);
   }
 }


