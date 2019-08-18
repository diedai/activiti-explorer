 package org.activiti.explorer.ui.task.data;
 
 import java.util.Date;
 import java.util.Map;
 import org.activiti.engine.history.HistoricTaskInstance;
 import org.activiti.engine.task.DelegationState;
 import org.activiti.engine.task.Task;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class HistoricTaskWrapper
   implements Task
 {
   protected String id;
   protected String name;
   protected String description;
   protected int priority;
   protected String owner;
   protected String assignee;
   protected Date dueDate;
   protected String category;
   protected String parentTaskId;
   protected String tenantId;
   protected String formKey;
   
   public HistoricTaskWrapper(HistoricTaskInstance historicTaskInstance)
   {
     this.id = historicTaskInstance.getId();
     setName(historicTaskInstance.getName());
     setDescription(historicTaskInstance.getDescription());
     setDueDate(historicTaskInstance.getDueDate());
     setPriority(historicTaskInstance.getPriority());
     setOwner(historicTaskInstance.getOwner());
     setAssignee(historicTaskInstance.getAssignee());
     setTenantId(historicTaskInstance.getTenantId());
     setFormKey(historicTaskInstance.getFormKey());
   }
   
   public String getId() {
     return this.id;
   }
   
   public String getName() {
     return this.name;
   }
   
   public void setName(String name) {
     this.name = name;
   }
   
   public String getDescription() {
     return this.description;
   }
   
   public void setDescription(String description) {
     this.description = description;
   }
   
   public int getPriority() {
     return this.priority;
   }
   
   public void setPriority(int priority) {
     this.priority = priority;
   }
   
   public String getOwner() {
     return this.owner;
   }
   
   public void setOwner(String owner) {
     this.owner = owner;
   }
   
   public String getAssignee() {
     return this.assignee;
   }
   
   public void setAssignee(String assignee) {
     this.assignee = assignee;
   }
   
   public DelegationState getDelegationState() {
     return null;
   }
   
   public void setDelegationState(DelegationState delegationState) {}
   
   public String getProcessInstanceId()
   {
     return null;
   }
   
   public String getExecutionId() {
     return null;
   }
   
   public String getProcessDefinitionId() {
     return null;
   }
   
   public Date getCreateTime() {
     return null;
   }
   
   public String getTaskDefinitionKey() {
     return null;
   }
   
   public Date getDueDate() {
     return this.dueDate;
   }
   
   public void setDueDate(Date dueDate) {
     this.dueDate = dueDate;
   }
   
   public String getCategory() {
     return this.category;
   }
   
   public void setCategory(String category) {
     this.category = category;
   }
   
   public void delegate(String userId) {}
   
   public void setParentTaskId(String parentTaskId)
   {
     this.parentTaskId = parentTaskId;
   }
   
   public String getParentTaskId() {
     return this.parentTaskId;
   }
   
   public boolean isSuspended() {
     return false;
   }
   
   public String getTenantId() {
     return this.tenantId;
   }
   
   public void setTenantId(String tenantId) {
     this.tenantId = tenantId;
   }
   
   public String getFormKey()
   {
     return this.formKey;
   }
   
   public void setFormKey(String formKey)
   {
     this.formKey = formKey;
   }
   
   public Map<String, Object> getTaskLocalVariables()
   {
     return null;
   }
   
   public Map<String, Object> getProcessVariables() {
     return null;
   }
   
   public void setLocalizedName(String name) {}
   
   public void setLocalizedDescription(String description) {}
 }


