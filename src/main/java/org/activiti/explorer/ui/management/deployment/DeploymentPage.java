 package org.activiti.explorer.ui.management.deployment;
 
 import com.vaadin.data.Item;
 import com.vaadin.data.Property;
 import com.vaadin.data.Property.ValueChangeEvent;
 import com.vaadin.data.Property.ValueChangeListener;
 import com.vaadin.ui.Table;
 import org.activiti.explorer.ExplorerApp;
 import org.activiti.explorer.data.LazyLoadingContainer;
 import org.activiti.explorer.data.LazyLoadingQuery;
 import org.activiti.explorer.navigation.UriFragment;
 import org.activiti.explorer.ui.ComponentFactory;
 import org.activiti.explorer.ui.Images;
 import org.activiti.explorer.ui.management.ManagementPage;
 import org.activiti.explorer.ui.util.ThemeImageColumnGenerator;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class DeploymentPage
   extends ManagementPage
 {
   private static final long serialVersionUID = 1L;
   protected String deploymentId;
   protected Table deploymentTable;
   protected LazyLoadingContainer deploymentListContainer;
   protected DeploymentFilter deploymentFilter;
   
   public DeploymentPage()
   {
     ExplorerApp.get().setCurrentUriFragment(new UriFragment("deployment"));
     
 
     this.deploymentFilter = ((DeploymentFilter)ExplorerApp.get().getComponentFactory(DeploymentFilterFactory.class).create());
   }
   
   public DeploymentPage(String deploymentId) {
     this();
     this.deploymentId = deploymentId;
   }
   
   protected void initUi()
   {
     super.initUi();
     
     if (this.deploymentId == null) {
       selectElement(0);
     } else {
       selectElement(this.deploymentListContainer.getIndexForObjectId(this.deploymentId));
     }
   }
   
   protected Table createList()
   {
     final Table deploymentTable = new Table();
     
     LazyLoadingQuery deploymentListQuery = new DeploymentListQuery(this.deploymentFilter);
     this.deploymentListContainer = new LazyLoadingContainer(deploymentListQuery, 30);
     deploymentTable.setContainerDataSource(this.deploymentListContainer);
     
 
     deploymentTable.addListener(new Property.ValueChangeListener() {
       private static final long serialVersionUID = 8811553575319455854L;
       
       public void valueChange(Property.ValueChangeEvent event) { Item item = deploymentTable.getItem(event.getProperty().getValue());
         if (item != null) {
           String deploymentId = (String)item.getItemProperty("id").getValue();
           DeploymentPage.this.setDetailComponent(new DeploymentDetailPanel(deploymentId, DeploymentPage.this));
           
 
           ExplorerApp.get().setCurrentUriFragment(new UriFragment(new String[] { "deployment", deploymentId }));
         }
         else
         {
           DeploymentPage.this.setDetailComponent(null);
           ExplorerApp.get().setCurrentUriFragment(new UriFragment("deployment"));
         }
         
       }
       
     });
     deploymentTable.addGeneratedColumn("icon", new ThemeImageColumnGenerator(Images.DEPLOYMENT_22));
     deploymentTable.setColumnWidth("icon", 22);
     
     deploymentTable.addContainerProperty("name", String.class, null);
     deploymentTable.setColumnHeaderMode(-1);
     
     return deploymentTable;
   }
 }


