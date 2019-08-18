 package org.activiti.explorer.ui.task.data;
 
 import org.activiti.engine.TaskService;
 import org.activiti.engine.task.TaskQuery;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class InboxListQuery
   extends AbstractTaskListQuery
 {
   protected TaskQuery getQuery()
   {
     return (TaskQuery)((TaskQuery)((TaskQuery)this.taskService.createTaskQuery().taskAssignee(this.userId)).orderByTaskId()).asc();
   }
 }


