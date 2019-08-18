 package org.activiti.explorer.ui.management.deployment;
 
 import java.io.Serializable;
 import org.activiti.explorer.ComponentFactories;
 import org.activiti.explorer.ui.ComponentFactory;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class DeploymentFilterFactory
   implements ComponentFactory<DeploymentFilter>, Serializable
 {
   private static final long serialVersionUID = 6401451647516737141L;
   private DeploymentFilter deploymentFilter;
   
   public void initialise(String environment)
   {
     if (this.deploymentFilter == null) {
       this.deploymentFilter = new DefaultDeploymentFilter();
     }
   }
   
 
 
 
   public void setComponentFactories(ComponentFactories componentFactories)
   {
     componentFactories.add(DeploymentFilterFactory.class, this);
   }
   
   public DeploymentFilter create() {
     return this.deploymentFilter;
   }
   
   public void setDeploymentFilter(DeploymentFilter deploymentFilter)
   {
     this.deploymentFilter = deploymentFilter;
   }
 }


