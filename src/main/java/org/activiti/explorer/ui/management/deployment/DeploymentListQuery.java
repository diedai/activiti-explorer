 package org.activiti.explorer.ui.management.deployment;
 
 import com.vaadin.data.Item;
 import com.vaadin.data.Property;
 import com.vaadin.data.util.ObjectProperty;
 import com.vaadin.data.util.PropertysetItem;
 import java.util.ArrayList;
 import java.util.List;
 import org.activiti.engine.ProcessEngine;
 import org.activiti.engine.ProcessEngines;
 import org.activiti.engine.RepositoryService;
 import org.activiti.engine.repository.Deployment;
 import org.activiti.engine.repository.DeploymentQuery;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.I18nManager;
 import org.activiti.explorer.data.AbstractLazyLoadingQuery;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class DeploymentListQuery
   extends AbstractLazyLoadingQuery
 {
   protected transient RepositoryService repositoryService;
   protected DeploymentFilter deploymentFilter;
   
   public DeploymentListQuery(DeploymentFilter deploymentFilter)
   {
     this.repositoryService = ProcessEngines.getDefaultProcessEngine().getRepositoryService();
     this.deploymentFilter = deploymentFilter;
   }
   
   public int size() {
     return (int)this.deploymentFilter.getCountQuery(this.repositoryService).count();
   }
   
   public List<Item> loadItems(int start, int count) {
     List<Deployment> deployments = this.deploymentFilter.getQuery(this.repositoryService).listPage(start, count);
     
     List<Item> items = new ArrayList();
     for (Deployment deployment : deployments) {
       items.add(this.deploymentFilter.createItem(deployment));
     }
     return items;
   }
   
   public Item loadSingleResult(String id) {
     Deployment deployment = (Deployment)this.repositoryService.createDeploymentQuery().deploymentId(id).singleResult();
     if (deployment != null) {
       return this.deploymentFilter.createItem(deployment);
     }
     return null;
   }
   
   public void setSorting(Object[] propertyIds, boolean[] ascending) {
     throw new UnsupportedOperationException();
   }
   
   public static class DeploymentListitem extends PropertysetItem implements Comparable<DeploymentListitem>
   {
     private static final long serialVersionUID = 1L;
     
     public DeploymentListitem(Deployment deployment) {
       addItemProperty("id", new ObjectProperty(deployment.getId(), String.class));
       if (deployment.getName() != null) {
         addItemProperty("name", new ObjectProperty(deployment.getName(), String.class));
       } else {
         addItemProperty("name", new ObjectProperty(ExplorerApp.get().getI18nManager().getMessage("deployment.no.name"), String.class));
       }
     }
     
     public int compareTo(DeploymentListitem other)
     {
       String name = (String)getItemProperty("name").getValue();
       String otherName = (String)other.getItemProperty("name").getValue();
       
       int comparison = name.compareTo(otherName);
       if (comparison != 0) {
         return comparison;
       }
       String id = (String)getItemProperty("id").getValue();
       String otherId = (String)other.getItemProperty("id").getValue();
       return id.compareTo(otherId);
     }
   }
 }


