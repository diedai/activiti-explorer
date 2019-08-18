 package org.activiti.explorer.ui.process.simple.editor.table;
 
 import java.io.Serializable;
 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.List;
 import java.util.Map;
 import org.activiti.explorer.ui.process.simple.editor.listener.TaskFormModelListener;
 import org.activiti.workflow.simple.definition.form.FormDefinition;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class TaskFormModel
   implements Serializable
 {
   protected List<TaskFormModelListener> formModelListeners = new ArrayList();
   
 
   protected Map<Object, FormDefinition> forms = new HashMap();
   
   public void addForm(Object taskItemId, FormDefinition form) {
     this.forms.put(taskItemId, form);
     fireFormAdded(taskItemId);
   }
   
   public void removeForm(Object taskItemId) {
     this.forms.remove(taskItemId);
     fireFormRemoved(taskItemId);
   }
   
   public FormDefinition getForm(Object id) {
     return (FormDefinition)this.forms.get(id);
   }
   
   public void addFormModelListener(TaskFormModelListener formAdditionListener) {
     this.formModelListeners.add(formAdditionListener);
   }
   
   protected void fireFormAdded(Object taskItemId) {
     for (TaskFormModelListener formAdditionListener : this.formModelListeners) {
       formAdditionListener.formAdded(taskItemId);
     }
   }
   
   protected void fireFormRemoved(Object taskItemId) {
     for (TaskFormModelListener formModelListener : this.formModelListeners) {
       formModelListener.formRemoved(taskItemId);
     }
   }
 }


