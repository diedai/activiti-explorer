 package org.activiti.explorer.ui.management.deployment;
 
 import java.io.Serializable;
 import org.activiti.engine.RepositoryService;
 import org.activiti.engine.repository.Deployment;
 import org.activiti.engine.repository.DeploymentBuilder;
 import org.activiti.engine.repository.DeploymentQuery;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class DefaultDeploymentFilter
   implements DeploymentFilter, Serializable
 {
   private static final long serialVersionUID = 9094140663326243967L;
   protected static final String PROPERTY_ID = "id";
   protected static final String PROPERTY_NAME = "name";
   protected static final String PROPERTY_KEY = "key";
   
   public DeploymentQuery getQuery(RepositoryService repositoryService)
   {
     return (DeploymentQuery)((DeploymentQuery)repositoryService.createDeploymentQuery().orderByDeploymentName().asc()).orderByDeploymentId().asc();
   }
   
   public DeploymentQuery getCountQuery(RepositoryService repositoryService) {
     return repositoryService.createDeploymentQuery();
   }
   
   public DeploymentListQuery.DeploymentListitem createItem(Deployment deployment) {
     return new DeploymentListQuery.DeploymentListitem(deployment);
   }
   
   public void beforeDeploy(DeploymentBuilder deployment) {}
 }


