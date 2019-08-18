 package org.activiti.explorer.ui.management.admin;
 
 import com.vaadin.data.Item;
 import com.vaadin.data.Property;
 import com.vaadin.data.Property.ValueChangeEvent;
 import com.vaadin.data.Property.ValueChangeListener;
 import com.vaadin.terminal.StreamResource;
 import com.vaadin.ui.Alignment;
 import com.vaadin.ui.Component;
 import com.vaadin.ui.Embedded;
 import com.vaadin.ui.HorizontalLayout;
 import com.vaadin.ui.Label;
 import com.vaadin.ui.Panel;
 import com.vaadin.ui.Table;
 import java.util.ArrayList;
 import java.util.Date;
 import java.util.HashMap;
 import java.util.List;
 import java.util.Map;
 import org.activiti.engine.HistoryService;
 import org.activiti.engine.IdentityService;
 import org.activiti.engine.ProcessEngine;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.engine.RepositoryService;
 import org.activiti.engine.history.HistoricDetail;
 import org.activiti.engine.history.HistoricDetailQuery;
 import org.activiti.engine.history.HistoricFormProperty;
 import org.activiti.engine.history.HistoricProcessInstance;
 import org.activiti.engine.history.HistoricProcessInstanceQuery;
 import org.activiti.engine.history.HistoricTaskInstance;
 import org.activiti.engine.history.HistoricTaskInstanceQuery;
 import org.activiti.engine.history.HistoricVariableUpdate;
 import org.activiti.engine.impl.RepositoryServiceImpl;
 import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
 import org.activiti.engine.repository.ProcessDefinition;
 import org.activiti.engine.repository.ProcessDefinitionQuery;
 import org.activiti.engine.runtime.ProcessInstance;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.ui.Images;
 import org.activiti.explorer.ui.custom.DetailPanel;
 import org.activiti.explorer.ui.custom.PrettyTimeLabel;
 import org.activiti.explorer.ui.custom.UserProfileLink;
 import org.activiti.explorer.ui.process.ProcessDefinitionImageStreamResourceBuilder;
 import org.activiti.explorer.ui.variable.VariableRendererManager;
 
 
 
 
 public class AdminCompletedInstancesPanel
   extends DetailPanel
 {
   private static final long serialVersionUID = 1L;
   protected transient HistoryService historyService;
   protected transient RepositoryService repositoryService;
   protected transient IdentityService identityService;
   protected transient I18nManager i18nManager;
   protected transient VariableRendererManager variableRendererManager;
   protected HorizontalLayout definitionsLayout;
   protected Table definitionsTable;
   protected Label noMembersTable;
   protected HorizontalLayout instancesLayout;
   protected Table instancesTable;
   protected Map<String, ManagementProcessDefinition> completedDefinitions;
   protected List<HistoricProcessInstance> instanceList;
   protected ManagementProcessDefinition selectedManagementDefinition;
   private Embedded currentEmbedded;
   private Label imageHeader;
   private Label tasksHeader;
   private Table taskTable;
   private Label noTasksLabel;
   private Label tasksEmptyHeader;
   private Label variablesHeader;
   private Label variablesEmptyHeader;
   private Table variablesTable;
   private Label noVariablesLabel;
   
   public AdminCompletedInstancesPanel()
   {
     this.historyService = ProcessEngines.getDefaultProcessEngine().getHistoryService();
     this.repositoryService = ProcessEngines.getDefaultProcessEngine().getRepositoryService();
     this.identityService = ProcessEngines.getDefaultProcessEngine().getIdentityService();
     this.variableRendererManager = ExplorerApp.get().getVariableRendererManager();
     this.instanceList = this.historyService.createHistoricProcessInstanceQuery().finished().list();
     this.i18nManager = ExplorerApp.get().getI18nManager();
     
     init();
   }
   
   protected void init() {
     setSizeFull();
     addStyleName("light");
     
     initPageTitle();
     initDefinitions();
     initInstances();
   }
   
   protected void initPageTitle() {
     HorizontalLayout layout = new HorizontalLayout();
     layout.setWidth(100.0F, 8);
     layout.addStyleName("title-block");
     layout.setSpacing(true);
     layout.setMargin(false, false, true, false);
     addDetailComponent(layout);
     
     Embedded groupImage = new Embedded(null, Images.PROCESS_50);
     layout.addComponent(groupImage);
     
     Label groupName = new Label(this.i18nManager.getMessage("admin.completed.title"));
     groupName.setSizeUndefined();
     groupName.addStyleName("h2");
     layout.addComponent(groupName);
     layout.setComponentAlignment(groupName, Alignment.MIDDLE_LEFT);
     layout.setExpandRatio(groupName, 1.0F);
   }
   
   protected void initDefinitions() {
     HorizontalLayout definitionsHeader = new HorizontalLayout();
     definitionsHeader.setSpacing(true);
     definitionsHeader.setWidth(100.0F, 8);
     definitionsHeader.addStyleName("block-holder");
     addDetailComponent(definitionsHeader);
     
     initDefinitionTitle(definitionsHeader);
     
     this.definitionsLayout = new HorizontalLayout();
     this.definitionsLayout.setWidth(100.0F, 8);
     addDetailComponent(this.definitionsLayout);
     initDefinitionsTable();
   }
   
   protected void initDefinitionTitle(HorizontalLayout membersHeader) {
     Label usersHeader = new Label(this.i18nManager.getMessage("admin.definitions"));
     usersHeader.addStyleName("h3");
     membersHeader.addComponent(usersHeader);
   }
   
   protected void initDefinitionsTable() {
     if ((this.instanceList == null) || (this.instanceList.isEmpty())) {
       this.noMembersTable = new Label(this.i18nManager.getMessage("admin.completed.none.found"));
       this.definitionsLayout.addComponent(this.noMembersTable);
     }
     else
     {
       this.completedDefinitions = new HashMap();
       for (HistoricProcessInstance instance : this.instanceList) {
         String processDefinitionId = instance.getProcessDefinitionId();
         ManagementProcessDefinition managementDefinition = null;
         if (this.completedDefinitions.containsKey(processDefinitionId)) {
           managementDefinition = (ManagementProcessDefinition)this.completedDefinitions.get(processDefinitionId);
         }
         else {
           ProcessDefinition definition = (ProcessDefinition)this.repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
           if (definition == null) {
             continue;
           }
           
           managementDefinition = new ManagementProcessDefinition();
           managementDefinition.processDefinition = definition;
           managementDefinition.runningInstances = new ArrayList();
           this.completedDefinitions.put(definition.getId(), managementDefinition);
         }
         
         managementDefinition.runningInstances.add(instance);
       }
       
       this.definitionsTable = new Table();
       this.definitionsTable.setWidth(100.0F, 8);
       this.definitionsTable.setHeight(250.0F, 0);
       
       this.definitionsTable.setEditable(false);
       this.definitionsTable.setImmediate(true);
       this.definitionsTable.setSelectable(true);
       this.definitionsTable.setSortDisabled(false);
       
       this.definitionsTable.addContainerProperty("id", String.class, null, this.i18nManager.getMessage("process.instance.id"), null, "b");
       this.definitionsTable.addContainerProperty("name", String.class, null, this.i18nManager.getMessage("process.instance.name"), null, "b");
       this.definitionsTable.addContainerProperty("nr of instances", String.class, null, this.i18nManager.getMessage("admin.nr.instances"), null, "b");
       
       for (ManagementProcessDefinition managementDefinition : this.completedDefinitions.values()) {
         this.definitionsTable.addItem(new String[] { managementDefinition.processDefinition.getId(), managementDefinition.processDefinition
           .getName(), 
           String.valueOf(managementDefinition.runningInstances.size()) }, managementDefinition.processDefinition
           .getId());
       }
       
       this.definitionsTable.addListener(new Property.ValueChangeListener() {
         private static final long serialVersionUID = 1L;
         
         public void valueChange(Property.ValueChangeEvent event) { Item item = AdminCompletedInstancesPanel.this.definitionsTable.getItem(event.getProperty().getValue());
           if (item != null) {
             String definitionId = (String)item.getItemProperty("id").getValue();
             AdminCompletedInstancesPanel.this.selectedManagementDefinition = ((AdminCompletedInstancesPanel.ManagementProcessDefinition)AdminCompletedInstancesPanel.this.completedDefinitions.get(definitionId));
             AdminCompletedInstancesPanel.this.refreshInstancesTable();
           }
           
         }
       });
       this.definitionsLayout.addComponent(this.definitionsTable);
     }
   }
   
   protected void initInstances() {
     HorizontalLayout instancesHeader = new HorizontalLayout();
     instancesHeader.setSpacing(true);
     instancesHeader.setWidth(100.0F, 8);
     instancesHeader.addStyleName("block-holder");
     addDetailComponent(instancesHeader);
     initInstancesTitle(instancesHeader);
     
     this.instancesLayout = new HorizontalLayout();
     this.instancesLayout.setWidth(100.0F, 8);
     addDetailComponent(this.instancesLayout);
     initInstancesTable();
   }
   
   protected void initInstancesTitle(HorizontalLayout instancesHeader) {
     Label instancesLabel = new Label(this.i18nManager.getMessage("process.instances"));
     instancesLabel.addStyleName("h3");
     instancesHeader.addComponent(instancesLabel);
   }
   
   protected void initInstancesTable()
   {
     this.instancesTable = new Table();
     this.instancesTable.setWidth(100.0F, 8);
     this.instancesTable.setHeight(250.0F, 0);
     
     this.instancesTable.setEditable(false);
     this.instancesTable.setImmediate(true);
     this.instancesTable.setSelectable(true);
     this.instancesTable.setSortDisabled(false);
     
     this.instancesTable.addContainerProperty("id", String.class, null, this.i18nManager.getMessage("process.instance.id"), null, "b");
     this.instancesTable.addContainerProperty("business key", String.class, null, this.i18nManager.getMessage("process.instance.businesskey"), null, "b");
     this.instancesTable.addContainerProperty("start user id", String.class, null, this.i18nManager.getMessage("admin.started.by"), null, "b");
     this.instancesTable.addContainerProperty("start activity id", String.class, null, this.i18nManager.getMessage("admin.start.activity"), null, "b");
     this.instancesTable.addContainerProperty("start time", String.class, null, this.i18nManager.getMessage("process.instance.started"), null, "b");
     this.instancesTable.addContainerProperty("end time", String.class, null, this.i18nManager.getMessage("task.complete.time"), null, "b");
     this.instancesTable.addContainerProperty("duration", String.class, null, this.i18nManager.getMessage("task.duration"), null, "b");
     
     this.instancesTable.addListener(new Property.ValueChangeListener() {
       private static final long serialVersionUID = 1L;
       
       public void valueChange(Property.ValueChangeEvent event) { Item item = AdminCompletedInstancesPanel.this.instancesTable.getItem(event.getProperty().getValue());
         if (item != null) {
           String instanceId = (String)item.getItemProperty("id").getValue();
           
           HistoricProcessInstance processInstance = null;
           for (HistoricProcessInstance instance : AdminCompletedInstancesPanel.this.selectedManagementDefinition.runningInstances) {
             if (instance.getId().equals(instanceId)) {
               processInstance = instance;
               break;
             }
           }
           
           if (processInstance != null)
             AdminCompletedInstancesPanel.this.addProcessImage(AdminCompletedInstancesPanel.this.selectedManagementDefinition.processDefinition, processInstance);
           AdminCompletedInstancesPanel.this.addTasks(processInstance);
           AdminCompletedInstancesPanel.this.addVariables(processInstance);
         }
         
       }
     });
     this.instancesLayout.addComponent(this.instancesTable);
   }
   
   protected void refreshInstancesTable() {
     this.instancesTable.removeAllItems();
     for (HistoricProcessInstance instance : this.selectedManagementDefinition.runningInstances) {
       this.instancesTable.addItem(new String[] { instance.getId(), instance
         .getBusinessKey(), instance
         .getStartUserId(), instance
         .getStartActivityId(), instance
         .getStartTime().toString(), instance
         .getEndTime() != null ? instance.getEndTime().toString() : "", instance
         .getDurationInMillis() != null ? instance.getDurationInMillis().toString() : "" }, instance
         .getId());
     }
   }
   
 
 
 
 
 
 
 
   protected void addProcessImage(ProcessDefinition processDefinition, HistoricProcessInstance processInstance)
   {
     if (this.currentEmbedded != null) {
       this.mainPanel.removeComponent(this.currentEmbedded);
     }
     
 
     ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity)((RepositoryServiceImpl)this.repositoryService).getDeployedProcessDefinition(processDefinition.getId());
     
 
     if ((processDefinitionEntity != null) && (processDefinitionEntity.isGraphicalNotationDefined()))
     {
       if (this.imageHeader == null) {
         this.imageHeader = new Label(this.i18nManager.getMessage("process.header.diagram"));
         this.imageHeader.addStyleName("h3");
         this.imageHeader.addStyleName("block-holder");
         this.imageHeader.addStyleName("no-line");
         addDetailComponent(this.imageHeader);
       }
       
 
       StreamResource diagram = new ProcessDefinitionImageStreamResourceBuilder().buildStreamResource(processDefinition, this.repositoryService);
       
       this.currentEmbedded = new Embedded(null, diagram);
       this.currentEmbedded.setType(1);
       addDetailComponent(this.currentEmbedded);
     }
   }
   
   protected String getProcessDisplayName(ProcessDefinition processDefinition, ProcessInstance processInstance) {
     if (processDefinition.getName() != null) {
       return processDefinition.getName() + " (" + processInstance.getId() + ")";
     }
     return processDefinition.getKey() + " (" + processInstance.getId() + ")";
   }
   
 
 
 
 
 
   protected void addTasks(HistoricProcessInstance processInstance)
   {
     if (this.tasksHeader != null) {
       this.mainPanel.removeComponent(this.tasksHeader);
       this.mainPanel.removeComponent(this.tasksEmptyHeader);
     }
     
     if (this.noTasksLabel != null) {
       this.mainPanel.removeComponent(this.noTasksLabel);
     }
     
     this.tasksHeader = new Label(this.i18nManager.getMessage("process.instance.header.tasks"));
     this.tasksHeader.addStyleName("h3");
     this.tasksHeader.addStyleName("block-holder");
     this.tasksHeader.addStyleName("no-line");
     addDetailComponent(this.tasksHeader);
     
     this.tasksEmptyHeader = new Label("&nbsp;", 3);
     addDetailComponent(this.tasksEmptyHeader);
     
     if (this.taskTable != null) {
       this.mainPanel.removeComponent(this.taskTable);
     }
     
     this.taskTable = new Table();
     this.taskTable.addStyleName("proc-inst-task-list");
     this.taskTable.setWidth(100.0F, 8);
     this.taskTable.setHeight(250.0F, 0);
     
 
 
 
 
 
     List<HistoricTaskInstance> tasks = ((HistoricTaskInstanceQuery)((HistoricTaskInstanceQuery)((HistoricTaskInstanceQuery)this.historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstance.getId())).orderByHistoricTaskInstanceEndTime().desc()).orderByHistoricTaskInstanceStartTime().desc()).list();
     
     if (!tasks.isEmpty())
     {
 
       this.taskTable.addContainerProperty("finished", Component.class, null, this.i18nManager.getMessage("admin.finished"), null, "c");
       this.taskTable.setColumnWidth("finished", 22);
       
       this.taskTable.addContainerProperty("name", String.class, null, this.i18nManager.getMessage("task.name"), null, "b");
       
       this.taskTable.addContainerProperty("priority", Integer.class, null, this.i18nManager.getMessage("task.priority"), null, "b");
       
       this.taskTable.addContainerProperty("assignee", Component.class, null, this.i18nManager.getMessage("task.assignee"), null, "b");
       
       this.taskTable.addContainerProperty("dueDate", Component.class, null, this.i18nManager.getMessage("task.duedate"), null, "b");
       
       this.taskTable.addContainerProperty("startDate", Component.class, null, this.i18nManager.getMessage("task.create.time"), null, "b");
       
       this.taskTable.addContainerProperty("endDate", Component.class, null, this.i18nManager.getMessage("task.complete.time"), null, "b");
       
 
       addDetailComponent(this.taskTable);
       
       for (HistoricTaskInstance task : tasks) {
         addTaskItem(task, this.taskTable);
       }
       
       this.taskTable.setPageLength(this.taskTable.size());
     }
     else {
       this.noTasksLabel = new Label(this.i18nManager.getMessage("process.instance.no.tasks"));
       addDetailComponent(this.noTasksLabel);
     }
   }
   
   protected void addTaskItem(HistoricTaskInstance task, Table taskTable) {
     Item item = taskTable.addItem(task.getId());
     
     if (task.getEndTime() != null) {
       item.getItemProperty("finished").setValue(new Embedded(null, Images.TASK_FINISHED_22));
     } else {
       item.getItemProperty("finished").setValue(new Embedded(null, Images.TASK_22));
     }
     
     item.getItemProperty("name").setValue(task.getName());
     item.getItemProperty("priority").setValue(Integer.valueOf(task.getPriority()));
     
     item.getItemProperty("startDate").setValue(new PrettyTimeLabel(task.getStartTime(), true));
     item.getItemProperty("endDate").setValue(new PrettyTimeLabel(task.getEndTime(), true));
     
     if (task.getDueDate() != null) {
       Label dueDateLabel = new PrettyTimeLabel(task.getEndTime(), this.i18nManager.getMessage("task.not.finished.yet"), true);
       item.getItemProperty("dueDate").setValue(dueDateLabel);
     }
     
     if (task.getAssignee() != null) {
       Component taskAssigneeComponent = getTaskAssigneeComponent(task.getAssignee());
       if (taskAssigneeComponent != null) {
         item.getItemProperty("assignee").setValue(taskAssigneeComponent);
       }
     }
   }
   
   protected Component getTaskAssigneeComponent(String assignee) {
     return new UserProfileLink(this.identityService, true, assignee);
   }
   
 
 
 
 
   protected void addVariables(HistoricProcessInstance processInstance)
   {
     if (this.variablesHeader != null) {
       this.mainPanel.removeComponent(this.variablesHeader);
       this.mainPanel.removeComponent(this.variablesEmptyHeader);
     }
     
     if (this.noVariablesLabel != null) {
       this.mainPanel.removeComponent(this.noVariablesLabel);
     }
     
     this.variablesHeader = new Label(this.i18nManager.getMessage("process.instance.header.variables"));
     this.variablesHeader.addStyleName("h3");
     this.variablesHeader.addStyleName("block-holder");
     this.variablesHeader.addStyleName("no-line");
     addDetailComponent(this.variablesHeader);
     
     this.variablesEmptyHeader = new Label("&nbsp;", 3);
     addDetailComponent(this.variablesEmptyHeader);
     
     if (this.variablesTable != null) {
       this.mainPanel.removeComponent(this.variablesTable);
     }
     
 
 
     List<HistoricDetail> variables = ((HistoricDetailQuery)this.historyService.createHistoricDetailQuery().processInstanceId(processInstance.getId()).orderByTime().desc()).list();
     
     if (!variables.isEmpty())
     {
       this.variablesTable = new Table();
       this.variablesTable.setWidth(60.0F, 8);
       this.variablesTable.setHeight(250.0F, 0);
       this.variablesTable.addStyleName("proc-inst-task-list");
       
       this.variablesTable.addContainerProperty("name", String.class, null, this.i18nManager.getMessage("process.instance.variable.name"), null, "b");
       this.variablesTable.addContainerProperty("value", String.class, null, this.i18nManager.getMessage("process.instance.variable.value"), null, "b");
       this.variablesTable.addContainerProperty("type", String.class, null, this.i18nManager.getMessage("group.type"), null, "b");
       
       List<String> variableNames = new ArrayList();
       for (HistoricDetail detail : variables) {
         if ((detail instanceof HistoricVariableUpdate)) {
           HistoricVariableUpdate variable = (HistoricVariableUpdate)detail;
           if (!variableNames.contains(variable.getVariableName())) {
             variableNames.add(variable.getVariableName());
             Item variableItem = this.variablesTable.addItem(variable.getVariableName());
             variableItem.getItemProperty("name").setValue(variable.getVariableName());
             
 
             String theValue = this.variableRendererManager.getStringRepresentation(variable.getValue());
             variableItem.getItemProperty("value").setValue(theValue);
             variableItem.getItemProperty("type").setValue("variable");
           }
         } else {
           HistoricFormProperty form = (HistoricFormProperty)detail;
           if (!variableNames.contains(form.getPropertyId())) {
             variableNames.add(form.getPropertyId());
             Item variableItem = this.variablesTable.addItem(form.getPropertyId());
             variableItem.getItemProperty("name").setValue(form.getPropertyId());
             variableItem.getItemProperty("value").setValue(form.getPropertyValue());
             variableItem.getItemProperty("type").setValue("form property");
           }
         }
       }
       
       this.variablesTable.setPageLength(variables.size());
       addDetailComponent(this.variablesTable);
     } else {
       this.noVariablesLabel = new Label(this.i18nManager.getMessage("process.instance.no.variables"));
       addDetailComponent(this.noVariablesLabel);
     }
   }
   
   class ManagementProcessDefinition
   {
     public ProcessDefinition processDefinition;
     public List<HistoricProcessInstance> runningInstances;
     
     ManagementProcessDefinition() {}
   }
 }


