 package org.activiti.explorer.ui.component.diagram;
 
 import com.vaadin.terminal.PaintException;
 import com.vaadin.terminal.PaintTarget;
 import com.vaadin.ui.AbstractComponent;
 import com.vaadin.ui.ClientWidget;
 import org.activiti.explorer.ui.component.diagram.client.ui.VProcessDiagram;
 
 
 
 @ClientWidget(VProcessDiagram.class)
 public class ProcessDiagramComponent
   extends AbstractComponent
 {
   private String processDefinitionKey;
   
   public void setProcessDefinitionKey(String processDefinitionKey)
   {
     this.processDefinitionKey = processDefinitionKey;
     requestRepaint();
   }
   
   public void paintContent(PaintTarget target)
     throws PaintException
   {
     super.paintContent(target);
     target.addAttribute("definitionKey", this.processDefinitionKey);
   }
 }


