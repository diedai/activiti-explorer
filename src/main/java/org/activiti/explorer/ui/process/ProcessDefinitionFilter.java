package org.activiti.explorer.ui.process;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;

public abstract interface ProcessDefinitionFilter
{
  public abstract ProcessDefinitionQuery getQuery(RepositoryService paramRepositoryService);
  
  public abstract ProcessDefinitionQuery getCountQuery(RepositoryService paramRepositoryService);
  
  public abstract ProcessDefinitionListQuery.ProcessDefinitionListItem createItem(ProcessDefinition paramProcessDefinition);
}


