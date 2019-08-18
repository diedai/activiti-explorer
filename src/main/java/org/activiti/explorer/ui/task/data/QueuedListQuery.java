package org.activiti.explorer.ui.task.data;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.TaskQuery;

public class QueuedListQuery extends AbstractTaskListQuery {
	protected String groupId;
	protected transient TaskService taskService;

	public QueuedListQuery(String groupId) {
		this.groupId = groupId;
		this.taskService = ProcessEngines.getDefaultProcessEngine().getTaskService();
	}

	protected TaskQuery getQuery() {
		return (TaskQuery) ((TaskQuery) ((TaskQuery) this.taskService.createTaskQuery()
				.taskCandidateGroup(this.groupId)).taskUnassigned().orderByTaskId()).asc();
	}
}
