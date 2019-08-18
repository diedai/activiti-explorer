 package org.activiti.explorer.ui.process.simple.editor.listener;
 
 import com.vaadin.ui.Button;
 import com.vaadin.ui.Button.ClickEvent;
 import com.vaadin.ui.Button.ClickListener;
 import org.activiti.explorer.ui.process.simple.editor.table.TaskTable;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class DeleteTaskClickListener
   implements ClickListener
 {
   private static final long serialVersionUID = 8903617821112289058L;
   protected TaskTable taskTable;
   
   public DeleteTaskClickListener(TaskTable taskTable)
   {
     this.taskTable = taskTable;
   }
   
   public void buttonClick(ClickEvent event) {
     if (this.taskTable.size() > 1) {
       Object id = event.getButton().getData();
       this.taskTable.removeItem(id);
     }
   }
 }


