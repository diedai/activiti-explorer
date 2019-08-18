 package org.activiti.explorer.ui.task;
 
 import com.vaadin.ui.Button;
 import com.vaadin.ui.Button.ClickEvent;
 import com.vaadin.ui.Button.ClickListener;
 import com.vaadin.ui.CustomComponent;
 import com.vaadin.ui.GridLayout;
 import com.vaadin.ui.HorizontalLayout;
 import com.vaadin.ui.Label;
 import com.vaadin.ui.VerticalLayout;
 import java.util.Collection;
 import java.util.List;
 import org.activiti.engine.ProcessEngine;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.engine.TaskService;
 import org.activiti.engine.task.IdentityLink;
 import org.activiti.engine.task.Task;
 import org.activiti.engine.task.TaskQuery;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.ViewManager;
 import org.activiti.explorer.ui.custom.SelectUsersPopupWindow;
 import org.activiti.explorer.ui.event.SubmitEvent;
 import org.activiti.explorer.ui.event.SubmitEventListener;
 import org.activiti.explorer.ui.task.listener.ChangeOwnershipListener;
 import org.activiti.explorer.ui.task.listener.ReassignAssigneeListener;
 import org.activiti.explorer.ui.task.listener.RemoveInvolvedPersonListener;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class TaskInvolvedPeopleComponent
   extends CustomComponent
 {
   private static final long serialVersionUID = 1L;
   protected transient TaskService taskService;
   protected I18nManager i18nManager;
   protected ViewManager viewManager;
   protected Task task;
   protected TaskDetailPanel taskDetailPanel;
   protected VerticalLayout layout;
   protected Label title;
   protected Button addPeopleButton;
   protected GridLayout peopleGrid;
   
   public TaskInvolvedPeopleComponent(Task task, TaskDetailPanel taskDetailPanel)
   {
     this.task = task;
     this.taskDetailPanel = taskDetailPanel;
     this.i18nManager = ExplorerApp.get().getI18nManager();
     this.viewManager = ExplorerApp.get().getViewManager();
     this.taskService = ProcessEngines.getDefaultProcessEngine().getTaskService();
     
     initUi();
   }
   
   protected void initUi() {
     addStyleName("block-holder");
     addStyleName("involve-people");
     
     initLayout();
     initHeader();
     initPeopleGrid();
   }
   
   protected void initLayout() {
     this.layout = new VerticalLayout();
     setCompositionRoot(this.layout);
   }
   
   protected void initHeader() {
     HorizontalLayout headerLayout = new HorizontalLayout();
     headerLayout.setWidth(100.0F, 8);
     this.layout.addComponent(headerLayout);
     
     initTitle(headerLayout);
     initAddPeopleButton(headerLayout);
   }
   
   protected void initTitle(HorizontalLayout headerLayout) {
     this.title = new Label(this.i18nManager.getMessage("task.people"));
     this.title.addStyleName("h3");
     this.title.setWidth(100.0F, 8);
     headerLayout.addComponent(this.title);
     headerLayout.setExpandRatio(this.title, 1.0F);
   }
   
   protected void initAddPeopleButton(HorizontalLayout headerLayout) {
     this.addPeopleButton = new Button();
     this.addPeopleButton.addStyleName("add");
     headerLayout.addComponent(this.addPeopleButton);
     
     this.addPeopleButton.addListener(new Button.ClickListener()
     {
       public void buttonClick(ClickEvent event)
       {
         final SelectUsersPopupWindow involvePeoplePopupWindow = new SelectUsersPopupWindow(TaskInvolvedPeopleComponent.this.i18nManager.getMessage("people.involve.popup.caption"), true);
         
         involvePeoplePopupWindow.addListener(new SubmitEventListener() {
           protected void submitted(SubmitEvent event) {
             Collection<String> selectedUserIds = involvePeoplePopupWindow.getSelectedUserIds();
             for (String userId : selectedUserIds) {
               String role = involvePeoplePopupWindow.getSelectedUserRole(userId);
               TaskInvolvedPeopleComponent.this.taskService.addUserIdentityLink(TaskInvolvedPeopleComponent.this.task.getId(), userId, role);
             }
             
             TaskInvolvedPeopleComponent.this.taskDetailPanel.notifyPeopleInvolvedChanged();
           }
           
 
           protected void cancelled(SubmitEvent event) {}
         });
         TaskInvolvedPeopleComponent.this.viewManager.showPopupWindow(involvePeoplePopupWindow);
       }
     });
   }
   
   protected void initPeopleGrid() {
     this.peopleGrid = new GridLayout();
     this.peopleGrid.setColumns(2);
     this.peopleGrid.setSpacing(true);
     this.peopleGrid.setMargin(true, false, false, false);
     this.peopleGrid.setWidth(100.0F, 8);
     this.layout.addComponent(this.peopleGrid);
     
     populatePeopleGrid();
   }
   
   protected void populatePeopleGrid() {
     initOwner();
     initAssignee();
     initInvolvedPeople();
   }
   
   protected void initOwner() {
     UserDetailsComponent ownerDetails = createOwnerComponent();
     this.peopleGrid.addComponent(ownerDetails);
   }
   
   protected UserDetailsComponent createOwnerComponent() {
     String roleMessage = this.task.getOwner() != null ? "task.owner" : "task.no.owner";
     
 
 
     return new UserDetailsComponent(this.task.getOwner(), this.i18nManager.getMessage(roleMessage), this.i18nManager.getMessage("task.owner.transfer"), new ChangeOwnershipListener(this.task, this.taskDetailPanel));
   }
   
   protected void initAssignee()
   {
     UserDetailsComponent assigneeDetails = createAssigneeComponent();
     this.peopleGrid.addComponent(assigneeDetails);
   }
   
   protected UserDetailsComponent createAssigneeComponent() {
     String roleMessage = this.task.getAssignee() != null ? "task.assignee" : "task.no.assignee";
     
 
 
     return new UserDetailsComponent(this.task.getAssignee(), this.i18nManager.getMessage(roleMessage), this.i18nManager.getMessage("task.assignee.reassign"), new ReassignAssigneeListener(this.task, this.taskDetailPanel));
   }
   
   protected void initInvolvedPeople()
   {
     List<IdentityLink> identityLinks = this.taskService.getIdentityLinksForTask(this.task.getId());
     for (IdentityLink identityLink : identityLinks) {
       if ((identityLink.getUserId() != null) && 
         (!"assignee".equals(identityLink.getType())) && 
         (!"owner".equals(identityLink.getType())))
       {
 
 
         UserDetailsComponent involvedDetails = new UserDetailsComponent(identityLink.getUserId(), identityLink.getType(), this.i18nManager.getMessage("task.involved.remove"), new RemoveInvolvedPersonListener(identityLink, this.task, this.taskDetailPanel));
         
         this.peopleGrid.addComponent(involvedDetails);
       }
     }
   }
   
   public void refreshPeopleGrid()
   {
     this.task = ((Task)((TaskQuery)this.taskService.createTaskQuery().taskId(this.task.getId())).singleResult());
     this.peopleGrid.removeAllComponents();
     populatePeopleGrid();
   }
   
   public void refreshAssignee() {
     this.task = ((Task)((TaskQuery)this.taskService.createTaskQuery().taskId(this.task.getId())).singleResult());
     this.peopleGrid.removeComponent(1, 0);
     this.peopleGrid.addComponent(createAssigneeComponent(), 1, 0);
   }
   
   public void refreshOwner() {
     this.task = ((Task)((TaskQuery)this.taskService.createTaskQuery().taskId(this.task.getId())).singleResult());
     this.peopleGrid.removeComponent(0, 0);
     this.peopleGrid.addComponent(createOwnerComponent(), 0, 0);
   }
 }


