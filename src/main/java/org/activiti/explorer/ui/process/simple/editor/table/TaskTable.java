 package org.activiti.explorer.ui.process.simple.editor.table;
 
 import com.vaadin.data.Item;
 import com.vaadin.data.Property;
 import com.vaadin.ui.Button;
 import com.vaadin.ui.CheckBox;
 import com.vaadin.ui.ComboBox;
 import com.vaadin.ui.HorizontalLayout;
 import com.vaadin.ui.Table;
 import com.vaadin.ui.TextField;
 import java.util.ArrayList;
 import java.util.Iterator;
 import java.util.List;
 import org.activiti.engine.IdentityService;
 import org.activiti.engine.ProcessEngine;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.engine.identity.Group;
 import org.activiti.engine.identity.GroupQuery;
 import org.activiti.engine.identity.User;
 import org.activiti.engine.identity.UserQuery;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.ui.process.simple.editor.listener.AddTaskClickListener;
 import org.activiti.explorer.ui.process.simple.editor.listener.DeleteTaskClickListener;
 import org.activiti.explorer.ui.process.simple.editor.listener.ShowFormClickListener;
 import org.activiti.explorer.ui.process.simple.editor.listener.TaskFormModelListener;
 import org.activiti.workflow.simple.definition.HumanStepDefinition;
 import org.activiti.workflow.simple.definition.form.FormDefinition;
 import org.apache.commons.lang3.StringUtils;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class TaskTable
   extends Table
   implements TaskFormModelListener
 {
   private static final long serialVersionUID = -2578437667358797351L;
   public static final String ID_NAME = "name";
   public static final String ID_ASSIGNEE = "assignee";
   public static final String ID_GROUPS = "groups";
   public static final String ID_DESCRIPTION = "description";
   public static final String ID_START_WITH_PREVIOUS = "startWithPrevious";
   public static final String ID_ACTIONS = "actions";
   protected I18nManager i18nManager;
   protected TaskFormModel taskFormModel = new TaskFormModel();
   
   public TaskTable() {
     this.i18nManager = ExplorerApp.get().getI18nManager();
     this.taskFormModel.addFormModelListener(this);
     
     setEditable(true);
     setColumnReorderingAllowed(true);
     
     setSizeFull();
     setPageLength(0);
     
     addContainerProperty("name", String.class, null);
     addContainerProperty("assignee", ComboBox.class, null);
     addContainerProperty("groups", ComboBox.class, null);
     addContainerProperty("description", TextField.class, null);
     addContainerProperty("startWithPrevious", CheckBox.class, null);
     addContainerProperty("actions", HorizontalLayout.class, null);
     
     setColumnHeader("name", this.i18nManager.getMessage("process.editor.task.name"));
     setColumnHeader("assignee", this.i18nManager.getMessage("process.editor.task.assignee"));
     setColumnHeader("groups", this.i18nManager.getMessage("process.editor.task.groups"));
     setColumnHeader("description", this.i18nManager.getMessage("process.editor.task.description"));
     setColumnHeader("startWithPrevious", this.i18nManager.getMessage("process.editor.task.concurrency"));
     setColumnHeader("actions", this.i18nManager.getMessage("process.editor.actions"));
     
     setColumnAlignment("name", "c");
     setColumnAlignment("assignee", "c");
     setColumnAlignment("groups", "c");
     setColumnAlignment("startWithPrevious", "c");
     setColumnAlignment("startWithPrevious", "c");
     setColumnWidth("actions", 170);
   }
   
   public void addTaskRow(HumanStepDefinition humanStepDefinition) {
     Object taskItemId = addTaskRow(null, humanStepDefinition.getName(), humanStepDefinition.getAssignee(), 
       getCommaSeperated(humanStepDefinition.getCandidateGroups()), humanStepDefinition.getDescription(), 
       Boolean.valueOf(humanStepDefinition.isStartsWithPrevious()));
     if (humanStepDefinition.getForm() != null) {
       this.taskFormModel.addForm(taskItemId, humanStepDefinition.getForm());
     }
   }
   
   protected String getCommaSeperated(List<String> list) {
     if ((list != null) && (!list.isEmpty())) {
       return StringUtils.join(list, ", ");
     }
     return null;
   }
   
   public void addDefaultTaskRow() {
     addDefaultTaskRowAfter(null);
   }
   
   public void addDefaultTaskRowAfter(Object itemId) {
     addTaskRow(itemId, null, null, null, null, null);
   }
   
 
   protected Object addTaskRow(Object previousTaskItemId, String taskName, String taskAssignee, String taskGroups, String taskDescription, Boolean startWithPrevious)
   {
     Object newItemId = null;
     if (previousTaskItemId == null) {
       newItemId = addItem();
     } else {
       newItemId = addItemAfter(previousTaskItemId);
     }
     Item newItem = getItem(newItemId);
     
 
     newItem.getItemProperty("name").setValue(taskName == null ? "my task" : taskName);
     
 
     ComboBox assigneeComboBox = new ComboBox();
     assigneeComboBox.setNullSelectionAllowed(true);
     Iterator localIterator;
     User user;
     try { for (localIterator = ((UserQuery)ProcessEngines.getDefaultProcessEngine().getIdentityService().createUserQuery().orderByUserFirstName().asc()).list().iterator(); localIterator.hasNext();) { user = (User)localIterator.next();
         assigneeComboBox.addItem(user.getId());
         assigneeComboBox.setItemCaption(user.getId(), user.getFirstName() + " " + user.getLastName());
       }
     }
     catch (Exception localException2) {}
     
 
     if (taskAssignee != null) {
       assigneeComboBox.select(taskAssignee);
     }
     
     newItem.getItemProperty("assignee").setValue(assigneeComboBox);
     
 
     ComboBox groupComboBox = new ComboBox();
     groupComboBox.setNullSelectionAllowed(true);
     try
     {
       for (Group group : ((GroupQuery)ProcessEngines.getDefaultProcessEngine().getIdentityService().createGroupQuery().orderByGroupName().asc()).list()) {
         groupComboBox.addItem(group.getId());
         groupComboBox.setItemCaption(group.getId(), group.getName());
       }
     }
     catch (Exception localException1) {}
     
 
     if (taskGroups != null) {
       groupComboBox.select(taskGroups);
     }
     
     newItem.getItemProperty("groups").setValue(groupComboBox);
     
 
     TextField descriptionTextField = new TextField();
     descriptionTextField.setColumns(16);
     descriptionTextField.setRows(1);
     if (taskDescription != null) {
       descriptionTextField.setValue(taskDescription);
     }
     newItem.getItemProperty("description").setValue(descriptionTextField);
     
 
     CheckBox startWithPreviousCheckBox = new CheckBox(this.i18nManager.getMessage("process.editor.task.startwithprevious"));
     startWithPreviousCheckBox.setValue(Boolean.valueOf(startWithPrevious == null ? false : startWithPrevious.booleanValue()));
     newItem.getItemProperty("startWithPrevious").setValue(startWithPreviousCheckBox);
     
 
     newItem.getItemProperty("actions").setValue(generateActionButtons(newItemId));
     
     return newItemId;
   }
   
   protected HorizontalLayout generateActionButtons(Object taskItemId) {
     HorizontalLayout actionButtons = new HorizontalLayout();
     
     FormDefinition form = this.taskFormModel.getForm(taskItemId);
     
     Button formButton = new Button(form == null ? this.i18nManager.getMessage("process.editor.task.form.create") : this.i18nManager.getMessage("process.editor.task.form.edit"));
     formButton.addListener(new ShowFormClickListener(this.taskFormModel, taskItemId));
     formButton.setData(taskItemId);
     actionButtons.addComponent(formButton);
     
     Button deleteTaskButton = new Button("-");
     deleteTaskButton.setData(taskItemId);
     deleteTaskButton.addListener(new DeleteTaskClickListener(this));
     actionButtons.addComponent(deleteTaskButton);
     
     Button addTaskButton = new Button("+");
     addTaskButton.setData(taskItemId);
     addTaskButton.addListener(new AddTaskClickListener(this));
     actionButtons.addComponent(addTaskButton);
     
     return actionButtons;
   }
   
   public List<HumanStepDefinition> getSteps() {
     List<HumanStepDefinition> steps = new ArrayList();
     for (Object itemId : getItemIds()) {
       Item item = getItem(itemId);
       
       HumanStepDefinition humanStepDefinition = new HumanStepDefinition();
       
       String name = (String)item.getItemProperty("name").getValue();
       if ((name != null) && (name.length() > 0)) {
         humanStepDefinition.setName(name);
       }
       
       String assignee = (String)((ComboBox)item.getItemProperty("assignee").getValue()).getValue();
       if ((assignee != null) && (assignee.length() > 0)) {
         humanStepDefinition.setAssignee(assignee);
       }
       
       String groups = (String)((ComboBox)item.getItemProperty("groups").getValue()).getValue();
       List<String> candidateGroups = new ArrayList();
       if ((groups != null) && (groups.length() > 0)) {
         for (String group : groups.split(",")) {
           candidateGroups.add(group.trim());
         }
       }
       humanStepDefinition.setCandidateGroups(candidateGroups);
       
       String description = (String)((TextField)item.getItemProperty("description").getValue()).getValue();
       if ((description != null) && (description.length() > 0)) {
         humanStepDefinition.setDescription(description);
       }
       
       humanStepDefinition.setStartsWithPrevious(((CheckBox)item.getItemProperty("startWithPrevious").getValue()).booleanValue());
       
       FormDefinition formDefinition = this.taskFormModel.getForm(itemId);
       humanStepDefinition.setForm(formDefinition);
       
       steps.add(humanStepDefinition);
     }
     return steps;
   }
   
   public void formAdded(Object taskItemId)
   {
     getItem(taskItemId).getItemProperty("actions").setValue(generateActionButtons(taskItemId));
   }
   
   public void formRemoved(Object taskItemId)
   {
     getItem(taskItemId).getItemProperty("actions").setValue(generateActionButtons(taskItemId));
   }
 }


