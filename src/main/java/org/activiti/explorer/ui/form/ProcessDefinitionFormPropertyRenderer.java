 package org.activiti.explorer.ui.form;
 
 import com.vaadin.ui.ComboBox;
 import com.vaadin.ui.Field;
 import java.util.List;
 import org.activiti.engine.ProcessEngine;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.engine.RepositoryService;
 import org.activiti.engine.form.FormProperty;
 import org.activiti.engine.repository.ProcessDefinition;
 import org.activiti.engine.repository.ProcessDefinitionQuery;
 import org.activiti.explorer.form.ProcessDefinitionFormType;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class ProcessDefinitionFormPropertyRenderer
   extends AbstractFormPropertyRenderer
 {
   private static final long serialVersionUID = 1L;
   
   public ProcessDefinitionFormPropertyRenderer()
   {
     super(ProcessDefinitionFormType.class);
   }
   
   public Field getPropertyField(FormProperty formProperty) {
     ComboBox comboBox = new ComboBox(getPropertyLabel(formProperty));
     comboBox.setRequired(formProperty.isRequired());
     comboBox.setRequiredError(getMessage("form.field.required", new Object[] { getPropertyLabel(formProperty) }));
     comboBox.setEnabled(formProperty.isWritable());
     
 
 
 
 
 
     List<ProcessDefinition> processDefinitions = ((ProcessDefinitionQuery)((ProcessDefinitionQuery)ProcessEngines.getDefaultProcessEngine().getRepositoryService().createProcessDefinitionQuery().orderByProcessDefinitionName().asc()).orderByProcessDefinitionVersion().asc()).list();
     
     for (ProcessDefinition processDefinition : processDefinitions) {
       comboBox.addItem(processDefinition.getId());
       String name = processDefinition.getName() + " (v" + processDefinition.getVersion() + ")";
       comboBox.setItemCaption(processDefinition.getId(), name);
     }
     
 
     if (!processDefinitions.isEmpty()) {
       comboBox.setNullSelectionAllowed(false);
       comboBox.select(((ProcessDefinition)processDefinitions.get(0)).getId());
     }
     
     return comboBox;
   }
 }


