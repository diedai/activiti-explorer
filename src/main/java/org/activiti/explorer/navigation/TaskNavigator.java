 package org.activiti.explorer.navigation;
 
 import java.util.ArrayList;
 import java.util.List;
 import org.activiti.engine.ActivitiException;
 import org.activiti.engine.HistoryService;
 import org.activiti.engine.IdentityService;
 import org.activiti.engine.ProcessEngine;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.engine.TaskService;
 import org.activiti.engine.history.HistoricTaskInstanceQuery;
 import org.activiti.engine.identity.Group;
 import org.activiti.engine.identity.GroupQuery;
 import org.activiti.engine.task.Task;
 import org.activiti.engine.task.TaskQuery;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.NotificationManager;
 import org.activiti.explorer.ViewManager;
 import org.activiti.explorer.identity.LoggedInUser;
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class TaskNavigator
   implements Navigator
 {
   public static final String TASK_URI_PART = "tasks";
   public static final String CATEGORY_TASKS = "tasks";
   public static final String CATEGORY_INBOX = "inbox";
   public static final String CATEGORY_QUEUED = "queued";
   public static final String CATEGORY_INVOLVED = "involved";
   public static final String CATEGORY_ARCHIVED = "archived";
   public static final String PARAMETER_CATEGORY = "category";
   public static final String PARAMETER_GROUP = "group";
   protected transient TaskService taskService;
   protected transient HistoryService historyService;
   protected transient IdentityService identityService;
   
   public TaskNavigator()
   {
     this.taskService = ProcessEngines.getDefaultProcessEngine().getTaskService();
     this.identityService = ProcessEngines.getDefaultProcessEngine().getIdentityService();
     this.historyService = ProcessEngines.getDefaultProcessEngine().getHistoryService();
   }
   
   public String getTrigger() {
     return "tasks";
   }
   
   public void handleNavigation(UriFragment uriFragment) {
     String category = uriFragment.getParameter("category");
     String taskId = uriFragment.getUriPart(1);
     
     if (taskId == null) {
       directToCategoryPage(category, uriFragment);
     } else {
       directToSpecificTaskPage(category, taskId, uriFragment);
     }
   }
   
   protected void directToCategoryPage(String category, UriFragment uriFragment) {
     ViewManager viewManager = ExplorerApp.get().getViewManager();
     if ("tasks".equals(category)) {
       viewManager.showTasksPage();
     } else if ("inbox".equals(category)) {
       viewManager.showInboxPage();
     } else if ("queued".equals(category)) {
       viewManager.showQueuedPage(uriFragment.getParameter("group"));
     } else if ("involved".equals(category)) {
       viewManager.showInvolvedPage();
     } else if ("archived".equals(category)) {
       viewManager.showArchivedPage();
     } else {
       throw new ActivitiException("Couldn't find a matching category");
     }
   }
   
   protected void directToSpecificTaskPage(String category, String taskId, UriFragment uriFragment) {
     Task task = (Task)((TaskQuery)this.taskService.createTaskQuery().taskId(taskId)).singleResult();
     ViewManager viewManager = ExplorerApp.get().getViewManager();
     String loggedInUserId = ExplorerApp.get().getLoggedInUser().getId();
     
     boolean pageFound = false;
     if ("tasks".equals(category)) {
       if (loggedInUserId.equals(task.getOwner())) {
         viewManager.showTasksPage(task.getId());
         pageFound = true;
       }
     } else if ("inbox".equals(category)) {
       if (loggedInUserId.equals(task.getAssignee())) {
         viewManager.showInboxPage(task.getId());
         pageFound = true;
       }
     } else if ("queued".equals(category)) {
       String groupId = uriFragment.getParameter("group");
       
 
 
 
       boolean isTaskAssignedToGroup = ((TaskQuery)((TaskQuery)this.taskService.createTaskQuery().taskId(task.getId())).taskCandidateGroup(groupId)).count() == 1L;
       
 
 
 
       boolean isUserMemberOfGroup = this.identityService.createGroupQuery().groupMember(loggedInUserId).groupId(groupId).count() == 1L;
       
       if ((isTaskAssignedToGroup) && (isUserMemberOfGroup)) {
         viewManager.showQueuedPage(groupId, task.getId());
         pageFound = true;
       }
     }
     else if ("involved".equals(category))
     {
 
       boolean isUserInvolved = ((TaskQuery)this.taskService.createTaskQuery().taskInvolvedUser(loggedInUserId)).count() == 1L;
       
       if (isUserInvolved) {
         viewManager.showInvolvedPage(task.getId());
         pageFound = true;
       }
     } else if ("archived".equals(category)) {
       if (task == null)
       {
 
 
 
         boolean isOwner = ((HistoricTaskInstanceQuery)((HistoricTaskInstanceQuery)this.historyService.createHistoricTaskInstanceQuery().taskId(taskId)).taskOwner(loggedInUserId)).finished().count() == 1L;
         
         if (isOwner) {
           viewManager.showArchivedPage(taskId);
           pageFound = true;
         }
       }
     } else {
       throw new ActivitiException("Couldn't find a matching category");
     }
     
     if (!pageFound)
     {
       viewManager.showTaskPage(taskId);
     }
   }
   
   protected void showNavigationError(String taskId) {
     String description = ExplorerApp.get().getI18nManager().getMessage("navigation.error.not.involved", new Object[] { taskId });
     ExplorerApp.get().getNotificationManager().showErrorNotification("navigation.error.not.involved.title", description);
   }
   
   protected List<String> getGroupIds(String userId) {
     List<String> groupIds = new ArrayList();
     List<Group> groups = this.identityService.createGroupQuery().groupMember(userId).list();
     for (Group group : groups) {
       groupIds.add(group.getId());
     }
     
     return groupIds;
   }
 }


