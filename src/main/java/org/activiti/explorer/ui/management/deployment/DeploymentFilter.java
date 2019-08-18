package org.activiti.explorer.ui.management.deployment;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.DeploymentQuery;

public abstract interface DeploymentFilter
{
  public abstract DeploymentQuery getQuery(RepositoryService paramRepositoryService);
  
  public abstract DeploymentQuery getCountQuery(RepositoryService paramRepositoryService);
  
  public abstract DeploymentListQuery.DeploymentListitem createItem(Deployment paramDeployment);
  
  public abstract void beforeDeploy(DeploymentBuilder paramDeploymentBuilder);
}


