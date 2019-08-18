 package org.activiti.explorer.ui.process.simple.editor.listener;
 
 import com.vaadin.ui.Button;
 import com.vaadin.ui.Button.ClickEvent;
 import com.vaadin.ui.Button.ClickListener;
 import org.activiti.explorer.ui.process.simple.editor.table.PropertyTable;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class AddPropertyClickListener
   implements ClickListener
 {
   private static final long serialVersionUID = 7191125475609217512L;
   protected PropertyTable propertyTable;
   
   public AddPropertyClickListener(PropertyTable propertyTable)
   {
     this.propertyTable = propertyTable;
   }
   
   public void buttonClick(ClickEvent event) {
     Object itemId = event.getButton().getData();
     this.propertyTable.addPropertyRowAfter(itemId);
   }
 }


