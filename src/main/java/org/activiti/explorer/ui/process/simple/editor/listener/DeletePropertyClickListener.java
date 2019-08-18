 package org.activiti.explorer.ui.process.simple.editor.listener;
 
 import com.vaadin.ui.Button;
 import com.vaadin.ui.Button.ClickEvent;
 import com.vaadin.ui.Button.ClickListener;
 import org.activiti.explorer.ui.process.simple.editor.table.PropertyTable;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class DeletePropertyClickListener
   implements ClickListener
 {
   private static final long serialVersionUID = 8903617821112289058L;
   protected PropertyTable propertyTable;
   
   public DeletePropertyClickListener(PropertyTable propertyTable)
   {
     this.propertyTable = propertyTable;
   }
   
   public void buttonClick(ClickEvent event) {
     if (this.propertyTable.size() > 1) {
       Object id = event.getButton().getData();
       this.propertyTable.removeItem(id);
     }
   }
 }


