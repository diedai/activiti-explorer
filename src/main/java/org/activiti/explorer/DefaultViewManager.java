 package org.activiti.explorer;
 
 import com.vaadin.ui.Window;
 import java.io.Serializable;
 import java.util.ArrayList;
 import java.util.Iterator;
 import java.util.List;
 import org.activiti.editor.ui.EditorProcessDefinitionPage;
 import org.activiti.engine.HistoryService;
 import org.activiti.engine.IdentityService;
 import org.activiti.engine.ProcessEngine;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.engine.TaskService;
 import org.activiti.engine.history.HistoricTaskInstanceQuery;
 import org.activiti.engine.identity.Group;
 import org.activiti.engine.identity.GroupQuery;
 import org.activiti.engine.task.IdentityLink;
 import org.activiti.engine.task.Task;
 import org.activiti.engine.task.TaskQuery;
 import org.activiti.explorer.identity.LoggedInUser;
 import org.activiti.explorer.ui.AbstractPage;
 import org.activiti.explorer.ui.MainWindow;
 import org.activiti.explorer.ui.custom.ToolBar;
 import org.activiti.explorer.ui.management.admin.AdministrationPage;
 import org.activiti.explorer.ui.management.crystalball.CrystalBallPage;
 import org.activiti.explorer.ui.management.db.DatabasePage;
 import org.activiti.explorer.ui.management.deployment.DeploymentPage;
 import org.activiti.explorer.ui.management.identity.GroupPage;
 import org.activiti.explorer.ui.management.identity.UserPage;
 import org.activiti.explorer.ui.management.job.JobPage;
 import org.activiti.explorer.ui.management.processdefinition.ActiveProcessDefinitionPage;
 import org.activiti.explorer.ui.management.processdefinition.SuspendedProcessDefinitionPage;
 import org.activiti.explorer.ui.process.MyProcessInstancesPage;
 import org.activiti.explorer.ui.process.ProcessDefinitionPage;
 import org.activiti.explorer.ui.process.simple.editor.SimpleTableEditor;
 import org.activiti.explorer.ui.profile.ProfilePopupWindow;
 import org.activiti.explorer.ui.reports.RunReportsPage;
 import org.activiti.explorer.ui.reports.SavedReportsPage;
 import org.activiti.explorer.ui.task.ArchivedPage;
 import org.activiti.explorer.ui.task.InboxPage;
 import org.activiti.explorer.ui.task.InvolvedPage;
 import org.activiti.explorer.ui.task.QueuedPage;
 import org.activiti.explorer.ui.task.TasksPage;
 import org.activiti.workflow.simple.definition.WorkflowDefinition;
 import org.springframework.beans.factory.annotation.Autowired;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class DefaultViewManager
   implements ViewManager, Serializable
 {
   private static final long serialVersionUID = -1712344958488358861L;
   protected AbstractPage currentPage;
   @Autowired
   protected MainWindow mainWindow;
   protected transient TaskService taskService;
   protected transient HistoryService historyService;
   protected transient IdentityService identityService;
   
   public DefaultViewManager()
   {
     this.taskService = ProcessEngines.getDefaultProcessEngine().getTaskService();
     this.historyService = ProcessEngines.getDefaultProcessEngine().getHistoryService();
     this.identityService = ProcessEngines.getDefaultProcessEngine().getIdentityService();
   }
   
   public void showLoginPage() {
     if (!this.mainWindow.isShowingLoginPage()) {
       this.mainWindow.showLoginPage();
     }
   }
   
   public void showDefaultPage() {
     this.mainWindow.showDefaultContent();
     showInboxPage();
   }
   
   public void showPopupWindow(Window window) {
     this.mainWindow.addWindow(window);
   }
   
 
 
 
 
 
 
 
 
   public void showTaskPage(String taskId)
   {
     Task task = (Task)((TaskQuery)this.taskService.createTaskQuery().taskId(taskId)).singleResult();
     String loggedInUserId = ExplorerApp.get().getLoggedInUser().getId();
     
     if (task == null)
     {
 
       boolean isOwner = ((HistoricTaskInstanceQuery)((HistoricTaskInstanceQuery)this.historyService.createHistoricTaskInstanceQuery().taskId(taskId)).taskOwner(loggedInUserId)).count() == 1L;
       if (isOwner) {
         showArchivedPage(taskId);
       } else {
         showNavigationError(taskId);
       }
     } else if (loggedInUserId.equals(task.getOwner())) {
       showTasksPage(taskId);
     } else if (loggedInUserId.equals(task.getAssignee())) {
       showInboxPage(taskId);
     } else if (((TaskQuery)this.taskService.createTaskQuery().taskInvolvedUser(loggedInUserId)).count() == 1L) {
       showInvolvedPage(taskId);
     }
     else {
       List<String> groupIds = getGroupIds(loggedInUserId);
       List<IdentityLink> identityLinks = this.taskService.getIdentityLinksForTask(task.getId());
       Iterator<IdentityLink> identityLinkIterator = identityLinks.iterator();
       
       boolean pageFound = false;
       while ((!pageFound) && (identityLinkIterator.hasNext())) {
         IdentityLink identityLink = (IdentityLink)identityLinkIterator.next();
         if ((identityLink.getGroupId() != null) && (groupIds.contains(identityLink.getGroupId()))) {
           showQueuedPage(identityLink.getGroupId(), task.getId());
           pageFound = true;
         }
       }
       
 
       if (!pageFound) {
         showNavigationError(taskId);
       }
     }
   }
   
   protected List<String> getGroupIds(String userId) {
     List<String> groupIds = new ArrayList();
     List<Group> groups = this.identityService.createGroupQuery().groupMember(userId).list();
     for (Group group : groups) {
       groupIds.add(group.getId());
     }
     
     return groupIds;
   }
   
   protected void showNavigationError(String taskId) {
     ExplorerApp.get().getNotificationManager().showErrorNotification("navigation.error.not.involved.title", 
     
       ExplorerApp.get().getI18nManager().getMessage("navigation.error.not.involved", new Object[] { taskId }));
   }
   
   public void showTasksPage() {
     switchView(new TasksPage(), "task", "tasks");
   }
   
   public void showTasksPage(String taskId) {
     switchView(new TasksPage(taskId), "task", "tasks");
   }
   
   public void showInboxPage() {
     switchView(new InboxPage(), "task", "inbox");
   }
   
   public void showInboxPage(String taskId) {
     switchView(new InboxPage(taskId), "task", "inbox");
   }
   
   public void showQueuedPage(String groupId) {
     switchView(new QueuedPage(groupId), "task", "queued");
   }
   
   public void showQueuedPage(String groupId, String taskId) {
     switchView(new QueuedPage(groupId, taskId), "task", "queued");
   }
   
   public void showInvolvedPage() {
     switchView(new InvolvedPage(), "task", "involved");
   }
   
   public void showInvolvedPage(String taskId) {
     switchView(new InvolvedPage(taskId), "task", "involved");
   }
   
   public void showArchivedPage() {
     switchView(new ArchivedPage(), "task", "archived");
   }
   
   public void showArchivedPage(String taskId) {
     switchView(new ArchivedPage(taskId), "task", "archived");
   }
   
 
   public void showDeployedProcessDefinitionPage()
   {
     switchView(new ProcessDefinitionPage(), "process", "deployedProcessDefinitions");
   }
   
   public void showDeployedProcessDefinitionPage(String processDefinitionId) {
     switchView(new ProcessDefinitionPage(processDefinitionId), "process", "deployedProcessDefinitions");
   }
   
   public void showEditorProcessDefinitionPage() {
     switchView(new EditorProcessDefinitionPage(), "process", "editorProcessDefinitions");
   }
   
   public void showEditorProcessDefinitionPage(String modelId) {
     switchView(new EditorProcessDefinitionPage(modelId), "process", "editorProcessDefinitions");
   }
   
   public void showMyProcessInstancesPage() {
     switchView(new MyProcessInstancesPage(), "process", "myProcessInstances");
   }
   
   public void showMyProcessInstancesPage(String processInstanceId) {
     switchView(new MyProcessInstancesPage(processInstanceId), "process", "myProcessInstances");
   }
   
   public void showSimpleTableProcessEditor(String processName, String processDescription) {
     switchView(new SimpleTableEditor(processName, processDescription), "process", null);
   }
   
   public void showSimpleTableProcessEditor(String modelId, WorkflowDefinition workflowDefinition) {
     switchView(new SimpleTableEditor(modelId, workflowDefinition), "process", null);
   }
   
 
   public void showRunReportPage()
   {
     switchView(new RunReportsPage(), "report", "runReports");
   }
   
   public void showRunReportPage(String reportId) {
     switchView(new RunReportsPage(reportId), "report", "runReports");
   }
   
   public void showSavedReportPage()
   {
     switchView(new SavedReportsPage(), "report", "savedResults");
   }
   
   public void showSavedReportPage(String reportId)
   {
     switchView(new SavedReportsPage(reportId), "report", "savedResults");
   }
   
 
   public void showDatabasePage()
   {
     switchView(new DatabasePage(), "manage", "database");
   }
   
   public void showDatabasePage(String tableName) {
     switchView(new DatabasePage(tableName), "manage", "database");
   }
   
   public void showDeploymentPage() {
     switchView(new DeploymentPage(), "manage", "deployments");
   }
   
   public void showDeploymentPage(String deploymentId) {
     switchView(new DeploymentPage(deploymentId), "manage", "deployments");
   }
   
   public void showActiveProcessDefinitionsPage() {
     switchView(new ActiveProcessDefinitionPage(), "manage", "activeProcessDefinitions");
   }
   
   public void showActiveProcessDefinitionsPage(String processDefinitionId) {
     switchView(new ActiveProcessDefinitionPage(processDefinitionId), "manage", "activeProcessDefinitions");
   }
   
   public void showSuspendedProcessDefinitionsPage() {
     switchView(new SuspendedProcessDefinitionPage(), "manage", "suspendedProcessDefinitions");
   }
   
   public void showSuspendedProcessDefinitionsPage(String processDefinitionId) {
     switchView(new SuspendedProcessDefinitionPage(processDefinitionId), "manage", "suspendedProcessDefinitions");
   }
   
   public void showJobPage() {
     switchView(new JobPage(), "manage", "jobs");
   }
   
   public void showJobPage(String jobId) {
     switchView(new JobPage(jobId), "manage", "jobs");
   }
   
   public void showUserPage() {
     switchView(new UserPage(), "manage", "users");
   }
   
   public void showUserPage(String userId) {
     switchView(new UserPage(userId), "manage", "users");
   }
   
   public void showGroupPage() {
     switchView(new GroupPage(), "manage", "groups");
   }
   
   public void showGroupPage(String groupId) {
     switchView(new GroupPage(groupId), "manage", "groups");
   }
   
   public void showProcessInstancePage() {
     throw new UnsupportedOperationException();
   }
   
   public void showProcessInstancePage(String processInstanceId) {
     throw new UnsupportedOperationException();
   }
   
   public void showAdministrationPage() {
     switchView(new AdministrationPage(), "manage", "administration");
   }
   
   public void showAdministrationPage(String managementId) {
     switchView(new AdministrationPage(managementId), "manage", "administration");
   }
   
   public void showCrystalBallPage() {
     switchView(new CrystalBallPage(), "manage", "crystalball");
   }
   
 
   public void showProfilePopup(String userId)
   {
     showPopupWindow(new ProfilePopupWindow(userId));
   }
   
 
   protected void switchView(AbstractPage page, String mainMenuActive, String subMenuActive)
   {
     this.currentPage = page;
     this.mainWindow.setMainNavigation(mainMenuActive);
     this.mainWindow.switchView(page);
     if ((subMenuActive != null) && (page.getToolBar() != null)) {
       page.getToolBar().setActiveEntry(subMenuActive);
     }
   }
   
   public AbstractPage getCurrentPage() {
     return this.currentPage;
   }
   
   public void setMainWindow(MainWindow mainWindow) {
     this.mainWindow = mainWindow;
   }
 }


