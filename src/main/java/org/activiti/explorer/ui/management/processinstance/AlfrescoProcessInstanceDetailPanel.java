 package org.activiti.explorer.ui.management.processinstance;
 
 import com.vaadin.ui.Component;
 import com.vaadin.ui.Label;
 import org.activiti.explorer.ui.AbstractTablePage;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class AlfrescoProcessInstanceDetailPanel
   extends ProcessInstanceDetailPanel
 {
   private static final long serialVersionUID = 1L;
   
   public AlfrescoProcessInstanceDetailPanel(String processInstanceId, AbstractTablePage processInstancePage)
   {
     super(processInstanceId, processInstancePage);
   }
   
 
   protected Component getTaskAssigneeComponent(String assignee)
   {
     return new Label(assignee);
   }
 }


